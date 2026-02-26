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

package com.example.victor_ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.victor_ai.data.local.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    abstract fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    abstract suspend fun getAllMessagesOnce(): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    abstract suspend fun clearAll()

    @Query("DELETE FROM chat_messages WHERE backendId = :backendId")
    abstract suspend fun deleteByBackendId(backendId: Int)

    @Query("DELETE FROM chat_messages WHERE backendId IN (:ids)")
    abstract suspend fun deleteByBackendIds(ids: List<Int>)

    @Transaction
    open suspend fun upsertByBackendId(messages: List<ChatMessageEntity>) {
        val backendIds = messages.mapNotNull { it.backendId }
        if (backendIds.isNotEmpty()) {
            deleteByBackendIds(backendIds)
        }
        insertMessages(messages)
    }

    @Query("UPDATE chat_messages SET emoji = :emoji WHERE backendId = :backendId")
    abstract suspend fun updateEmojiByBackendId(backendId: Int, emoji: String?)

    @Query("SELECT * FROM chat_messages WHERE backendId = :backendId LIMIT 1")
    abstract suspend fun getByBackendId(backendId: Int): ChatMessageEntity?

    @Query("SELECT COUNT(*) FROM chat_messages")
    abstract suspend fun getMessagesCount(): Int
}
