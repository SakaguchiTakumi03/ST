package local.hal.st31.android.post2db90727;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class Post2DBDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        String title = "";
        String message = "";
        Bundle extras = getArguments();
        String name = extras.getString("name");
        String studentid = extras.getString("studentid");
        String seatno = extras.getString("seatno");
        String status = extras.getString("status");
        String msg = extras.getString("msg");
        String serialno = extras.getString("serialno");
        String timestamp = extras.getString("timestamp");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        title = "name「" + name + "」\n" + "studentid「" + studentid + "」seatno「" + seatno + "」";
        message = "statusid「" + status + "」\nmsg「" + msg + "」\nserialno「" + serialno + "」\ntimestamp「" + timestamp + "」";
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.btn_dialog_positive,null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
