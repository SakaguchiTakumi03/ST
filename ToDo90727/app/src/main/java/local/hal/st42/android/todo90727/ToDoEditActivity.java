package local.hal.st42.android.todo90727;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ToDoEditActivity extends AppCompatActivity {
    private int _mode = MainActivity.MODE_INSERT;
    private long _idNo = 0;

    //エミュレータの時刻をデフォルト値とする
    private long longTimeInMillis = System.currentTimeMillis();

    private DatabaseHelper _helper;

    private String strNowDate;

    private Calendar cal = Calendar.getInstance();

    private long switchVal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_edit);

        _helper = new DatabaseHelper(ToDoEditActivity.this);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode",MainActivity.MODE_INSERT);

        //戻るボタン
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tvTitleEdit = findViewById(R.id.tvTitleEdit);
        tvTitleEdit.setText(R.string.tv_title_edit);

        TextView tvDate = findViewById(R.id.tvDate);

        //現在日時取得
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        strNowDate = nowDate.format(formatter);

        //表示
        tvDate.setText(strNowDate);

        Button button = (Button) findViewById(R.id.switchButton);

        if(_mode == MainActivity.MODE_INSERT){
            //insert時の処理
//            button.setEnabled(false);
        }
        else{
            //edit時の処理
            _idNo = intent.getLongExtra("idNo",0);

            SQLiteDatabase db = _helper.getWritableDatabase();


            ToDo todo = DataAccess.findByPK(db,_idNo);

            EditText etInputTask = findViewById(R.id.etInputTask);
            etInputTask.setText(todo.getName());

            EditText etInputNote = findViewById(R.id.etInputNote);
            etInputNote.setText(todo.getNote());

            tvDate = findViewById(R.id.tvDate);
            tvDate.setText(dateGetTimeInMillis(todo.getDeadline(),"yyyy年MM月[dd日"));
            longTimeInMillis = todo.getDeadline();

            Switch sButton = findViewById(R.id.switchButton);
            long getButtonVal = todo.getDone();
            if(getButtonVal == 1){
                sButton.setChecked(true);
            }
        }
    }

    @Override
    protected void onDestroy(){
        _helper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if(_mode == MainActivity.MODE_INSERT){
            inflater.inflate(R.menu.option_to_do_create_menu,menu);
        }else{
            inflater.inflate(R.menu.option_to_do_edit_menu,menu);
        }
        return true;
    }

    public String dateGetTimeInMillis(long longTimeInMillis, String format){
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(format);
        ZonedDateTime zoneDate = Instant.ofEpochMilli(longTimeInMillis).atZone(ZoneId.systemDefault());
        String strDate = zoneDate.format(dtFormat);
        return strDate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menuSave:
                EditText etInputTask = findViewById(R.id.etInputTask);
                String inputTask = etInputTask.getText().toString();
                //タスク名未入力処理
                if (inputTask.equals("")) {
                    Toast.makeText(ToDoEditActivity.this, R.string.msg_input_message, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    EditText etInputNote = findViewById(R.id.etInputNote);
                    String inputNote = etInputNote.getText().toString();
                    Switch tSwitch = (Switch) findViewById(R.id.switchButton);
                    SQLiteDatabase db = _helper.getWritableDatabase();
                    if (_mode == MainActivity.MODE_INSERT) {
                        DataAccess.insert(db, inputTask, longTimeInMillis, switchVal, inputNote);
                    } else {
                        if(tSwitch.isChecked()){
                            switchVal = 1;
                        }
                        DataAccess.update(db, _idNo, inputTask, longTimeInMillis, switchVal, inputNote);
                    }
                }
                finish();
                return true;
            case R.id.menuDelete:
                DialogFragment dialog = new DialogFragment(_helper,_idNo);
                FragmentManager manager = getSupportFragmentManager();
                dialog.show(manager,"DialogFragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //tvDateのonClickメソッド
    public void tvDateClick(View view) {
        TextView tvDate = findViewById(R.id.tvDate);
        String strDate = tvDate.getText().toString();
        strDate = strDate.replace("年","");
        strDate = strDate.replace("月","");
        strDate = strDate.replace("日","");

        int year =  Integer.parseInt(strDate.substring(0,4));
        int month  = Integer.parseInt(strDate.substring(4,6)) -1;
        int dayOfMonth = Integer.parseInt(strDate.substring(6,8));

        DatePickerDialog dateDialog = new DatePickerDialog(ToDoEditActivity.this, new DatePickerDialogDateSetListener(), year, month, dayOfMonth);
        dateDialog.show();
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy年[]M月[]d日 HH:mm:ss").withZone(ZoneId.systemDefault());
            ZonedDateTime dt = ZonedDateTime.parse(year+"年"+(month+1)+"月"+dayOfMonth+"日 23:23:23", parseFormatter);
            longTimeInMillis = dt.toInstant().toEpochMilli();
            Log.i("millis",Long.toString(longTimeInMillis));

            //Viewに表示する処理
            TextView tvDate = findViewById(R.id.tvDate);
            tvDate.setText(dateGetTimeInMillis(longTimeInMillis,"yyyy年MM月dd日"));
        }
    }
}
