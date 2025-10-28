package com.example.victor_ai.domain.model

import com.squareup.moshi.Json


data class Track(
    val id: Int,
    val filename: String,
    @Json(name = "file_path") val filePath: String,
    val title: String,
    val artist: String,
    val album: String?,
    val year: Int?,
    val genre: String?,
    val duration: Float,
    @Json(name = "track_number") val trackNumber: Int?,
    val bitrate: Int,
    @Json(name = "file_size") val fileSize: Long,
    @Json(name = "energy_description") val energyDescription: String?,
    @Json(name = "temperature_description") val temperatureDescription: String?
)
data class TrackDescriptionUpdate(
    val account_id: String,
    val track_id: String,
    val energy_description: String?,
    val temperature_description: String?
)