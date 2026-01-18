# 🚀 Как собрать Release APK для раскатки

## Быстрый старт

```bash
# 1. Очистить проект
./gradlew clean

# 2. Собрать release APK
./gradlew assembleRelease

# 3. APK будет здесь:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## ⚙️ Что включено в Release

### ✅ Автоматически включается:
- **ProGuard обфускация** - код не читается
- **Удаление неиспользуемых ресурсов** - APK меньше на ~30%
- **Все Log.* удалены** - 0 логов в production
- **HTTP logging отключен** - нет утечки данных
- **Оптимизация кода** - быстрее работает

### ❌ Отключено в Release:
- Debug логи
- HTTP request/response logging
- Stack traces в логах
- Debug информация

---

## 🔑 Подписание APK (опционально)

### Если у тебя уже есть keystore:
```bash
# Подпиши APK
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore /path/to/your.keystore \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  your-key-alias

# Выровняй APK (zipalign)
zipalign -v 4 app-release-unsigned.apk app-release.apk
```

### Если keystore нет - создай:
```bash
keytool -genkey -v \
  -keystore victor-ai.keystore \
  -alias victor-ai \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**⚠️ ВАЖНО:** Сохрани keystore и пароль! Без них не сможешь обновить приложение!

---

## 🔧 Настройка автоматического подписания

Добавь в `app/build.gradle.kts`:

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/your.keystore")
            storePassword = "your-store-password"
            keyAlias = "your-key-alias"
            keyPassword = "your-key-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // ... остальные настройки
        }
    }
}
```

**⚠️ НЕ КОММИТЬ пароли в git!** Используй `local.properties`:

```properties
# local.properties
RELEASE_STORE_FILE=/path/to/keystore
RELEASE_STORE_PASSWORD=your-password
RELEASE_KEY_ALIAS=your-alias
RELEASE_KEY_PASSWORD=your-password
```

И читай в `build.gradle.kts`:

```kotlin
val keystorePropertiesFile = rootProject.file("local.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["RELEASE_STORE_FILE"] as String)
            storePassword = keystoreProperties["RELEASE_STORE_PASSWORD"] as String
            keyAlias = keystoreProperties["RELEASE_KEY_ALIAS"] as String
            keyPassword = keystoreProperties["RELEASE_KEY_PASSWORD"] as String
        }
    }
}
```

---

## 📦 Проверка APK

### Размер APK:
```bash
ls -lh app/build/outputs/apk/release/app-release.apk
```

Ожидаемо: **35-45 MB** (с ProGuard)

### Проверка подписи:
```bash
jarsigner -verify -verbose -certs app-release.apk
```

### Анализ APK:
В Android Studio: **Build → Analyze APK...**

Проверь:
- Размер APK
- Что ProGuard сработал (классы обфусцированы)
- Нет лишних ресурсов

---

## 🧪 Тестирование Release APK

### 1. Установи на реальное устройство:
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

### 2. Проверь основные функции:
- ✅ Авторизация работает
- ✅ Чат работает
- ✅ Музыка играет
- ✅ Будильник срабатывает
- ✅ Карта открывается
- ✅ Нет крашей

### 3. Проверь логи (должны быть пустые):
```bash
adb logcat | grep "Victor\|OkHttp\|Retrofit"
```

**Ожидаемо:** Никаких логов от приложения!

---

## 🐛 Если что-то сломалось

### ProGuard удалил нужный класс:
Добавь в `proguard-rules.pro`:
```proguard
-keep class com.example.your.class.** { *; }
```

### Краш в release, но работает в debug:
1. Проверь ProGuard mapping: `app/build/outputs/mapping/release/mapping.txt`
2. Деобфусцируй stack trace: **Build → Analyze Stack Trace...**

### APK слишком большой:
1. Проверь `isShrinkResources = true` включен
2. Удали неиспользуемые ресурсы из `res/`
3. Используй WebP вместо PNG
4. Включи App Bundle вместо APK

---

## 📤 Раскатка на друзей

### Вариант 1: Прямая установка
1. Отправь APK через Telegram/WhatsApp
2. Друзья скачивают и устанавливают
3. Нужно включить "Установка из неизвестных источников"

### Вариант 2: Firebase App Distribution
```bash
# Установи Firebase CLI
npm install -g firebase-tools

# Залогинься
firebase login

# Загрузи APK
firebase appdistribution:distribute \
  app/build/outputs/apk/release/app-release.apk \
  --app YOUR_FIREBASE_APP_ID \
  --groups "testers" \
  --release-notes "Первая версия для тестирования"
```

### Вариант 3: Google Drive / Dropbox
1. Загрузи APK на Drive
2. Поделись ссылкой с друзьями
3. Они скачивают и устанавливают

---

## 🔒 Безопасность

### ✅ Что защищено:
- Код обфусцирован ProGuard
- Логи удалены
- Токены не логируются
- HTTP logging отключен

### ⚠️ Что еще можно улучшить:
- Включить EncryptedSharedPreferences для токенов
- Добавить certificate pinning
- Добавить root detection
- Настроить Crashlytics для мониторинга

---

## 📊 Чеклист перед раскаткой

- [ ] Собрал release APK
- [ ] Подписал APK (если нужно)
- [ ] Протестировал на реальном устройстве
- [ ] Проверил размер APK (~35-45 MB)
- [ ] Проверил что логи не выводятся
- [ ] Проверил основные функции
- [ ] Обновил версию в `build.gradle.kts` (versionCode, versionName)
- [ ] Создал release notes для друзей

---

## 🎉 Готово!

Теперь можешь раскатывать APK на друзей!

**Важно:**
- Сохрани keystore и пароли
- Сохрани ProGuard mapping для деобфускации крашей
- Собирай feedback от друзей
- Мониторь ошибки (если настроил Crashlytics)

---

**Удачи! 🚀**

