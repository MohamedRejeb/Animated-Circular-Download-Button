package com.mohamedbenrejeb.animatedcirculardownloadbutton

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mohamedbenrejeb.animatedcirculardownloadbutton.ui.theme.AnimatedCircularDownloadButtonTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DownloadButton(
    onClick: () -> Unit,
    strokeColor: Color,
    strokeSize: Dp,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val scope = rememberCoroutineScope()

    var isAnimating by remember { mutableStateOf(false) }
    val tapAnimation = remember { Animatable(1f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(isAnimating) {
                detectTapGestures(
                    onPress = {
                        scope.launch {
                            tapAnimation.animateTo(0.8f)
                        }
                        tryAwaitRelease()
                        scope.launch {
                            tapAnimation.animateTo(1f)
                        }
                    },
                    onTap = {
                        isAnimating = !isAnimating
                        onClick()
                    }
                )
            }
            .graphicsLayer {
                alpha = tapAnimation.value
                scaleX = tapAnimation.value
                scaleY = tapAnimation.value
            }
    ) {
        RoundedCircularProgressIndicator(
            progress = 1f,
            strokeWidth = strokeSize,
            color = strokeColor.copy(alpha = 0.3f),
            modifier = Modifier
                .fillMaxSize()
        )

        RoundedCircularProgressIndicator(
            progress = progress,
            strokeWidth = strokeSize,
            color = strokeColor,
            modifier = Modifier
                .fillMaxSize()
        )

        val animationOneProgress = remember { Animatable(0f) }
        val animationTwoProgress = remember { Animatable(0f) }
        val animationThreeProgress = remember { Animatable(0f) }
        val animationFourProgress = remember { Animatable(0f) }
        val animationFiveProgress = remember { Animatable(0f) }
        val animationSixProgress = remember { Animatable(0f) }
        val animationSevenProgress = remember { Animatable(0f) }

        val animationDuration = 200

        LaunchedEffect(key1 = isAnimating) {
            if (!isAnimating) {
                animationOneProgress.snapTo(0f)
                animationTwoProgress.snapTo(0f)
                animationThreeProgress.snapTo(0f)
                animationFourProgress.snapTo(0f)
                animationFiveProgress.snapTo(0f)
                animationSixProgress.snapTo(0f)
                animationSevenProgress.snapTo(0f)
                return@LaunchedEffect
            }

            launch {
                animationOneProgress.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        delayMillis = 0,
                        easing = LinearEasing
                    )
                )
                animationTwoProgress.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        delayMillis = 0,
                        easing = CubicBezierEasing(0.34f, 1.8f, 0.64f, 1f)
                    )
                )
            }

            launch {
                animationThreeProgress.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        delayMillis = (animationDuration * 1.5f).roundToInt(),
                        easing = EaseOut
                    )
                )

                animationFourProgress.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = 0,
                        easing = LinearEasing
                    )
                )

                val spec = infiniteRepeatable<Float>(
                    tween(
                        durationMillis = 600,
                        delayMillis = 0,
                        easing = LinearEasing
                    ),
                    RepeatMode.Restart
                )

                animationFiveProgress.animateTo(
                    1f,
                    spec
                )
            }

        }
        
        LaunchedEffect(
            key1 = progress,
            key2 = animationFiveProgress.value,
        ) {
            if (progress != 1f) return@LaunchedEffect

            if (
                animationFiveProgress.value >= 0.9f
            ) {
                animationFiveProgress.snapTo(0f)
            }

            if (
                animationFiveProgress.value == 0f
            ) {
                animationSixProgress.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = 0,
                        easing = LinearEasing
                    )
                )
                animationSevenProgress.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = 0,
                        easing = LinearEasing
                    )
                )
            }
        }

        val downloadPath = remember { Path() }

        Canvas(
            modifier = Modifier
                .fillMaxSize(fraction = 0.5f)
        ) {
            drawRect(
                color = colorScheme.primary.copy(alpha = 0.2f)
            )

            downloadPath.reset()

            val arrowStartY = size.height * 3/5f

            val downloadLineHeight = size.height * (1f - animationOneProgress.value)
            val downloadLineY = (size.height - downloadLineHeight)/2f * (1f-animationThreeProgress.value) - animationThreeProgress.value * (size.height * 1/2f - 4.dp.toPx())

            downloadPath.moveTo(size.width / 2f, downloadLineY)
            downloadPath.lineTo(size.width / 2f, downloadLineY + downloadLineHeight)

            val downloadLineWidth = size.width - (size.width * (2/6f) * (1f - animationTwoProgress.value))
            val downloadLineX = 0f + (size.width * (1 / 6f) * (1f - animationTwoProgress.value))

            if (
                animationThreeProgress.value != 1f
                || animationThreeProgress.isRunning
            ) {
                downloadPath.moveTo(downloadLineX, arrowStartY)
                downloadPath.quadraticBezierTo(
                    downloadLineWidth / 4f + downloadLineX,
                    arrowStartY + ((size.height - arrowStartY) / (2f - 1f * animationTwoProgress.value) * (1f-animationTwoProgress.value)),
                    size.width / 2f,
                    arrowStartY + (size.height - arrowStartY) * (1f-animationTwoProgress.value)
                )

                downloadPath.moveTo(downloadLineX + downloadLineWidth, arrowStartY)
                downloadPath.quadraticBezierTo(
                    downloadLineWidth * 3/4f + downloadLineX,
                    arrowStartY + ((size.height - arrowStartY) / (2f - 1f * animationTwoProgress.value) * (1f-animationTwoProgress.value)),
                    size.width / 2f,
                    arrowStartY + (size.height - arrowStartY) * (1f-animationTwoProgress.value)
                )
            } else if (
                animationFourProgress.isRunning
                || animationFiveProgress.isRunning
                || animationSixProgress.isRunning
            ) {
                val sinusoidalSize = Size(downloadLineWidth, size.height * 1/5f)
                val points = getSinusoidalPoints(
                    sinusoidalSize,
                    downloadLineWidth*(1f-animationFourProgress.value),
                    downloadLineWidth*(animationSixProgress.value),
                    if (animationFiveProgress.isRunning) animationFiveProgress.value else 0f,
                )
                downloadPath.moveTo(0f + downloadLineX, points.first().y + (arrowStartY - sinusoidalSize.height/2f))
                points.forEach { offset: Offset ->
                    downloadPath.lineTo(offset.x + downloadLineX, offset.y + (arrowStartY - sinusoidalSize.height/2f))
                }
            } else {
                val checkMarkHeight = size.height * 3/6f
                val checkMarkWidth = size.width - (size.width * (2/6f) * (animationSevenProgress.value))
                val checkMarkX = 0f + (size.width * (1/6f) * (animationSevenProgress.value))

                downloadPath.moveTo(
                    checkMarkX,
                    arrowStartY - checkMarkHeight * 1/4f * animationSevenProgress.value
                )
                downloadPath.lineTo(
                    checkMarkX + checkMarkWidth / 3f,
                    arrowStartY + checkMarkHeight * 1/4f * animationSevenProgress.value
                )
                downloadPath.lineTo(
                    checkMarkX + checkMarkWidth,
                    arrowStartY - checkMarkHeight * 3/4f * animationSevenProgress.value
                )
            }

            drawPath(
                color = strokeColor,
                path = downloadPath,
                style = Stroke(
                    width = strokeSize.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(
                    fraction = 0.2f + (0.1f * animationFourProgress.value) - (0.1f * animationSevenProgress.value)
                )
        ) {
            Text(
                text = "${(progress * 100).roundToInt()}%",
                style = typography.titleLarge,
                color = strokeColor,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = if (animationSevenProgress.value > 0f) {
                            1f - animationSevenProgress.value
                        } else {
                            animationFourProgress.value
                        }
                    }
            )
        }
    }
}

@Preview()
@Composable
fun DownloadButtonPreview() {
    AnimatedCircularDownloadButtonTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            val progress = remember {
                Animatable(0f)
            }
            var startDownload by remember {
                mutableStateOf(false)
            }

            DownloadButton(
                onClick = { startDownload = !startDownload },
                strokeColor = MaterialTheme.colorScheme.onPrimary,
                strokeSize = 8.dp,
                progress = progress.value,
                modifier = Modifier.size(300.dp)
            )

            LaunchedEffect(key1 = startDownload) {
                if (startDownload) {
                    progress.animateTo(
                        1f,
                        tween(6000, 1000)
                    )
                } else {
                    progress.snapTo(0f)
                }
            }
        }
    }
}