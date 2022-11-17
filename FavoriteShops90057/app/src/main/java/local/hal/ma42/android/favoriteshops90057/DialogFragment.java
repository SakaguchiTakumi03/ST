package local.hal.ma42.android.favoriteshops90057;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private long _idNo;
    private DatabaseHelper _helper;

    public DialogFragment(DatabaseHelper helper,long idNo){
        _helper = helper;
        _idNo = idNo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("削除してもよろしいですか？");
        builder.setPositiveButton("はい", new DialogButtonClickListener());
        builder.setNegativeButton("いいえ", new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            Activity parent = getActivity();

            System.out.print(_idNo);
            String msg = "";
            if(which == DialogInterface.BUTTON_POSITIVE){
                msg = "選択されたお店がが削除されました。";
                SQLiteDatabase db = _helper.getWritableDatabase();
                DataAccess.delete(db, _idNo);
                parent.finish();
                Toast.makeText(parent, msg, Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(parent, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
