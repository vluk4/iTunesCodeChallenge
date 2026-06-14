package com.vluk4.itunescodechallenge.core.designsystem.component

import androidx.compose.ui.tooling.preview.Preview
import com.vluk4.itunescodechallenge.core.designsystem.theme.ITunesCodeChallengeTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import com.vluk4.itunescodechallenge.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsBottomSheet(
    title: String,
    subtitle: String,
    onViewAlbum: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            )

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onViewAlbum)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Album,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.view_album),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoreOptionsBottomSheetPreview() {
    ITunesCodeChallengeTheme {
        MoreOptionsBottomSheet(
            title = "Song Title",
            subtitle = "Artist Name",
            onViewAlbum = {},
            onDismiss = {}
        )
    }
}
