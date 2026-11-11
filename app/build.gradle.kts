
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.recordatoriosapp"

    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.recordatoriosapp"

        minSdk = 23

        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // Compose BOM
    val composeBom =
        platform("androidx.compose:compose-bom:2026.09.00")

    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.13.0")

    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.ui:ui-graphics")

    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material Design 3
    implementation("androidx.compose.material3:material3")

    // Herramientas de desarrollo
    debugImplementation(
        "androidx.compose.ui:ui-tooling"
    )

    // Pruebas
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation(
        "androidx.compose.ui:ui-test-junit4"
    )

    debugImplementation(
        "androidx.compose.ui:ui-test-manifest"
    )
}