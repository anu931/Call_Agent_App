package com.example.crm_app

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class CallRecorderService : Service() {

    private var recorder: MediaRecorder? = null
    private var filePath: String? = null
    private var startTime: Long = 0
    private var callerNumber: String = "Unknown"

    companion object {
        const val ACTION_START = "START"
        const val ACTION_STOP  = "STOP"
        const val EXTRA_NUMBER = "number"
        const val SERVER_URL   = "http://192.168.1.3:8000/calls/log"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                callerNumber = intent.getStringExtra(EXTRA_NUMBER) ?: "Unknown"
                startRecording()
                startTime = System.currentTimeMillis()
            }
            ACTION_STOP -> {
                val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                stopRecording()
                // Only save if call was answered (has valid number and duration)
                if (duration > 0 && callerNumber != "Unknown" && filePath != null) {
                    saveToBackend(callerNumber, duration, filePath!!)
                } else {
                    Log.d("CRM", "Skipping save — rejected or invalid call")
                }
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun startRecording() {
        val dir = getExternalFilesDir(null) ?: filesDir
        val safeName = callerNumber.replace("+", "").replace(" ", "_")
        filePath = "${dir.absolutePath}/${safeName}_${System.currentTimeMillis()}.m4a"

        try {
            recorder = (if (Build.VERSION.SDK_INT >= 31)
                MediaRecorder(this) else @Suppress("DEPRECATION") MediaRecorder()
            ).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(44100)
                setAudioEncodingBitRate(96000)
                setOutputFile(filePath)
                prepare()
                start()
            }
            Log.d("CRM", "Recording: $filePath")
        } catch (e: Exception) {
            Log.e("CRM", "Recording failed: ${e.message}")
            filePath = null
        }
    }

    private fun stopRecording() {
        try {
            recorder?.stop()
            recorder?.release()
            recorder = null
            Log.d("CRM", "Saved: $filePath")
        } catch (e: Exception) {
            Log.e("CRM", "Stop error: ${e.message}")
            filePath = null
        }
    }

    private fun saveToBackend(number: String, duration: Int, recording: String) {
        Thread {
            try {
                val now = Date()
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(now)
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
                val json = """{"number":"$number","name":"Unknown","duration":$duration,"recording":"$recording","date":"$date","time":"$time"}"""

                val conn = URL(SERVER_URL).openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.outputStream.use { it.write(json.toByteArray()) }
                Log.d("CRM", "Backend [${conn.responseCode}]: $number, ${duration}s")
                conn.disconnect()
            } catch (e: Exception) {
                Log.e("CRM", "Backend failed: ${e.message}")
            }
        }.start()
    }
}