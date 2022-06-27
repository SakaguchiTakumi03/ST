package local.hal.st42.android.todo90727;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import local.hal.st42.android.todo90727.dataaccess.AppDatabase;
import local.hal.st42.android.todo90727.dataaccess.Tasks;
import local.hal.st42.android.todo90727.dataaccess.TasksDAO;

import static local.hal.st42.android.todo90727.Consts.ALL;
import static local.hal.st42.android.todo90727.Consts.FINISH;
import static local.hal.st42.android.todo90727.Consts.MODE_EDIT;
import static local.hal.st42.android.todo90727.Consts.MODE_INSERT;
import static local.hal.st42.android.todo90727.Consts.UNFINISH;

public class MainActivity extends AppCompatActivity {

//    static final int MODE_INSERT = 1;
//    static final int MODE_EDIT = 2;

    private int _menuCategory;

//    private static final int ALL = 1;
//    private static final int FINISH = 2;
//    private static final int UNFINISH = 3;

    private static final String PREFS_NAME = "PSPrefsFile";

    private static final int DEFAULT_SELECT = 1;

//    private DatabaseHelper _helper;
    private AppDatabase _db;

    private String titleName = "";

    private RecyclerView _rvToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _menuCategory = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).getInt("selectedMenu",DEFAULT_SELECT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        titleName = "ToDoリスト";
        toolbarLayout.setTitle(titleName);
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

//        _helper = new DatabaseHelper(MainActivity.this);

        _db = AppDatabase.getDatabase(MainActivity.this);

        _rvToDo = findViewById(R.id.rvToDo);
        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        _rvToDo.setLayoutManager(layout);
        DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
        _rvToDo.addItemDecoration(decoration);
        createRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ToDoEditActivity.class);
                intent.putExtra("mode",MODE_INSERT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        createRecyclerView();
    }

    private class ListItemClickListener implements View.OnClickListener {
        private long _id;

        public ListItemClickListener(long id) {_id = id;}

        @Override
        public void onClick(View view) {
//            TextView tvTitleRow = view.findViewById(R.id.tvNameRow);
//            int idNo = (int) tvTitleRow.getTag();
            Intent intent = new Intent(MainActivity.this,ToDoEditActivity.class);
            intent.putExtra("mode",MODE_EDIT);
            intent.putExtra("idNo", _id);

            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        boolean returnVal = true;
        switch (item.getItemId()){
            case R.id.menuAllList:
                _menuCategory = ALL;
                editor.putInt("selectedMenu",ALL);
                break;
            case R.id.menuFinished:
                _menuCategory = FINISH;
                editor.putInt("selectedMenu",FINISH);
                break;
            case R.id.menuUnFinished:
                _menuCategory = UNFINISH;
                editor.putInt("selectedMenu",UNFINISH);
                break;
            default:
                returnVal = super.onOptionsItemSelected(item);
                break;
        }
        editor.apply();
        if(returnVal){
            createRecyclerView();
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem menuListOptionTitle = menu.findItem(R.id.menuListOptionTitle);
        switch (_menuCategory){
            case ALL:
                menuListOptionTitle.setTitle(R.string.menu_all_list);
                break;
            case FINISH:
                menuListOptionTitle.setTitle(R.string.menu_finish_list);
                break;
            case UNFINISH:
                menuListOptionTitle.setTitle(R.string.menu_unFinished_list);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void createRecyclerView() {
        switch (_menuCategory){
            case FINISH:
                createList(FINISH);
                break;
            case UNFINISH:
                createList(UNFINISH);
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
            future = tasksDAO.findAll();
        }else if(state == 2){
            future = tasksDAO.findFinished();
        }else {
            future = tasksDAO.findUnFinished();
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
        ToDoListAdapter adapter = new ToDoListAdapter(tasksList);
        _rvToDo.setAdapter(adapter);
    }

    private class ToDoViewHolder extends RecyclerView.ViewHolder {
        public TextView _tvNameRow;
        public TextView _tvFixedDateRow;
        public CheckBox _cbTaskCheckRow;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            _tvNameRow = itemView.findViewById(R.id.tvNameRow);
            _tvFixedDateRow = itemView.findViewById(R.id.tvFixedDateRow);
            _cbTaskCheckRow = itemView.findViewById(R.id.cbTaskCheckRow);
        }
    }

    private class ToDoListAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
        private List<Tasks> _listData;

        public ToDoListAdapter(List<Tasks> listData) {
            _listData = listData;
        }

        @Override
        public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View row = inflater.inflate(R.layout.row, parent, false);
            ToDoViewHolder holder = new ToDoViewHolder(row);
            return holder;
        }

        @Override
        public void onBindViewHolder(ToDoViewHolder holder, int position) {
            boolean checked = false;
//            ToDo item = _listData.get(position);
            Tasks item = _listData.get(position);

            ToDoEditActivity toDoEditActivity = new ToDoEditActivity();
            LinearLayout row = (LinearLayout) holder._cbTaskCheckRow.getParent();
            int rColor = androidx.appcompat.R.drawable.abc_list_selector_holo_light;

            String tempDateStr = toDoEditActivity.dateGetTimeInMillis(item.deadline,"yyyy年MM月dd日");
            String setText = "期限：";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = null;
            try {
                date = sdf.parse(tempDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date nowDate = new Date();
            Calendar nowCal = Calendar.getInstance();
            nowCal.setTime(nowDate);
            nowCal.set(Calendar.HOUR_OF_DAY,0);
            nowCal.set(Calendar.MINUTE,0);
            nowCal.set(Calendar.SECOND,0);
            nowCal.set(Calendar.MILLISECOND,0);
            nowDate = nowCal.getTime();
            if(date.before(nowDate)){
                setText += "過ぎてます！！！";
                holder._tvFixedDateRow.setTextColor(Color.RED);
            }else if(date.equals(nowDate)){
                Log.d("log_date","equals");
                setText += "今日";
                holder._tvFixedDateRow.setTextColor(Color.BLUE);
            }else{
                Log.d("log_date","after");
                setText += tempDateStr;
            }
            if(item.done == 1){
                checked = true;
                rColor = androidx.appcompat.R.drawable.abc_list_selector_disabled_holo_dark;
            }else{
                checked = false;
                rColor = androidx.appcompat.R.drawable.abc_list_selector_holo_light;
            }
            row.setBackgroundResource(rColor);

            holder._cbTaskCheckRow.setChecked(checked);
            holder._cbTaskCheckRow.setTag(item.id);
            holder.itemView.setOnClickListener(new ListItemClickListener(item.id));
            holder._tvNameRow.setText(item.name);
            holder._tvFixedDateRow.setText(setText);
            holder._cbTaskCheckRow.setOnClickListener(new OnCheckBoxClickListener());
        }

        @Override
        public int getItemCount() {
            return _listData.size();
        }
    }

    private class OnCheckBoxClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TasksDAO tasksDAO = _db.createTasksDAO();
            CheckBox cbTaskCheck = (CheckBox) view;
            int isChecked = -1;
            if(cbTaskCheck.isChecked()){
                isChecked = 1;
            }else{
                isChecked = 0;
            }
//            boolean isChecked = cbTaskCheck.isChecked();
            int id = (int) cbTaskCheck.getTag();
            long result = 0;
            try{
                ListenableFuture<Integer> future = tasksDAO.changeTaskChecked(id,isChecked);
                result = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(result <= 0){
                Toast.makeText(MainActivity.this , R.string.msg_save_error, Toast.LENGTH_SHORT).show();
            }
//            createRecyclerView();
            onResume();
        }
    }
}