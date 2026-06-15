package com.vluk4.itunescodechallenge.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import com.vluk4.itunescodechallenge.core.designsystem.theme.SliderActiveEnd
import com.vluk4.itunescodechallenge.core.designsystem.theme.SliderActiveStart
import com.vluk4.itunescodechallenge.core.designsystem.theme.SliderInactiveTrack
import com.vluk4.itunescodechallenge.core.designsystem.theme.SliderThumbCenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Slider(
        value = progress,
        onValueChange = onProgressChange,
        modifier = modifier,
        thumb = { SliderThumb() },
        track = { state -> GradientTrack(state) },
    )
}

@Composable
private fun SliderThumb() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(22.dp)
    ) {
        // Ring.
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    brush = Brush.radialGradient(listOf(SliderActiveStart, SliderActiveEnd)),
                    shape = CircleShape
                ),
        )
        // Core.
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(SliderThumbCenter),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GradientTrack(state: SliderState) {
    val range = (state.valueRange.endInclusive - state.valueRange.start).takeIf { it > 0f } ?: 1f
    val fraction = ((state.value - state.valueRange.start) / range).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .drawBehind {
                val centerY = size.height / 2f
                val strokeWidth = size.height
                val activeEndX = size.width * fraction

                // Remaining (inactive) track.
                drawLine(
                    color = SliderInactiveTrack,
                    start = Offset(0f, centerY),
                    end = Offset(size.width, centerY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )

                // Played (active) track with the cyan→teal gradient.
                if (activeEndX > 0f) {
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(SliderActiveStart, SliderActiveEnd),
                            startX = 0f,
                            endX = activeEndX,
                        ),
                        start = Offset(0f, centerY),
                        end = Offset(activeEndX, centerY),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                    )
                }
            },
    )
}

@Preview
@Composable
private fun PlaybackSliderPreview() {
    ITunesCodeChallengeTheme {
        PlaybackSlider(progress = 0.6f, onProgressChange = {})
    }
}
