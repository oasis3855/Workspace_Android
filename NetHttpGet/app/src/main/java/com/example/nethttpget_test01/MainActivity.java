package com.example.nethttpget_test01;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // セキュリティ確保のため、デバッグ終了後にテスト・スクリプト・ディレクトリごと削除する
    private final String STR_TARGET_URL = "https://www.example.com/cgi-bin/get-post.cgi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 受信データ表示用のテキストエリアを縦スクロール可能にする
        TextView textData = findViewById(R.id.textView_Data);
        textData.setMovementMethod(new ScrollingMovementMethod());

        findViewById(R.id.button_receiveThread).setOnClickListener(this);
        findViewById(R.id.button_receiveExecutor).setOnClickListener(this);
        findViewById(R.id.button_receiveExecutorService).setOnClickListener(this);
        findViewById(R.id.button_postExecutor).setOnClickListener(this);
        findViewById(R.id.button_close).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view != null) {

            TextView textData = findViewById(R.id.textView_Data);
            textData.setText("HTTP通信スレッド動作中");

            // 「終了」ボタンが押された場合の処理 : プログラムを終了する
            if (view.getId() == R.id.button_close) {
                finish();
            } else if (view.getId() == R.id.button_receiveThread) {
                HttpFetch_Thread();
            } else if (view.getId() == R.id.button_receiveExecutor) {
                HttpFetch_Executor();
            } else if (view.getId() == R.id.button_receiveExecutorService) {
                HttpFetch_ExecutorService();
            } else if (view.getId() == R.id.button_postExecutor) {
                HttpFetch_PostExecutor();
            }
        }
    }

    /**
     * Thread を用いて非同期処理を行い、HTTP GET で URLパラメータを送る方法
     * (ExecutorService.shutdown();のような終了処理がクラスに存在しないため、手動で多重起動を管理する必要あり)
     */
    private void HttpFetch_Thread() {
        Thread thread = new Thread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                // 非同期タスクの多重起動防止のため、このタスクが終了するまでの間ボタンを無効化する
                Change_Button_State(false);
                // HTTP接続・データ読込用。try...finallyでまとめて閉じるため、初期化されていない場合の検知用にnullを代入しておく
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(STR_TARGET_URL +
                            "?param1=" + URLEncoder.encode("value1", StandardCharsets.UTF_8.toString()) +
                            "&param2=" + URLEncoder.encode("引数 2<>", StandardCharsets.UTF_8.toString()));
                    connection = (HttpURLConnection) url.openConnection();
                    // 推奨 : タイムアウトを設定
                    connection.setConnectTimeout(5000);    // 接続タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    connection.setReadTimeout(5000);        // データ受信タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    // 任意 : User-AgentやAccept-Languageを手動設定
                    connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13;) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/114.0.5735.196 Mobile Safari/537.36");
                    connection.addRequestProperty("Accept-Language", "ja-JP,en-US");    // ISO形式:ja-JP, Java形式 ja_JP
                    connection.setRequestMethod("GET");
                    // connect() の記述は必須ではないが、HTTPレスポンスコードで200(HTTP OK)以外をエラーとするために実施する
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new Exception(String.format("Error HTTP response code : %d", responseCode));
                    }
                    // HTTP connection よりデータを1行ずつ全てダウンロードし、文字列 response に逐次追加する
                    inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    // ダウンロードしたデータを画面表示する
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText(response.toString());
                        }
                    });

                } catch (Exception e) {
                    // e.printStackTrace();    // Logcat for debugging
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText("Error Message : " + Objects.toString(e.getMessage(), "(No Message)"));
                        }
                    });
                } finally {
                    try {
                        if (reader != null) reader.close();
                        if (inputStream != null) inputStream.close();
                        if (connection != null) connection.disconnect();
                    } catch (Exception e) {
                        // e.printStackTrace();    // Logcat for debugging
                    }
                }
                Change_Button_State(true);
            }
        });
        thread.start();
    }

    /**
     * Executor を用いて非同期処理を行い、HTTP GET で URLパラメータを送る方法
     * (ExecutorService.shutdown();のような終了処理がクラスに存在しないため、手動で多重起動を管理する必要あり)
     */
    private void HttpFetch_Executor() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // 非同期タスクの多重起動防止のため、このタスクが終了するまでの間ボタンを無効化する
                Change_Button_State(false);
                // HTTP接続・データ読込用。try...finallyでまとめて閉じるため、初期化されていない場合の検知用にnullを代入しておく
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(STR_TARGET_URL +
                            "?param1=" + URLEncoder.encode("value1", StandardCharsets.UTF_8.toString()) +
                            "&param2=" + URLEncoder.encode("引数 2<>", StandardCharsets.UTF_8.toString()));
                    connection = (HttpURLConnection) url.openConnection();
                    // 推奨 : タイムアウトを設定
                    connection.setConnectTimeout(5000);    // 接続タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    connection.setReadTimeout(5000);        // データ受信タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    connection.setRequestMethod("GET");
                    // connect() の記述は必須ではないが、HTTPレスポンスコードで200(HTTP OK)以外をエラーとするために実施する
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new Exception(String.format("Error HTTP response code : %d", responseCode));
                    }
                    // HTTP connection よりデータを1行ずつ全てダウンロードし、文字列 response に逐次追加する
                    inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    // ダウンロードしたデータを画面表示する
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText(response.toString());
                        }
                    });

                } catch (Exception e) {
                    // e.printStackTrace();    // Logcat for debugging
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText("Error Message :\n" + Objects.toString(e.getMessage(), "(No Message)"));
                        }
                    });
                } finally {
                    try {
                        if (reader != null) reader.close();
                        if (inputStream != null) inputStream.close();
                        if (connection != null) connection.disconnect();
                    } catch (Exception e) {
                        // e.printStackTrace();    // Logcat for debugging
                    }
                }
                Change_Button_State(true);
            }
        });
    }

    /**
     * ExecutorService を用いて非同期処理を行い、HTTP GET で URLパラメータを送る方法
     * (新たな非同期タスクをキューに追加させないよう、終了処理 shutdown(); を明示的に書く必要がある)
     */
    private void HttpFetch_ExecutorService() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // 非同期タスクの多重起動防止のため、このタスクが終了するまでの間ボタンを無効化する
                Change_Button_State(false);
                // HTTP接続・データ読込用。try...finallyでまとめて閉じるため、初期化されていない場合の検知用にnullを代入しておく
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(STR_TARGET_URL +
                            "?param1=" + URLEncoder.encode("value1", StandardCharsets.UTF_8.toString()) +
                            "&param2=" + URLEncoder.encode("引数 2<>", StandardCharsets.UTF_8.toString()));
                    connection = (HttpURLConnection) url.openConnection();
                    // 推奨 : タイムアウトを設定
                    connection.setConnectTimeout(5000);    // 接続タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    connection.setReadTimeout(5000);        // データ受信タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    connection.setRequestMethod("GET");
                    // connect() の記述は必須ではないが、HTTPレスポンスコードで200(HTTP OK)以外をエラーとするために実施する
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new Exception(String.format("Error HTTP response code : %d", responseCode));
                    }
                    // HTTP connection よりデータを1行ずつ全てダウンロードし、文字列 response に逐次追加する
                    inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    // ダウンロードしたデータを画面表示する
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText(response.toString());
                        }
                    });

                } catch (Exception e) {
                    // e.printStackTrace();    // Logcat for debugging
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText("Error Message :\n" + Objects.toString(e.getMessage(), "(No Message)"));
                        }
                    });
                } finally {
                    try {
                        if (reader != null) reader.close();
                        if (inputStream != null) inputStream.close();
                        if (connection != null) connection.disconnect();
                    } catch (Exception e) {
                        // e.printStackTrace();    // Logcat for debugging
                    }
                }
                Change_Button_State(true);
            }
        });

        // 終了処理（メモリリークを防止するため、必須）
        // ※ executor.execute(new Runnable() {...}); により、すでに実行キューに入っているタスクは
        //    実行される。shutdown()は、これ以降に新たにキューに追加されないということを意味する。
        //    なお、直ちにシャットダウンする場合はshutdownNow();を用いる。
        executorService.shutdown();
    }

    /**
     * Executor を用いて非同期処理を行い、HTTP POST で パラメータを送る方法
     * (ExecutorService.shutdown();のような終了処理がクラスに存在しないため、手動で多重起動を管理する必要あり)
     */
    private void HttpFetch_PostExecutor() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // 非同期タスクの多重起動防止のため、このタスクが終了するまでの間ボタンを無効化する
                Change_Button_State(false);

                // HTTP接続・データ読込用。try...finallyでまとめて閉じるため、初期化されていない場合の検知用にnullを代入しておく
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                BufferedReader reader = null;
                try {
                    // POSTアクセスする前に、HEADでファイルの存在のみ確認する
                    int responseCodeHEAD = isUrlValid(STR_TARGET_URL);
                    if (responseCodeHEAD != HttpURLConnection.HTTP_OK) {
                        throw new Exception(String.format("Error HTTP response code : %d", responseCodeHEAD));
                    }

                    URL url = new URL(STR_TARGET_URL +
                            "?param1=" + URLEncoder.encode("value1_url", StandardCharsets.UTF_8.toString()) +
                            "&param2=" + URLEncoder.encode("引数 2<>_url", StandardCharsets.UTF_8.toString()));
                    connection = (HttpURLConnection) url.openConnection();
                    // 推奨 : タイムアウトを設定
                    connection.setConnectTimeout(5000);    // 接続タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    connection.setReadTimeout(5000);        // データ受信タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);
                    // connect() の記述は必須ではないが、HTTPレスポンスコードで200(HTTP OK)以外をエラーとするために実施する
                    // connection.connect();

                    // 送信するパラメータ
                    String stringParams = "param1=" + URLEncoder.encode("value1", StandardCharsets.UTF_8.toString()) +
                            "&param2=" + URLEncoder.encode("引数 2<>", StandardCharsets.UTF_8.toString());
                    // 送信するパラメータを PrintStream で書き込む （try-with-resources文の終了時にps.close()が自動実行される）
                    try (PrintStream ps = new PrintStream(connection.getOutputStream())) {
                        ps.print(stringParams);  // 改行なし
                    }

                    // HTTP connection よりデータを1行ずつ全てダウンロードし、文字列 response に逐次追加する
                    inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }

                    // ダウンロードしたデータを画面表示する
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText(response.toString());
                        }
                    });

                } catch (Exception e) {
                    // e.printStackTrace();    // Logcat for debugging
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            TextView textData = findViewById(R.id.textView_Data);
                            textData.setText("Error Message :\n" + Objects.toString(e.getMessage(), "(No Message)"));
                        }
                    });
                } finally {
                    try {
                        if (reader != null) reader.close();
                        if (inputStream != null) inputStream.close();
                        if (connection != null) connection.disconnect();
                    } catch (Exception e) {
                        // e.printStackTrace();    // Logcat for debugging
                    }
                }
                Change_Button_State(true);
            }
        });
    }

    /**
     * 指定したURLにHTTP HEADアクセスして、ファイルの存在を確認する
     *
     * @param urlString : URL
     * @return responseCode : レスポンスコード(正常の場合はHttpURLConnection.HTTP_OK=200), サーバ未到達の場合は 0
     */
    private int isUrlValid(String urlString) {
        HttpURLConnection connection = null;
        int responseCode = 0;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            // 推奨 : タイムアウトを設定
            connection.setConnectTimeout(5000);    // 接続タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
            connection.setReadTimeout(5000);        // データ受信タイムアウト ミリ秒（デフォルトは0で無限秒）。推奨値は5000から10000 msec
            connection.setRequestMethod("HEAD");
            responseCode = connection.getResponseCode();
        } catch (Exception e) {
            if(Objects.requireNonNull(e.getMessage()).contains("timeout")){
                responseCode = 408;
            }
            // e.printStackTrace();    // Logcat for debugging
        } finally {
            try {
                if (connection != null) connection.disconnect();
            } catch (Exception e) {
                // e.printStackTrace();    // Logcat for debugging
            }
        }

        return (responseCode);
    }

    /***
     * 非同期タスクを起動するためのボタンを有効化(グレーアウト)/無効化する
     *
     * @param boolEnable : trueはボタン有効, falseはボタン無効(グレーアウト)
     */
    private void Change_Button_State(boolean boolEnable) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Button button_ReceiveThread = findViewById(R.id.button_receiveThread);
                button_ReceiveThread.setEnabled(boolEnable);
                Button button_ReceiveExecutor = findViewById(R.id.button_receiveExecutor);
                button_ReceiveExecutor.setEnabled(boolEnable);
                Button button_ReceiveExecutorService = findViewById(R.id.button_receiveExecutorService);
                button_ReceiveExecutorService.setEnabled(boolEnable);
                Button button_PostExecutor = findViewById(R.id.button_postExecutor);
                button_PostExecutor.setEnabled(boolEnable);
            }
        });
    }
}