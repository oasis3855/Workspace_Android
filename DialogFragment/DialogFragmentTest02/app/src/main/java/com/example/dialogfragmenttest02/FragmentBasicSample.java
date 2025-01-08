package com.example.dialogfragmenttest02;
/*
DialogFragmentを用いたAlertダイアログ
（xmlレイアウトを読み込み、AlertDialog.Builderでダイアログを構築する）

任意の型・個数の引数を（ActivityMainから）受け取り、ダイアログでの操作の結果（どのボタンが押されたかや、任意の引数）を
呼び出し側（のActivityMain）に返すことができる

MainActivity -> DialogFragment : 値の転送 Bundle
DialogFragment -> MainActivity : 値の転送 Bundle、ボタン押下 interface
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FragmentBasicSample extends DialogFragment {

    // ダイアログで押されたボタンイベントを、呼び出し側に返すinterfaceを定義する
    private NoticeDialogListener noticeDialogListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            noticeDialogListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogFragment.listener");
        }

        // getTargetFragment は非推奨メソッド
//        noticeDialogListener = (NoticeDialogListener) getTargetFragment();
//        if (noticeDialogListener == null) {
//            noticeDialogListener = (NoticeDialogListener) getParentFragment();
//            if (noticeDialogListener == null)
//                // 今回の事例では、getActivity() の値が使われる
//                noticeDialogListener = (NoticeDialogListener) getActivity();
//        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edittext, null);
        builder.setView(view)
                .setTitle("AlertDialog on DialogFragment")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editText_Str = getDialog().findViewById(R.id.editText_Str);
                        EditText editText_Int = getDialog().findViewById(R.id.editText_Int);
                        // 呼び出し側（MainActivity）に実装したリスナメソッドを呼び出す
                        // Intentを使って変数を返す場合
//                noticeDialogListener.onDialogButtonOkClick(FragmentBasicSample.this, intent);
                        // Bundleを使って変数を返す場合
                        Bundle bundle = new Bundle();
                        bundle.putString("KEY_STR", editText_Str.getText().toString());
                        bundle.putInt("KEY_INT", Integer.parseInt(editText_Int.getText().toString()));

                        // 呼び出し側（MainActivity）に実装したリスナメソッドを呼び出す
                        // Intentを使って変数を返す場合
//                noticeDialogListener.onDialogButtonOkClick(FragmentBasicSample.this, intent);
                        // Bundleを使って変数を返す場合
                        noticeDialogListener.onDialogButtonOkClick(FragmentBasicSample.this, bundle);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        noticeDialogListener.onDialogButtonCancelClick(FragmentBasicSample.this);
                    }
                });

        // 呼び出し側（ActivityMain）から変数を受け取って、テキストボックスに表示する
        EditText editText_Str = view.findViewById(R.id.editText_Str);
        editText_Str.setText(requireArguments().getString("KEY_STR", "no arg !"));
        EditText editText_Int = view.findViewById(R.id.editText_Int);
        editText_Int.setText(String.format("%d", requireArguments().getInt("KEY_INT")));

        return builder.create();
//        return super.onCreateDialog(savedInstanceState);
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
