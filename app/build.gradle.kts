import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt) // Apply Hilt plugin
    id("org.jetbrains.kotlin.kapt") // Direct application
    alias(libs.plugins.kotlinxSerialization)
    id("kotlin-parcelize")

}

android {
    namespace = "com.amolina.mapscompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.amolina.mapscompose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Load secrets.properties explicitly
        val secretsFile = rootProject.file("secrets.properties")
        if (secretsFile.exists()) {
            val secrets = Properties()
            secrets.load(FileInputStream(secretsFile))
            manifestPlaceholders["GOOGLE_MAPS_API_KEY"] = secrets["GOOGLE_MAPS_API_KEY"] ?: ""
        } else {
            throw GradleException("secrets.properties file not found!")
        }

        println("Google Maps API Key: ${manifestPlaceholders["GOOGLE_MAPS_API_KEY"]}")

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
    implementation(libs.google.maps.services)
    implementation(libs.google.maps.compose)
    implementation(libs.accompanist.permissions)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler) // For annotation processing

    // Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Jetpack Compose integration with Hilt
    implementation(libs.hilt.compose)

    implementation(libs.java.poet)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.gson)
}

hilt {
    enableAggregatingTask = false
}

kapt {
    correctErrorTypes = true
}