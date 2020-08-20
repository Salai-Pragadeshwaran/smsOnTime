package com.example.schedulemsg

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(version = 1, entities = [ScheduledMsg::class])
abstract class HistoryDatabase : RoomDatabase(){
    companion object{
        fun get(application: Application): HistoryDatabase{
            return Room.databaseBuilder(application, HistoryDatabase::class.java, "history data").allowMainThreadQueries().build()
        }
    }

    abstract fun getHistoryDao(): HistoryDao
}