package com.sakawet.simplenotepad.ui.noteedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.sakawet.simplenotepad.data.repository.NotesRepository
import com.sakawet.simplenotepad.ui.navigation.AppDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteEditUiState(
    val title: String = "",
    val content: String = "",
    val isExisting: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val finished: Boolean = false,
    val showDeleteConfirm: Boolean = false
)

class NoteEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: NotesRepository
) : ViewModel() {

    private val noteId: Long = savedStateHandle[AppDestinations.NOTE_ID_ARG]
        ?: AppDestinations.NEW_NOTE_ID
    private val isNewNote: Boolean = noteId == AppDestinations.NEW_NOTE_ID

    private val _uiState = MutableStateFlow(
        NoteEditUiState(
            isExisting = !isNewNote,
            isLoading = !isNewNote
        )
    )
    val uiState: StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    init {
        if (!isNewNote) {
            viewModelScope.launch {
                val note = runCatching { repository.getNote(noteId) }.getOrElse {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = it.errorMessage ?: "")
                    }
                    return@launch
                }
                if (note == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Note not found") }
                } else {
                    _uiState.update {
                        it.copy(
                            title = note.title,
                            content = note.content,
                            isExisting = true,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value, errorMessage = null) }
    }

    fun onContentChange(value: String) {
        _uiState.update { it.copy(content = value, errorMessage = null) }
    }

    fun save() {
        val title = _uiState.value.title.trim()
        val content = _uiState.value.content.trim()
        if (title.isEmpty()) {
            _uiState.update { it.copy(errorMessage = TITLE_REQUIRED) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                if (isNewNote) {
                    repository.createNote(title, content)
                } else {
                    repository.updateNote(noteId, title, content)
                }
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false, finished = true) }
            }.onFailure {
                _uiState.update { it.copy(isSaving = false, errorMessage = GENERIC_ERROR) }
            }
        }
    }

    fun requestDelete() {
        if (!isNewNote) {
            _uiState.update { it.copy(showDeleteConfirm = true) }
        }
    }

    fun dismissDelete() {
        _uiState.update { it.copy(showDeleteConfirm = false) }
    }

    fun confirmDelete() {
        if (isNewNote) return
        viewModelScope.launch {
            runCatching { repository.deleteNote(noteId) }
                .onSuccess {
                    _uiState.update {
                        it.copy(showDeleteConfirm = false, finished = true)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(showDeleteConfirm = false, errorMessage = GENERIC_ERROR)
                    }
                }
        }
    }

    companion object {
        const val TITLE_REQUIRED = "title_required"
        const val GENERIC_ERROR = "generic_error"

        fun factory(repository: NotesRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return NoteEditViewModel(
                        savedStateHandle = extras.createSavedStateHandle(),
                        repository = repository
                    ) as T
                }
            }
        }
    }
}
