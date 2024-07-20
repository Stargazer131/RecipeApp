package com.stargazer.recipeapp.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stargazer.recipeapp.dao.NotesDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NotesDao
}