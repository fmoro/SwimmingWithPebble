package org.frikis.zayer.swimmingwithpebble;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final UUID WATCHAPP_UUID = UUID.fromString("e5d2f0e9-6460-40de-8117-4fb26ab7731b");
    private static final String LOG_TAG = "SWP";

    private final StringBuilder mDisplayText = new StringBuilder();

    private PebbleKit.PebbleDataLogReceiver dataLogReceiver = null;

    private List<AccelData> accelDataList = new ArrayList<AccelData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent myIntent = new Intent(MainActivity.this, MainService.class);
        MainActivity.this.startService(myIntent);

        final Handler handler = new Handler();

        /*dataLogReceiver = new PebbleKit.PebbleDataLogReceiver(WATCHAPP_UUID) {
            @Override
            public void receiveData(Context context, UUID logUuid, Long timestamp, Long tag, byte[] data) {
                // super() (removed from IDE-generated stub to avoid exception)
                Log.i(LOG_TAG, "New data for session " + logUuid.toString() + "with a lenght of: " + data.length);

                AccelData accelData = new AccelData(data);
                accelDataList.add(accelData);

                Log.i(LOG_TAG, accelData.toCSV());
                // Cumulatively add the new data item to a TextView's current text
                // mDisplayText.append(timestamp.toString() + ": " + data + "s since watchapp launch.\n");

            }

            @Override
            public void onFinishSession(Context context, UUID logUuid, Long timestamp, Long tag) {
                super.onFinishSession(context, logUuid, timestamp, tag);
                Log.i(LOG_TAG, "Session " + logUuid.toString() + " finished!");
                boolean saved = saveData(context, logUuid);
                if (saved) {
                    mDisplayText.append("Saved file session-" + logUuid.toString() + ".csv\n");
                } else {
                    mDisplayText.append("Unable to save session-" + logUuid.toString() + "\n");
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUi();
                    }
                });
            }
        };

        // Register the receiver
        PebbleKit.registerDataLogReceiver(getApplicationContext(), dataLogReceiver);*/
    }

    private void updateUi() {
        TextView textView = (TextView) findViewById(R.id.log_data_text_view);
        textView.setText(mDisplayText.toString());
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getPrivateExternalStorageDir(Context context) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), "SWP");

        /*File file = null;
        if (Build.VERSION.SDK_INT >= 19) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/SWP/");
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents" + "/SWP/");
        }*/

        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    public File getPublicExternalStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), "SWP");
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        } else {
            file.setReadable(true);
            file.setWritable(true);
            file.setExecutable(true);
        }
        Log.e(LOG_TAG, "directory: " + file.getAbsolutePath());
        return file;
    }

    private boolean saveData(Context context, UUID logUuid) {
        if (!this.isExternalStorageWritable()) {
            Log.e(LOG_TAG, "External storage is not writable");
            return false;
        }
        File dir = this.getPublicExternalStorageDir();
        if (dir == null) {
            Log.e(LOG_TAG, "Directory has not been created");
            return false;
        }

        File file = new File(dir, "session-" + logUuid.toString() + ".csv");
        try {
            file.createNewFile();
            file.setExecutable(true);
            file.setWritable(true);
            file.setReadable(true);

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(AccelData.CSV_HEADER.getBytes());
            for (AccelData accelData : this.accelDataList) {
                outputStream.write(accelData.toCSV().getBytes());
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "File not found");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "IOException");
            return false;
        }
        return true;
    }
}
