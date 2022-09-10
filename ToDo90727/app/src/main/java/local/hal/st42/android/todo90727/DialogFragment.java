package local.hal.st42.android.todo90727;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import local.hal.st42.android.todo90727.dataaccess.AppDatabase;
import local.hal.st42.android.todo90727.dataaccess.Tasks;
import local.hal.st42.android.todo90727.dataaccess.TasksDAO;
import local.hal.st42.android.todo90727.viewmodel.ToDoEditViewModel;

import static local.hal.st42.android.todo90727.Consts.MODE_INSERT;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private int _mode = MODE_INSERT;

    private long _idNo = 0;

//    private DatabaseHelper _helper;
//    private AppDatabase _db;

    private ToDoEditViewModel _todoEditViewModel;

    public DialogFragment(ToDoEditViewModel toDoEditViewModel){
        _todoEditViewModel = toDoEditViewModel;
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
                Tasks tasks = new Tasks();
                tasks.id = idNo;
                int result = _todoEditViewModel.delete(idNo);
                if(result <= 0){
                    Toast.makeText(parent,R.string.msg_delete_error,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(parent,msg,Toast.LENGTH_SHORT).show();
                    parent.finish();
                }
            }
            Log.d("DialogFragment","end");
        }
    }
}