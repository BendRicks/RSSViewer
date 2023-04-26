package ru.bendricks.rssviewer.viermodel

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.bendricks.rssviewer.R
import ru.bendricks.rssviewer.activity.MainActivity
import ru.bendricks.rssviewer.activity.RssWebViewActivity
import ru.bendricks.rssviewer.entity.Note
import kotlin.streams.toList

class NoteAdapter(private var notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteHolder>(),
    Filterable {

    private var filteredNotes: List<Note>

    init {
        filteredNotes = notes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.note_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(filteredNotes[position])
    }

    override fun getItemCount() = filteredNotes.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshNotes(notes: List<Note>) {
        this.notes = notes
        this.filteredNotes = ArrayList(notes)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if (charSearch.isEmpty()){
                    filteredNotes = notes
                } else {
                    filteredNotes = notes.stream().filter {
                        it.title.lowercase().contains(charSearch.lowercase())
                                || it.description.lowercase().contains(charSearch.lowercase())
                                || it.time.lowercase().contains(charSearch.lowercase())
                    }.toList()
                }
                val filterResults = FilterResults()
                filterResults.values = filteredNotes
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredNotes = p1?.values as java.util.ArrayList<Note>
                println(filteredNotes)
                notifyDataSetChanged()
            }
        }
    }

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val titleTextView: TextView
        val descriptionTextView: TextView
        val timeTextView: TextView
        lateinit var url: String

        init {
            itemView.setOnClickListener(this)
            titleTextView = itemView.findViewById(R.id.note_name)
            descriptionTextView = itemView.findViewById(R.id.note_description)
            timeTextView = itemView.findViewById(R.id.date_view)
        }

        fun bind(note : Note)  {
            this.url = note.url
            titleTextView?.text = note.title
            descriptionTextView?.text = Html.fromHtml(note.description, Html.FROM_HTML_MODE_COMPACT)
            timeTextView?.text = note.time
        }

        override fun onClick(p0: View?)  {
            val mainActivity = MainActivity._mainActivity
            val intent = Intent(mainActivity, RssWebViewActivity::class.java)
            intent.putExtra(RssWebViewActivity.URL, url)
            mainActivity.startActivity(intent)
        }
    }

}