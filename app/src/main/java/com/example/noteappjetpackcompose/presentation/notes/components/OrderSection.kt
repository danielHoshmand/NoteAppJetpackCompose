package com.example.noteappjetpackcompose.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noteappjetpackcompose.feature_note.domain.util.NoteOrder
import com.example.noteappjetpackcompose.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChanged: (NoteOrder) -> Unit
) {
    Column(modifier = modifier) {

        Row(modifier = modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title,
                onSelect = { onOrderChanged(NoteOrder.Title(orderType = noteOrder.orderType)) })

            Spacer(modifier = modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = noteOrder is NoteOrder.Date,
                onSelect = { onOrderChanged(NoteOrder.Date(orderType = noteOrder.orderType)) })

            Spacer(modifier = modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = noteOrder is NoteOrder.Color,
                onSelect = { onOrderChanged(NoteOrder.Color(orderType = noteOrder.orderType)) })
        }

        Spacer(modifier = modifier.height(16.dp))
        Row(modifier = modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChanged(noteOrder.copy(OrderType.Ascending)) })

            Spacer(modifier = modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChanged(noteOrder.copy(OrderType.Descending)) })
        }
    }
}