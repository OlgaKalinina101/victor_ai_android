package com.example.victor_ai.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MetadataConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): Map<String, Any> {
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun toString(map: Map<String, Any>): String {
        return gson.toJson(map)
    }
}
