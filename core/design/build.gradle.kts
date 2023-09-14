plugins {
    id("shoppa.library")
    id("shoppa.library.network")
    id("shoppa.library.compose")
}

android {
    namespace = "com.shoppa.core.design"
}

dependencies {
    implementation(libs.material)
    implementation(libs.bundles.compose)
}
