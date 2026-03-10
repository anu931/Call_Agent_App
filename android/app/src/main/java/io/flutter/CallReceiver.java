package com.example.call_agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver{

    private static String lastState = "";
    private static CallRecorder recorder;

    @Override
    public void onReceive(Context context, Intent intent){
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if(state == null)return;

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            lastState = "RINGING";
        }
        
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
           
           if(!lastState.equals("OFFHOOK")){
            recorder = new CallRecorder();
            recorder.startRecording();

           }

           lastState = "OFFHOOK";
        
        }
    
    if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){

        if(recorder != null){
            recorder.stopRecording();
        }

        lastState = "IDLE";
      }
   }
}