package ru.bendricks.rssviewer.entity

import java.net.URL
import java.time.LocalDateTime

data class Note (
    var title: String = "",
    var description: String = "",
    var url: String = "",
    var time: String = ""
)