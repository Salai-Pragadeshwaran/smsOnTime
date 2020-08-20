package com.example.schedulemsg

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    lateinit var calendar: Calendar
    var phoneNum = ""
    var msg = ""

    var history = ArrayList<ScheduledMsg>()
    lateinit var historyDatabase: HistoryDatabase

    companion object
    {
        @JvmStatic
        var allSchedule = ArrayList<ScheduledMsg>()
        @JvmStatic
        lateinit var historyRecycler2: RecyclerView
        @JvmStatic
        var reqCode : Int = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        historyDatabase = HistoryDatabase.get(application)

        var permission1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        )
        var permission2 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        )
        if((permission1!=PackageManager.PERMISSION_GRANTED)&&(permission2!=PackageManager.PERMISSION_GRANTED)){
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE), 103)
        }
        else if(permission1!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 101)
        }
        else if(permission2!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 102)
        }

        historyRecycler2 = findViewById(R.id.historyRecycler)

        //ReadData(application)
        allSchedule.addAll(historyDatabase.getHistoryDao().getAllSchedule() as ArrayList<ScheduledMsg>)
        var historyAdapter = HistoryAdapter(allSchedule, this)
        historyRecycler2.adapter = historyAdapter
        if (allSchedule.size-1>=0) {
            reqCode = allSchedule[allSchedule.size-1].requestCode
        }



        calendar = Calendar.getInstance()

        setTime.setOnClickListener {
            var timepicker = TimePickerFragment()
            timepicker.show(supportFragmentManager, "Time Picker")
        }

        setDate.setOnClickListener {
            var datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, "Date Picker")
        }

        scheduleMsg.setOnClickListener {
            if(editTextPhone.text.toString()!="" && message.text.toString()!="") {
                phoneNum = editTextPhone.text.toString()
                editTextPhone.text = null
                msg = message.text.toString()
                message.text = null
                scheduleSMS(calendar.timeInMillis)
            }else{
                Toast.makeText(this, "Enter Valid phone number and message", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun scheduleSMS(timeInMillis: Long) {
        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent: Intent = Intent(this, SmsBroadcastReceiver::class.java)
        intent.putExtra("NUMBER", phoneNum)
        intent.putExtra("MESSAGE", msg)
        var scheduledMsg = ScheduledMsg(phoneNumber = phoneNum, message = msg, scheduledTime = timeInMillis, requestCode = ++reqCode)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, reqCode, intent, 0)
        alarmManager.setExact(AlarmManager.RTC, timeInMillis, pendingIntent)
        //InsertData(application, scheduledMsg)
        historyDatabase.getHistoryDao().insertSchedule(scheduledMsg)
        allSchedule.add(scheduledMsg)
        var historyAdapter = HistoryAdapter(allSchedule, this)
        historyRecycler2.adapter = historyAdapter
        Toast.makeText(this, "SMS scheduled !", Toast.LENGTH_SHORT).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    class InsertData(val applicationn: Application, val scheduledMsg: ScheduledMsg) : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            HistoryDatabase.get(application = applicationn).getHistoryDao().insertSchedule(scheduledMsg)
            return null
        }

    }

    class ReadData(val applicationn: Application) : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
           MainActivity.allSchedule  = HistoryDatabase.get(application = applicationn).getHistoryDao().getAllSchedule() as ArrayList<ScheduledMsg>
            var historyAdapter = HistoryAdapter(allSchedule, applicationn.applicationContext)
            historyRecycler2.adapter = historyAdapter
            reqCode = allSchedule[allSchedule.size-1].requestCode
            return null
        }

    }
}

