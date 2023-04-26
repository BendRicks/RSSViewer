package ru.bendricks.rssviewer.rss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import ru.bendricks.rssviewer.entity.Note
import ru.bendricks.rssviewer.entity.NoteBuilder
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDateTime

class RssGetter {

    fun getRSS(url: URL) : ArrayList<Note>{
        val rssList = ArrayList<Note>()
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false
        val xpp = factory.newPullParser()
        xpp.setInput(getInputStream(url), "UTF-8")

        var insideItem = false
        var eventType = xpp.eventType
        val builder = NoteBuilder()

        while (eventType != XmlPullParser.END_DOCUMENT){
            if (eventType == XmlPullParser.START_TAG){
                if (xpp.name.equals("item", true)){
                    insideItem = true;
                } else if (insideItem) {
                    if (xpp.name.equals("title", true)) {
                        builder.title(xpp.nextText())
                    } else if (xpp.name.equals("description", true)) {
                        builder.description(xpp.nextText())
                    } else if (xpp.name.equals("link", true)) {
                        builder.url(xpp.nextText())
                    } else if (xpp.name.equals("pubDate", true)) {
                        builder.time(xpp.nextText())
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG && xpp.name.equals("item", true)){
                insideItem = false
                rssList.add(builder.build())
            }

            eventType = xpp.next()
        }
        return rssList
    }

    private fun getInputStream(url: URL): InputStream {
        return try {
            url.openConnection().getInputStream()
        } catch (exception: IOException) {
            throw MalformedURLException("Incorrect url")
        }
    }

}