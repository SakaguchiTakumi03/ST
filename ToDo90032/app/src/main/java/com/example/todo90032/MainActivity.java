package com.example.todo90032;

import static com.example.todo90032.Consts.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo90032.dataaccess.AppDatabase;
import com.example.todo90032.dataaccess.Tasks;
import com.example.todo90032.dataaccess.TasksDAO;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.common.util.concurrent.ListenableFuture;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    static final String PREFS_NAME = "PSPrefsFile";
    private RecyclerView _rvTaskList;
    private int _listCategory;
    private AppDatabase _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        toolbarLayout.setTitle(getString(R.string.app_name));
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int listTypeCode = settings.getInt("listType", ALL);
        _listCategory = listTypeCode;
        _rvTaskList = findViewById(R.id.lvTaskList);
        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        _rvTaskList.setLayoutManager(layout);
        DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
        _rvTaskList.addItemDecoration(decoration);
        _db = AppDatabase.getDatabase(MainActivity.this);

        // createListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem menuListOptionTitle = menu.findItem(R.id.menuDisplayType);
        switch (_listCategory){
            case ALL:
                menuListOptionTitle.setTitle(R.string.menu_display_all);
                break;
            case TODO:
                menuListOptionTitle.setTitle(R.string.menu_display_todo);
                break;
            case DONE:
                menuListOptionTitle.setTitle(R.string.menu_display_done);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnVal = true;
        int itemId = item.getItemId();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        switch(itemId){
            case R.id.menuDisplayTypeAll:
                _listCategory = ALL;
                break;
            case R.id.menuDisplayTypeTodo:
                _listCategory = TODO;
                break;
            case R.id.menuDisplayTypeDone:
                _listCategory = DONE;
                break;
            default:
                returnVal = super.onOptionsItemSelected(item);
                break;
        }
        if(returnVal){
            editor.putInt("listType", _listCategory);
            editor.apply();
            createListView();
            invalidateOptionsMenu();
        }
        return returnVal;
    }

    private void createListView(){
        switch(_listCategory){
            case TODO:
                createList(TODO);
                break;
            case DONE:
                createList(DONE);
                break;
            default:
                createList(ALL);
                break;
        }
    }

    private void createList(int state){
        super.onResume();
        TasksDAO tasksDAO = _db.createTasksDAO();
        ListenableFuture<List<Tasks>> future;
        if(state == 1){
            future = tasksDAO.findTodo();
        }else if(state == 2){
            future = tasksDAO.findDone();
        } else {
            future = tasksDAO.findAll();
        }
        List<Tasks> tasksList = new ArrayList<>();
        try {
            tasksList = future.get();
        }
        catch (ExecutionException ex) {
            Log.e("MainActivity", "データ取得処理失敗", ex);
        }
        catch(InterruptedException ex) {
            Log.e("MainActivity", "データ取得処理失敗", ex);
        }
        TodoListAdapter adapter = new TodoListAdapter(tasksList);
        _rvTaskList.setAdapter(adapter);
    }

    private class TodoViewHolder extends RecyclerView.ViewHolder {
        public TextView _tvTitle;
        public TextView _tvDeadline;
        public CheckBox _cbDone;

        public TodoViewHolder(View itemView){
            super(itemView);
            _tvTitle = itemView.findViewById(R.id.title);
            _tvDeadline = itemView.findViewById(R.id.deadline);
            _cbDone = itemView.findViewById(R.id.done);
        }
    }

    private class TodoListAdapter extends RecyclerView.Adapter<TodoViewHolder>{
        private List<Tasks> _listData;

        public TodoListAdapter(List<Tasks> listData){
            _listData = listData;
        }

        @Override
        public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View row = inflater.inflate(R.layout.row, parent, false);
            row.setOnClickListener(new ListItemClickListener());
            TodoViewHolder holder = new TodoViewHolder(row);
            return holder;
        }

        @Override
        public void onBindViewHolder(TodoViewHolder holder, int position){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            Tasks item = _listData.get(position);
            Timestamp deadline = item.deadline;
            String setText = "期限: ";
            String tempDateStr = sdf.format(deadline);
            Timestamp nowDate = new Timestamp(System.currentTimeMillis());
            String nowDateStr = sdf.format(nowDate);

            if(tempDateStr.equals(nowDateStr)){
                setText += "今日";
                holder._tvDeadline.setText(setText);
                holder._tvDeadline.setTextColor(Color.BLUE);
            }else if(deadline.before(nowDate)){
                setText += tempDateStr;
                holder._tvDeadline.setText(setText);
                holder._tvDeadline.setTextColor(Color.RED);
            }else{
                setText += tempDateStr;
                holder._tvDeadline.setText(setText);
            }
            holder._tvTitle.setText(item.name);
            holder._tvTitle.setTag(item.id);
            holder._cbDone.setChecked(item.done == 1 ? true : false );
            holder._cbDone.setTag(item.id);
            holder._cbDone.setOnClickListener(new CheckBoxClickListener());
        }

        @Override
        public int getItemCount() {
            return _listData.size();
        }
    }

    private class CheckBoxClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            CheckBox cbDone = (CheckBox) view;
            int isChecked = cbDone.isChecked() ? 1 : 0;
            int id = (int) cbDone.getTag();
            TasksDAO tasksDAO = _db.createTasksDAO();
            long result = 0;
            try {
                ListenableFuture<Integer> future = tasksDAO.updateOnlyDone(id, isChecked);
                result = future.get();
            }
            catch (ExecutionException ex){
                Log.e("MainActivity", "データ更新処理失敗", ex);
            }
            catch(InterruptedException ex){
                Log.e("MainActivity", "データ更新処理失敗", ex);
            }
            if(result <= 0){
                Toast.makeText(MainActivity.this, R.string.msg_save_error, Toast.LENGTH_SHORT).show();
            }
            createListView();
        }
    }

    private class ListItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            TextView title = view.findViewById(R.id.title);
            int id = (int) title.getTag();
            Intent intent = new Intent(MainActivity.this, ToDoEditActivity.class);
            intent.putExtra("mode", MODE_EDIT);
            intent.putExtra("idNo", id);
            startActivity(intent);
        }
    }

    public void onNewButtonClick(View view){
        Intent intent = new Intent(MainActivity.this, ToDoEditActivity.class);
        intent.putExtra("mode", MODE_INSERT);
        startActivity(intent);
    }
}