import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

// 🔑 Загружаем signing config из local.properties
val keystorePropertiesFile = rootProject.file("local.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.example.victor_ai"
    compileSdk = 35  // Нужно для Compose 1.9.x библиотек

    defaultConfig {
        applicationId = "com.example.victor_ai"
        minSdk = 26  // Android 8.0 (Oreo) - отличное покрытие устройств (~95%)
        targetSdk = 34  // Стабильная версия без новых ограничений Android 15
        versionCode = 2  // ↑ Увеличено для новой версии
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Network base URL (override via gradle.properties: API_BASE_URL=https://xxxx.ngrok-free.dev/)
        val rawApiBaseUrl = (project.findProperty("API_BASE_URL") as String?)?.trim()
        val normalizedApiBaseUrl = (rawApiBaseUrl?.takeIf { it.isNotBlank() }
            ?: "https://pentavalent-conrad-unbreathed.ngrok-free.dev/")
            .let { if (it.endsWith("/")) it else "$it/" }
            .replace("\"", "\\\"")

        buildConfigField("String", "BASE_URL", "\"$normalizedApiBaseUrl\"")

        // Demo key for /auth/resolve (MUST be provided via gradle.properties: DEMO_KEY=...)
        // Default is empty to prevent accidental login with placeholder keys.
        val rawDemoKey = (project.findProperty("DEMO_KEY") as String?)?.trim()
        val normalizedDemoKey = (rawDemoKey?.takeIf { it.isNotBlank() } ?: "")
            .replace("\"", "\\\"")
        buildConfigField("String", "DEMO_KEY", "\"$normalizedDemoKey\"")

        // Test user ID for development/fallback (override via gradle.properties: TEST_USER_ID=...)
        val rawTestUserId = (project.findProperty("TEST_USER_ID") as String?)?.trim()
        val normalizedTestUserId = (rawTestUserId?.takeIf { it.isNotBlank() } ?: "test_user")
            .replace("\"", "\\\"")
        buildConfigField("String", "TEST_USER_ID", "\"$normalizedTestUserId\"")
    }

    // 🔑 Signing configs для release
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["RELEASE_STORE_FILE"] as String? ?: "")
            storePassword = keystoreProperties["RELEASE_STORE_PASSWORD"] as String? ?: ""
            keyAlias = keystoreProperties["RELEASE_KEY_ALIAS"] as String? ?: ""
            keyPassword = keystoreProperties["RELEASE_KEY_PASSWORD"] as String? ?: ""
            
            // ✅ Явно включаем обе схемы подписи для максимальной совместимости
            enableV1Signing = true  // JAR Signature (для старых Android)
            enableV2Signing = true  // Full APK Signature (для Android 7.0+)
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")  // 🔑 Подписываем release APK
            isMinifyEnabled = true  // 🔥 ВКЛЮЧАЕМ обфускацию для production
            isShrinkResources = true  // 🔥 Удаляем неиспользуемые ресурсы
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "ENABLE_LOGGING", "false")  // 🔥 Отключаем логи в release
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            buildConfigField("boolean", "ENABLE_LOGGING", "true")  // ✅ Логи только в debug
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // ═══════════════════════════════════════════════════════════
    // 📦 CORE ANDROID
    // ═══════════════════════════════════════════════════════════
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)

    // ═══════════════════════════════════════════════════════════
    // 🎨 COMPOSE
    // ═══════════════════════════════════════════════════════════
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.runtime.livedata)
    implementation("androidx.compose.material:material-icons-extended")
    
    // ═══════════════════════════════════════════════════════════
    // 🧪 TESTING
    // ═══════════════════════════════════════════════════════════
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ═══════════════════════════════════════════════════════════
    // 🖼️ IMAGE LOADING & PROCESSING
    // ═══════════════════════════════════════════════════════════
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0")
    implementation("androidx.exifinterface:exifinterface:1.3.7")

    // ═══════════════════════════════════════════════════════════
    // 🌐 NETWORKING
    // ═══════════════════════════════════════════════════════════
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // OkHttp (обновлённая версия 4.12.0)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Cronet (experimental HTTP client)
    implementation(libs.cronet.embedded)

    // ═══════════════════════════════════════════════════════════
    // 📝 JSON PARSING
    // ═══════════════════════════════════════════════════════════
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")
    implementation("com.google.code.gson:gson:2.10.1")

    // ═══════════════════════════════════════════════════════════
    // 🔔 PUSH NOTIFICATIONS
    // ═══════════════════════════════════════════════════════════
    implementation("me.pushy:sdk:1.0.80")

    // ═══════════════════════════════════════════════════════════
    // 📍 LOCATION & MAPS
    // ═══════════════════════════════════════════════════════════
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.maps.android:android-maps-utils:3.0.0")
    implementation(libs.places)

    // ═══════════════════════════════════════════════════════════
    // 🎵 MEDIA PLAYBACK
    // ═══════════════════════════════════════════════════════════
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-session:1.2.0")
    implementation("androidx.media3:media3-common:1.2.0")
    implementation("androidx.media:media:1.7.0")

    // ═══════════════════════════════════════════════════════════
    // 🧭 NAVIGATION
    // ═══════════════════════════════════════════════════════════
    implementation("androidx.navigation:navigation-compose:2.9.5")

    // ═══════════════════════════════════════════════════════════
    // 💉 DEPENDENCY INJECTION (HILT)
    // ═══════════════════════════════════════════════════════════
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // ═══════════════════════════════════════════════════════════
    // 💾 DATA PERSISTENCE
    // ═══════════════════════════════════════════════════════════
    // Room (Kotlin 2.0+ compatible)
    implementation("androidx.room:room-runtime:2.7.0-alpha09")
    implementation("androidx.room:room-ktx:2.7.0-alpha09")
    kapt("androidx.room:room-compiler:2.7.0-alpha09")
    
    // DataStore (для preferences)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // ═══════════════════════════════════════════════════════════
    // 🛠️ UTILITIES
    // ═══════════════════════════════════════════════════════════
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    
    // Browser (для Custom Tabs)
    implementation("androidx.browser:browser:1.8.0")
}
