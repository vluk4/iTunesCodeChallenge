plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.vluk4.itunescodechallenge.core.testing"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    api(project(":core:domain"))
    api(project(":core:network"))

    api(libs.kotlinx.coroutines.test)
    api(libs.junit)
    api(libs.turbine)
}
