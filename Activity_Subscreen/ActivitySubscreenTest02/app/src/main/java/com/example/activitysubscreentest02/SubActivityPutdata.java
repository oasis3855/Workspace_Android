package com.example.activitysubscreentest02;
/*****
 * Subアクティビティ
 *
 * このクラスファイルの新規作成方法
 *     activity_sub_putdata.xml の tools:context=".SubActivityPutdata" にエラーが表示されるので、
 *     そのコンテキストメニューでclass新規作成を選択すれば、このクラスファイルが自動生成される。
 *
 *     classの派生元を「extends Activity」から「extends AppCompatActivity」に書き換えた
 *
 * app/src/main/AndroidManifest.xml に次の1行を追加すること
 *      <activity android:name=".SubActivity"></activity>
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivityPutdata extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // xmlからレイアウトリソースを読み込む
        setContentView(R.layout.activity_sub_putdata);

        // intentから受け渡しデータを読み込んで、editTextに表示する
        Intent intent = getIntent();
        EditText textStr01 = findViewById(R.id.editTextString01);
        textStr01.setText(intent.getStringExtra("KEY_STR01"));
        EditText textInt01 = findViewById(R.id.editTextInt01);
        textInt01.setText(Integer.toString(intent.getIntExtra("KEY_INT01", -1)));

        // ボタン押下のクリックイベントを登録
        findViewById(R.id.buttonCloseSubActivity).setOnClickListener(this);
    }

    // *****
    // 閉じるボタンを押したときの処理（このアクティビティを終了する）
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonCloseSubActivity) {
            // このActivityを閉じる（呼び出し元のActivity画面に戻る）
            this.finish();
        }
    }
}
