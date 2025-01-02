package com.example.activitysubscreentest01;
/*****
 * Subアクティビティ
 *
 * このクラスファイルの新規作成方法
 *     activity_sub.xml の tools:context=".SubActivityPutdata" にエラーが表示されるので、
 *     そのコンテキストメニューでclass新規作成を選択すれば、このクラスファイルが自動生成される。
 *
 * app/src/main/AndroidManifest.xml に次の1行を追加すること
 *      <activity android:name=".SubActivity"></activity>
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // xmlからレイアウトリソースを読み込む
        setContentView(R.layout.activity_sub);

        // 閉じるボタン
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
