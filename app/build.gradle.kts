plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.alienmantech.azure_nebula"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alienmantech.azure_nebula"
        minSdk = 28
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${getExtra("kotlinVersion")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${getExtra("coroutinesVersion")}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

//    implementation("androidx.core:core-ktx:1.6.0")
//    implementation("androidx.appcompat:appcompat:1.3.1")
//    implementation("com.google.android.material:material:1.4.0")
//    implementation("androidx.compose.ui:ui:1.0.5")
//    implementation("androidx.compose.material:material:1.0.5")
//    implementation("androidx.compose.ui:ui-tooling-preview:1.0.5")
//    implementation("androidx.activity:activity-compose:1.3.1")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
//    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")

    // Jetpack Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.05.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.navigation:navigation-compose:2.5.3") // Do NOT upgrade to 2.6.0 yet, deprecation on BackstackEntry.arguments usage
    implementation("androidx.compose.material:material-icons-extended:1.6.0-alpha02")

    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Compose Helper Libraries
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
//    ksp("androidx.room:room-compiler:$room_version")

//    implementation("androidx.room:room-runtime:2.3.0")
//    kapt("androidx.room:room-compiler:2.3.0")
//    implementation("androidx.room:room-ktx:2.3.0")
}

fun getExtra(key: String): String {
    return rootProject.extra[key] as String
}