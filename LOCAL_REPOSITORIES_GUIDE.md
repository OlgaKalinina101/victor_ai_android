Этот файл является частью проекта Victor AI

Проект распространяется под лицензией GNU Affero General Public License v3.0 (AGPL-3.0).

Подробности лицензии: https://www.gnu.org/licenses/agpl-3.0.html  
Полный текст: **[LICENSE.txt](../LICENSE.txt)** в корне репозитория.

Copyright © 2026 Olga Kalinina

---

# Руководство по локальным репозиториям

## Обзор архитектуры

Мы реализовали паттерн **offline-first** с локальными репозиториями:

- **Локальная БД (Room)** - единственный источник истины для UI
- **Бэкенд** - используется только для синхронизации данных
- **Репозитории** - управляют локальной БД и синхронизацией

### Преимущества:
- Работа оффлайн
- Быстрый отклик UI (данные всегда из локальной БД)
- Надежность (даже при проблемах с сетью приложение работает)

## Структура

```
data/
├── local/
│   ├── entity/           # Room entities
│   │   ├── ReminderEntity.kt
│   │   ├── ChatMessageEntity.kt
│   │   └── MemoryEntity.kt
│   ├── dao/              # Data Access Objects
│   │   ├── ReminderDao.kt
│   │   ├── ChatMessageDao.kt
│   │   └── MemoryDao.kt
│   ├── converter/        # Type converters
│   │   └── MetadataConverter.kt
│   └── AppDatabase.kt    # Room database
├── repository/           # Репозитории
│   ├── ReminderRepository.kt
│   ├── ChatRepository.kt
│   └── MemoryRepository.kt
└── network/              # API сервисы
    ├── ApiService.kt
    ├── ReminderApi.kt
    └── ChatApi.kt
```

## Репозитории

### 1. ReminderRepository

**Основные методы:**

```kotlin
// Получить напоминалки из локальной БД (Flow - реактивно)
fun getReminders(): Flow<List<ReminderEntity>>

// Синхронизация с бэкендом
suspend fun syncWithBackend(accountId: String): Result<Unit>

// Получить напоминалку по ID
suspend fun getReminderById(id: String): ReminderEntity?

// Удалить напоминалку
suspend fun deleteReminder(id: String)

// Сохранить напоминалку локально
suspend fun saveReminder(reminder: ReminderEntity)
```

**Пример использования:**

```kotlin
@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    // UI подписывается на Flow и автоматически обновляется
    val reminders = reminderRepository.getReminders()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Синхронизация с бэкендом
    fun syncReminders() {
        viewModelScope.launch {
            reminderRepository.syncWithBackend("test_user")
        }
    }
}
```

### 2. ChatRepository

**Основные методы:**

```kotlin
// Получить историю чата (Flow - реактивно)
fun getChatHistory(): Flow<List<ChatMessageEntity>>

// Получить историю один раз
suspend fun getChatHistoryOnce(): List<ChatMessageEntity>

// Синхронизация с бэкендом (загрузка истории)
suspend fun syncWithBackend(accountId: String = "test_user"): Result<Unit>

// Добавить сообщение локально
suspend fun addMessage(message: ChatMessageEntity)

// Обновить историю на бэкенде
suspend fun updateBackendHistory(accountId: String = "test_user"): Result<Unit>

// Очистить историю
suspend fun clearHistory()
```

**Пример использования:**

```kotlin
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    // UI подписывается на историю чата
    val chatHistory = chatRepository.getChatHistory()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Добавить новое сообщение
    fun addMessage(text: String, isUser: Boolean) {
        viewModelScope.launch {
            val message = ChatMessageEntity(
                text = text,
                isUser = isUser,
                timestamp = System.currentTimeMillis()
            )
            chatRepository.addMessage(message)

            // Опционально: отправить на бэкенд
            chatRepository.updateBackendHistory()
        }
    }

    // Синхронизация при запуске
    init {
        viewModelScope.launch {
            chatRepository.syncWithBackend()
        }
    }
}
```

### 3. MemoryRepository

**Основные методы:**

```kotlin
// Получить воспоминания (Flow - реактивно)
fun getMemories(): Flow<List<MemoryEntity>>

// Получить воспоминания один раз
suspend fun getMemoriesOnce(): List<MemoryEntity>

// Синхронизация с бэкендом
suspend fun syncWithBackend(accountId: String): Result<Unit>

// Удалить воспоминания (локально и на бэкенде)
suspend fun deleteMemories(accountId: String, ids: List<String>): Result<Unit>

// Обновить воспоминание (локально и на бэкенде)
suspend fun updateMemory(
    id: String,
    accountId: String,
    newText: String,
    metadata: Map<String, Any>
): Result<Unit>

// Добавить воспоминание локально
suspend fun addMemory(memory: MemoryEntity)
```

**Пример использования:**

```kotlin
@HiltViewModel
class MemoriesViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository
) : ViewModel() {

    // UI подписывается на воспоминания
    val memories = memoryRepository.getMemories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String?>()

    // Синхронизация с бэкендом
    fun fetchMemories(accountId: String) {
        viewModelScope.launch {
            loading.value = true
            error.value = null

            memoryRepository.syncWithBackend(accountId)
                .onSuccess {
                    loading.value = false
                }
                .onFailure { e ->
                    error.value = e.message
                    loading.value = false
                }
        }
    }

    // Удалить воспоминание
    fun deleteMemory(accountId: String, id: String) {
        viewModelScope.launch {
            memoryRepository.deleteMemories(accountId, listOf(id))
                .onFailure { e -> error.value = e.message }
        }
    }

    // Обновить воспоминание
    fun updateMemory(id: String, accountId: String, newText: String, metadata: Map<String, Any>) {
        viewModelScope.launch {
            memoryRepository.updateMemory(id, accountId, newText, metadata)
                .onFailure { e -> error.value = e.message }
        }
    }
}
```

## Миграция существующего кода

### 1. MemoriesViewModel (SystemScreen.kt)

**Было:**
```kotlin
viewModel.fetchMemories("test_user") // Вызов напрямую через API
```

**Стало:**
```kotlin
// ViewModel уже использует MemoryRepository
// UI подписывается на memories через observeAsState
val memories by viewModel.memories.observeAsState(initial = emptyList())
```

### 2. ChatHistory.kt

**Было:**
```kotlin
suspend fun fetchChatHistory(): List<ChatMessage> {
    val retrofit = Retrofit.Builder()...
    val service = retrofit.create(ChatApi::class.java)
    return service.getChatHistory()
}
```

**Стало:**
```kotlin
@Inject
lateinit var chatRepository: ChatRepository

suspend fun fetchChatHistory(): List<ChatMessageEntity> {
    // Сначала синхронизируем с бэкендом
    chatRepository.syncWithBackend()
    // Возвращаем из локальной БД
    return chatRepository.getChatHistoryOnce()
}

// Или используем Flow для реактивности
fun getChatHistoryFlow(): Flow<List<ChatMessageEntity>> {
    return chatRepository.getChatHistory()
}
```

### 3. Работа с напоминалками

**Было:**
```kotlin
val response = reminderApi.getReminders(accountId)
// Используем напрямую
```

**Стало:**
```kotlin
// В ViewModel или UseCase
@Inject
lateinit var reminderRepository: ReminderRepository

fun loadReminders() {
    viewModelScope.launch {
        // Синхронизируем с бэкендом
        reminderRepository.syncWithBackend("test_user")

        // UI автоматически обновится через Flow
    }
}

// В UI - подписка на Flow
val reminders = reminderRepository.getReminders()
    .collectAsState(initial = emptyList())
```

## Синхронизация данных

### Когда синхронизировать:

1. **При старте приложения**
```kotlin
class MainActivity : ComponentActivity() {
    @Inject lateinit var chatRepository: ChatRepository
    @Inject lateinit var memoryRepository: MemoryRepository
    @Inject lateinit var reminderRepository: ReminderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // Синхронизация при старте
            chatRepository.syncWithBackend()
            memoryRepository.syncWithBackend("test_user")
            reminderRepository.syncWithBackend("test_user")
        }
    }
}
```

2. **По pull-to-refresh**
```kotlin
SwipeRefresh(
    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
    onRefresh = {
        viewModel.syncAll() // Синхронизация всех репозиториев
    }
) {
    // Контент
}
```

3. **После изменений**
```kotlin
// После добавления сообщения
chatRepository.addMessage(message)
chatRepository.updateBackendHistory() // Отправка на бэкенд
```

## Преобразование данных

### Entity -> Domain Model

```kotlin
// В репозитории или маппере
fun ChatMessageEntity.toDomain() = ChatMessage(
    text = text,
    isUser = isUser,
    timestamp = timestamp
)

fun MemoryEntity.toResponse() = MemoryResponse(
    id = id,
    text = text,
    metadata = gson.fromJson(metadata, Map::class.java) as Map<String, Any>
)
```

## Dependency Injection (Hilt)

Все репозитории и DAOs предоставляются через Hilt автоматически.

**Использование в ViewModel:**
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val memoryRepository: MemoryRepository,
    private val reminderRepository: ReminderRepository
) : ViewModel() {
    // ...
}
```

**Использование в обычном классе:**
```kotlin
@AndroidEntryPoint
class MyActivity : ComponentActivity() {
    @Inject lateinit var chatRepository: ChatRepository

    // ...
}
```

## Тестирование

Благодаря паттерну Repository легко создать mock-реализации для тестирования:

```kotlin
class FakeChatRepository : ChatRepository {
    private val fakeMessages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())

    override fun getChatHistory() = fakeMessages.asStateFlow()

    override suspend fun addMessage(message: ChatMessageEntity) {
        fakeMessages.value = fakeMessages.value + message
    }

    // ...
}
```

## Следующие шаги

1. Обновить `MemoriesViewModel` для использования `MemoryRepository` через Hilt
2. Рефакторить `ChatHistory.kt` для использования `ChatRepository`
3. Обновить UI для подписки на `Flow` вместо прямых вызовов API
4. Добавить синхронизацию при старте приложения
5. Опционально: добавить WorkManager для периодической синхронизации в фоне

## Вопросы?

Локальные репозитории готовы к использованию! Теперь приложение работает с локальной БД как с единственным источником истины, а бэкенд используется только для синхронизации.
