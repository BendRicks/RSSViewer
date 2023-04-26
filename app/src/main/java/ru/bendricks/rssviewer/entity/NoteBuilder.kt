package ru.bendricks.rssviewer.entity

import java.net.URL
import java.time.LocalDateTime

class NoteBuilder {

    private var title: String = ""
    private var description: String = ""
    private var url: String = ""
    private var time = ""

    fun title(title: String):NoteBuilder{
        this.title = title
        return this
    }

    fun description(description: String):NoteBuilder{
        this.description = description
        return this
    }

    fun url(url: String):NoteBuilder{
        this.url = url
        return this
    }

    fun time(time: String):NoteBuilder{
        this.time = time
        return this
    }

    fun build():Note{
        return Note(title, description, url, time)
    }

}