package com.example.noteappjetpackcompose.di

import android.app.Application
import androidx.room.Room
import com.example.noteappjetpackcompose.feature_note.data.data_source.NoteDatabase
import com.example.noteappjetpackcompose.feature_note.data.repository.NoteRepositoryImpl
import com.example.noteappjetpackcompose.feature_note.domain.repository.NoteRepository
import com.example.noteappjetpackcompose.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn
class AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(app, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotesUseCase(repository = repository),
            deleteNote = DeleteNoteUseCase(repository = repository),
            addNote = AddNoteUseCase(repository = repository),
            getNote = GetNoteUseCase(repository = repository)
        )
    }
}