package com.example.thoughttracker.database

import androidx.lifecycle.LiveData

class TopicRepository(private val topicDao: TopicDao) {

    val readAllData:LiveData<Array<Topic>> = topicDao.readAllData()

    suspend fun addTopic(topic: Topic) {
        topicDao.addTopic(topic)
    }

    suspend fun updateTopic(topic: Topic){
        topicDao.updateTopic(topic)
    }

    suspend fun deleteTopic(topic:Topic){
        topicDao.deleteTopic(topic)
    }
}