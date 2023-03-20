package com.example.llc.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.llc.domain.models.Point
import com.example.llc.R

@Composable
fun TablePoints(points: List<Point>) = LazyRow(
    modifier = Modifier
        .fillMaxWidth()
        .border(
            border = BorderStroke(1.dp, Color.Black),
            shape = RectangleShape
        )
) {
    item {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight()
        ) {
            Text(text = stringResource(R.string.title_x))
            Spacer(Modifier.height(8.dp))
            Text(text = stringResource(R.string.title_y))
        }
    }
    items(points) { point ->
        Column(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight()
        ) {
            Text(text = "${point.x}")
            Spacer(Modifier.height(8.dp))
            Text(text = "${point.y}")
        }
    }
}