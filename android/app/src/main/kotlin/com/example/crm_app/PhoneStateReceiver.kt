package com.example.crm_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class PhoneStateReceiver : BroadcastReceiver() {

    companion object {
        // In-memory flags — set BEFORE SharedPreferences to block duplicates instantly
        @Volatile var isRecording = false
        @Volatile var callAnswered = false
        var lastNumber = "Unknown"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE) ?: return
        val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        Log.d("CRM", "State: $state | number: $number")

        when (state) {
            TelephonyManager.EXTRA_STATE_RINGING -> {
                // Phone ringing — just save number, don't record yet
                callAnswered = false
                if (!number.isNullOrEmpty()) lastNumber = number
            }

            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                // Call answered — start recording ONCE
                if (!isRecording) {
                    isRecording = true
                    callAnswered = true
                    if (!number.isNullOrEmpty()) lastNumber = number
                    sendToService(context, CallRecorderService.ACTION_START, lastNumber)
                    Log.d("CRM", "▶ Recording started: $lastNumber")
                }
            }

            TelephonyManager.EXTRA_STATE_IDLE -> {
                if (isRecording) {
                    // Call was answered and now ended — save log
                    isRecording = false
                    sendToService(context, CallRecorderService.ACTION_STOP, lastNumber)
                    Log.d("CRM", "■ Recording stopped: $lastNumber")
                } else {
                    // Call was rejected — do nothing
                    Log.d("CRM", "✗ Call rejected, not recording: $lastNumber")
                }
                // Reset
                callAnswered = false
                lastNumber = "Unknown"
            }
        }
    }

    private fun sendToService(context: Context, action: String, number: String) {
        context.startService(Intent(context, CallRecorderService::class.java).apply {
            this.action = action
            putExtra(CallRecorderService.EXTRA_NUMBER, number)
        })
    }
}