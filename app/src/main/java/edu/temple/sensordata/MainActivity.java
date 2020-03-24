package edu.temple.sensordata;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;

    Sensor sensor;

    SensorEventListener sensorEventListener;

    TextView xData, yData, zData;

    boolean playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xData = findViewById(R.id.xData);
        yData = findViewById(R.id.yData);
        zData = findViewById(R.id.zData);

        sensorManager = getSystemService(SensorManager.class);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    xData.setText(String.valueOf(event.values[0]));
                    yData.setText(String.valueOf(event.values[1]));
                    zData.setText(String.valueOf(event.values[2]));

                    if (event.values[0] < 2 &&
                            event.values[1] < 2 &&
                            event.values[2] < 2) {
                        freeFall();
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void freeFall () {
        findViewById(R.id.display).setBackgroundColor(Color.RED);

        if (!playing) {
            final MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.scream);

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playing = false;
                    mPlayer.release();

                    findViewById(R.id.display).setBackgroundColor(Color.WHITE);
                }
            });
            mPlayer.start();
        }
    }
}
