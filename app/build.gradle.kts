plugins {
    kotlin("kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    alias(libs.plugins.hilt)

    // Add this line to apply kapt in Kotlin DSL
    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "com.githubrepos.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.githubrepos.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "GITHUB_URL", "\"https://api.github.com/search/\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)

    implementation(libs.viewmodel)
    implementation(libs.livedata)
    implementation(libs.savedstate)

    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    ksp(libs.hilt.compiler)

    // Kotlin Coroutines dependencies
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    // Retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson) // Use Gson converter
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)

    // OkHttp dependencies
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Glide dependencies
    implementation(libs.glide)
    ksp(libs.glide.compiler) // Use kapt for annotation processing

    // Multidex
    implementation(libs.androidx.multidex)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.paging.runtime)

    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.chrome.custom.tabs)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.androidx.datastore.core.okio.jvm)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}