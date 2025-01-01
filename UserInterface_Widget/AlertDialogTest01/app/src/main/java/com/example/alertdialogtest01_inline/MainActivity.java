package com.example.alertdialogtest01_inline;
/*
 ユーザ入力インターフェースwidgetの作成例
 AlertDialogに実装編（その1）

 Okダイアログ、Yes/No/Cancelダイアログ、EditTextダイアログ、
 日付選択ダイアログ、時刻選択ダイアログ、EditTextの入力制限設定
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // xmlからレイアウトリソースを読み込む
        setContentView(R.layout.activity_main);

        // ボタン押下のクリックイベントを登録
        findViewById(R.id.buttonOk).setOnClickListener(this);
        findViewById(R.id.buttonYesNo).setOnClickListener(this);
        findViewById(R.id.buttonEditText_1).setOnClickListener(this);
        findViewById(R.id.buttonEditText_2).setOnClickListener(this);
        findViewById(R.id.buttonEditText_Date).setOnClickListener(this);
        findViewById(R.id.buttonEditText_DateDlg).setOnClickListener(this);
        findViewById(R.id.buttonEditText_TimeDlg).setOnClickListener(this);
        findViewById(R.id.buttonEditText_Int).setOnClickListener(this);
        findViewById(R.id.buttonEditText_AzFilter).setOnClickListener(this);
    }

    // *****
    // ボタン押下
    // implements View.OnClickListener を追記して自動的に挿入される
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonOk) {
            showAlertDialog_Ok();
        } else if (id == R.id.buttonYesNo) {
            showAlertDialog_YesNo();
        } else if (id == R.id.buttonEditText_1) {
            showAlertDialog_EditText_1();
        } else if (id == R.id.buttonEditText_2) {
            showAlertDialog_EditText_2();
        } else if (id == R.id.buttonEditText_Date) {
            showAlertDialog_EditText_Date();
        } else if (id == R.id.buttonEditText_DateDlg) {
            showAlertDialog_EditText_DateDlg();
        } else if (id == R.id.buttonEditText_TimeDlg) {
            showAlertDialog_EditText_TimeDlg();
        } else if (id == R.id.buttonEditText_Int) {
            showAlertDialog_EditText_Int();
        } else if (id == R.id.buttonEditText_AzFilter) {
            showAlertDialog_EditText_AzFilter();
        }
    }

    // *****
    // buttonOk押下したときに表示するAlertDialog
    // Okメッセージボックス
    private void showAlertDialog_Ok() {
        new AlertDialog.Builder(this)
                .setTitle("Ok AlertDialog")
                .setMessage("Okボタンを押してください")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Okが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // buttonYesNoCancel押下したときに表示するAlertDialog
    // Yes/No/Cancel 選択メッセージボックス
    private void showAlertDialog_YesNo() {
        new AlertDialog.Builder(this)
                .setTitle("Yes/No/Cancel AlertDialog")
                .setMessage("いづれかのボタンを押してください")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Yesが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Noが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setCancelable(true).show();

    }

    // *****
    // buttonEditText_1押下したときに表示するAlertDialog
    // テキスト入力インターフェース × 1個
    private void showAlertDialog_EditText_1() {
        // テキスト入力インターフェース
        final EditText inputText = new EditText(this);
        inputText.setLines(1);
        inputText.setText("初期文字列");
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("EditText 1行")
                .setMessage("文字列を入力してください")
                // テキスト入力インターフェース1個を紐付け
                .setView(inputText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "入力された文字列 : " + inputText.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // buttonEditText_2押下したときに表示するAlertDialog
    // テキスト入力インターフェース × 2個
    private void showAlertDialog_EditText_2() {
        // 複数のユーザ インターフェースを縦列配置するレイアウトを定義
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        // テキスト入力インターフェース 1つめ
        TextView text1 = new TextView(this);
        text1.setText("入力1");
        layout.addView(text1);

        final EditText inputText1 = new EditText(this);
        inputText1.setLines(1);
        inputText1.setText("初期文字列1");
        inputText1.setInputType(InputType.TYPE_CLASS_TEXT);

        // 作成したテキスト入力インターフェースをレイアウトに追加する
        layout.addView(inputText1);

        // テキスト入力インターフェース 2つめ
        TextView text2 = new TextView(this);
        text2.setText("入力2");
        layout.addView(text2);

        final EditText inputText2 = new EditText(this);
        inputText2.setLines(1);
        inputText2.setText("初期文字列2");
        inputText2.setInputType(InputType.TYPE_CLASS_TEXT);

        // 作成したテキスト入力インターフェースをレイアウトに追加する
        layout.addView(inputText2);

        new AlertDialog.Builder(this)
                .setTitle("EditText 1行")
                .setMessage("文字列を入力してください")
                // レイアウトを紐付け
                .setView(layout)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "入力された文字列 : " + inputText1.getText().toString() + "＆" + inputText2.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // buttonEditText_Date押下したときに表示するAlertDialog
    // テキスト入力インターフェース（年月日の入力ガイド付き）
    private void showAlertDialog_EditText_Date() {
        final EditText inputText = new EditText(this);
        inputText.setLines(1);
        inputText.setHint("YYYY/MM/DD");
        // setInputTypeはキーボードの初期値を決めるだけで、日付に合致しない文字列でも入力できてしまう
        inputText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);

        new AlertDialog.Builder(this)
                .setTitle("EditText(日付)")
                .setMessage("年月日を入力してください")
                .setView(inputText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "入力された文字列 : " + inputText.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();

    }

    // *****
    // buttonEditText_DateDlg押下したときに表示するAlertDialog
    // テキスト入力インターフェース（手動入力不可で、クリックするとカレンダーダイアログが表示される）
    private void showAlertDialog_EditText_DateDlg() {
        final EditText inputText = new EditText(this);
        inputText.setLines(1);
        inputText.setHint("YYYY/MM/DD");
        // キーボードを表示しない
        inputText.setInputType(InputType.TYPE_NULL);
        // テキスト入力インターフェースをクリックしたときの処理
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // カレンダー ダイアログを表示する（ユーザ定義したメソッドを呼び出す）
                showDatePickerDialog(inputText);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("EditText(日付)")
                .setMessage("年月日を入力してください")
                .setView(inputText).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "入力された文字列 : " + inputText.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // 日付選択のためのカレンダーダイアログ
    // （Activityから直接呼び出す場合は、CalendarView を AlertDialog に組み込む方法でも、同様の機能が実現できる）
    private void showDatePickerDialog(EditText inputText) {
        // 現在の日付を取得（ダイアログの初期値として利用するため）
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 日付選択ダイアログの構築
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 選択された日付をEditTextに表示
                inputText.setText(String.format("%d/%02d/%02d", year, monthOfYear + 1, dayOfMonth));
            }
        }, year, month, day);

        // 日付選択ダイアログの表示
        datePickerDialog.show();
    }

    // *****
    // buttonEditText_TimeDlg押下したときに表示するAlertDialog
    // テキスト入力インターフェース（手動入力不可で、クリックすると時刻選択ダイアログが表示される）
    private void showAlertDialog_EditText_TimeDlg() {
        final EditText inputText = new EditText(this);
        inputText.setLines(1);
        inputText.setHint("HH:MM");
        // キーボードを表示しない
        inputText.setInputType(InputType.TYPE_NULL);
        // テキスト入力インターフェースをクリックしたときの処理
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 時刻選択ダイアログを表示する（ユーザ定義したメソッドを呼び出す）
                showTimePickerDialog(inputText);
            }
        });

        new AlertDialog
                .Builder(this)
                .setTitle("EditText(時刻)")
                .setMessage("時刻を入力してください")
                .setView(inputText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "入力された文字列 : " + inputText.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // 時刻選択のためのアナログ時計（時／分）ダイアログ
    private void showTimePickerDialog(EditText inputText) {
        // 現在の時刻を取得（ダイアログの初期値として利用するため）
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // 選択された時刻をEditTextに表示
                inputText.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, hour, minute, true);

        // 時刻選択ダイアログの表示
        timePickerDialog.show();
    }

    // *****
    // buttonEditText_Int押下したときに表示するAlertDialog
    // テキスト入力インターフェース（setInputTypeにより、数値のみ入力可）
    private void showAlertDialog_EditText_Int() {
        final EditText inputText = new EditText(this);
        inputText.setLines(1);
        inputText.setHint("0123");
        // setInputTypeはキーボードの初期値を決めるだけで、数値に合致しない文字列でも入力できてしまう
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
                .setTitle("EditText(数値)")
                .setMessage("整数を入力してください")
                .setView(inputText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "入力された文字列 : " + inputText.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();

    }

    // *****
    // buttonEditText_AzFilter押下したときに表示するAlertDialog
    // テキスト入力インターフェースに入力フィルターを適用し、アルファベット（A-Z）のみに入力を制限
    private void showAlertDialog_EditText_AzFilter() {
        final EditText inputText = new EditText(this);
        inputText.setLines(1);
        inputText.setHint("ABC...XYZ");
        // setInputTypeはキーボードの初期値を決めるだけで、大文字A-Zに合致しない文字列でも入力できてしまう
        inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        // 入力フィルターを定義
        InputFilter filter_AZ = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.toString().matches("^[A-Z]+$")) {
                    return charSequence;
                } else {
                    return "";
                }
            }
        };
        // テキスト入力インターフェースに、ユーザ定義した入力フィルターを紐付け
        inputText.setFilters(new InputFilter[]{filter_AZ});

        new AlertDialog.Builder(this)
                .setTitle("EditText(A-Z Filter)")
                .setMessage("アルファベット大文字の文字列を入力してください")
                .setView(inputText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "入力された文字列 : " + inputText.getText().toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "Cancelが押された", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();

    }


}