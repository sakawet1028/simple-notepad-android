package com.sakawet.simplenotepad.ui.navigation

object AppDestinations {
    const val NOTES_LIST = "notes_list"
    const val NOTE_ID_ARG = "noteId"
    const val NOTE_EDIT = "note_edit"
    const val NOTE_EDIT_ROUTE = "note_edit/{noteId}"
    const val NEW_NOTE_ID = -1L

    fun noteEditRoute(noteId: Long = NEW_NOTE_ID): String = "$NOTE_EDIT/$noteId"
}
