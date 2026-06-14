package com.vluk4.itunescodechallenge.feature.songs

import com.vluk4.itunescodechallenge.core.domain.model.Song

data class SongsUiState(
    val query: String = "",
    val selectedSong: Song? = null,
    val isMoreOptionsVisible: Boolean = false,
) {
    /** Blank query = browse recently-played; non-blank = show paged search results. */
    val isSearching: Boolean get() = query.isNotBlank()
}
