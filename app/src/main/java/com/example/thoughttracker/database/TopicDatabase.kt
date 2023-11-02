package com.example.thoughttracker.database

import android.content.Context
import androidx.room.*

@Database(entities = [Topic::class], version =1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TopicDatabase : RoomDatabase(){

    abstract fun topicDao(): TopicDao

    companion object{
        @Volatile
        private var INSTANCE:TopicDatabase? = null

        fun getDatabase(context:Context): TopicDatabase {
            val tempinstance= INSTANCE
            if(tempinstance!=null){
                return tempinstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    TopicDatabase::class.java,
                    "topic_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }

    }

}