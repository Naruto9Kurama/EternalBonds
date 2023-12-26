plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.creator.exoplayer"

}

dependencies {

    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-dash:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-hls:2.19.1")
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("androidx.databinding:databinding-runtime:8.2.0")
//    implementation ("com.arthenica:mobile-ffmpeg-full:4.4")
//    implementation ("org.nanohttpd:nanohttpd:2.3.1")

    implementation(project(":librarys:nanohttpd"))
    implementation(project(":librarys:websocket"))
    implementation("org.java-websocket:Java-WebSocket:1.5.4")
    implementation ("org.nanohttpd:nanohttpd:2.3.1")
}