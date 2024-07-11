package akhtemov.vladlen.simplenotes.presentation.ext

import akhtemov.vladlen.simplenotes.presentation.fragments.notelist.NoteListFragmentDirections
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.goToNoteDetail(id: String) {
    val action = NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(id)
    findNavController().navigate(action)
}