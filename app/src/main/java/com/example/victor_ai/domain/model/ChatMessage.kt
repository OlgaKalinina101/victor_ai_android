/**
Victor AI - Personal AI Companion for Android
Copyright (C) 2025-2026 Olga Kalinina

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.
 */

package com.example.victor_ai.domain.model

import com.example.victor_ai.data.local.entity.ChatMessageEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ChatMessage(
    @Json(name = "text") val text: String,
    @Json(name = "is_user") val isUser: Boolean,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "id") val id: Int? = null,  // ID из БД (null для SessionContext сообщений)
    val isSynced: Boolean = true,  // Флаг синхронизации с бэкендом (НЕ сериализуется в JSON)
    @Json(name = "image_count") val imageCount: Int = 0,  // Количество прикрепленных изображений
    @Json(name = "vision_context") val visionContext: String? = null,  // Контекст изображения (если было отправлено)
    @Json(name = "emoji") val emoji: String? = null,  // Эмодзи-реакция на сообщение
    // Swipe meta: к какому старому сообщению пользователь вернулся свайпом (если было)
    @Json(name = "swiped_message_id") val swipedMessageId: Int? = null,
    @Json(name = "swiped_message_text") val swipedMessageText: String? = null
)

// ========================
// Mappers
// ========================

/**
 * Маппер ChatMessage -> ChatMessageEntity (для сохранения в Room)
 */
fun ChatMessage.toEntity() = ChatMessageEntity(
    text = text,
    isUser = isUser,
    timestamp = timestamp,
    backendId = id,
    visionContext = visionContext,
    emoji = emoji
)

/**
 * Маппер ChatMessageEntity -> ChatMessage (для отображения в UI)
 */
fun ChatMessageEntity.toChatMessage() = ChatMessage(
    text = text,
    isUser = isUser,
    timestamp = timestamp,
    id = backendId,
    isSynced = true,  // Все сообщения из Room считаются синхронизированными
    visionContext = visionContext,
    emoji = emoji
)