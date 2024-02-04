pluginManagement {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/jcenter")
        maven(url = "https://developer.huaweicloud.com/repo/")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/public")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/google")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/jcenter")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/gradle-plugin")
        google()
        mavenCentral()
        gradlePluginPortal()
        // 配置鲸鸿动能SDK的Maven仓地址
        maven ( url = "https://developer.huawei.com/repo/" )
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/jcenter")
        maven(url = "https://developer.huaweicloud.com/repo/")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/public")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/google")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/jcenter")
        maven(url = "https://mirrors.tuna.tsinghua.edu.cn/gradle-plugin")
        google()
        mavenCentral()
        // 配置鲸鸿动能SDK的Maven仓地址
        maven ( url = "https://developer.huawei.com/repo/" )
    }
}

rootProject.name = "EternalBonds"
include(":app")
include(":librarys")
include(":librarys:webrtc")
include(":librarys:exoplayer")
include(":librarys:nanohttpd")
include(":librarys:common")
include(":librarys:websocket")
include(":librarys:remote_controller")
