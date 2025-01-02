package com.example.activitysubscreentest02;
/*****
 * Mainアクティビティ
 *
 * Subアクティビティを起動し、intentを利用しデータを相互に受け渡しするサンプルプログラム
 *
 * 1) Mainアクティビティ -> Subアクティビティ
 *     startActivity(intent) を利用してSubアクティビティを起動
 *
 * 2) Subアクティビティ -> Mainアクティビティ
 *     Activity Result APIを利用する方法
 *     ActivityResultLauncher.launch(intent) を利用してSubアクティビティを起動
 *
 *     ※ startActivityForResult(intent, REQUEST_CODE) は非推奨となったため、
 *     Activity Result APIを利用する
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 受け渡しデータ用editTextに初期値設定
        EditText textStr01 = findViewById(R.id.editTextString01);
        textStr01.setText("初期文字列");
        EditText textInt01 = findViewById(R.id.editTextInt01);
        textInt01.setText("1024");

        // ボタン押下のクリックイベントを登録
        findViewById(R.id.buttonSubActivityPutdata).setOnClickListener(this);
        findViewById(R.id.buttonSubActivityReturndata).setOnClickListener(this);

        // Activity Result API を利用して Subアクティビティを起動するための準備
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::handleActivityResult
        );
    }

    // *****
    // Subアクティビティ データを渡す・受け取る ボタンを押したときの処理
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSubActivityPutdata) {
            // Subアクティビティにデータを渡すボタンが押されたときの処理
            Intent intent = new Intent(this, SubActivityPutdata.class);
            // 受け渡しデータをeditTextから読み込み、intentに送る（書き込む）
            EditText textStr01 = findViewById(R.id.editTextString01);
            intent.putExtra("KEY_STR01", textStr01.getText().toString());
            EditText textInt01 = findViewById(R.id.editTextInt01);
            intent.putExtra("KEY_INT01", Integer.parseInt(textInt01.getText().toString()));
            // Subアクティビティを起動
            startActivity(intent);

        } else if (id == R.id.buttonSubActivityReturndata) {
            // Subアクティビティからデータを受け取るボタンが押されたときの処理
            Intent intent = new Intent(this, SubActivityReturndata.class);
            // Activity Result API を利用して Subアクティビティを起動する
            activityResultLauncher.launch(intent);
        }
    }

    // *****
    // Activity Result API で、起動したSubアクティビティからの戻り値を受け取るためのメソッド
    private void handleActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            // Subアクティビティでデータを格納したintentを開く
            Intent intent = result.getData();
            // Subアクティビティから受け取ったデータをEditTextに表示する
            EditText textStr01 = findViewById(R.id.editTextString01);
            String s1 = intent.getStringExtra("KEY_STR01");
            textStr01.setText(intent.getStringExtra("KEY_STR01"));
            EditText textInt01 = findViewById(R.id.editTextInt01);
            int i1 = intent.getIntExtra("KEY_INT01", 0);
            textInt01.setText(Integer.toString(intent.getIntExtra("KEY_INT01", 0)));
        }
    }
}
