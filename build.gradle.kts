// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.androidx.room) apply false
}

buildscript {
    dependencies {
        // Add the Google services classpath
        classpath(libs.google.services)
    }
}
