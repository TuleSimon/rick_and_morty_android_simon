// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // Android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    // Kotlin
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    // Code generation
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.android.room) apply false

    // Dependency Injection
    alias(libs.plugins.google.dagger.hilt.android) apply false
}
