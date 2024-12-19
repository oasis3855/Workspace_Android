package com.example.displaywakeuptest01;
/*
 * 照度センサー、画面明るさ、画面スリープ・復帰のテストプログラム
 *
 * 1) スレッドを用いて、1秒毎に画面情報表示＆ログ出力（現在時刻、照度センサー値 ほか）
 * 2) 画面スリープ状態の時、指定した照度以上を検知すると画面スリープ解除する
 *
 * API 17 (Android 4.2) 以降で非推奨（deprecated）となったPowerManager.FULL_WAKE_LOCK を使う方法
 * 画面ON時にセキュリティロックは解除できない
 *
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // タイマーで1秒毎に画面を書き換えるため（スレッド制御）
    private final Handler handler = new Handler(Looper.getMainLooper());
    // 画面消灯する照度レベル指定（初期値）
    private final float luxThreshold = 200.0f;
    private Runnable runnable;
    // 照度センサー読み出し用
    private SensorManager sensorManager;
    private float lightSensorValue;
    // 画面点灯に使う
    private PowerManager.WakeLock wakeLock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 照度センサー値取得のため
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // スクリーンOFFでもバックグラウンドでセンサーを使う場合は、ここでregisterListenerを実行。
        // そうでなければ、OnResume()内でregisterListenerを、onPause()内でunregisterListenerを実行する
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        lightSensorValue = 1000.0f;    // 照度初期値は 1000 (Lux)とする

        // 画面消灯のモード選択
        RadioGroup radioScreenOffMode = findViewById(R.id.radioGroupScreenWakeup);
        // （初期状態は OFF を選択する）
        radioScreenOffMode.check(R.id.radioButtonOFF);

        runnable = new Runnable() {
            @Override
            public void run() {

                // *****
                // デバッグ用ログ出力
                {
                    // "com.example.[APP_NAME].MainActivity$1" より "APP_NAME" を抜き出し、ログ出力時の tag として利用する
                    String strClassName = Objects.requireNonNull(new Object() {
                    }.getClass().getEnclosingClass()).getName();
                    String[] arr = strClassName.split("[.]");
                    Log.d(arr[(arr.length - 2)], String.format("lux=%.2f, screenBright=%.2f", lightSensorValue, getWindow().getAttributes().screenBrightness));
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

                // *****
                // 照度センサー値（Lux）が指定値を超えている場合は、画面を点灯する
                if (radioScreenOffMode.getCheckedRadioButtonId() == R.id.radioButtonON && lightSensorValue >= luxThreshold) {
                    ScreenActivate();
                }

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
        // 残っているスレッドを削除
        handler.removeCallbacks(runnable);
        // （プログラムが終了するときには）センサー イベント リスナーを解除する
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        // wakeLockを開放する
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
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

    private void ScreenActivate() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (!powerManager.isInteractive()) {

            // *****
            // デバッグ用ログ出力
            {
                // "com.example.[APP_NAME].MainActivity$1" より "APP_NAME" を抜き出し、ログ出力時の tag として利用する
                String strClassName = Objects.requireNonNull(new Object() {
                }.getClass().getEnclosingClass()).getName();
                String[] arr = strClassName.split("[.]");
                Log.d(arr[(arr.length - 2)], "ScreenActivate");
            }

            // ***** 方法1
            // これは効果が無い
//            this.setTurnScreenOn(true);

            // ***** 方法2
            // 1回目だけ点灯するが、2回目以降は効果がない
//            if ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON) == 0) {
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);   // 1回目のみ効果あり
//            }

            // ***** 方法3
            // API 17 (Android 4.2) 以降で非推奨（deprecated）となったPowerManager.FULL_WAKE_LOCK を使う方法
            // （app/src/main/AndroidManifest.xmlに <uses-permission android:name="android.permission.WAKE_LOCK"/> を追記する）
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }
            wakeLock = null;
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyApp::MyWakelockTag");
            // 10秒 画面ON
            wakeLock.acquire(10 * 1000L);
        }
    }
}