package local.hal.st42.android.customizedlistview1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * ST42 Androidサンプル02 リストビュー各行のカスタマイズ
 *
 * 注文確認ダイアログクラス。
 *
 * @author Shinzo SAITO
 */
public class OrderConfirmDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                Bundle extras = getArguments();
                String name = extras.getString("name");
                Integer price = extras.getInt("price");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_confirm_title);
                String message = getString(R.string.dialog_confirm_message, name, price);
                builder.setMessage(message);
                builder.setPositiveButton(R.string.dialog_confirm_btn_positive, new DialogButtonClickListener());
                builder.setNegativeButton(R.string.dialog_confirm_btn_negative, new DialogButtonClickListener());
                AlertDialog dialog = builder.create();
                return dialog;
        }
    
        /**
         * 注文確認ダイアログのボタンが押されたときの処理が記述されたメンバクラス。
         */
        private class DialogButtonClickListener implements DialogInterface.OnClickListener {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
        }
}
