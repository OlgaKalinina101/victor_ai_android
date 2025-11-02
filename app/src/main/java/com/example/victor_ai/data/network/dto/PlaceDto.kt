package com.example.victor_ai.data.network.dto

data class PlacesResponse(
    val places: List<PlaceDto>,
    val total: Int,
    val categories: List<String>,
    val region: String
)

data class PlaceDto(
    val id: Int,
    val name: String,
    val type: String,
    val emoji: String,
    val icon: String,
    val color: String,
    val lat: Double,
    val lon: Double,
    val amenities: List<String>
)