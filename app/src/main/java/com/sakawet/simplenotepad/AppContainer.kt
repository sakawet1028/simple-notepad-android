package com.sakawet.simplenotepad

import android.content.Context
import com.sakawet.simplenotepad.data.local.NoteDatabase
import com.sakawet.simplenotepad.data.repository.NotesRepository

class AppContainer(context: Context) {
    private val database = NoteDatabase.getInstance(context)
    val notesRepository: NotesRepository = NotesRepository(database.noteDao())
}
