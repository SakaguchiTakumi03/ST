package local.hal.st42.android.todo90727;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.sql.Date;
import java.util.concurrent.ExecutionException;

import local.hal.st42.android.todo90727.dataaccess.AppDatabase;
import local.hal.st42.android.todo90727.dataaccess.Tasks;
import local.hal.st42.android.todo90727.dataaccess.TasksDAO;

import static local.hal.st42.android.todo90727.Consts.MODE_INSERT;

public class ToDoEditActivity extends AppCompatActivity {
    private int _mode = MODE_INSERT;
    private long _idNo = 0;

    //エミュレータの時刻をデフォルト値とする
    private long longTimeInMillis = System.currentTimeMillis();

    private AppDatabase _db;

    private String strNowDate;

    private Calendar cal = Calendar.getInstance();

    private long switchVal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_edit);

        _db = AppDatabase.getDatabase(ToDoEditActivity.this);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode",MODE_INSERT);

        Toolbar toolbar = findViewById(R.id.toolbarToDoEdit);
        setSupportActionBar(toolbar);

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

        if(_mode == MODE_INSERT){
            //insert時の処理
            button.setEnabled(false);
        }
        else{
            //edit時の処理
            _idNo = intent.getLongExtra("idNo",0);
            TasksDAO tasksDAO = _db.createTasksDAO();
            ListenableFuture<Tasks> future = tasksDAO.findByPK((int) _idNo);
            try{
                Tasks tasks = future.get();
                EditText etInputTask = findViewById(R.id.etInputTask);
                etInputTask.setText(tasks.name);

                EditText etInputNote = findViewById(R.id.etInputNote);
                etInputNote.setText(tasks.note);

                tvDate = findViewById(R.id.tvDate);
                tvDate.setText(dateGetTimeInMillis(tasks.deadline.getTime(),"yyyy年MM月[dd日"));
                longTimeInMillis = tasks.deadline.getTime();

                Switch sButton = findViewById(R.id.switchButton);
                if(tasks.done == 1){
                    sButton.setChecked(true);
                }else{
                    sButton.setChecked(false);
                }

            } catch (InterruptedException ex) {
                Log.e("ToDoEditActivity","データ取得失敗",ex);
            } catch (ExecutionException ex) {
                Log.e("ToDoEditActivity","データ取得失敗",ex);
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if(_mode == MODE_INSERT){
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
                EditText etInputNote = findViewById(R.id.etInputNote);
                Switch tSwitch = (Switch) findViewById(R.id.switchButton);
                String inputTask = etInputTask.getText().toString();
                String inputNote = etInputNote.getText().toString();
                //タスク名未入力処理
                if (inputTask.equals("")) {
                    Toast.makeText(ToDoEditActivity.this, R.string.msg_input_message, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    TasksDAO tasksDAO = _db.createTasksDAO();
                    Tasks tasks = new Tasks();
                    tasks.name = inputTask;
                    tasks.deadline = new Date(longTimeInMillis);
                    if(tSwitch.isChecked() == true){
                        tasks.done = 1;
                    }else {
                        tasks.done = 0;
                    }
                    tasks.note = inputNote;
                    long result = 0;
                    tasks.note = inputNote;
                    try {
                        if (_mode == MODE_INSERT) {
                            ListenableFuture<Long> future = tasksDAO.insert(tasks);
                            Log.d("future_tag","_insert");
                            result = future.get();
                        } else {
                            tasks.id = (int) _idNo;
                            ListenableFuture<Integer> future = tasksDAO.update(tasks);
                            Log.d("future_tag","_update");
                            result = future.get();
                        }
                    } catch (InterruptedException ex) {
                        Log.e("ToDoEditActivity", "データ更新処理失敗", ex);
                    } catch (ExecutionException ex) {
                        Log.e("ToDoEditActivity", "データ更新処理失敗", ex);
                    }
                    if(result <= 0){
                        Toast.makeText(ToDoEditActivity.this, R.string.msg_save_error,Toast.LENGTH_SHORT).show();
                        Log.e("save_error","save_error");
                    }else{
                        finish();
                    }
                }
                return true;
            case R.id.menuDelete:
                Log.d("hgoehoge",_db.toString());
                DialogFragment dialog = new DialogFragment(_db);
                Bundle extras = new Bundle();
                extras.putInt("id", (int) _idNo);
                dialog.setArguments(extras);
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
