package com.octopus.inc.domain.models

data class Note(
    val id: String,
    val title: String,
    val desc: String = "",
    var date: String = "",
    var time: String = ""
)