package com.example.alertdialogtest03;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonEditText).setOnClickListener(this);
        findViewById(R.id.buttonEditText_DateTime).setOnClickListener(this);
        findViewById(R.id.buttonSpinner).setOnClickListener(this);
        findViewById(R.id.buttonSwitch).setOnClickListener(this);
        findViewById(R.id.buttonChipM2).setOnClickListener(this);
        findViewById(R.id.buttonChipM3).setOnClickListener(this);
        findViewById(R.id.buttonSeekbar).setOnClickListener(this);
        findViewById(R.id.buttonRatingbar).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonEditText) {
            showDialog_EditText();
        } else if (id == R.id.buttonEditText_DateTime) {
            showDialog_EditText_Datetime();
        } else if (id == R.id.buttonSpinner) {
            showDialog_Spinner();
        } else if (id == R.id.buttonSwitch) {
            showDialog_Switch();
        } else if (id == R.id.buttonChipM2) {
            showDialog_Chip(2);
        } else if (id == R.id.buttonChipM3) {
            showDialog_Chip(3);
        } else if (id == R.id.buttonSeekbar) {
            showDialog_Seekbar();
        } else if (id == R.id.buttonRatingbar) {
            showDialog_Ratingbar();
        }
    }


    // *****
    // buttonEditText押下したときに表示するAlertDialog
    // テキスト入力Widgetを表示する。
    private void showDialog_EditText() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edittext, null);

        // AlertDialogの表示
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("EditTextのテスト")
                // OKボタンを押したときの処理
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final EditText editText_all = view.findViewById(R.id.editText_all);
                        final EditText editText_A2zMaxlen = view.findViewById(R.id.editText_A2zMaxlen);
                        final EditText editText_Number = view.findViewById(R.id.editText_Number);
                        final EditText editText_Email = view.findViewById(R.id.editText_Email);
                        final EditText editText_Password = view.findViewById(R.id.editText_Password);
                        Toast toast = Toast.makeText(MainActivity.this,
                                "1) : " + editText_all.getText() + "\n2)" + editText_A2zMaxlen.getText() +
                                        "\n3)" + editText_Number.getText() + "\n4)" + editText_Email.getText() +
                                        "\n4)" + editText_Password.getText(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }


    // *****
    // buttonEditText_DateTime押下したときに表示するAlertDialog
    // テキスト入力（DatePickerDialog/TimePickerDialog 利用）Widgetを表示する。
    @SuppressLint("ClickableViewAccessibility")
    private void showDialog_EditText_Datetime() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edittext_datetime, null);

        // EditTextをクリックしたときに、DatePickerDialog を表示する
        final EditText editText_DateDialog = view.findViewById(R.id.editText_DateDialog);
        editText_DateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(editText_DateDialog);
            }
        });

        // EditTextをクリックしたときにキーボードを表示するのを抑止する
        editText_DateDialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // ソフトキーボードを表示しないようにする
                    // editText_DateDialog.setInputType(InputType.TYPE_NULL);
                    editText_DateDialog.requestFocus();
                    editText_DateDialog.performClick(); // DatePickerDialogを表示
                }
                return true;
            }
        });

        // EditTextをクリックしたときに、TimePickerDialog を表示する
        final EditText editText_TimeDialog = view.findViewById(R.id.editText_TimeDialog);
        editText_TimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(editText_TimeDialog);
            }
        });

        // EditTextをクリックしたときにキーボードを表示するのを抑止する
        editText_TimeDialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // ソフトキーボードを表示しないようにする
                    // editText_TimeDialog.setInputType(InputType.TYPE_NULL);
                    editText_TimeDialog.requestFocus();
                    editText_TimeDialog.performClick(); // TimePickerDialogを表示
                }
                return true;
            }
        });

        // AlertDialogを表示する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("EditTextのテスト")
                // OKボタンを押したときの処理
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final EditText editText_Date = view.findViewById(R.id.editText_Date);
                        final EditText editText_DateDialog = view.findViewById(R.id.editText_DateDialog);
                        final EditText editText_TimeDialog = view.findViewById(R.id.editText_TimeDialog);
                        Toast toast = Toast.makeText(MainActivity.this,
                                "1) : " + editText_Date.getText() + "\n2)" + editText_DateDialog.getText() +
                                        "\n3)" + editText_TimeDialog.getText(), Toast.LENGTH_LONG);
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
    // buttonSpinner押下したときに表示するAlertDialog
    // スピナーを表示（選択肢より1つだけ選択できる）。
    private void showDialog_Spinner() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_spinner, null);

        // 選択肢をString配列に格納する
        final String[] items = new String[20];
        for (int i = 0; i < 20; i++)
            items[i] = String.format("選択肢 %d", i + 1);
        // ArrayAdapterを作成し、選択肢の配列を紐付け
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        // スピナーの表示形式を指定する
        //      simple_spinner_dropdown_item : 択一リスト風
        //      simple_spinner_item : 伝統的なドロップダウン・リスト
        //      simple_list_item_checked : チェックボックス風
        //      simple_list_item_single_choice : ラジオボタン風
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // スピナーを定義
        Spinner spinner = view.findViewById(R.id.spinner01);
        spinner.setAdapter(adapter);
        // スピナーの選択初期値（指定しない場合は、最初の選択肢が選択されている）
        spinner.setSelection(2);

        // AlertDialogを表示する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Spinnerのテスト")
                // OKボタンを押したときの処理
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "選択 : " + spinner.getSelectedItem(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }


    // *****
    // buttonSwitch押下したときに表示するAlertDialog
    // スイッチ（切り替えボタン） widgetを表示する
    private void showDialog_Switch() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_switch, null);

        // スイッチONのときに「チェック アイコン」を表示するには、XMLでapp:thumbIconを指定するのではなく、
        // ここでチェック状態に応じて設定を行う
        //
        // アイコンはGoogleのアイコンライブラリなどから16dpのものを取得し、res/drawableにコピーする
        //      https://fonts.google.com/icons?icon.size=24&icon.color=%23e8eaed
        final MaterialSwitch switchctl02 = view.findViewById(R.id.switch02);
        switchctl02.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) switchctl02.setThumbIconResource(R.drawable.check_16dp);
            else switchctl02.setThumbIconDrawable(null);
        });

        // AlertDialogを表示する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Switchのテスト")
                // OKボタンを押したときの処理
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // SwitchMaterialはMaterial 2、MaterialSwitchはMaterial 3のスイッチ
                        final SwitchMaterial switchctl01 = view.findViewById(R.id.switch01);
                        final MaterialSwitch switchctl02 = view.findViewById(R.id.switch02);
                        final ToggleButton togglectl01 = view.findViewById(R.id.togglebutton01);
                        Toast toast = Toast.makeText(MainActivity.this,
                                "スイッチ状態 : " + (switchctl01.isChecked() ? "ON" : "OFF") + ", " + (switchctl02.isChecked() ? "ON" : "OFF") +
                                        ", " + (togglectl01.isChecked() ? "ON" : "OFF"),
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }


    // *****
    // buttonChip押下したときに表示するAlertDialog
    // チップwidget（ON/OFFボタン）を表示する
    private void showDialog_Chip(int material_ver) {
        LayoutInflater inflater = getLayoutInflater();
        int layoutResource = R.layout.layout_chip_material_2;
        if (material_ver == 3) {
            layoutResource = R.layout.layout_chip_material_3;
        }
        View view = inflater.inflate(layoutResource, null);

        // xmlではなく、java側でアイコンを表示する場合
//        Chip chip = view.findViewById(R.id.chip3);
//        chip.setChipIcon(getResources().getDrawable(R.drawable.ic_check,null));

        // AlertDialogを表示する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chipのテスト")
                .setView(view)
                // OKボタンを押したときの処理
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = "";
                        ChipGroup chipgrp = view.findViewById(R.id.chip_group);
                        for (int j = 0; j < chipgrp.getChildCount(); j++) {
                            Chip chip = (Chip) chipgrp.getChildAt(j);
                            str += chip.isChecked() ? "ON " : "OFF ";
                        }
                        Toast toast = Toast.makeText(MainActivity.this, "チップ状態 : " + str, Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                // Cancelボタンを押したときの処理
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .create()
                .show();
    }


    // *****
    // buttonSeekbar押下したときに表示するAlertDialog
    // シークバーwidget（ON/OFFボタン）を表示する
    private void showDialog_Seekbar() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_seekbar, null);
        SeekBar seekbar = view.findViewById(R.id.seekBar);
        // 左右キーで10刻みで調整可能にする（シークバーをドラッグした場合は、この設定値以外もとり得る）
        seekbar.setKeyProgressIncrement(10);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int j = (i / 10) * 10;
                if (i != j) {
                    seekbar.setProgress(j);
                }
                TextView text = view.findViewById(R.id.textCurrentVal);
                text.setText("現在の値 : " + j);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // AlertDialogを表示する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Seekbarのテスト")
                // OKボタンを押したときの処理
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "値 : " + seekbar.getProgress(), Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                // Cancelボタンを押したときの処理
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .create()
                .show();
    }


    // *****
    // buttonRatingbar押下したときに表示するAlertDialog
    // レーティングバーwidgetを表示する
    private void showDialog_Ratingbar() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_ratingbar, null);
        final RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                final TextView text = view.findViewById(R.id.textCurrentVal);
                text.setText("現在値 : " + ratingBar.getRating());
            }
        });

        // AlertDialogを表示する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Ratingbarのテスト")
                // OKボタンを押したときの処理
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "選択値 : " + ratingBar.getRating(), Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                // Cancelボタンを押したときの処理
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .create()
                .show();
    }

}
