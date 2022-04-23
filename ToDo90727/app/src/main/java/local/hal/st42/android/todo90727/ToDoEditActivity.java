package local.hal.st42.android.todo90727;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ToDoEditActivity extends AppCompatActivity {
    private int _mode = MainActivity.MODE_INSERT;
    private long _idNo = 0;
    private DatabaseHelper _helper;

    private String strNowDate;

    private Calendar cal = Calendar.getInstance();

    private long defoSwichVal = 0;

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

//        if(_mode == MainActivity.MODE_INSERT){
//            TextView tvTitleEdit = findViewById(R.id.tvTitleList);
//            tvTitleEdit.setText(R.string.tv_title_edit);
//        }
//        else{
//            _idNo = intent.getLongExtra("idNo",0);
//            SQLiteDatabase db = _helper.getWritableDatabase();
//            ToDo shopsData = DataAccess.findByPK(db,_idNo);
//
//            EditText etInputName = findViewById(R.id.etInputName);
//            etInputName.setText(shopsData.getName());
//
//            EditText etInputTell = findViewById(R.id.etInputTel);
//            etInputTell.setText(shopsData.getTel());
//
//            EditText etInputUrl = findViewById(R.id.etInputUrl);
//            etInputUrl.setText(shopsData.getUrl());
//
//            EditText etInputNote = findViewById(R.id.etInputNote);
//            etInputNote.setText(shopsData.getNote());
//        }

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

    public long getTimeInMillis(){
        long millis = cal.getTimeInMillis();
        return millis;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                Uri uri = Uri.parse(_articleURL);
//                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                startActivity(intent);
//                log("uri",uri.toString());
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("debug","入ったよ１");
                finish();
                return true;
            case R.id.menuSave:
                EditText etInputTask = findViewById(R.id.etInputTask);
                String inputTask = etInputTask.getText().toString();
//                String inputTitle = findViewById(R.id.etInputTitle).toString();//候補
                if(inputTask.equals("")){
                    Toast.makeText(ToDoEditActivity.this,R.string.msg_input_message,Toast.LENGTH_SHORT).show();
                }else{
                    EditText etInputNote = findViewById(R.id.etInputNote);
                    String inputNote = etInputNote.getText().toString();
                    Switch s = (Switch) findViewById(R.id.switch_button);

                    SQLiteDatabase db = _helper.getWritableDatabase();
                    if(_mode == MainActivity.MODE_INSERT){
                        long milles = getTimeInMillis();
                        DataAccess.insert(db,inputTask,milles,defoSwichVal,inputNote);
                        Log.d("mode","true");
                    }else{
                        Log.d("mode","false");
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //tvDateのonClickメソッド
    public void tvDateClick(View view){
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH);
        int nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(ToDoEditActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
        dateDialog.show();
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String msg = year+"年"+(month + 1)+"月"+dayOfMonth+"日";
            TextView tvDate = findViewById(R.id.tvDate);
            tvDate.setText(msg);
        }
    }
}
