package com.example.dialogfragmenttest01;
/*
DialogFragmentを用いたカスタムダイアログを起動するメイン・アクティビティ

任意の型・個数の引数をDialogFragmentに渡し、ダイアログでの操作の結果（どのボタンが押されたかや、任意の引数）を
呼び出し側（のActivityMain）で受け取ることができる

値の受け渡しにはBundleを使う。（intentを使ってもよいが、一般的にintentはActivity間で用いられるもの）
MainActivity -> DialogFragment : 値の転送 Bundle
DialogFragment -> MainActivity : 値の転送 Bundle、ボタン押下 interface
 */

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentBasicSample.NoticeDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // xmlからレイアウトリソースを読み込む
        setContentView(R.layout.activity_main);

        // ボタン押下のクリックイベントを登録
        findViewById(R.id.buttonBasicSample).setOnClickListener(this);
    }

    // *****
    // ボタン押下
    // implements View.OnClickListener を追記して自動的に挿入される
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonBasicSample) {
            // ダイアログ表示処理をメソッドに切り出している
            showDialog_Basic01();
        }
    }

    // *****
    // buttonBasicSample押下したとき
    // DialogFragment FragmentBasicSample を表示する
    private void showDialog_Basic01() {
        DialogFragment dlg = new FragmentBasicSample();
        // Fragmentにデータ（文字列と数値）を引き渡す
        Bundle bundle = new Bundle();
        EditText editText_Str = this.findViewById(R.id.editText_Str);
        bundle.putString("KEY_STR", editText_Str.getText().toString());
        EditText editText_Int = this.findViewById(R.id.editText_Int);
        bundle.putInt("KEY_INT", Integer.parseInt(editText_Int.getText().toString()));
        dlg.setArguments(bundle);
        // カスタムダイアログFragmentBasicSampleを表示する
        dlg.show(getSupportFragmentManager(), "FragmentBasicSample");
    }

    // *****
    // DialogFragment FragmentBasicSample で interface 定義されたリスナーメソッド
    // ダイアログでOkボタンを押したときに呼び出される
    @Override
    public void onDialogButtonOkClick(DialogFragment dialog, Bundle bundle) {
        // DialogFragmentから返される値をテキストボックス EditText に反映する
        EditText editText_Str = this.findViewById(R.id.editText_Str);
//        editText_Str.setText(intent.getStringExtra("KEY_STR"));   // 値の受け渡しにintentを利用する場合
        editText_Str.setText(bundle.getString("KEY_STR", "not found"));
//        editText_Int.setText(Integer.toString(intent.getIntExtra("KEY_INT", 0)));     // 値の受け渡しにintentを利用する場合
        EditText editText_Int = this.findViewById(R.id.editText_Int);
        editText_Int.setText(Integer.toString(bundle.getInt("KEY_INT", 0)));

        Toast.makeText(this, "Okボタンがタップされました", Toast.LENGTH_LONG)
                .show();
    }

    // *****
    // DialogFragment FragmentBasicSample で interface 定義されたリスナーメソッド
    // ダイアログでCancelボタンを押したときに呼び出される
    @Override
    public void onDialogButtonCancelClick(DialogFragment dialog) {
        Toast.makeText(this, "Cancelボタンがタップされました", Toast.LENGTH_LONG)
                .show();
    }
}
