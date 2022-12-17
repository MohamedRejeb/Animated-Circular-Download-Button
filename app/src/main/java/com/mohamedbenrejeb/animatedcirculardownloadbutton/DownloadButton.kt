package com.mohamedbenrejeb.animatedcirculardownloadbutton

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohamedbenrejeb.animatedcirculardownloadbutton.ui.theme.AnimatedCircularDownloadButtonTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun DownloadButton(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
//            .border(8.dp, colorScheme.primary, CircleShape)
    ) {
        val progress = 0.6f
        val degrees = 180f - 180f * progress

        RoundedCircularProgressIndicator(
            progress = 1f,
            strokeWidth = 8.dp,
            color = colorScheme.primary.copy(alpha = 0.3f),
            modifier = Modifier
                .fillMaxSize()
        )

        RoundedCircularProgressIndicator(
            progress = progress,
            strokeWidth = 8.dp,
            modifier = Modifier
                .fillMaxSize()
        )

        val animationOneProgress = remember { Animatable(1f) }
        val animationTwoProgress = remember { Animatable(1f) }
        val animationThreeProgress = remember { Animatable(1f) }

        val animationDuration = 400

        LaunchedEffect(key1 = Unit) {
            delay(1000)
            
            launch {
                animationOneProgress.animateTo(
                    0f,
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        delayMillis = 0,
                        easing = LinearEasing
                    )
                )
            }

            launch {
                animationTwoProgress.animateTo(
                    0f,
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        delayMillis = animationDuration,
                        easing = EaseOutBack
                    )
                )
            }

            launch {
                animationThreeProgress.animateTo(
                    0f,
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        delayMillis = (animationDuration * 1.7f).roundToInt(),
                        easing = LinearEasing
                    )
                )
            }

        }

        val downloadPath = remember { Path() }

        Canvas(
            modifier = Modifier
                .fillMaxSize(fraction = 0.6f)
        ) {
            drawRect(
                color = colorScheme.primary.copy(alpha = 0.2f)
            )

//            drawArc(
//                color = colorScheme.primary,
//                startAngle = 45f,
//                sweepAngle = 45f,
//                useCenter = false,
//                topLeft = Offset(x = 0f, y = 0f),
//                size = Size(size.width, size.height),
//                style = Stroke(
//                    width = with(density) { 8.dp.toPx() }
//                )
//            )

//            path.cubicTo(
//                0f, 0f,
//                size.width / 2f, 0f,
//                size.width, size.height,
//            )

            downloadPath.reset()

            downloadPath.quadraticBezierTo(
                0f, 0f, size.width / 2f, size.height / 2f
            )

            val arrowStartHeight = size.height * 3/5f

            val downloadLineHeight = max(
                8.dp.toPx(),
                size.height * animationOneProgress.value
            )
            val downloadLineY = (size.height - downloadLineHeight)/2f * animationThreeProgress.value

            downloadPath.moveTo(size.width / 2f, downloadLineY)
            downloadPath.lineTo(size.width / 2f, downloadLineY + downloadLineHeight)

            if (animationTwoProgress.value == 1f) {
                downloadPath.moveTo(size.width * 1/6f, arrowStartHeight)
                downloadPath.lineTo(size.width / 2f, size.height)
                downloadPath.moveTo(size. width / 2f, size.height)
                downloadPath.lineTo(size.width * 5/6f, arrowStartHeight)
            } else {
                val arcWidth = size.width * 4/6f
                val arcHeight = abs((size.height - arrowStartHeight) * 2 * animationTwoProgress.value)

                if (arcHeight <= 1f) {
                    downloadPath.moveTo(size.width * 1/6f, arrowStartHeight)
                    downloadPath.lineTo(size.width * 5/6f, arrowStartHeight)
                } else {
                    val sweepAngleDegrees =
                        if (
                            animationTwoProgress.value > 1f
                            || animationTwoProgress.value < 0f
                        ) -180f else 180f

                    downloadPath.arcTo(
                        rect = Rect(
                            offset = Offset(size.width * 1/6f, arrowStartHeight - arcHeight/2f),
                            size = Size(arcWidth, arcHeight)
                        ),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = sweepAngleDegrees,
                        forceMoveTo = true
                    )
//                    downloadPath.addArc(
//                        oval = Rect(
//                            offset = Offset(size.width * 1/6f, arrowStartHeight - arcHeight/2f),
//                            size = Size(arcWidth, arcHeight)
//                        ),
//                        startAngleDegrees = 0f,
//                        sweepAngleDegrees = 180f
//                    )
                }
            }


            drawPath(
                color = colorScheme.primary,
                path = downloadPath,
                style = Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

@Preview
@Composable
fun DownloadButtonPreview() {
    AnimatedCircularDownloadButtonTheme {
        Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DownloadButton(
                modifier = Modifier.width(100.dp)
            )
        }
    }
}