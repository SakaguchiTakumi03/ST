package com.example.todo90032;

import static com.example.todo90032.Consts.*;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.todo90032.dataaccess.AppDatabase;
import com.example.todo90032.dataaccess.Tasks;
import com.example.todo90032.dataaccess.TasksDAO;
import com.google.common.util.concurrent.ListenableFuture;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ToDoEditActivity extends AppCompatActivity {
    private int _mode = MODE_INSERT;
    private int _idNo = 0;
    private AppDatabase _db;
    private DeleteDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_edit);
        _db = AppDatabase.getDatabase(ToDoEditActivity.this);
        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode", MODE_INSERT);
        Toolbar toolbar = findViewById(R.id.editToolbar);
        setSupportActionBar(toolbar);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        if(_mode == MODE_INSERT){
            TextView tvTitleEdit = findViewById(R.id.tvTitleEdit);
            tvTitleEdit.setText(R.string.tv_title_insert);
            TextView tvTaskDeadline = findViewById(R.id.tvTaskDeadlineValue);
            Calendar cl = Calendar.getInstance();
            tvTaskDeadline.setText(sdf.format(cl.getTime()));
        }else{
            _idNo = intent.getIntExtra("idNo", 0);
            TasksDAO tasksDAO = _db.createTasksDAO();
            ListenableFuture<Tasks> future = tasksDAO.findByPK(_idNo);
            try {
                Tasks tasks = future.get();
                EditText etInputTaskName = findViewById(R.id.etTaskName);
                etInputTaskName.setText(tasks.name);
                TextView etInputTaskDeadline = findViewById(R.id.tvTaskDeadlineValue);
                etInputTaskDeadline.setText(sdf.format(tasks.deadline));
                Switch sDone = findViewById(R.id.sDone);
                sDone.setChecked(tasks.done == 1 ? true : false);
                EditText etInputTaskNote = findViewById(R.id.etTaskNote);
                etInputTaskNote.setText(tasks.note);
            }
            catch (ExecutionException ex){
                Log.e("ToDoEditActivity", "データ取得処理失敗", ex);
            }
            catch (InterruptedException ex){
                Log.e("ToDoEditActivity", "データ取得処理失敗", ex);
            }
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if(_mode == MODE_INSERT){
            inflater.inflate(R.menu.menu_options_activity_to_do_edit_insert, menu);
        }else{
            inflater.inflate(R.menu.menu_options_activity_to_do_edit_update, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnSave:
                EditText etInputName = findViewById(R.id.etTaskName);
                String inputName = etInputName.getText().toString();
                if (inputName.equals("")) {
                    Toast.makeText(ToDoEditActivity.this, R.string.msg_input_name, Toast.LENGTH_SHORT).show();
                } else {
                    TextView etInputDeadline = findViewById(R.id.tvTaskDeadlineValue);
                    String inputDeadLine = etInputDeadline.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    long result = 0;
                    try {
                        Date date = sdf.parse(inputDeadLine);
                        Tasks tasks = new Tasks();
                        EditText etName = findViewById(R.id.etTaskName);
                        tasks.name = etName.getText().toString();
                        tasks.deadline = new Timestamp(date.getTime());
                        Switch sDone = findViewById(R.id.sDone);
                        tasks.done = sDone.isChecked() ? 1 : 0;
                        EditText etInputNote = findViewById(R.id.etTaskNote);
                        tasks.note = etInputNote.getText().toString();
                        TasksDAO tasksDAO = _db.createTasksDAO();
                        try {
                            if (_mode == MODE_INSERT) {
                                ListenableFuture<Long> future = tasksDAO.insert(tasks);
                                result = future.get();
                            } else {
                                tasks.id = _idNo;
                                ListenableFuture<Integer> future = tasksDAO.update(tasks);
                                result = future.get();
                            }
                        }
                        catch(ExecutionException ex){
                            Log.e("ToDoEditActivity", "データ更新処理失敗", ex);
                        }
                        catch(InterruptedException ex){
                            Log.e("ToDoEditActivity", "データ更新処理失敗", ex);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(result <= 0){
                        Toast.makeText(ToDoEditActivity.this, R.string.msg_save_error, Toast.LENGTH_SHORT).show();
                    }else{
                        finish();
                    }
                }
                return true;
            case R.id.btnDelete:
                DeleteDialogFragment dialog = new DeleteDialogFragment(_db);
                Bundle extras = new Bundle();
                extras.putInt("id", _idNo);
                dialog.setArguments(extras);
                FragmentManager manager = getSupportFragmentManager();
                dialog.show(manager, "DeleteDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDeadlineClick(View view){
        TextView etInputDeadline = findViewById(R.id.tvTaskDeadlineValue);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date deadLine = new Date();
        try {
            deadLine = sdf.parse(etInputDeadline.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(deadLine);
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH);
        int nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(ToDoEditActivity.this, new DatePickerDialogDateSetListener(), nowYear, nowMonth, nowDayOfMonth);
        dialog.show();
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            TextView etInputDeadline = findViewById(R.id.tvTaskDeadlineValue);
            etInputDeadline.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
        }
    }
}