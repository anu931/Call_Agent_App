package io.flutter;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallRecorder {

    private MediaRecorder recorder;
    private String filePath;

    public void startRecording(Context context) {

        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File dir = new File(context.getExternalFilesDir(null), "recordings");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            filePath = dir.getAbsolutePath() + "/call_" + timeStamp + ".3gp";

            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(filePath);

            recorder.prepare();
            recorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String stopRecording() {

        try {

            recorder.stop();
            recorder.release();
            recorder = null;

            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}