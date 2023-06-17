package com.pallaw.currencyconverter.data.local.database.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapTypeConverter {
    @TypeConverter
    fun fromMap(map: Map<String, Double>?): String? {
        return map?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toMap(json: String?): Map<String, Double>? {
        val type = object : TypeToken<Map<String, Double>?>() {}.type
        return Gson().fromJson(json, type)
    }
}