package com.stargazer.recipeapp.di

import android.app.Application
import androidx.room.Room
import com.stargazer.recipeapp.dao.NotesDao
import com.stargazer.recipeapp.model.NoteDatabase
import com.stargazer.recipeapp.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(app, NoteDatabase::class.java, "note_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideNoteDao(db: NoteDatabase): NotesDao {
        return db.getNotesDao()
    }

    @Provides
    fun provideNoteRepository(noteDao: NotesDao): NoteRepository {
        return NoteRepository(noteDao)
    }
}
