package com.octopus.inc.data.mapper

import com.octopus.inc.data.model.NoteEntity
import com.octopus.inc.domain.models.Note
import javax.inject.Inject

class NoteMapper @Inject constructor() : Mapper<NoteEntity, Note> {

    override fun mapFromEntity(type: NoteEntity): Note {
        return Note(
            id = type.id,
            title = type.title,
            desc = type.description,
            date = type.date,
            time = type.time
        )
    }

    override fun mapToEntity(type: Note): NoteEntity {
        return NoteEntity(
            id = type.id,
            title = type.title,
            description = type.desc,
            date = type.date,
            time = type.time
        )
    }

}