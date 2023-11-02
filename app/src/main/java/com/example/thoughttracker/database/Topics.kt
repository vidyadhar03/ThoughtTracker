 package com.example.thoughttracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "all_topics")
data class Topic (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var topic:String,
    val time:String,
    var thoughts:String
): Serializable

@kotlinx.serialization.Serializable
data class Thought(val topic:String, val time:String, var thought: String, val ispublic:Boolean) : java.io.Serializable
