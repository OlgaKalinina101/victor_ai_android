# 📋 Настройка .gitignore для Victor AI

## Что было сделано

Создан полноценный `.gitignore` для Android проекта.

## 🚫 Что теперь игнорируется

### Android Build артефакты
- `*.apk`, `*.aab` - APK и AAB файлы
- `*.dex` - Dalvik executable файлы
- `*.class` - Java class файлы
- `build/` - все build директории
- `app/debug/` - debug APK папка

### Kotlin
- `.kotlin/` - Kotlin compiler cache
- `*.kotlin_module` - Kotlin module файлы

### Gradle
- `.gradle/` - Gradle cache
- `local.properties` - локальная конфигурация SDK

### IDE (Android Studio / IntelliJ)
- `*.iml` - module файлы
- `.idea/workspace.xml` - workspace настройки
- `.idea/gradle.xml` - gradle конфигурация
- `.idea/misc.xml` - разные локальные настройки
- `.idea/compiler.xml` - настройки компилятора
- `.idea/deviceManager.xml` - менеджер устройств
- `.idea/deploymentTargetSelector.xml` - выбор цели развертывания
- `.idea/caches/` - кеши IDE
- `.idea/libraries/` - библиотеки
- `.idea/modules.xml` - модули

### Безопасность
- `*.jks`, `*.keystore` - keystore файлы для подписи
- Crashlytics конфигурация

### OS файлы
- `.DS_Store` - macOS
- `Thumbs.db`, `Desktop.ini` - Windows

## ⚠️ Спорные файлы

### `google-services.json`
**Текущий статус:** закоммичен в git

**Рекомендация:** 
- Если проект **публичный** → удалите из git (содержит Firebase API ключи)
- Если проект **приватный** → можно оставить (для удобства)

**Как удалить:**
```bash
git rm --cached app/google-services.json
git commit -m "chore: remove google-services.json from git"
```

## 🔧 Очистка git от ненужных файлов

Некоторые файлы уже закоммичены в git, но не должны там быть.

### Автоматическая очистка

Запустите один из скриптов:

**PowerShell:**
```powershell
.\cleanup-git.ps1
```

**Batch:**
```batch
cleanup-git.bat
```

### Ручная очистка

```bash
# Удаляем .idea файлы, которые не нужны
git rm --cached .idea/gradle.xml
git rm --cached .idea/compiler.xml
git rm --cached .idea/deploymentTargetSelector.xml
git rm --cached .idea/deviceManager.xml
git rm --cached .idea/misc.xml
git rm --cached .idea/migrations.xml
git rm --cached .idea/studiobot.xml

# Опционально: удаляем google-services.json
# git rm --cached app/google-services.json

# Коммитим изменения
git add .gitignore
git commit -m "chore: update .gitignore and remove tracked files"
```

## ✅ Что оставлено в git (из .idea)

Эти файлы полезны для команды:
- `.idea/codeStyles/` - стиль кода проекта
- `.idea/inspectionProfiles/` - профили инспекции кода
- `.idea/vcs.xml` - настройки VCS
- `.idea/runConfigurations.xml` - конфигурации запуска

## 📝 После очистки

1. Запустите cleanup скрипт
2. Проверьте изменения: `git status`
3. Закоммитьте: `git commit -m "chore: update .gitignore"`
4. Запушьте: `git push`

## 🔍 Проверка

Проверить, что файлы больше не отслеживаются:
```bash
git ls-files | findstr "gradle.xml"
```

Если команда ничего не вернет - файл успешно удален из git!

## 🚀 Для новых разработчиков

После клонирования репозитория создайте `local.properties`:
```properties
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
```

И `app/google-services.json` (если удалили из git) - попросите у команды или скачайте из Firebase Console.
