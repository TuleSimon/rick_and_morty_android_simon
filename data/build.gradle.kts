plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.android.room)
    alias(libs.plugins.google.dagger.hilt.android)
}

android {
    namespace = "com.simon.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    room {
        schemaDirectory("$projectDir/schemas")
    }

}

dependencies {
    implementation(project(":domain"))
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
}