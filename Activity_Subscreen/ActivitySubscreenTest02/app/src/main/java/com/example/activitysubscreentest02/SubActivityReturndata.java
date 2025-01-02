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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivityReturndata extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // xmlからレイアウトリソースを読み込む
        setContentView(R.layout.activity_sub_returndata);

        // 受け渡しデータ用editTextに初期値設定
        EditText textStr01 = findViewById(R.id.editTextString01);
        textStr01.setText("送り返す文字列");
        EditText textInt01 = findViewById(R.id.editTextInt01);
        textInt01.setText("-2048");

        // ボタン押下のクリックイベントを登録
        findViewById(R.id.buttonCloseSubActivity).setOnClickListener(this);
    }

    // *****
    // 閉じるボタンを押したときの処理（このアクティビティを終了する）
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonCloseSubActivity) {
            // 受け渡しデータをeditTextから読み込み、intentに送る（書き込む）
            Intent intent = new Intent(this, MainActivity.class);
            EditText textStr01 = findViewById(R.id.editTextString01);
            intent.putExtra("KEY_STR01", textStr01.getText().toString());
            EditText textInt01 = findViewById(R.id.editTextInt01);
            intent.putExtra("KEY_INT01", Integer.parseInt(textInt01.getText().toString()));

            // result code は手動で設定する必要がある（この処理をしないと、RESULT_CANCEL が返る
            setResult(Activity.RESULT_OK, intent);
            // このActivityを閉じる（呼び出し元のActivity画面に戻る）
            this.finish();
        }
    }
}
