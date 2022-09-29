package com.example.noteappjetpackcompose.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappjetpackcompose.feature_note.domain.model.InvalidNoteException
import com.example.noteappjetpackcompose.feature_note.domain.model.Note
import com.example.noteappjetpackcompose.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCases,
    // Hilt provide it for use
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private var currentNoteId: Int? = null

    private val _noteTitleState = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitleState: State<NoteTextFieldState> = _noteTitleState

    private val _noteContentState =
        mutableStateOf(NoteTextFieldState(hint = "Enter some content..."))
    val noteContentState: State<NoteTextFieldState> = _noteContentState

    private val _noteColorState = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColorState: State<Int> = _noteColorState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCase.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitleState.value = _noteTitleState.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteContentState.value = _noteContentState.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )

                        _noteColorState.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitleState.value = _noteTitleState.value.copy(text = event.title)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitleState.value = _noteTitleState.value.copy(
                    isHintVisible = !event.focusState.isFocused && _noteTitleState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContentState.value = _noteContentState.value.copy(text = event.title)
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContentState.value = _noteContentState.value.copy(
                    isHintVisible = !event.focusState.isFocused && _noteContentState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColorState.value = event.color
            }

            is AddEditNoteEvent.Save -> {
                viewModelScope.launch {
                    try {
                        noteUseCase.addNote(
                            Note(
                                title = _noteTitleState.value.text,
                                content = _noteContentState.value.text,
                                timestemp = System.currentTimeMillis(),
                                color = _noteColorState.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}