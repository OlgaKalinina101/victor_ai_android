package com.example.victor_ai.domain.model

import com.google.gson.annotations.SerializedName


data class ChatMessage(
    @SerializedName("text") val text: String,
    @SerializedName("is_user") val isUser: Boolean,
    @SerializedName("timestamp") val timestamp: Long
)