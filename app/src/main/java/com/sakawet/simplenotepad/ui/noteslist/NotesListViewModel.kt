package com.sakawet.simplenotepad.ui.noteslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sakawet.simplenotepad.data.local.NoteEntity
import com.sakawet.simplenotepad.data.repository.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class NotesListUiState(
    val notes: List<NoteEntity> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class NotesListViewModel(
    repository: NotesRepository
) : ViewModel() {

    val uiState: StateFlow<NotesListUiState> = repository.observeNotes()
        .map { notes -> NotesListUiState(notes = notes, isLoading = false) }
        .catch { emit(NotesListUiState(isLoading = false, errorMessage = it.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NotesListUiState()
        )

    companion object {
        fun factory(repository: NotesRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesListViewModel(repository) as T
                }
            }
        }
    }
}
