package com.example.noteappjetpackcompose.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappjetpackcompose.feature_note.domain.model.Note
import com.example.noteappjetpackcompose.feature_note.domain.use_case.NoteUseCases
import com.example.noteappjetpackcompose.feature_note.domain.util.NoteOrder
import com.example.noteappjetpackcompose.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    // For fix each time we get notes we canceled the old coroutine
    private var getNotesJob: Job? = null

    private var recentlyDeleteNote: Note? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {

        when (event) {
            is NotesEvent.Order -> {
                if (_state.value.noteOrder::class == event.noteOrder::class && _state.value.noteOrder.orderType == event.noteOrder.orderType) {
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeleteNote = event.note
                }
            }

            is NotesEvent.RestoreNotes -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeleteNote ?: return@launch)
                    recentlyDeleteNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder = noteOrder).onEach { notes ->
            _state.value = _state.value.copy(notes = notes, noteOrder = noteOrder)
        }.launchIn(viewModelScope)
    }
}