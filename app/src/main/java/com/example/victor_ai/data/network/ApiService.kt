package com.example.victor_ai.data.network

import DiaryEntry
import DiaryResponse
import android.util.Log
import com.example.victor_ai.data.network.dto.Achievement
import com.example.victor_ai.data.network.dto.AssistantRequest
import com.example.victor_ai.data.network.dto.AssistantResponse
import com.example.victor_ai.data.network.dto.DeleteRequest
import com.example.victor_ai.data.network.dto.DeleteResponse
import com.example.victor_ai.data.network.dto.JournalEntry
import com.example.victor_ai.data.network.dto.JournalEntryIn
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.data.network.dto.PlacesResponse
import com.example.victor_ai.data.network.dto.ReminderResponse
import com.example.victor_ai.data.network.dto.StatsResponse
import com.example.victor_ai.data.network.dto.UpdateHistoryRequest
import com.example.victor_ai.data.network.dto.UpdateHistoryResponse
import com.example.victor_ai.data.network.dto.UpdateMemoryRequest
import com.example.victor_ai.data.network.dto.UpdateMemoryResponse
import com.example.victor_ai.data.network.dto.WalkSessionCreate
import com.example.victor_ai.data.network.dto.WalkSessionResponse
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.domain.model.Track
import com.example.victor_ai.domain.model.TrackDescriptionUpdate
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Streaming
import java.time.Instant

interface ApiService {
    @POST("assistant/message")
    suspend fun sendAssistantRequest(
        @Body request: AssistantRequest
    ): AssistantResponse

    @Streaming  // ← важно!
    @POST("assistant/message/stream")
    fun sendAssistantRequestStream(
        @Body request: AssistantRequest
    ): Call<ResponseBody>

    @POST("assistant/reminders/done")
    suspend fun markReminderAsDone(
        @Body body: ReminderRequest
    ): Response<ReminderResponse>

    @POST("assistant/reminders/delay")
    suspend fun delayReminder(
        @Body body: ReminderRequest
    ): Response<ReminderResponse>


    @POST("assistant/diary")
    suspend fun sendDiaryEntry(@Body entry: DiaryEntry): Response<DiaryResponse>

    @GET("assistant/usage")
    suspend fun getModelUsage(@Query("account_id") accountId: String): List<ModelUsage>

    @GET("assistant/memories")
    suspend fun getMemories(@Query("account_id") accountId: String): List<MemoryResponse>

    @POST("assistant/memories/delete")
    suspend fun deleteMemories(
        @Query("account_id") accountId: String,
        @Body request: DeleteRequest
    ): DeleteResponse

    @PUT("assistant/memories/update")
    suspend fun updateMemory(
        @Query("record_id") recordId: String,
        @Query("account_id") accountId: String,
        @Body request: UpdateMemoryRequest
    ): UpdateMemoryResponse

    @GET("assistant/tracks")
    suspend fun getTracks(
        @Query("account_id") accountId: String
    ): List<Track>

    @POST("assistant/track-description")
    suspend fun updateTrackDescription(
        @Body update: TrackDescriptionUpdate
    ): Map<String, String>

    @GET("/")
    suspend fun checkConnection(): Response<Unit>

}

interface ChatApi {
    @GET("chat/history")
    suspend fun getChatHistory(
        @Query("account_id") accountId: String = "test_user"
    ): List<ChatMessage>

    @PUT("chat/update_history") // 👈 если на бэке endpoint именно такой
    @Headers("Content-Type: application/json")
    suspend fun updateChatHistory(
        @Body request: UpdateHistoryRequest,
        @Query("account_id") accountId: String = "test_user"
    ): UpdateHistoryResponse
}


interface PlacesApi {
    @GET("assistant/places")
    suspend fun getPlaces(
        @Query("limit") limit: Int = 15000,
        @Query("offset") offset: Int = 0,
        @Query("bbox") bbox: String? = null // "min_lon,min_lat,max_lon,max_lat"
    ): com.example.victor_ai.ui.places.PlacesResponse

    // 🏃 Прогулки
    @POST("/api/walk_sessions/")
    suspend fun createWalkSession(@Body body: WalkSessionCreate): Response<WalkSessionResponse>

    // 📔 Дневник
    @GET("/api/journal/")
    suspend fun getJournalEntries(@Query("account_id") accountId: String): Response<List<JournalEntry>>

    @POST("/api/journal/")
    suspend fun createJournalEntry(@Body entry: JournalEntryIn): Response<Map<String, Any>>

    // 🏆 Достижения
    @GET("/api/achievements/")
    suspend fun getAchievements(): Response<List<Achievement>>

    // 📊 Статистика
    @GET("/api/stats/")
    suspend fun getStats(@Query("account_id") accountId: String): Response<StatsResponse>
}
data class ReminderRequest(val reminder_id: String)

suspend fun sendToDiaryEntry(text: String) {
    val diaryApi = RetrofitInstance.api // ← настроенный Retrofit
    val diaryEntry = DiaryEntry(
        account_id = "test_user", // ← подставь свой ID
        entry_text = text,
        timestamp = Instant.now().toString()
    )
    val response = diaryApi.sendDiaryEntry(diaryEntry)
    if (!response.isSuccessful) {
        Log.e("Diary", "Ошибка: ${response.errorBody()?.string()}")
    }
}



