package local.hal.st42.android.originalapp90727;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import local.hal.st42.android.originalapp90727.R;
import local.hal.st42.android.originalapp90727.dataaccess.Books;
import local.hal.st42.android.originalapp90727.viewmodel.EditViewModel;

import static local.hal.st42.android.originalapp90727.Consts.*;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

//    private int _mode = MODE_INSERT;

    private long _idNo = 0;

    private EditViewModel _editViewModel;

    public DialogFragment(EditViewModel editViewModel){
        _editViewModel = editViewModel;
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
                Bundle bundle = getArguments();
                int idNo = bundle.getInt("id",0);
                Books tasks = new Books();
                tasks.id = idNo;
                int result = _editViewModel.delete(idNo);
                if(result <= 0){
                    Toast.makeText(parent,R.string.msg_delete_error,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(parent,msg,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(parent,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
            Log.d("DialogFragment","end");
        }
    }
}