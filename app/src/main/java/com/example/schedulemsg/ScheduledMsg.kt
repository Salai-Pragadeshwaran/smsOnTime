package com.example.schedulemsg

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScheduledMsg(
    @PrimaryKey(autoGenerate = true) var requestCode: Int,
    var phoneNumber: String,
    var message: String,
    var scheduledTime: Long
){
}