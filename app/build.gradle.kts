import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        getByName("debug") {
            val keystoreProperties = Properties().apply {
                val file = rootProject.file("local.properties")
                if (file.exists()) {
                    load(file.inputStream())
                }
            }
            val keyStorePath = keystoreProperties.getProperty("DEBUG_KEYSTORE_PATH")
            storeFile = if (keyStorePath != null) {
                file(keyStorePath)
            } else {
                file("${System.getProperty("user.home")}/.android/debug.keystore")
            }
        }
    }
    namespace = "com.trip.myapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.trip.myapp"
        minSdk = 28
        targetSdk = 35
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // google fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // material icon
    api(libs.androidx.material.icons)
    api(libs.androidx.material.icons.android)

    // hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)

    // hilt navigation compose
    implementation(libs.androidx.hilt.navigation.compose)

    // navigation compose
    implementation(libs.androidx.navigation.compose)

    // serialization
    implementation(libs.kotlinx.serialization.json.jvm)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ui.auth)
    implementation(libs.play.services.auth)
    implementation("com.google.firebase:firebase-firestore")
}
