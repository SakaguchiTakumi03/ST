package com.example.todo90032;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.todo90032.dataaccess.AppDatabase;
import com.example.todo90032.dataaccess.Tasks;
import com.example.todo90032.dataaccess.TasksDAO;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class DeleteDialogFragment extends DialogFragment {
    private AppDatabase _db;

    private long _idNo = 0;

    public DeleteDialogFragment(AppDatabase db){
        _db = db;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveinstanceState){
        Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle(R.string.dlg_full_title);
        builder.setMessage(R.string.dlg_full_msg);
        builder.setPositiveButton(R.string.dlg_btn_ok, new DeleteDialogButtonClickListener());
        builder.setNegativeButton(R.string.dlg_btn_cancel, new DeleteDialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DeleteDialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which){
            if(which == DialogInterface.BUTTON_POSITIVE){
                Activity parent = getActivity();
                Bundle extras = getArguments();
                int idNo = extras.getInt("id", 0);
                Tasks tasks = new Tasks();
                tasks.id = idNo;
                TasksDAO tasksDAO = _db.createTasksDAO();
                long result = 0;
                ListenableFuture<Integer> future = tasksDAO.delete(tasks);
                try {
                    result = future.get();
                }
                catch (ExecutionException ex){
                    Log.e("DeleteConfirmDialog", "データ更新処理失敗", ex);
                }
                catch(InterruptedException ex){
                    Log.e("DeleteConfirmDialog", "データ更新処理失敗", ex);
                }
                if(result < 0) {
                    Toast.makeText(parent, R.string.msg_delete_error, Toast.LENGTH_SHORT).show();
                }
                else {
                    parent.finish();
                }
            }
        }
    }
}
