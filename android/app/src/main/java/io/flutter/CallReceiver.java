package com.example.call_agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.io.File;
import android.os.Environment;

public class CallReceiver extends BroadcastReceiver{

    private static String lastState = "";
    private static CallRecorder recorder;

    @Override
    public void onReceive(Context context, Intent intent){
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
             
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
   private void createRecordingFolder(){

    File dir=new File(
        Environment.getExternalStorageDirectory() + "/CallAgent/recordings"
        
    );
    if(!dir.exists()){
     dir.mkdirs();
    }
   }
}