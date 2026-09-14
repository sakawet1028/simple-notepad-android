package com.sakawet.simplenotepad

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sakawet.simplenotepad.ui.navigation.AppDestinations
import com.sakawet.simplenotepad.ui.noteedit.NoteEditScreen
import com.sakawet.simplenotepad.ui.noteslist.NotesListScreen

@Composable
fun SimpleNotepadApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.NOTES_LIST
    ) {
        composable(route = AppDestinations.NOTES_LIST) {
            NotesListScreen(
                onCreateNote = {
                    navController.navigate(AppDestinations.NOTE_EDIT)
                }
            )
        }
        composable(route = AppDestinations.NOTE_EDIT) {
            NoteEditScreen(
                onBack = { navController.popBackStack() },
                onSave = {
                    // Persistence arrives in Mastery Step 2.
                    navController.popBackStack()
                }
            )
        }
    }
}
