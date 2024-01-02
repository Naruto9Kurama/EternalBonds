package com.creator.common.enums

object Enums {
    enum class PlayerRole{
        Client, Server
    }

    enum class FileRequestCode{
        VIDEO,PHOTO
    }

    enum class VideoType{
        MP4,MKV
    }
    enum class PlaybackSource{
        LOCAL_FILES,HTTP,SCREEN_CASTING
    }
}