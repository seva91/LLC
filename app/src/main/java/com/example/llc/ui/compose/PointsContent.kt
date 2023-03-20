package com.example.llc.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.llc.R
import com.example.llc.domain.models.Point

@Composable
fun PointsContent(
    points: List<Point>,
    unitState: Int,
    zoomInClick: () -> Unit,
    zoomOutClick: () -> Unit,
    saveClick: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state = rememberScrollState())
        .background(color = MaterialTheme.colors.background)
        .padding(16.dp)
) {
    TablePoints(points)
    Spacer(Modifier.height(16.dp))
    Box(
        Modifier
            .fillMaxWidth()
            .weight(1f)) {
        Graph(points, unitState)
        Button(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            onClick = saveClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_save),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Button(
                onClick = zoomInClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }

            Button(
                onClick = zoomOutClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_remove),
                    contentDescription = null
                )
            }
        }
    }
}