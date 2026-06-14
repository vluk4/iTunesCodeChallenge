package com.vluk4.itunescodechallenge.core.data.mapper

import com.vluk4.itunescodechallenge.core.database.entity.SongEntity
import com.vluk4.itunescodechallenge.core.domain.model.Song
import com.vluk4.itunescodechallenge.core.network.model.NetworkSong

private fun String?.toHighResArtwork(): String =
    this?.replace("100x100bb", "600x600bb").orEmpty()

fun NetworkSong.toEntityOrNull(searchQuery: String?, sortIndex: Int?): SongEntity? {
    val id = trackId ?: return null
    val title = trackName ?: return null
    return SongEntity(
        id = id,
        title = title,
        artistName = artistName.orEmpty(),
        collectionId = collectionId ?: 0L,
        collectionName = collectionName.orEmpty(),
        artworkUrl = artworkUrl100.toHighResArtwork(),
        previewUrl = previewUrl,
        genre = primaryGenreName,
        trackTimeMillis = trackTimeMillis,
        releaseDate = releaseDate,
        searchQuery = searchQuery,
        sortIndex = sortIndex,
        lastPlayedAt = null,
    )
}

fun SongEntity.toDomain(): Song = Song(
    id = id,
    title = title,
    artistName = artistName,
    collectionId = collectionId,
    collectionName = collectionName,
    artworkUrl = artworkUrl,
    previewUrl = previewUrl,
    genre = genre,
    trackTimeMillis = trackTimeMillis,
    releaseDate = releaseDate,
)

fun Song.toEntity(lastPlayedAt: Long?): SongEntity = SongEntity(
    id = id,
    title = title,
    artistName = artistName,
    collectionId = collectionId,
    collectionName = collectionName,
    artworkUrl = artworkUrl,
    previewUrl = previewUrl,
    genre = genre,
    trackTimeMillis = trackTimeMillis,
    releaseDate = releaseDate,
    searchQuery = null,
    sortIndex = null,
    lastPlayedAt = lastPlayedAt,
)

fun NetworkSong.toDomainOrNull(): Song? {
    val id = trackId ?: return null
    val title = trackName ?: return null
    return Song(
        id = id,
        title = title,
        artistName = artistName.orEmpty(),
        collectionId = collectionId ?: 0L,
        collectionName = collectionName.orEmpty(),
        artworkUrl = artworkUrl100.toHighResArtwork(),
        previewUrl = previewUrl,
        genre = primaryGenreName,
        trackTimeMillis = trackTimeMillis,
        releaseDate = releaseDate,
    )
}
