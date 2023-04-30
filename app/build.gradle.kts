plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)

    implementation(libs.koin.core)

    implementation(project(":lib"))
}

compose.desktop {
    application {
        mainClass = "visualizer.MainKt"
    }
}
