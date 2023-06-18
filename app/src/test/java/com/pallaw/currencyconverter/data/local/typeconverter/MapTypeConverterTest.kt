package com.pallaw.currencyconverter.data.local.typeconverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pallaw.currencyconverter.data.local.database.typeconverter.MapTypeConverter
import org.junit.Assert.assertEquals
import org.junit.Test

class MapTypeConverterTest {

    private val converter = MapTypeConverter()

    @Test
    fun `fromMap converts Map to JSON String`() {
        // Arrange
        val map = mapOf("USD" to 1.0, "EUR" to 0.85, "GBP" to 0.73)

        // Act
        val jsonString = converter.fromMap(map)

        // Assert
        val expectedJson = Gson().toJson(map)
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun `toMap converts JSON String to Map`() {
        // Arrange
        val json = "{\"USD\":1.0,\"EUR\":0.85,\"GBP\":0.73}"

        // Act
        val map = converter.toMap(json)

        // Assert
        val expectedMap = Gson().fromJson<Map<String, Double>>(
            json,
            object : TypeToken<Map<String, Double>>() {}.type
        )
        assertEquals(expectedMap, map)
    }

    @Test
    fun `fromMap returns null for null input`() {
        // Act
        val jsonString = converter.fromMap(null)

        // Assert
        assertEquals(null, jsonString)
    }

    @Test
    fun `toMap returns null for null input`() {
        // Act
        val map = converter.toMap(null)

        // Assert
        assertEquals(null, map)
    }
}
