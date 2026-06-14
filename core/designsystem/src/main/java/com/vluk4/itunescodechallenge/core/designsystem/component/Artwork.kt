package com.vluk4.itunescodechallenge.core.designsystem.component

import androidx.compose.ui.tooling.preview.Preview
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Artwork(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    cornerRadius: Dp = 8.dp,
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}

@Preview
@Composable
private fun ArtworkPreview() {
    ITunesCodeChallengeTheme {
        Artwork(
            url = "",
            contentDescription = "Preview Artwork"
        )
    }
}
