package ru.bendricks.rssviewer.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import ru.bendricks.rssviewer.R
import ru.bendricks.rssviewer.databinding.ActivityMainBinding
import ru.bendricks.rssviewer.rss.RssGetter
import ru.bendricks.rssviewer.viermodel.NoteAdapter
import ru.bendricks.rssviewer.viermodel.NoteViewModel
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val noteViewModel by lazy { ViewModelProviders.of(this)[NoteViewModel::class.java] }

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEdit: SearchView
    private lateinit var rssUrlEdit: EditText
    private lateinit var loadRSSButton: Button

    companion object {
        lateinit var _mainActivity: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val adapter = NoteAdapter(ArrayList())

        loadRSSButton = findViewById(R.id.load_rss_button)
        loadRSSButton.setOnClickListener(this::onLoadRSS)
        recyclerView = findViewById(R.id.notes_list)
        searchEdit = findViewById(R.id.search_query)
        rssUrlEdit = findViewById(R.id.rss_url_edit)

        searchEdit.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        noteViewModel.getListNotes().observe(this) {
            it?.let {
                adapter.refreshNotes(it)
            }
        }

        _mainActivity = this
    }

    fun onLoadRSS(view: View){ //TODO Перенести запрос в интернет в отдельный поток https://stackoverflow.com/questions/58767733/the-asynctask-api-is-deprecated-in-android-11-what-are-the-alternatives
        val url = rssUrlEdit.text.toString()
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val list = RssGetter().getRSS(URL(url))
                handler.post {
                    noteViewModel.updateListNotes(list)
                }
            } catch (ex: MalformedURLException) {
                handler.post {
                    Toast.makeText(this, "Incorrect url", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (ex: XmlPullParserException) {
                handler.post {
                    Toast.makeText(this, "Error parsing rss", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (ex: IOException) {
                handler.post {
                    Toast.makeText(this, "I/O error", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}