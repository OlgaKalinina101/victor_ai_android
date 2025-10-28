package com.example.victor_ai.logic

import com.example.victor_ai.data.network.dto.AssistantRequest
import com.example.victor_ai.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

suspend fun processStreamingMessage(
    request: AssistantRequest,
    onChunkReceived: (String) -> Unit,
    onMetadataReceived: (Map<String, Any>) -> Unit = {}  // ← новый callback
): Result<String> = withContext(Dispatchers.IO) {
    try {
        val call = RetrofitInstance.api.sendAssistantRequestStream(request)
        val response = call.execute()

        if (!response.isSuccessful) {
            return@withContext Result.failure(
                Exception("HTTP ${response.code()}: ${response.message()}")
            )
        }

        val fullResponse = StringBuilder()
        val reader = response.body()?.byteStream()?.bufferedReader()

        reader?.use { bufferedReader ->
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                try {
                    val json = JSONObject(line!!)

                    when {
                        json.has("chunk") -> {
                            val chunk = json.getString("chunk")
                            fullResponse.append(chunk)

                            withContext(Dispatchers.Main) {
                                onChunkReceived(chunk)
                            }
                        }

                        json.has("metadata") -> {
                            val metadata = json.getJSONObject("metadata")
                            val map = metadata.keys().asSequence().associateWith { key ->
                                metadata.get(key)
                            }

                            withContext(Dispatchers.Main) {
                                onMetadataReceived(map)
                            }
                        }

                        json.has("done") -> {
                            break
                        }

                        json.has("error") -> {
                            val error = json.getString("error")
                            return@withContext Result.failure(Exception(error))
                        }
                    }
                } catch (e: JSONException) {
                    continue
                }
            }
        }

        Result.success(fullResponse.toString())

    } catch (e: Exception) {
        Result.failure(e)
    }
}
