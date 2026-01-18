# 🔧 Руководство по конфигурации проекта

## 📋 Структура конфигов

### `gradle.properties` (корневой)
**Путь:** `Victor_AI/gradle.properties`

Это **единственный** `gradle.properties` в проекте. Содержит все настройки приложения.

```properties
# Backend URL
API_BASE_URL=https://victor-api-olga.ngrok-free.dev/

# Test/Development user ID
TEST_USER_ID=test_user
```

### `local.properties`
**Путь:** `Victor_AI/local.properties`

Локальные настройки (не коммитится в git):
```properties
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk

# Для подписи release APK:
RELEASE_STORE_FILE=path/to/keystore.jks
RELEASE_STORE_PASSWORD=your_store_password
RELEASE_KEY_ALIAS=your_key_alias
RELEASE_KEY_PASSWORD=your_key_password

# Demo key для авторизации:
DEMO_KEY=your_demo_key_here
```

## 🔐 Доступные переменные

### 1. **API_BASE_URL** (обязательная)
Backend URL для всех API запросов.

**Где используется:**
- Retrofit
- API вызовы
- Streaming треков

**Как изменить:**
```properties
API_BASE_URL=https://your-new-api.ngrok-free.dev/
```

**В коде:**
```kotlin
BuildConfig.BASE_URL  // Доступно везде
```

### 2. **TEST_USER_ID** (опциональная, default: `test_user`)
ID пользователя для разработки/тестирования. Используется как fallback когда нет реального аккаунта.

**Где используется:**
- `UserProvider.kt` - fallback при getChatMeta
- `HorizontalScrollMenu.kt` - проверка "является ли пользователь креатором"

**Как изменить:**
```properties
TEST_USER_ID=dev_user_123
```

**В коде:**
```kotlin
BuildConfig.TEST_USER_ID  // Доступно везде
```

### 3. **DEMO_KEY** (обязательная для авторизации)
Ключ для /auth/resolve и регистрации через Web Demo.

**Где хранится:** `local.properties` (не коммитится!)

**Как добавить:**
```properties
DEMO_KEY=your_secret_demo_key
```

**В коде:**
```kotlin
BuildConfig.DEMO_KEY  // Доступно везде
```

## ⚙️ Как это работает

1. **gradle.properties** → Gradle считывает переменные
2. **build.gradle.kts** → Передает через `buildConfigField()`
3. **BuildConfig** → Автоматически генерируется
4. **Kotlin код** → Использует `BuildConfig.VARIABLE_NAME`

## 🚀 Примеры использования

### Смена backend URL для тестирования

```properties
# gradle.properties
API_BASE_URL=https://test-backend.ngrok-free.dev/
```

Пересобираем проект:
```bash
.\gradlew.bat clean assembleDebug
```

### Изменение test user ID

```properties
# gradle.properties
TEST_USER_ID=developer_001
```

Теперь в коде:
```kotlin
UserProvider.getCurrentUserId()  // Вернет "developer_001" если нет реального аккаунта
```

### Добавление новой переменной

**1. Добавь в gradle.properties:**
```properties
MY_NEW_VARIABLE=some_value
```

**2. Добавь в build.gradle.kts:**
```kotlin
val myVar = (project.findProperty("MY_NEW_VARIABLE") as String?)?.trim() ?: "default_value"
buildConfigField("String", "MY_NEW_VARIABLE", "\"$myVar\"")
```

**3. Используй в коде:**
```kotlin
val value = BuildConfig.MY_NEW_VARIABLE
```

## ❓ FAQ

### Что делать после изменения gradle.properties?

1. **Sync Gradle:**
   - Android Studio: File → Sync Project with Gradle Files
   - Или: `.\gradlew.bat --refresh-dependencies`

2. **Rebuild проект:**
   ```bash
   .\gradlew.bat clean assembleDebug
   ```

3. **Пересоберется BuildConfig** с новыми значениями

### Где хранить секреты?

**НЕ коммитьте в git:**
- `local.properties` - для локальных секретов
- Keystore файлы
- API ключи

**Можно коммитить:**
- `gradle.properties` - если проект приватный
- Или используйте переменные окружения для CI/CD

### Как добавить переменную окружения?

Gradle автоматически читает переменные окружения:

```bash
# Windows PowerShell
$env:API_BASE_URL="https://prod-api.example.com/"
.\gradlew.bat assembleRelease

# Linux/Mac
export API_BASE_URL="https://prod-api.example.com/"
./gradlew assembleRelease
```

Приоритет: **Переменные окружения > gradle.properties**

## 📝 Чеклист для нового разработчика

1. ✅ Клонируй репозиторий
2. ✅ Создай `local.properties`:
   ```properties
   sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
   DEMO_KEY=your_demo_key
   ```
3. ✅ Проверь `gradle.properties` - должен быть `API_BASE_URL`
4. ✅ Sync Gradle в Android Studio
5. ✅ Запусти `.\gradlew.bat assembleDebug`


