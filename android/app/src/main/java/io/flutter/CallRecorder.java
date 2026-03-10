package com.example.call_agent;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallRecorder {

    private MediaRecorder recorder;
    private String filePath;

    public void startRecording() {

        try {

            String timestamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File dir = new File(
                    Environment.getExternalStorageDirectory(),
                    "CallAgent/recordings"
            );

            if (!dir.exists()) {
                dir.mkdirs();
            }

            filePath = dir.getAbsolutePath() + "/call_" + timestamp + ".mp4";

            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(filePath);

            recorder.prepare();
            recorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {

        try {

            if (recorder != null) {

                recorder.stop();
                recorder.release();
                recorder = null;

                saveCallLog(filePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCallLog(String path) {

        // Later you will insert this into SQLite
        // Example fields:
        // phone_number
        // file_path
        // timestamp
        // duration

    }
}