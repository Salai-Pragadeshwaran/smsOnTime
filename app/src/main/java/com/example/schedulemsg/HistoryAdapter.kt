package com.example.schedulemsg

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(val history: ArrayList<ScheduledMsg>, private val mcontext: Context):
RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        internal var ph: TextView
        internal var ms: TextView
        internal var status: TextView
        init{
            ph = itemView.phoneInfo
            ms = itemView.messageInfo
            status = itemView.statusInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ph.text = history[position].phoneNumber
        holder.ms.text = history[position].message
        if(System.currentTimeMillis()>history[position].scheduledTime){
            holder.status.text = "Sent"
        }else{
            var cal : Calendar = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis= history[position].scheduledTime
            var sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy\nHH:mm:ss", Locale.getDefault())
            holder.status.text = sdf.format(cal.timeInMillis)
        }
    }

}