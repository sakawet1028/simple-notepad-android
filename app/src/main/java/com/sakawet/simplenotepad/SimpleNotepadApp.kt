package com.sakawet.simplenotepad

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sakawet.simplenotepad.ui.navigation.AppDestinations
import com.sakawet.simplenotepad.ui.noteedit.NoteEditScreen
import com.sakawet.simplenotepad.ui.noteedit.NoteEditViewModel
import com.sakawet.simplenotepad.ui.noteslist.NotesListScreen
import com.sakawet.simplenotepad.ui.noteslist.NotesListViewModel

@Composable
fun SimpleNotepadApp() {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as SimpleNotepadApplication
    val repository = application.container.notesRepository

    NavHost(
        navController = navController,
        startDestination = AppDestinations.NOTES_LIST
    ) {
        composable(route = AppDestinations.NOTES_LIST) {
            val listViewModel: NotesListViewModel = viewModel(
                factory = NotesListViewModel.factory(repository)
            )
            NotesListScreen(
                viewModel = listViewModel,
                onCreateNote = {
                    navController.navigate(AppDestinations.noteEditRoute())
                },
                onOpenNote = { noteId ->
                    navController.navigate(AppDestinations.noteEditRoute(noteId))
                }
            )
        }
        composable(
            route = AppDestinations.NOTE_EDIT_ROUTE,
            arguments = listOf(
                navArgument(AppDestinations.NOTE_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = AppDestinations.NEW_NOTE_ID
                }
            )
        ) { backStackEntry ->
            val editViewModel: NoteEditViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = NoteEditViewModel.factory(repository)
            )
            NoteEditScreen(
                viewModel = editViewModel,
                onBack = { navController.popBackStack() },
                onFinished = { navController.popBackStack() }
            )
        }
    }
}
