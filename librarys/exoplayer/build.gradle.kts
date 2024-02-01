plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.creator.exoplayer"

}

dependencies {

//    api ("com.google.android.exoplayer:exoplayer-core:2.19.1")
//    api ("com.google.android.exoplayer:exoplayer-dash:2.19.1")
//    api ("com.google.android.exoplayer:exoplayer-ui:2.19.1")
//    api ("com.google.android.exoplayer:exoplayer-hls:2.19.1")

    implementation("androidx.leanback:leanback:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("androidx.databinding:databinding-runtime:8.2.0")
    api ("androidx.media3:media3-exoplayer:1.2.1")
    api ("androidx.media3:media3-exoplayer-dash:1.2.1")
    api ("androidx.media3:media3-ui:1.2.1")
    implementation(project(":librarys:nanohttpd"))
    implementation(project(":librarys:websocket"))
}