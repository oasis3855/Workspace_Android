package com.example.dialogfragmenttest01;
/*
DialogFragmentを用いたカスタムダイアログ
（xmlレイアウトを読み込み、Viewを構築するカスタムダイアログ）

任意の型・個数の引数を（ActivityMainから）受け取り、ダイアログでの操作の結果（どのボタンが押されたかや、任意の引数）を
呼び出し側（のActivityMain）に返すことができる

MainActivity -> DialogFragment : 値の転送 Bundle
DialogFragment -> MainActivity : 値の転送 Bundle、ボタン押下 interface
 */

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FragmentBasicSample extends DialogFragment {

    // ダイアログで押されたボタンイベントを、呼び出し側に返すinterfaceを定義する
    private NoticeDialogListener noticeDialogListener;

    // *****
    // ボタンイベントを返すinterfaceを初期化する
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            noticeDialogListener = (NoticeDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement DialogFragment.listener");
        }

//        // getTargetFragment は非推奨メソッド
//        noticeDialogListener = (NoticeDialogListener) getTargetFragment();
//        if (noticeDialogListener == null) {
//            noticeDialogListener = (NoticeDialogListener) getParentFragment();
//            if (noticeDialogListener == null)
//                // 今回の事例では、getActivity() の値が使われる
//                noticeDialogListener = (NoticeDialogListener) getActivity();
//        }
    }

    // *****
    // 任意のxmlレイアウトリソースを読み込んでカスタムダイアログを作る場合は、onCreateViewで構築作業を行う
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LayoutInflater inflater = getActivity().getLayoutInflater();
        // xmlからレイアウトリソースを読み込む
        View view = inflater.inflate(R.layout.fragment_edittext, container);

        // 呼び出し側（ActivityMain）から変数を受け取って、テキストボックスに表示する
        EditText editText_Str = view.findViewById(R.id.editText_Str);
        editText_Str.setText(requireArguments().getString("KEY_STR", "no arg !"));
        EditText editText_Int = view.findViewById(R.id.editText_Int);
        editText_Int.setText(Integer.toString(requireArguments().getInt("KEY_INT")));

        // Okボタンを押下したときの処理
        Button buttonOk = view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText_Str = getDialog().findViewById(R.id.editText_Str);
                EditText editText_Int = getDialog().findViewById(R.id.editText_Int);
                // Intentを使って（呼び出し側のMainActivityに）変数を返す場合の処理
//                Intent intent = new Intent();
//                intent.putExtra("KEY_STR", editText_Str.getText().toString());
//                intent.putExtra("KEY_INT", Integer.parseInt(editText_Int.getText().toString()));
                // Bundleを使って（呼び出し側のMainActivityに）変数を返す場合の処理
                Bundle bundle = new Bundle();
                bundle.putString("KEY_STR", editText_Str.getText().toString());
                bundle.putInt("KEY_INT", Integer.parseInt(editText_Int.getText().toString()));

                // 呼び出し側（MainActivity）に実装したリスナメソッドを呼び出す
                // Intentを使って変数を返す場合
//                noticeDialogListener.onDialogButtonOkClick(FragmentBasicSample.this, intent);
                // Bundleを使って変数を返す場合
                noticeDialogListener.onDialogButtonOkClick(FragmentBasicSample.this, bundle);

                // ダイアログを閉じる
                dismiss();
            }
        });
        // Cancelボタンを押下したときの処理
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 呼び出し側（MainActivity）に実装したリスナメソッドを呼び出す
                noticeDialogListener.onDialogButtonCancelClick(FragmentBasicSample.this);

                // ダイアログを閉じる
                dismiss();
            }
        });

//        return super.onCreateView(inflater, container, savedInstanceState);
        // *****
        // Android Developers 開発 ガイド より引用
        //      onCreateView() から返される View 自動的にダイアログに追加されます。
        //      つまり、ほとんどの場合、デフォルトの空のダイアログにビューが入力されるため、
        //      onCreateDialog() をオーバーライドする必要はありません。
        return view;
    }

    // *****
    // ダイアログ表示時に、サイズを調整する（画面の短辺長さの90%を、ダイアログ横幅とする）
    @Override
    public void onStart() {
        super.onStart();

        // 画面の短い側の長さを取得
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (getActivity().getWindowManager() == null) return;
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int minSize = Math.min(screenWidth, screenHeight);

        // 画面の短い側の90%をダイアログの幅とする
        if (getDialog().getWindow() == null || getDialog().getWindow().getAttributes() == null)
            return;
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (minSize * 0.9);

        // 属性を再設定
        getDialog().getWindow().setAttributes(params);
    }

    // *****
    // ダイアログで押されたボタンイベントを、呼び出し側に返すinterfaceを定義する
    // interfaceメソッドの引数が、呼び出し側MainActivityに返される
    // なお、第1引数のdialogは、Googleの公式サンプルがそうなっていたので、利用しないがそのままにしている
    public interface NoticeDialogListener {
        // 任意の引数を「Bundleに紐づけて」返す場合
        public void onDialogButtonOkClick(DialogFragment dialog, Bundle bundle);

        public void onDialogButtonCancelClick(DialogFragment dialog);
        // 任意の引数を「Intentに紐づけて」返す場合
//        public void onDialogButtonOkClick(DialogFragment dialog, Intent intent);
        // 返したい引数を列挙しても良い
//        public void onDialogButtonOkClick(String str, Integer num);
    }
}
