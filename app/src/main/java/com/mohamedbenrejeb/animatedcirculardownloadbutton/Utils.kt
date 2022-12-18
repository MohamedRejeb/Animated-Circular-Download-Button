package com.mohamedbenrejeb.animatedcirculardownloadbutton

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.max
import kotlin.math.sin

fun getSinusoidalPoints(
    size: Size,
    startOffset: Float = 0f,
    endOffset: Float = 0f,
    startPercentage: Float = 0f
): MutableList<Offset> {
    val points = mutableListOf<Offset>()
    val verticalCenter = size.height / 2

    for (x in endOffset.toInt() until (size.width - startOffset).toInt() step 10) {
        val y = (sin((x + startPercentage * size.width) * (2f * Math.PI / size.width)) * verticalCenter + verticalCenter).toFloat()
        points.add(Offset(x.toFloat() + startOffset - endOffset, y))
    }

    if (points.isEmpty() || endOffset > 0f)
        points.add(Offset(size.width, size.height / 2))

    return points
}