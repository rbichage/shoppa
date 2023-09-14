import com.android.build.api.dsl.ApplicationExtension
import com.shoppa.configureKotlinAndroid
import com.shoppa.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }


            extensions.configure<ApplicationExtension> {

                configureKotlinAndroid(this)

                defaultConfig {
                    targetSdk = 33
                }

                buildFeatures {
                    buildConfig = true
                }

                buildTypes {

                    debug {
                        applicationIdSuffix = ".debug"
                        versionNameSuffix = ".debug"
                        isMinifyEnabled = false
                    }
                }

            }

            dependencies {
                add("implementation", libs.findLibrary("timber").get())
            }
        }

    }
}