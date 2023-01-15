package akhtemov.vladlen.simplenotes.presentation.model

data class NoteView (
    val id: String,
    val title: String,
    val desc: String = "",
    var date: String = "",
    var time: String = ""
)