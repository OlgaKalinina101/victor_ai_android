plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.victor_ai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.victor_ai"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.compose.material.core)
    implementation(libs.material3)
    implementation(libs.androidx.runtime)
    implementation(libs.places)
    implementation(libs.foundation)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.ui.text)
    implementation(libs.runtime)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.compose.runtime.runtime)
    implementation(libs.ui.text)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.cronet.embedded)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.ads.mobile.sdk)
    implementation(libs.androidx.compose.ui.ui.graphics)
    implementation(libs.androidx.compose.foundation.foundation)
    implementation(libs.androidx.animation)
    implementation(libs.androidx.animation.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0") // для поддержки gif

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // OkHttp (для логов)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Moshi (JSON)
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

    // Зависимости для FCM
    //implementation("com.google.firebase:firebase-messaging:24.0.0")

    // Pushy SDK
    implementation("me.pushy:sdk:1.0.80")

    implementation("androidx.compose.material3:material3:1.3.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.compose.material:material-icons-extended")

    implementation("com.google.android.gms:play-services-location:21.0.1")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // ExoPlayer - для стабильного воспроизведения музыки
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-session:1.2.0")
    implementation("androidx.media3:media3-common:1.2.0")

    implementation("androidx.navigation:navigation-compose:2.9.5")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Hilt + ViewModel
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Если используешь ViewModel в Activity
    implementation("androidx.activity:activity-compose:1.9.0")

    implementation("com.google.maps.android:android-maps-utils:3.0.0")

    // JSON парсинг (оставляем Gson для работы с данными)
    implementation("com.google.code.gson:gson:2.10.1")

    // Room (обновлена версия для совместимости с Kotlin 2.0+)
    implementation("androidx.room:room-runtime:2.7.0-alpha09")
    implementation("androidx.room:room-ktx:2.7.0-alpha09")
    kapt("androidx.room:room-compiler:2.7.0-alpha09")
}
