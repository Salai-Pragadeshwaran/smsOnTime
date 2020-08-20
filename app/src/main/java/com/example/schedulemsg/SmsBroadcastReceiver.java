package com.example.schedulemsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNum = intent.getStringExtra("NUMBER");
        String msg = intent.getStringExtra("MESSAGE");
        Toast.makeText(context, "Scheduled SMS sent!", Toast.LENGTH_SHORT).show();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, msg, null, null);
    }

}
