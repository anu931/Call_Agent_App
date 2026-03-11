package io.flutter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {

    private static String lastState = "";
    private static CallRecorder recorder;

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
          currentNumber = number;
        }
        else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                recorder = new CallRecorder();
                audioPath = recorder.startRecording(context);

            }

        else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                recorder.stopRecording();
                CallLogDatabase.insertLog(context, currentNumber, audioPath, "CALL");
            
        }
    }
}