package ru.bendricks.rssviewer.viermodel

import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.bendricks.rssviewer.entity.Note
import ru.bendricks.rssviewer.rss.RssGetter
import java.net.URL

class NoteViewModel : ViewModel() {

    var notesList : MutableLiveData<List<Note>> = MutableLiveData();

    fun getListNotes() = notesList

    fun updateListNotes(list: ArrayList<Note>) {
        notesList.value = list
    }


}