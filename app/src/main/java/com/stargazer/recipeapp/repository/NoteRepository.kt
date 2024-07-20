package com.stargazer.recipeapp.repository


import androidx.lifecycle.LiveData
import com.stargazer.recipeapp.dao.NotesDao
import com.stargazer.recipeapp.model.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesDao: NotesDao) {
    val allNotes: LiveData<List<Note>> = notesDao.getAllNotes()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }
}
