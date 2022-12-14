package com.example.noteappjetpackcompose.presentation.notes

import com.example.noteappjetpackcompose.feature_note.domain.model.Note
import com.example.noteappjetpackcompose.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNotes : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}