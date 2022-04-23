package local.hal.st31.android.itarticlecollection90727;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class Dialog extends DialogFragment {
    @Override
    public android.app.Dialog onCreateDialog(Bundle saveInstanceState){
        String display = "";
        String message = "";
        Bundle extras = getArguments();
        String title = extras.getString("title");
        String url = extras.getString("url");
        String comment = extras.getString("comment");
        String name = extras.getString("name");
        String studentid = extras.getString("studentid");
        String seatno = extras.getString("seatno");
        String status = extras.getString("status");
        String msg = extras.getString("msg");
        String timestamp = extras.getString("timestamp");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        display = "【title】"+title+"\n【url】"+url+"\n【comment】"+comment;
        builder.setTitle(display);
        message = "【name】"+name+"\n【studentid】"+studentid+"\n【seatno】"+seatno+"\n【status】"+status+"\n【msg】"+msg+"\n【timestamp】"+timestamp;
        builder.setMessage(message);
        builder.setPositiveButton("了解",null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
