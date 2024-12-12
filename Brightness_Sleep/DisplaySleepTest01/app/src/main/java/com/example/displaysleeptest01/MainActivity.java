package com.example.displaysleeptest01;
/*
 * 照度センサー、画面明るさ、画面スリープのテストプログラム
 *
 * 1) スレッドを用いて、1秒毎に画面情報表示＆ログ出力（現在時刻、照度センサー値 ほか）
 *
 * ※ スリープ抑止、画面明るさ変更のテストプログラムを作るにあたっての「スケルトン」とする
 *
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // タイマーで1秒毎に画面を書き換えるため
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    // 照度センサー読み出し用
    private SensorManager sensorManager;
    private float lightSensorValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Android Studioで自動作成されるソースコードの一部は不要なのでコメントアウトした
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // 照度センサー値取得のため
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // スクリーンOFFでもバックグラウンドでセンサーを使う場合は、ここでregisterListenerを実行。
        // そうでなければ、OnResume()内でregisterListenerを、onPause()内でunregisterListenerを実行する
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        lightSensorValue = 1000.0f;    // 照度初期値は 1000 (Lux)とする

        runnable = new Runnable() {

            @SuppressLint("DefaultLocale")
            @Override
            public void run() {

                // *****
                // デバッグ用ログ出力
                {
                    // "com.example.[APP_NAME].MainActivity$1" より "APP_NAME" を抜き出し、ログ出力時の tag として利用する
                    String strClassName = Objects.requireNonNull(new Object() {
                    }.getClass().getEnclosingClass()).getName();
                    String[] arr = strClassName.split("[.]");
                    Log.d(arr[(arr.length - 2)], String.format("lux=%.2f", lightSensorValue));
                }

                // *****
                // 時刻の表示
                Calendar calendar = Calendar.getInstance();
                TextView textTime = findViewById(R.id.textViewTime);
                textTime.setText(String.format("Time %02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));

                // *****
                // 照度（Lux）の表示
                TextView textLightSensor = findViewById(R.id.textViewLux);
                textLightSensor.setText(String.format("%.2f", lightSensorValue));

                // 残っているスレッドを削除
                handler.removeCallbacks(runnable);
                // スレッドを1秒後に再帰呼出し
                handler.postDelayed(runnable, 1000);
            }
        };
        // スレッド初回実行
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 残っているスレッドを削除する
        handler.removeCallbacks(runnable);
        // （プログラムが終了するときには）センサー イベント リスナーを解除する
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    // センサー・イベント・リスナーでオーバーライドされる関数
    //（class MainActivity に implements SensorEventListener を記述すると、自動的に作成される）
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // 照度センサー値の場合は、メンバ変数lightSensorValueにコピーする
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightSensorValue = sensorEvent.values[0];
        }
    }

    // センサー・イベント・リスナーでオーバーライドされる関数
    //（class MainActivity に implements SensorEventListener を記述すると、自動的に作成される）
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}