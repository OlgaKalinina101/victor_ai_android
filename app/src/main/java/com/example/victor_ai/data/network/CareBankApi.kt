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

package com.example.victor_ai.data.network

import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.dto.CareBankEntryCreate
import com.example.victor_ai.data.network.dto.CareBankEntryDto
import com.example.victor_ai.data.network.dto.CareBankSettingsRead
import com.example.victor_ai.data.network.dto.CareBankSettingsUpdate
import com.example.victor_ai.data.network.dto.ScreenshotAnalysisResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API для работы с банком заботы
 */
interface CareBankApi {

    /**
     * Создать новую запись в банке заботы
     * 
     * @param entry Данные записи для создания
     * @return Созданная запись
     */
    @POST("care_bank")
    suspend fun createCareBankEntry(
        @Body entry: CareBankEntryCreate
    ): Response<CareBankEntryDto>

    /**
     * Получить все записи банка заботы пользователя
     * 
     * @param accountId ID пользователя
     * @return Список записей банка заботы
     */
    @GET("care_bank/{account_id}")
    suspend fun getCareBankEntries(
        @Path("account_id") accountId: String
    ): Response<List<CareBankEntryDto>>

    /**
     * Обработать скриншот для добавления в банк заботы
     * 
     * @param accountId ID пользователя
     * @param screenshot Файл скриншота
     * @param query Дополнительный запрос для обработки
     * @return Результат анализа скриншота
     */
    @Multipart
    @POST("care_bank/process-screenshot")
    suspend fun processScreenshot(
        @Part("account_id") accountId: RequestBody,
        @Part screenshot: MultipartBody.Part,
        @Part("query") query: RequestBody? = null
    ): Response<ScreenshotAnalysisResponse>

    /**
     * Получить настройки банка заботы
     * 
     * @param accountId ID пользователя
     * @return Настройки банка заботы
     */
    @GET("care_bank/settings/{account_id}")
    suspend fun getCareBankSettings(
        @Path("account_id") accountId: String
    ): Response<CareBankSettingsRead>

    /**
     * Создать или обновить настройки банка заботы
     * 
     * @param settings Новые настройки
     * @return Обновленные настройки
     */
    @POST("care_bank/settings")
    suspend fun upsertCareBankSettings(
        @Body settings: CareBankSettingsUpdate
    ): Response<CareBankSettingsRead>
}

