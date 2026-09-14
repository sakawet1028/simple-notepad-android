package com.sakawet.simplenotepad.data.repository

import com.sakawet.simplenotepad.data.local.NoteDao
import com.sakawet.simplenotepad.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {
    fun observeNotes(): Flow<List<NoteEntity>> = noteDao.observeAll()

    suspend fun getNote(id: Long): NoteEntity? = noteDao.getById(id)

    suspend fun createNote(title: String, content: String): Long {
        return noteDao.insert(
            NoteEntity(
                title = title,
                content = content,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        noteDao.update(
            NoteEntity(
                id = id,
                title = title,
                content = content,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteNote(id: Long) {
        noteDao.deleteById(id)
    }
}
