package org.frikis.zayer.swimmingwithpebble;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final UUID WATCHAPP_UUID = UUID.fromString("e5d2f0e9-6460-40de-8117-4fb26ab7731b");
    private static final String TAG = "SWP";

    private final StringBuilder mDisplayText = new StringBuilder();

    private PebbleKit.PebbleDataLogReceiver dataLogReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Handler handler = new Handler();

        dataLogReceiver = new PebbleKit.PebbleDataLogReceiver(WATCHAPP_UUID) {
            @Override
            public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, byte[] data) {
                // super() (removed from IDE-generated stub to avoid exception)
                Log.i(TAG, "New data for session " + tag + "!");

                // Cumulatively add the new data item to a TextView's current text
                mDisplayText.append(timestamp.toString() + ": " + data + "s since watchapp launch.\n");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUi();
                    }
                });
            }

            @Override
            public void onFinishSession(Context context, UUID logUuid, Long timestamp, Long tag) {
                super.onFinishSession(context, logUuid, timestamp, tag);
                Log.i(TAG, "Session " + tag + " finished!");
            }
        };

        // Register the receiver
        PebbleKit.registerDataLogReceiver(getApplicationContext(), dataLogReceiver);
    }

    private void updateUi() {
        TextView textView = (TextView) findViewById(R.id.log_data_text_view);
        textView.setText(mDisplayText.toString());
    }
}
