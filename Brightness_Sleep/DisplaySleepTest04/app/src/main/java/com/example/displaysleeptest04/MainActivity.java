package com.example.displaysleeptest04;
/*
 * 照度センサー、画面明るさ、画面スリープのテストプログラム
 *
 * 1) スレッドを用いて、1秒毎に画面情報表示＆ログ出力（現在時刻、照度センサー値 ほか）
 * 2) 照度センサー値により、画面明るさを2段階に切り替え（このアプリのみ : WindowManager.LayoutParams.screenBrightness）
 * 3) 画面スリープ抑止（システム全体 : PowerManager.FULL_WAKE_LOCK）
 *
 * ※ 非推奨機能（PowerManager.FULL_WAKE_LOCK）の利用
 *   This constant was deprecated in API level 17 (Android 4.2).
 *   Most applications should use WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
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
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // タイマーで1秒毎に画面を書き換えるため（スレッド制御）
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    // 画面消灯する照度レベル指定（初期値）
    private final float luxThreshold = 200.0f;
    // スクリーン明るさ設定値
    private final float settingValScreenBright = 0.8f;
    private final float settingValScreenDark = 0.1f;
    // 照度センサー読み出し用
    private SensorManager sensorManager;
    private float lightSensorValue;
    // 電源管理
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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

        // 画面消灯のモード選択
        RadioGroup radioScreenOffMode = findViewById(R.id.radioGroupScreenOffMode);
        // （初期状態は OFF を選択する）
        radioScreenOffMode.check(R.id.radioButtonOFF);

        // 電源コントロール（WakeLock設定で利用する）
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        // WakeLock未取得状態（nullを入れておく）
        wakeLock = null;

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
                // 照度しきい値（Lux）の表示
                TextView textLuxThreshold = findViewById(R.id.textViewLuxThreshold);
                textLuxThreshold.setText(String.format("%.2f", luxThreshold));

                // *****
                // 現在の画面照度
                TextView textBrightness = findViewById(R.id.textViewBrightness);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                textBrightness.setText(String.format("%.2f", lp.screenBrightness));

                // *****
                // スクリーン タイムアウト秒数の表示
                TextView textScreenTimeout = findViewById(R.id.textViewScreenTimeout);
                int settingScreenTimeout = 0;
                try {
                    // スクリーン タイムアウト秒数（ミリ秒 単位）が 0 〜 ... で得られる
                    settingScreenTimeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT) / 1000;
                    textScreenTimeout.setText(String.format("%d sec", settingScreenTimeout));
                } catch (Settings.SettingNotFoundException e) {
                    textScreenTimeout.setText("ERR");
                }

                // *****
                // スクリーン常時ON設定（WakeLock）状態の表示
                TextView textWakeLock = findViewById(R.id.textViewWakeLock);
                if (wakeLock == null) {
                    textWakeLock.setText("null");
                } else if (wakeLock.isHeld()) {
                    textWakeLock.setText("ON");
                } else {
                    textWakeLock.setText("OFF");
                }

                // *****
                // 照度センサー（Lux）による画面の明・暗切り替え処理
                // 「暗」に切り替え
                // 「明」に切り替え
                SetScreenBrightness(!(lightSensorValue < luxThreshold));

                // *****
                // スクリーン常時ON設定（WakeLock）のシステム反映
                // *****
                //     app/src/main/AndroidManifest.xml に次の権限設定を追記する
                //     <uses-permission android:name="android.permission.WAKE_LOCK" />
                // *****
                if (radioScreenOffMode.getCheckedRadioButtonId() == R.id.radioButtonON) {
                    if (wakeLock == null) {
                        // FULL_WAKE_LOCK は Android API 17 以降は非推奨
                        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyApp::MyWakelockTag");
                    }
                    if (!wakeLock.isHeld()) {
                        wakeLock.acquire();
                        // デバッグログ出力
                        {
                            // "com.example.[APP_NAME].MainActivity$1" より "APP_NAME" を抜き出し、ログ出力時の tag として利用する
                            String strClassName = Objects.requireNonNull(new Object() {
                            }.getClass().getEnclosingClass()).getName();
                            String[] arr = strClassName.split("[.]");
                            Log.d(arr[(arr.length - 2)], "wakeLock.acquire()");
                        }
                    }

                } else {
                    if (wakeLock != null && wakeLock.isHeld()) {
                        wakeLock.release();
                        // デバッグログ出力
                        {
                            // "com.example.[APP_NAME].MainActivity$1" より "APP_NAME" を抜き出し、ログ出力時の tag として利用する
                            String strClassName = Objects.requireNonNull(new Object() {
                            }.getClass().getEnclosingClass()).getName();
                            String[] arr = strClassName.split("[.]");
                            Log.d(arr[(arr.length - 2)], "wakeLock.release()");
                        }
                    }

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
        // 電源管理 WakeLockの開放
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

    // ディスプレイ照度の明暗を設定する
    // 引数 boolScreenOn ---> true : 明 , false : 暗
    private void SetScreenBrightness(boolean boolScreenOn) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (boolScreenOn) {
            // 画面照度を「明」に変更する
            if (lp.screenBrightness != settingValScreenBright) {
                lp.screenBrightness = settingValScreenBright;
                getWindow().setAttributes(lp);
            }
        } else {
            // 画面照度を「暗」に変更する
            if (lp.screenBrightness != settingValScreenDark) {
                lp.screenBrightness = settingValScreenDark;
                getWindow().setAttributes(lp);
            }
        }
    }
}