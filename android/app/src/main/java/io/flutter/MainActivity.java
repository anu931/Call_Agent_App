package io.flutter.plugins;

import android.content.Intent;
import android.os.Bundle;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "call_agent_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MethodChannel(
                getFlutterEngine().getDartExecutor().getBinaryMessenger(),
                CHANNEL
        ).setMethodCallHandler((call, result) -> {

            if (call.method.equals("startService")) {

                Intent intent = new Intent(this, CallMonitorService.class);
                startForegroundService(intent);

                result.success("Service Started");

            } else {
                result.notImplemented();
            }

        });
    }
}