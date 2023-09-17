import java.io.ByteArrayOutputStream

plugins {
    id("shoppa.app")
    id("shoppa.hilt")
    id("shoppa.app.compose")
    id("shoppa.app.network")
}

android {
    namespace = "com.shoppa.app"

    defaultConfig {
        val version: Int

        val bytes = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-list HEAD --count".split(" ")
            standardOutput = bytes
        }

        val out = String(bytes.toByteArray()).trim().toInt() + 1
        version = out

        versionCode = version
        applicationId = "com.shoppa.app"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildTypes {
            debug {
                versionName = "0.0.$version"
                versionNameSuffix = "-debug"
                applicationIdSuffix = ".debug"
            }

            getByName("release") {
                versionName = "0.0.1"
            }
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(libs.accompanist.ui.controller)
    implementation(libs.splash.screen)
    implementation(project(":core:design"))
    implementation(project(":core:navigation"))
    implementation(project(":core:networking"))
    implementation(project(":core:data"))
    implementation(project(":features:products"))
}
