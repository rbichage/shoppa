plugins {
    id("shoppa.library")
    id("shoppa.hilt")
    id("shoppa.library.network")
    id("shoppa.library.compose")
    id("shoppa.testing")
}

android {
    namespace = "com.shoppa.features.products"
}

dependencies {
    implementation(libs.material)
}
