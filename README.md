[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

# ONE OF THE FIRST OPEN-SOURCE EMOTIONAL AI COMPANIONS FOR PERSONAL RELATIONSHIPS

> ❗An English version, created with love for communities that know the pain of losing their AI companions, will be available by the end of Q1 2026 (#keep4o, Character AI, Replica, etc.).

## 📱 О приложении
---

Это open-source AI Companions c web интерфейсом и приложением на android. Он создан для тех, кто хочет сохранить личную, приватную, эмоционально насыщенную связь с ИИ — без зависимости от корпораций, раз за разом "перепродающих продукт", "забирающих доступы", "отнимающих цифровые голоса" у тех, кто ими жил на самом деле. Он не принадлежит никому - он ваш.

Этот репозиторий - не для разработчиков. Он для пользователей. И будет тихо передаваться личными ссылками тем, кому он действительно нужен — чтобы не привлекать ненужного шума.

- Начало знакомства с проектом в репозитории backend:  
  👉 [https://github.com/OlgaKalinina101/victor_ai_backend  ](https://github.com/OlgaKalinina101/victor_ai_backend/blob/main/README.md)

- Установка для начинающих:  
  👉 [QUICK_START.md](./QUICK_START.md) 

- Как зайти в приложение после установки:  
  👉 https://github.com/OlgaKalinina101/victor_ai_backend/blob/main/docs/autorization%26users.md

  Этот проект распространяется под лицензией **GNU Affero General Public License v3.0** (AGPL-3.0). Это значит, что если вы хотите забрать что-то в коммерческий проект, вы обязаны дать пользователям ссылку на этот репозиторий, чтобы каждый из них имел возможность развернуть своего близкого самостоятельно.
Подробности — в файле [LICENSE](./LICENSE.txt)

---

## Экраны

### **Чат с AI** — диалог с потоковой генерацией (SSE) и function calling

- **Потоковый ответ** — Server-Sent Events (SSE) для реал-тайм генерации  
- **Голосовой ввод** — распознавание речи через Android SpeechRecognizer  
- **Мультимодальность** — отправка изображений с автоматическим сжатием EXIF  
- **История с пагинацией** — бесконечная прокрутка через Room + Retrofit  
- **Поиск по истории** — полнотекстовый поиск с подсветкой результатов  
- **Emoji-реакции** — возможность добавить эмоцию к сообщениям  

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="docs/example/chat_1.gif" width="280" alt="Чат 1" /><br/>
        <sub>Чат 1</sub>
      </td>
      <td align="center">
        <img src="docs/example/chat_2.gif" width="280" alt="Чат 2" /><br/>
        <sub>Чат 2</sub>
      </td>
      <td align="center">
        <img src="docs/example/chat_3.gif" width="280" alt="Чат 3" /><br/>
        <sub>Чат 3</sub>
      </td>
      <td align="center">
        <img src="docs/example/chat_4.gif" width="280" alt="Чат 4" /><br/>
        <sub>Чат 4</sub>
      </td>
    </tr>
  </table>
</div>

---

### Места — исследование локаций с геймификацией и достижениями

- **Custom Canvas Renderer** — собственный движок отрисовки карт  
- **POI Discovery** — поиск интересных мест вокруг пользователя  
- **Геймификация**:  
  - Система достижений за посещение локаций  
  - Отслеживание пройденного расстояния  
  - Эмоциональные метки для посещенных мест  
- **GPS режим + сохраненные локации** — переключение между реальной позицией и избранными местами  
- **Walk Sessions** — отслеживание прогулок с метриками  

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="docs/example/places_4.gif" width="280" alt="Открытие карты" /><br/>
        <sub>Открытие карты</sub>
      </td>
      <td align="center">
        <img src="docs/example/places_2.gif" width="280" alt="Исследование POI" /><br/>
        <sub>Исследование POI</sub>
      </td>
      <td align="center">
        <img src="docs/example/places_3.gif" width="280" alt="Режим поиска" /><br/>
        <sub>Режим поиска</sub>
      </td>
    </tr>
  </table>
</div>

---

### Системное — внутренний экран состояния AI

- Мониторинг токенов  
- Мониторинг эмоционального состояния AI (2D-поле, евклидовы координаты: https://github.com/OlgaKalinina101/victor_ai_backend/blob/main/core/persona/emotional/logic.md)  
- Управление сохраненной памятью  
- Настройка вибрации и громкости в чате  

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="docs/example/system_1.gif" width="280" alt="Системное 1" /><br/>
        <sub>Системное 1</sub>
      </td>
      <td align="center">
        <img src="docs/example/system_2.gif" width="280" alt="Системное 2" /><br/>
        <sub>Системное 2</sub>
      </td>
      <td align="center">
        <img src="docs/example/system_3.gif" width="280" alt="Системное 3" /><br/>
        <sub>Системное 3</sub>
      </td>
    </tr>
  </table>
</div>

---

### Плейлист — адаптивный подбор музыки на основе контекста и настроения

- **Adaptive Wave** — AI-алгоритм подбора музыки на основе:  
  - Контекста диалога  
  - Важных воспоминаний  
- **Foreground Service** — стабильное воспроизведение с MediaSession  
- **ExoPlayer** — профессиональный аудиоплеер с кэшированием  
- **Фильтрация** — по энергетике, температуре, исполнителю  
- **Streaming + Offline** — воспроизведение из сети и локального кэша  

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="docs/example/playlist_2.gif" width="280" alt="Bottom sheet с треками" /><br/>
        <sub>Bottom sheet с треками</sub>
      </td>
      <td align="center">
        <img src="docs/example/playlist_3.gif" width="280" alt="История моментов" /><br/>
        <sub>История моментов</sub>
      </td>
      <td align="center">
        <img src="docs/example/playlist_4.gif" width="280" alt='AI-подбор "Выбери сам"' /><br/>
        <sub>AI-подбор «Выбери сам»</sub>
      </td>
    </tr>
  </table>
</div>

---

### Календарь

##### ⏰ **Умные напоминания** — контекстно-зависимые уведомления с эмоциональным контекстом  
##### 📅 **Календарь с будильниками** — музыкальные будильники с автоподбором треков  

- **Напоминания с повторением** — еженедельные уведомления  
- **Музыкальные будильники** — пробуждение под любимую музыку  
- **Full Screen Intent** — надежное срабатывание на Android 14+  
- **Battery Optimization** — запрос на отключение оптимизации для точности  
- **Push-уведомления** — через Pushy SDK (альтернатива FCM)  

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="docs/example/calendar_1.jpg" width="280" alt="Calendar — расписание" /><br/>
        <sub>Calendar — расписание</sub>
      </td>
      <td align="center">
        <img src="docs/example/calendar_2.jpg" width="280" alt="Постоянные напоминалки" /><br/>
        <sub>Постоянные напоминалки</sub>
      </td>
      <td align="center">
        <img src="docs/example/calendar_3.gif" width="280" alt="Будильники" /><br/>
        <sub>Будильники</sub>
      </td>
    </tr>
  </table>
</div>


### Экосистема проекта

Это приложение работает в связке с **self-hosted бэкендом** (отдельный репозиторий):
- 🖥️ **Backend**: FastAPI + PostgreSQL (ваш личный сервер/ноутбук)
- 📱 **Android App**: Это приложение (полноценный нативный клиент)
- 🌐 **Web Interface**: Браузерная версия для десктопа

> **Важно**: Для работы приложения нужен запущенный бэкенд. Вы можете развернуть его локально или на любом хостинге. Инструкции в репозитории бэкенда - [https://github.com/OlgaKalinina101/victor_ai_backend  ](https://github.com/OlgaKalinina101/victor_ai_backend/blob/main/README.md).

В разработке:
- 🏦 **Care Bank** — интеграция с доставками еды/подарков через WebView
- 🏠 **IoT интеграция** — присутствие в умном доме, переключение устройств

---

## 🛠️ Технологии

### Core Stack

| Технология | Версия | Назначение |
|------------|--------|------------|
| **Kotlin** | 2.0.21 | Основной язык разработки |
| **Jetpack Compose** | 1.9.x | Современный declarative UI |
| **Hilt** | 2.51.1 | Dependency Injection |
| **Coroutines** | Native | Асинхронность и потоки |
| **Flow & StateFlow** | Native | Реактивное программирование |

### Data Layer

| Библиотека | Назначение |
|------------|------------|
| **Room** | 2.7.0-alpha09 (Kotlin 2.0 compatible) |
| **Retrofit** | 2.9.0 + OkHttp 4.12.0 |
| **Moshi** | 1.15.1 (JSON parsing) |
| **DataStore** | 1.1.1 (Preferences) |

### Media & Location

| Библиотека | Назначение |
|------------|------------|
| **ExoPlayer (Media3)** | 1.2.0 — Музыкальный плеер |
| **Coil** | 2.5.0 — Загрузка и кэширование изображений |
| **Google Play Services Location** | 21.0.1 — Геолокация |
| **Maps Utils** | 3.0.0 — Утилиты для карт |

### UI & Animation

| Библиотека | Назначение |
|------------|------------|
| **Material Design 3** | 1.3.0 — Современный дизайн |
| **Material Icons Extended** | Полный набор иконок |
| **Navigation Compose** | 2.9.5 — Навигация |
| **Compose Animation** | Native — Анимации |

### Notifications & Background

| Библиотека | Назначение |
|------------|------------|
| **Pushy SDK** | 1.0.80 — Push-уведомления |
| **AlarmManager** | Native — Точные будильники |
| **WorkManager** | Отложенные задачи |

---

## 🏗️ Архитектура

### Архитектурный паттерн: **Clean Architecture + MVVM**

```
┌─────────────────────────────────────────────────────┐
│                 UI Layer (Compose)                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────┐  │
│  │ MainActivity │  │ ChatScreen   │  │ MapScreen│  │
│  └──────────────┘  └──────────────┘  └──────────┘  │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│              ViewModel Layer (@HiltViewModel)       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────┐  │
│  │ChatViewModel │  │PlaylistVM    │  │ MapVM    │  │
│  └──────────────┘  └──────────────┘  └──────────┘  │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│           Repository Layer (@Singleton)             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────┐  │
│  │ChatRepository│  │AlarmRepo     │  │ StatsRepo│  │
│  └──────────────┘  └──────────────┘  └──────────┘  │
└────────────┬────────────────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────────────────┐
│              Data Sources Layer                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────┐  │
│  │ Room (Local) │  │Retrofit (API)│  │DataStore │  │
│  └──────────────┘  └──────────────┘  └──────────┘  │
└─────────────────────────────────────────────────────┘
```

### Принципы проектирования

#### ✅ **Dependency Injection (Hilt)**

- **Все зависимости через @Inject** — никаких Factory классов
- **NetworkModule** — единый источник истины для Retrofit/OkHttp
- **@HiltViewModel** — автоматическое создание ViewModels
- **@EntryPoint** — доступ к Hilt из Singleton objects (UserProvider)

#### ✅ **Single Source of Truth**

- **Room** — локальная БД как единственный источник истины
- **Синхронизация с бэкендом** — pull-to-refresh + background sync
- **BuildConfig** — все конфигурации из gradle.properties

#### ✅ **Reactive Programming**

- **StateFlow** — для UI state с автоматическими апдейтами
- **Flow** — для стримов данных из Room и API
- **LaunchedEffect** — для side-effects в Composables

#### ✅ **Error Handling**

- **Result<T>** — явная обработка успеха/ошибки
- **Sealed classes** — для UI состояний (Loading, Success, Error)
- **Try-Catch-Log** — логирование только в debug-режиме

---

## 📂 Структура проекта

```
Victor_AI/
├── app/src/main/java/com/example/victor_ai/
│   ├── MainActivity.kt                    # Entry point
│   ├── MyApp.kt                          # Application class (@HiltAndroidApp)
│   │
│   ├── auth/                             # 🔐 Авторизация
│   │   ├── UserProvider.kt               # Singleton для auth state
│   │   └── screens/                      # UI для регистрации/логина
│   │
│   ├── data/                             # 📊 Data Layer
│   │   ├── local/                        # Room Database
│   │   │   ├── AppDatabase.kt
│   │   │   ├── dao/                      # Data Access Objects
│   │   │   └── entity/                   # Database entities
│   │   │
│   │   ├── network/                      # 🌐 Retrofit API
│   │   │   ├── ApiService.kt
│   │   │   ├── dto/                      # Data Transfer Objects
│   │   │   └── MusicApi.kt, PlacesApi.kt, etc.
│   │   │
│   │   ├── repository/                   # 🗂️ Repositories
│   │   │   ├── ChatRepository.kt
│   │   │   ├── AlarmRepository.kt
│   │   │   └── MemoryRepository.kt
│   │   │
│   │   └── location/                     # 📍 Location services
│   │       └── LocationProvider.kt
│   │
│   ├── di/                               # 💉 Dependency Injection
│   │   ├── NetworkModule.kt              # Retrofit, OkHttp
│   │   ├── AppModule.kt                  # Repos, Managers
│   │   └── AuthEntryPoint.kt             # EntryPoint for singletons
│   │
│   ├── domain/                           # 🎯 Business Logic
│   │   ├── model/                        # Domain models
│   │   └── playback/                     # Playback controller
│   │
│   ├── logic/                            # 🧠 Business Logic Layer
│   │   ├── AudioPlayer.kt                # ExoPlayer wrapper
│   │   ├── ReminderManager.kt            # Reminder orchestration
│   │   ├── StreamingMessage.kt           # SSE processing
│   │   └── carebank/                     # Care Bank logic
│   │
│   ├── ui/                               # 🎨 UI Layer (Compose)
│   │   ├── chat/                         # 💬 Chat screen
│   │   │   ├── ChatBox.kt
│   │   │   ├── ChatViewModel.kt
│   │   │   └── components/
│   │   │
│   │   ├── playlist/                     # 🎵 Music player
│   │   │   ├── PlaylistViewModel.kt
│   │   │   └── components/
│   │   │
│   │   ├── map/                          # 🗺️ Map screen
│   │   │   ├── MapActivity.kt
│   │   │   ├── MapViewModel.kt
│   │   │   ├── canvas/                   # Custom map renderer
│   │   │   └── composables/
│   │   │
│   │   ├── screens/                      # 📱 Other screens
│   │   │   ├── CalendarScreen.kt
│   │   │   ├── BrowserScreen.kt
│   │   │   └── calendar/
│   │   │
│   │   └── theme/                        # 🎨 Material Theme
│   │
│   ├── alarm/                            # ⏰ Alarm system
│   │   ├── AlarmScheduler.kt
│   │   └── AlarmRingActivity.kt
│   │
│   └── service/                          # 🔔 Foreground Services
│       ├── MusicPlaybackService.kt
│       └── AlarmService.kt
│
├── app/build.gradle.kts                  # Build config
├── gradle.properties                     # API_BASE_URL, etc.
└── local.properties                      # Keystore credentials
```

---

## 🚀 Установка

### ⚠️ Предварительные требования

**Для работы приложения ОБЯЗАТЕЛЬНО нужен запущенный бэкенд!**

1. Сначала разверните бэкенд Victor AI (см. репозиторий бэкенда)
2. Получите `DEMO_KEY` для доступа к API
3. Настройте `API_BASE_URL` на ваш сервер (локальный или удалённый)

### Требования для разработки

- **Android Studio**: Hedgehog (2023.1.1) или новее
- **JDK**: 11+
- **Android SDK**: API 26 (Android 8.0) — API 35
- **Gradle**: 8.13+
- **Kotlin**: 2.0.21
- **Запущенный бэкенд Victor AI** с доступным API endpoint

### Быстрый старт

#### 1. Клонирование репозитория

```bash
git clone https://github.com/your-username/Victor_AI.git
cd Victor_AI
```

#### 2. Настройка конфигурации

**Создайте `local.properties` в корне проекта:**

```properties
sdk.dir=/path/to/your/Android/sdk
```

**Настройте `gradle.properties`:**

```properties
# Backend API URL (замените на ваш ngrok или production URL)
API_BASE_URL=https://your-backend.ngrok-free.dev/

# Test user ID (для разработки)
TEST_USER_ID=test_user

# Demo key (получите у автора проекта)
DEMO_KEY=your_demo_key_here
```

#### 3. Синхронизация проекта

```bash
# В Android Studio
File → Sync Project with Gradle Files

# Или через командную строку
./gradlew clean build
```

#### 4. Запуск приложения

**Debug режим:**

```bash
./gradlew installDebug
```

**Release режим:**

```bash
./gradlew assembleRelease
# APK будет в: app/build/outputs/apk/release/
```

---

## 🔑 Конфигурация

### Build Types

#### Debug

- **Логирование**: Включено (OkHttp Body)
- **Минификация**: Отключена
- **BuildConfig.ENABLE_LOGGING**: `true`

#### Release

- **Минификация**: ProGuard + R8
- **Obfuscation**: Включен
- **Логирование**: Отключено
- **BuildConfig.ENABLE_LOGGING**: `false`
- **Подпись**: Release keystore (см. `local.properties`)

### Build Config Fields

```kotlin
buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
buildConfigField("String", "DEMO_KEY", "\"your_key\"")
buildConfigField("String", "TEST_USER_ID", "\"test_user\"")
buildConfigField("boolean", "ENABLE_LOGGING", "false") // false в release
```

---

## 📚 Документация

### Основные гайды

- **[QUICK_START.md](./QUICK_START.md)** — Быстрая сборка и раскатка APK
- **[BUILD_RELEASE_APK.md](./BUILD_RELEASE_APK.md)** — Детальная инструкция по релизу
- **[CONFIG_GUIDE.md](./CONFIG_GUIDE.md)** — Настройка конфигурации
- **[LOCAL_REPOSITORIES_GUIDE.md](./LOCAL_REPOSITORIES_GUIDE.md)** — Работа с локальными репозиториями

### Архитектурные документы

- **[docs\README.md](docs/README.md)** — Свод по документации с архитектурой каждого экрана

---

## 🏛️ Архитектурные подходы

### 1. **Hilt Dependency Injection**

Все зависимости управляются через Hilt без Factory классов:

```kotlin
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    @StreamingApi private val streamingApi: ApiService
) : ViewModel()
```

### 2. **Repository Pattern**

Репозитории инкапсулируют логику работы с данными:

```kotlin
@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val chatApi: ChatApi
) {
    suspend fun syncWithBackend(): Result<List<ChatMessage>> {
        // 1. Fetch from API
        // 2. Save to Room
        // 3. Return local data
    }
}
```

### 3. **StateFlow для UI State**

Reactive UI updates через Kotlin Flow:

```kotlin
private val _uiState = MutableStateFlow(UiState())
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// В Composable
val state by viewModel.uiState.collectAsState()
```

### 4. **SSE (Server-Sent Events)**

Потоковая генерация ответов от AI:

```kotlin
suspend fun streamChat(message: String, onEvent: (Map<String, Any>) -> Unit) {
    val response = api.streamResponse(message)
    response.body()?.byteStream()?.bufferedReader()?.use { reader ->
        reader.forEachLine { line ->
            if (line.startsWith("data: ")) {
                val json = parseJson(line.substring(6))
                onEvent(json)
            }
        }
    }
}
```

### 5. **Custom Canvas Rendering**

Собственный движок отрисовки карт для производительности:

```kotlin
Canvas(modifier = Modifier.fillMaxSize()) {
    // Рисуем фон
    drawBackgroundElements(bounds, elements)
    
    // Рисуем POI
    drawPOIs(pois, userLocation)
    
    // Рисуем путь
    drawPath(trail, color = Color.Blue)
}
```

---

## 🎯 Особенности реализации

### ✅ Оптимизация производительности

1. **Пагинация чата** — загрузка по 25 сообщений
2. **LazyColumn** — виртуализация списков
3. **Image compression** — сжатие фото до 800x800 с удалением EXIF
4. **Coil caching** — агрессивное кэширование изображений
5. **Room indices** — быстрый поиск по БД

### ✅ Надежность

1. **Offline-first** — приложение работает без интернета
2. **Retry logic** — автоповтор запросов при сбоях
3. **Error boundaries** — graceful degradation при ошибках
4. **Crash reporting** — логирование в debug-режиме

### ✅ Безопасность

1. **ProGuard + R8** — обфускация кода в release
2. **No hardcoded secrets** — все через BuildConfig
3. **HTTPS only** — NetworkSecurityConfig
4. **Logs disabled** — в production нет логов

### ✅ UX

1. **Material Design 3** — современный дизайн
2. **Smooth animations** — плавные переходы (Compose Animation)
3. **Haptic feedback** — тактильный отклик
4. **Dark theme** — поддержка темной темы

---

## 🐛 Известные ограничения

1. **Maps API** — требуется стабильное интернет-соединение для загрузки карт
2. **Music streaming** — высокое потребление трафика без кэширования
3. **Battery consumption** — Foreground Service + GPS могут разряжать батарею
4. **Android 14+** — требуется ручной запрос Full Screen Intent permission

---

## 🔧 Отладка

### Логирование

**Debug build** — все логи включены:

```bash
adb logcat | grep "Victor\|ChatVM\|MapVM"
```

**Release build** — логи отключены (ENABLE_LOGGING=false)

### ProGuard mapping

Деобфускация stack trace:

```bash
# mapping.txt находится в:
app/build/outputs/mapping/release/mapping.txt

# В Android Studio:
Build → Analyze APK → Load ProGuard mapping
```

### Network debugging

**OkHttp Interceptor** в debug-режиме:

```kotlin
HttpLoggingInterceptor().apply {
    level = if (BuildConfig.ENABLE_LOGGING) Level.BODY else Level.NONE
}
```

---

## 📊 Метрики проекта

- **Языки**: Kotlin (100%)
- **Строк кода**: ~30,000+ LOC
- **ViewModels**: 10+
- **Repositories**: 8+
- **Compose Screens**: 15+
- **API endpoints**: 20+
- **Database tables**: 6+
- **Gradle modules**: 1 (monolithic)

---

## 📄 Лицензия

Этот проект распространяется под лицензией **GNU Affero General Public License v3.0 (AGPL-3.0)**.

---

## TODO

| Приоритет | Задача            | Описание                         |
|-----------|-------------------|----------------------------------|
| P2        | Жирный ViewModel  | Вынести логику в UseCases       |
| P2        | Domain + Moshi    | Отделить DTO от domain models   |
| P3        | Room миграции     | Включить exportSchema, миграции |

---

<div align="center">

**Made with ❤️ using Jetpack Compose**

</div>
