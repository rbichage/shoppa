plugins {
    id("shoppa.library")
    id("shoppa.android.room")
    id("shoppa.hilt")
    id("shoppa.testing")
}

android {
    namespace = "com.shoppa.core.database"
}

dependencies {
    testImplementation(libs.robolectric)
}
