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

// ---------- WALK SESSIONS ----------
data class StepPoint(
    val lat: Double,
    val lon: Double,
    val timestamp: String
)

data class POIVisit(
    val account_id: String,  // ← добавили
    val poi_id: String,
    val poi_name: String,
    val distance_from_start: Float,
    val found_at: String,
    val emotion_emoji: String,
    val emotion_label: String,
    val emotion_color: String
)

data class WalkSessionCreate(
    val account_id: String,
    val start_time: String,
    val end_time: String,
    val distance_m: Float,
    val steps: Int,
    val mode: String,
    val notes: String?,
    val poi_visits: List<POIVisit>,
    val step_points: List<StepPoint>
)

data class WalkSessionResponse(val status: String, val session_id: Int)

// ---------- JOURNAL ----------
data class JournalEntry(
    val id: Int,
    val date: String,
    val text: String,
    val photo_path: String?,
    val poi_name: String?,
    val session_id: Int?
)

data class JournalEntryIn(
    val date: String,
    val text: String,
    val photo_path: String?,
    val poi_name: String?,
    val session_id: Int?,
    val account_id: String
)

// ---------- ACHIEVEMENTS ----------
data class Achievement(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val icon: String?,
    val unlocked_at: String?
)

// ---------- STATS ----------
data class StatsResponse(
    val today_distance: Float,
    val today_steps: Int,
    val weekly_chart: List<Float>,
    val streak: Int,
    val achievements: List<String>
)