package com.example.schedulemsg

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {

    @Insert
    fun insertSchedule(schedule: ScheduledMsg)

    @Query("select * from ScheduledMsg")
    fun getAllSchedule():List<ScheduledMsg>
}