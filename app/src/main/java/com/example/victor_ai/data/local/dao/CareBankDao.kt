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
import com.example.victor_ai.data.local.entity.CareBankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CareBankDao {
    @Query("SELECT * FROM care_bank_entries WHERE accountId = :accountId")
    fun getEntriesByAccount(accountId: String): Flow<List<CareBankEntity>>

    @Query("SELECT * FROM care_bank_entries WHERE emoji = :emoji AND accountId = :accountId")
    suspend fun getEntryByEmoji(emoji: String, accountId: String): CareBankEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: CareBankEntity)

    @Query("DELETE FROM care_bank_entries WHERE emoji = :emoji AND accountId = :accountId")
    suspend fun deleteEntry(emoji: String, accountId: String)

    @Query("DELETE FROM care_bank_entries WHERE accountId = :accountId")
    suspend fun clearEntriesByAccount(accountId: String)

    @Query("DELETE FROM care_bank_entries")
    suspend fun clearAll()
}

