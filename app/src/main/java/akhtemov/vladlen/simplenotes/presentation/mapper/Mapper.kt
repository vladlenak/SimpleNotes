package akhtemov.vladlen.simplenotes.presentation.mapper

interface Mapper<V, D> {
    fun mapToView(type: D): V
    fun mapFromView(type: V): D
}