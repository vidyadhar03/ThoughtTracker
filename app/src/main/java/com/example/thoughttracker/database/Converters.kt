package com.example.thoughttracker.database

import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class Converters {
    @ExperimentalSerializationApi
    @TypeConverter
    fun toString(value : Array<Thought>) = Json.encodeToString(value)

    @ExperimentalSerializationApi
    @TypeConverter
    fun fromString(value: String) = Json.decodeFromString<Array<Thought>>(value)
}