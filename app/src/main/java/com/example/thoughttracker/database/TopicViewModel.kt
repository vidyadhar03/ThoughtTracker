package com.example.thoughttracker.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicViewModel(application : Application) : AndroidViewModel(application) {


    val readAllData : LiveData<Array<Topic>>
    private val repository:TopicRepository

    init{
        val topicDao= TopicDatabase.getDatabase(application).topicDao()
        repository = TopicRepository(topicDao)
        readAllData=repository.readAllData
    }

    fun addTopic(topic: Topic){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTopic(topic)
        }
    }

    fun updateTopic(topic: Topic){
        viewModelScope.launch ( Dispatchers.IO ){
            repository.updateTopic(topic)
        }
    }

    fun deleteTopic(topic: Topic){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteTopic(topic)
        }
    }

}