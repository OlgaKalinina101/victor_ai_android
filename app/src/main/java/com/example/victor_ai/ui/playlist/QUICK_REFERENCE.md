# 🎵 Плейлист — Быстрая шпаргалка

## 🎯 Как добавить новую функцию?

### 1️⃣ Добавить поле в state

```kotlin
// PlaylistUiState.kt
data class PlaylistUiState(
    // ... существующие поля
    val myNewField: String = ""  // ← Добавь сюда
)
```

### 2️⃣ Обновлять в ViewModel

```kotlin
// PlaylistViewModel.kt
fun updateMyNewField(value: String) {
    _uiState.update { it.copy(myNewField = value) }
}
```

### 3️⃣ Использовать в UI

```kotlin
// PlaylistScreen.kt
val uiState by viewModel.uiState.collectAsState()

Text(text = uiState.myNewField)  // ← Читай отсюда

Button(onClick = { 
    viewModel.updateMyNewField("новое значение")  // ← Обновляй через VM
})
```

---

## 📝 Частые задачи

### Добавить новый фильтр

```kotlin
// 1. Добавь в PlaylistUiState
val genreFilter: String? = null

// 2. Добавь метод в ViewModel
fun updateGenreFilter(genre: String?) {
    _uiState.update { it.copy(genreFilter = genre) }
}

// 3. Обнови фильтрацию в startUiStateUpdater()
val filtered = tracksWithCache.filter { track ->
    (state.energyFilter == null || track.energyDescription == state.energyFilter) &&
    (state.genreFilter == null || track.genre == state.genreFilter)  // ← Добавь
}

// 4. Добавь UI в PlaylistSheet
DropdownMenu(
    items = uiState.genreOptions,
    onSelect = { viewModel.updateGenreFilter(it) }
)
```

### Добавить новое действие с треком

```kotlin
// 1. Добавь метод в ViewModel
fun shareTrack(trackId: Int) {
    viewModelScope.launch {
        try {
            val track = _rawTracks.value.find { it.id == trackId }
            // ... логика шаринга
        } catch (e: Exception) {
            _uiState.update { it.copy(error = ErrorState.ShareError(e.message)) }
        }
    }
}

// 2. Используй в UI
IconButton(onClick = { viewModel.shareTrack(track.id) }) {
    Icon(Icons.Default.Share, "Share")
}
```

### Добавить новый тип streaming события

```kotlin
// 1. В MusicApi добавь обработку в onEvent callback
event.containsKey("recommendation") -> {
    val recommendation = event["recommendation"] as? String
    _uiState.update { it.copy(recommendation = recommendation) }
}

// 2. Бэкенд отправляет
{"recommendation": "Попробуй также послушать джаз!"}
```

---

## 🐛 Частые ошибки

### ❌ Логика в Composable

```kotlin
// ❌ ПЛОХО
@Composable
fun MyScreen() {
    val filtered = tracks.filter { it.energy == "High" }  // Логика в UI!
}

// ✅ ХОРОШО
@Composable
fun MyScreen() {
    val uiState by viewModel.uiState.collectAsState()
    // Фильтрация уже сделана в ViewModel
    LazyColumn {
        items(uiState.tracks) { ... }
    }
}
```

### ❌ Remember для бизнес-логики

```kotlin
// ❌ ПЛОХО
var selectedFilter by remember { mutableStateOf<String?>(null) }

// ✅ ХОРОШО
val uiState by viewModel.uiState.collectAsState()
Text(text = uiState.energyFilter ?: "Все")
```

### ❌ LazyColumn без key

```kotlin
// ❌ ПЛОХО
items(tracks) { track -> ... }  // Плохая производительность

// ✅ ХОРОШО
items(tracks, key = { it.id }) { track -> ... }  // Эффективная рекомпозиция
```

---

## 📊 Отладка

### Включить детальные логи

```bash
adb logcat -s PlaylistViewModel:D MusicApiImpl:D AudioPlayer:D
```

### Проверить streaming

```kotlin
// Смотри в логах:
🎵 Starting streaming wave  // ← Запрос отправлен
📡 Response received        // ← Ответ получен
📝 Stream log: ...          // ← Каждый лог
✅ Stream completed         // ← Завершено
```

### Проверить фильтры

```kotlin
// Добавь лог в ViewModel
Log.d(TAG, "Filter updated: energy=${_uiState.value.energyFilter}")
Log.d(TAG, "Filtered tracks count: ${_uiState.value.tracks.size}")
```

---

## 🚀 Performance tips

### 1. Используй remember для вычислений

```kotlin
// ❌ Вычисляется при каждой рекомпозиции
val editingTrack = tracks.firstOrNull { it.id == editingTrackId }

// ✅ Вычисляется только при изменении dependencies
val editingTrack = remember(editingTrackId, tracks) {
    tracks.firstOrNull { it.id == editingTrackId }
}
```

### 2. Избегай тяжёлых операций в item

```kotlin
// ❌ Форматирование в каждом item
items(tracks) { track ->
    Text("Duration: ${formatDuration(track.duration)}")  // Вызывается N раз!
}

// ✅ Форматирование в маппере
// track.toUiModel() уже содержит formattedDuration
items(tracks) { track ->
    Text("Duration: ${track.formattedDuration}")  // Готовая строка
}
```

### 3. Используй derivedStateOf для производных состояний

```kotlin
val hasPlayingTrack by remember {
    derivedStateOf { uiState.currentPlayingTrackId != null }
}
```

---

## 📚 Где что искать?

| Что нужно | Где искать |
|-----------|-----------|
| Добавить поле в state | `PlaylistUiState.kt` |
| Логика работы с треками | `PlaylistViewModel.kt` |
| API запросы | `MusicApi.kt` + `MusicApiImpl.kt` |
| UI главного экрана | `PlaylistScreen.kt` |
| UI списка треков | `PlaylistSheet.kt` |
| UI элемента трека | `components/TrackItemCompact.kt` |
| Форматирование данных | `PlaylistUiState.kt` (маппер `toUiModel()`) |
| Работа с плеером | `logic/AudioPlayer.kt` |
| Foreground service | `logic/MusicPlaybackService.kt` |

---

## 🎨 UI Guidelines

### Цвета

```kotlin
val grayText = Color(0xFFE0E0E0)       // Основной текст
val barEmpty = Color(0xFF555555)      // Неактивные элементы
val barFilled = Color(0xFFCCCCCC)     // Активные элементы
val background = Color(0xFF2B2929)    // Фон
```

### Шрифт

```kotlin
val didactGothic = FontFamily(Font(R.font.didact_gothic))
```

### Размеры

```kotlin
fontSize = 20.sp    // Заголовки
fontSize = 16.sp    // Названия треков
fontSize = 14.sp    // Детали
fontSize = 12.sp    // Timestamps
```



