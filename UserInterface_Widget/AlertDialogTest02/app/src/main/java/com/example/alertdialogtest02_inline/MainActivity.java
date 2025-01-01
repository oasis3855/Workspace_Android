package com.example.alertdialogtest02_inline;
/*
 ユーザ入力インターフェースwidgetの作成例
 AlertDialogに実装編（その2）

 択一リスト、ラジオボタン、チェックボックス
 スイッチ、チップ、シークバー、レーティングバー、カレンダー
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // xmlからレイアウトリソースを読み込む
        setContentView(R.layout.activity_main);

        // ボタン押下のクリックイベントを登録
        findViewById(R.id.buttonList).setOnClickListener(this);
        findViewById(R.id.buttonRadiobutton).setOnClickListener(this);
        findViewById(R.id.buttonCheckbox).setOnClickListener(this);
        findViewById(R.id.buttonSwitch).setOnClickListener(this);
        findViewById(R.id.buttonChip).setOnClickListener(this);
        findViewById(R.id.buttonSeekBar).setOnClickListener(this);
        findViewById(R.id.buttonRatingBar).setOnClickListener(this);
        findViewById(R.id.buttonCalendarView).setOnClickListener(this);
    }

    // *****
    // ボタン押下
    // implements View.OnClickListener を追記して自動的に挿入される
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonList) {
            showAlertDialog_List();
        } else if (id == R.id.buttonRadiobutton) {
            showAlertDialog_Radiobutton();
        } else if (id == R.id.buttonCheckbox) {
            showAlertDialog_Checkbox();
        } else if (id == R.id.buttonSwitch) {
            showAlertDialog_Switch();
        } else if (id == R.id.buttonChip) {
            showAlertDialog_Chip();
        } else if (id == R.id.buttonSeekBar) {
            showAlertDialog_SeekBar();
        } else if (id == R.id.buttonRatingBar) {
            showAlertDialog_RatingBar();
        } else if (id == R.id.buttonCalendarView) {
            showAlertDialog_CalendarView();
        }
    }

    // *****
    // buttonList押下したときに表示するAlertDialog
    // 択一リストを表示（リストより1つだけ選択できる）。選択肢は文字列配列をsetItemsメソッドで設定する
    private void showAlertDialog_List() {
        // 選択肢をString配列に格納する
        final String[] items = new String[20];
        for (int i = 0; i < 20; i++)
            items[i] = String.format("選択肢 %d", i + 1);
        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("択一リスト AlertDialog")
                // リスト表示において、setMessageメソッドは利用できない
                // .setMessage("リストより1つ選択してください")
                // 選択肢を格納した文字列配列を設定し、選択肢をクリックしたときの処理を記述する
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "選択 : " + items[i], Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // buttonRadiobutton押下したときに表示するAlertDialog
    // ラジオボタンを表示（選択肢より1つだけ選択できる）。選択肢は文字列配列をsetSingleChoiceItemsメソッドで設定する
    private void showAlertDialog_Radiobutton() {
        // 選択肢をString配列に格納する
        final String[] items = new String[20];
        for (int i = 0; i < 20; i++)
            items[i] = String.format("選択肢 %d", i + 1);
        // Alert Dialog表示中に選択された選択肢のindex番号を一時保管する変数
        int[] idxSelected = {0};
        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("ラジオボタン AlertDialog")
                // 選択肢を格納した文字列配列を設定し、選択肢をクリックしたときの処理を記述する
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    // クリックされた選択肢（ラジオボタン）の位置を覚えておき、setPositiveButtonのクリック時に読み出す
                    public void onClick(DialogInterface dialogInterface, int i) {
                        idxSelected[0] = i;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "選択 : " + items[idxSelected[0]], Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // buttonCheckbox押下したときに表示するAlertDialog
    // チェックボックスを表示（選択肢より複数が選択できる）。選択肢は文字列配列をsetSingleChoiceItemsメソッドで設定する
    private void showAlertDialog_Checkbox() {
        // 選択肢をString配列に格納する
        final String[] items = new String[5];
        for (int i = 0; i < 5; i++)
            items[i] = String.format("選択肢 %d", i + 1);
        // チェックボックス状態の初期値（trueはチェックされた状態）
        boolean[] idxChecked = {false, false, false, false, false};
        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("チェックボックス AlertDialog")
                // 選択肢を格納した文字列配列・チェック状態の配列を設定し、選択肢をクリックしたときの処理を記述する
                .setMultiChoiceItems(items, idxChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // チェック状態が変化したら、（setPositiveButtonのクリック時に参照するために）保存する
                        idxChecked[i] = b;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = "";
                        for (int j = 0; j < idxChecked.length; j++) {
                            str += idxChecked[j] ? "ON " : "OFF ";
                        }
                        Toast toast = Toast.makeText(MainActivity.this, "チェック状態 : " + str, Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // buttonSwitch押下したときに表示するAlertDialog
    // スイッチ（切り替えボタン） widgetを表示する
    private void showAlertDialog_Switch() {
        // スイッチwidgetを作成し、レイアウトに紐付ける
        // Switch/SwitchCompat/SwitchMaterial のいづれでも動作可。見た目も変わらない模様
//        final Switch switchctl = new Switch(this);
//        final SwitchCompat switchctl = new SwitchCompat(this);
        final SwitchMaterial switchctl = new SwitchMaterial(this);
        switchctl.setText("スイッチ");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(switchctl);
        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("スイッチ AlertDialog")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "スイッチ状態 : " + (switchctl.isChecked() ? "ON" : "OFF"), Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .show();
    }

    // *****
    // buttonChip押下したときに表示するAlertDialog
    // チップwidget（ON/OFFボタン）を表示する
    private void showAlertDialog_Chip() {
        // 複数のChipをChipGroupにまとめて紐付ける
        ChipGroup chipgrp = new ChipGroup(this);
        for (int j = 0; j < 5; j++) {
            Chip chip = new Chip(this);
            chip.setText("Chip" + Integer.toString(j + 1));
            chip.setCheckable(true);
            chipgrp.addView(chip);
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(chipgrp);
        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("チップ AlertDialog")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = "";
                        for (int j = 0; j < chipgrp.getChildCount(); j++) {
                            Chip chip = (Chip) chipgrp.getChildAt(j);
                            str += chip.isChecked() ? "ON " : "OFF ";
                        }
                        Toast toast = Toast.makeText(MainActivity.this, "チップ状態 : " + str, Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .show();
    }

    // *****
    // buttonSeekBar押下したときに表示するAlertDialog
    // シークバーを表示
    private void showAlertDialog_SeekBar() {
        final TextView text = new TextView(this);
        text.setText("現在の値 : 50");

        final SeekBar seekbar = new SeekBar(this);
        // setMinはAPI26以上でしかサポートされない
        //seekbar.setMin(0);
        seekbar.setMax(100);
        // 初期値は50とする
        seekbar.setProgress(50);
        // 左右キーで10刻みで調整可能にする（シークバーをドラッグした場合は、この設定値以外もとり得る）
        seekbar.setKeyProgressIncrement(10);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // 読み出した値が10刻みでない場合は、1の位を切り捨ててシークバーの値を再設定する
                int j = (i / 10) * 10;
                if (i != j) {
                    seekbar.setProgress(j);
                }
                text.setText("現在の値 : " + j);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // シークバーをレイアウトに紐付け
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(text);
        layout.addView(seekbar);

        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("シークバー AlertDialog")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "値 : " + seekbar.getProgress(), Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();

                    }
                })
                .show();
    }

    // *****
    // buttonRatingBar押下したときに表示するAlertDialog
    // レーティングバー（評価のスター）を表示する
    private void showAlertDialog_RatingBar() {
        final TextView text = new TextView(this);
        text.setText("現在の値 : 3");

        // レーティングバーを定義
        final RatingBar ratingbar = new RatingBar(this);
        // 最大値を5とする
        ratingbar.setNumStars(5);
        // 変化ステップを1にする
        ratingbar.setStepSize(1.0f);
        // 初期値
        ratingbar.setRating(3.0f);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                text.setText("現在の値 : " + String.format("%1.0f", v));
            }
        });

        // レーティングバーをレイアウトに紐付け
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(text);
        layout.addView(ratingbar);

        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("レーティングバー AlertDialog")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "評価値 : " + ratingbar.getRating(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }

    // *****
    // buttonCalendarView押下したときに表示するAlertDialog
    // カレンダーViewを表示
    // (DatePickerDialog でも同様の機能が実現できる。コーディング例はAlertDialogTest01を参照）
    private void showAlertDialog_CalendarView() {
        // 年月日をViewに設定・Viewから読み出すための変数
        Calendar selectdate = Calendar.getInstance();
        // 年月日の初期設定。月は0から
        selectdate.set(2024, 0, 1);
        // カレンダーViewを構築
        final CalendarView calendar = new CalendarView(this);
        calendar.setDate(selectdate.getTimeInMillis());
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                // 現在の選択年月日を保存しておく（i=year, i1=month-1, i2=day）
                selectdate.set(i, i1, i2);
            }
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(calendar);

        // Alert Dialogを構築・表示
        new AlertDialog.Builder(this)
                .setTitle("カレンダー AlertDialog")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Toast toast = Toast.makeText(MainActivity.this, "選択日 : " + sdf.format(selectdate.getTime()), Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(MainActivity.this, "キャンセル", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
                .show();
    }
}
