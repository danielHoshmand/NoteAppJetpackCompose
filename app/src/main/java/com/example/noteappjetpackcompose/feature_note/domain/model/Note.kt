package com.example.noteappjetpackcompose.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteappjetpackcompose.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestemp: Long,
    val color: Int,
    @PrimaryKey val id: Int?,
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)
