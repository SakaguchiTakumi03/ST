package local.hal.st42.android.todo90727;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private int _mode = MainActivity.MODE_INSERT;

    private long _idNo = 0;

    private DatabaseHelper _helper;

    public DialogFragment(DatabaseHelper helper, long idNo){
        _helper = helper;
        _idNo = idNo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage(R.string.dlg_full_msg);
        builder.setPositiveButton(R.string.dlg_btn_ok, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dlg_btn_ng, new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d("DialogFragment","start");

            Activity parent = getActivity();

            String msg = "";


            if(which == DialogInterface.BUTTON_POSITIVE){
                msg = getString(R.string.dlg_full_toast_ok);
                SQLiteDatabase db = _helper.getWritableDatabase();
                DataAccess.delete(db,_idNo);
                parent.finish();
                Toast.makeText(parent,msg,Toast.LENGTH_SHORT).show();
            }
            Log.d("DialogFragment","end");
        }
    }
}