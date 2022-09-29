package com.example.noteappjetpackcompose.feature_note.domain.util

sealed class NoteOrder(val orderType: OrderType) {

    class Title(orderType: OrderType) : NoteOrder(orderType)
    class Date(orderType: OrderType) : NoteOrder(orderType)
    class Color(orderType: OrderType) : NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder = when (this) {
        is Title -> Title(orderType = orderType)
        is Date -> Date(orderType = orderType)
        is Color -> Color(orderType = orderType)
    }
}