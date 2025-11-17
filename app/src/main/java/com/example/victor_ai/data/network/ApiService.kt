package com.example.victor_ai.data.network

import DiaryEntry
import DiaryResponse
import android.util.Log
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.dto.Achievement
import com.example.victor_ai.data.network.dto.AssistantRequest
import com.example.victor_ai.data.network.dto.AssistantResponse
import com.example.victor_ai.data.network.dto.ChatMetaResponse
import com.example.victor_ai.data.network.dto.ChatMetaUpdateRequest
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
import com.example.victor_ai.domain.model.TrackStats
import com.example.victor_ai.domain.model.WaveResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    // GET /chat_meta/{account_id}
    @GET("chat_meta/{account_id}")
    suspend fun getChatMeta(
        @Path("account_id") accountId: String
    ): Response<ChatMetaResponse>

    // PATCH /chat_meta/{account_id}
    @PATCH("chat_meta/{account_id}")
    suspend fun updateChatMeta(
        @Path("account_id") accountId: String,
        @Body body: ChatMetaUpdateRequest
    ): Response<ChatMetaResponse>

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

    @GET("tracks/stats")
    suspend fun getTrackStats(
        @Query("account_id") accountId: String,
        @Query("period") period: String = "week"
    ): TrackStats


    @POST("assistant/playlist/run")
    suspend fun runPlaylistChain(
        @Query("account_id") accountId: String,
        @Query("extra_context") extraContext: String? = null
    ): Map<String, Any>

    @POST("tracks/run_playlist_wave")
    suspend fun runPlaylistWave(
        @Query("account_id") accountId: String,
        @Query("energy") energy: String? = null,
        @Query("temperature") temperature: String? = null
    ): WaveResponse


    @GET("/")
    suspend fun checkConnection(): Response<Unit>

}

interface ChatApi {
    @GET("assistant/chat/history")
    suspend fun getChatHistory(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("limit") limit: Int = 25,
        @Query("before_id") beforeId: Int? = null
    ): com.example.victor_ai.data.network.dto.ChatHistoryResponse

    @PUT("assistant/chat/update_history")
    @Headers("Content-Type: application/json")
    suspend fun updateChatHistory(
        @Body request: UpdateHistoryRequest,
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): UpdateHistoryResponse

    @GET("assistant/chat/history/search")
    suspend fun searchChatHistory(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("query") query: String,
        @Query("offset") offset: Int = 0,
        @Query("context_before") contextBefore: Int = 10,
        @Query("context_after") contextAfter: Int = 10
    ): com.example.victor_ai.data.network.dto.SearchResult
}


interface PlacesApi {
    @GET("assistant/places")
    suspend fun getPlaces(
        @Query("limit") limit: Int = 15000,
        @Query("offset") offset: Int = 0,
        @Query("bbox") bbox: String? = null // "min_lon,min_lat,max_lon,max_lat"
    ): com.example.victor_ai.ui.map.models.PlacesResponse

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
        account_id = UserProvider.getCurrentUserId(),
        entry_text = text,
        timestamp = Instant.now().toString()
    )
    val response = diaryApi.sendDiaryEntry(diaryEntry)
    if (!response.isSuccessful) {
        Log.e("Diary", "Ошибка: ${response.errorBody()?.string()}")
    }
}



