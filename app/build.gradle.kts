plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.android.room)
    alias(libs.plugins.google.dagger.hilt.android)
}

android {
    namespace = "com.simon.rickandmorty"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.simon.rickandmorty"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }


    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    /* -------------------- Core -------------------- */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    /* -------------------- Compose -------------------- */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.runtime)
    debugImplementation(libs.bundles.compose.debug)

    /* -------------------- Navigation -------------------- */
    implementation(libs.androidx.navigation.compose)

    /* -------------------- Networking -------------------- */
    implementation(libs.bundles.ktor)

    /* -------------------- Image Loading -------------------- */
    implementation(libs.bundles.coil)

    /* -------------------- Database -------------------- */
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    /* -------------------- Dependency Injection -------------------- */
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    /* -------------------- Logging -------------------- */
    implementation(libs.timber)

    /* -------------------- Testing -------------------- */
    testImplementation(libs.junit)
    implementation(libs.kotlinx.collections.immutable)

    implementation("androidx.palette:palette-ktx:1.0.0")


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
