plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    // Add this line to apply kapt in Kotlin DSL
    id("org.jetbrains.kotlin.kapt")
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    implementation(libs.androidx.core.ktx)

    implementation(libs.viewmodel)
    implementation(libs.livedata)
    implementation(libs.lifecycle.runtime)
    implementation(libs.savedstate)

    // Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Kotlin Coroutines dependencies
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    // Retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson) // Use Gson converter

    // OkHttp dependencies
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Glide dependencies
    implementation(libs.glide)
    kapt(libs.glide.compiler) // Use kapt for annotation processing

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}