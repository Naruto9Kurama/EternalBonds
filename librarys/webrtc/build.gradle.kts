plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.creator.webrtc"
}

dependencies {

    implementation("org.java-websocket:Java-WebSocket:1.5.4")
    implementation("org.webrtc:google-webrtc:1.0.+")

}