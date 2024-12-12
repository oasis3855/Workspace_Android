package com.example.displaysleeptest05;
/*
 * 照度センサー、画面明るさ、画面スリープのテストプログラム
 *
 * 1) スレッドを用いて、1秒毎に画面情報表示＆ログ出力（現在時刻、照度センサー値 ほか）
 * 2) 照度センサー値により、画面明るさを2段階に切り替え（システム全体 : Settings.System.SCREEN_BRIGHTNESS）
 *
 * ※ システム全体の画面明るさ設定を得るための Intent の実装
 *
 */

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
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
    private final int settingValScreenBright = 80;
    private final int settingValScreenDark = 10;
    // 照度センサー読み出し用
    private SensorManager sensorManager;
    private float lightSensorValue;

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

        // ディスプレイ照度設定の権限を得る
        GetPermissionScreenBrightness();

        // 照度センサー値取得のため
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // スクリーンOFFでもバックグラウンドでセンサーを使う場合は、ここでregisterListenerを実行。
        // そうでなければ、OnResume()内でregisterListenerを、onPause()内でunregisterListenerを実行する
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        lightSensorValue = 1000.0f;    // 照度初期値は 1000 (Lux)とする

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
                // 照度しきい値（Lux）の表示
                TextView textLuxThreshold = findViewById(R.id.textViewLuxThreshold);
                textLuxThreshold.setText(String.format("%.2f", luxThreshold));

                // *****
                // 現在の画面照度
                TextView textBrightness = findViewById(R.id.textViewBrightness);
                int brightness = 0;
                try {
                    // Brightnessが 0 〜 255 で得られる
                    brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                    textBrightness.setText(String.format("%d", brightness));
                } catch (Settings.SettingNotFoundException e) {
                    textBrightness.setText("ERR");
                }

                // *****
                // 照度センサー（Lux）による画面の明・暗切り替え処理
                // 「暗」に切り替え
                // 「明」に切り替え
                SetScreenBrightness(!(lightSensorValue < luxThreshold));

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
    }

    // センサー・イベント・リスナーでオーバーライドされる関数
    //（class MainActivity に implements SensorEventListener を記述すると、自動的に作成される）    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // 照度センサー値の場合は、メンバ変数lightSensorValueにコピーする
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightSensorValue = sensorEvent.values[0];
        }
    }

    // センサー・イベント・リスナーでオーバーライドされる関数
    //（class MainActivity に implements SensorEventListener を記述すると、自動的に作成される）    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // ディスプレイ照度の明暗を設定する
    // 引数 boolScreenOn ---> true : 明 , false : 暗
    //
    // API 22以下を対象とした権限取得設定を、app/src/main/AndroidManifest.xml に追記する
    // <uses-permission android:name="android.permission.WRITE_SETTINGS">
    //
    private void SetScreenBrightness(boolean boolScreenOn) {
        int val;
        if (boolScreenOn) {
            val = settingValScreenBright;
        } else {
            val = settingValScreenDark;
        }

        if (Settings.System.canWrite(this)) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, val);
        } else {
            // ここに権限取得インテントを設置すると、1秒毎に新たなインテントが表示されてしまう。
            // （インテント画面でユーザの操作を待たずにstartActivity行を通過してしまうため。）
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M /* 23.0 */) {
//                // Android 6.0 (M) 以降、ユーザに「システム設定の変更」権限を求める
//                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivity(intent);
//            }
        }
    }

    private void GetPermissionScreenBrightness() {
        if (!Settings.System.canWrite(this)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M /* 23.0 */) {
                // Android 6.0 (M) 以降、ユーザに「システム設定の変更」権限を求める
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }

    }
}