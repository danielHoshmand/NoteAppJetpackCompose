package com.example.noteappjetpackcompose.feature_note.domain.use_case

import com.example.noteappjetpackcompose.feature_note.domain.model.Note
import com.example.noteappjetpackcompose.feature_note.domain.repository.NoteRepository

class GetNoteUseCase(val repository: NoteRepository) {

    suspend operator fun invoke(id: Int): Note? = repository.getNoteById(id)
}