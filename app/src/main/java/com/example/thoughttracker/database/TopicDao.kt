package com.example.thoughttracker.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTopic(topic: Topic)

    @Update
    fun updateTopic(topic: Topic)

    @Delete
    fun deleteTopic(topic:Topic)

    @Query("SELECT * FROM all_topics ORDER BY id ASC")
    fun readAllData():LiveData<Array<Topic>>

}