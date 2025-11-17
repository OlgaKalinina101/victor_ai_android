package com.example.victor_ai.domain.model

import com.example.victor_ai.data.local.entity.ChatMessageEntity

/**
 * Маппер для конвертации ChatMessage -> ChatMessageEntity
 */
fun ChatMessage.toEntity() = ChatMessageEntity(
    text = text,
    isUser = isUser,
    timestamp = timestamp
)
