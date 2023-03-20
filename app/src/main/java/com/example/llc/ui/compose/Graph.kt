package com.example.llc.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.llc.domain.models.Point

@Composable
fun Graph(points: List<Point>, unitState: Int) = Canvas(
    modifier = Modifier
        .fillMaxSize()
        .clip(RectangleShape),
) {
    val unit = unitState.dp.toPx()
    val strokeWidth = 1.dp.toPx()
    val delimiterSize = if (unitState > 1) 2.dp.toPx() else 0f
    val arrowSize = 10.dp.toPx()

    drawRect(
        color = Color.Black,
        style = Stroke(width = strokeWidth)
    )

    drawPoints(
        points = points.map {
            Offset(
                center.x + it.x * unit,
                center.y - it.y * unit
            )
        },
        pointMode = PointMode.Lines,
        color = Color.Blue,
        strokeWidth = strokeWidth
    )

    fun drawDelimiterY(y: Float) = drawLine(
        color = Color.Black,
        start = Offset(center.x - delimiterSize, y),
        end = Offset(center.x + delimiterSize, y),
        strokeWidth = strokeWidth
    )

    var index = 1
    var y = center.y + index * unit
    while (y < size.height) {
        drawDelimiterY(y)
        y = center.y + ++index * unit
    }

    index = 1
    y = center.y - index * unit
    while (y > 0) {
        drawDelimiterY(y)
        y = center.y - ++index * unit
    }

    fun drawDelimiterX(x: Float) = drawLine(
        color = Color.Black,
        start = Offset(x, center.y - delimiterSize),
        end = Offset(x, center.y + delimiterSize),
        strokeWidth = strokeWidth
    )

    index = 1
    var x = center.x + index * unit
    while (x < size.width) {
        drawDelimiterX(x)
        x = center.x + ++index * unit
    }

    index = 1
    x = center.x - index * unit
    while (x > 0) {
        drawDelimiterX(x)
        x = center.x - ++index * unit
    }

    drawLine(
        color = Color.Black,
        start = Offset(center.x, 0f),
        end = Offset(center.x - arrowSize, arrowSize),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = Color.Black,
        start = Offset(center.x, 0f),
        end = Offset(center.x + arrowSize, arrowSize),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = Color.Black,
        start = Offset(center.x, 0f),
        end = Offset(center.x, size.height),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = Color.Black,
        start = Offset(size.width, center.y),
        end = Offset(size.width - arrowSize, center.y + arrowSize),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = Color.Black,
        start = Offset(size.width, center.y),
        end = Offset(size.width - arrowSize, center.y - arrowSize),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = Color.Black,
        start = Offset(0f, center.y),
        end = Offset(size.width, center.y),
        strokeWidth = strokeWidth
    )
}