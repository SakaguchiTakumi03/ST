package local.hal.st42.android.todo90727;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.InvalidationTracker;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import local.hal.st42.android.todo90727.dataaccess.Tasks;
import local.hal.st42.android.todo90727.dataaccess.TasksDAO;
import local.hal.st42.android.todo90727.viewmodel.MainViewModel;

import static local.hal.st42.android.todo90727.Consts.ALL;
import static local.hal.st42.android.todo90727.Consts.FINISH;
import static local.hal.st42.android.todo90727.Consts.MODE_EDIT;
import static local.hal.st42.android.todo90727.Consts.MODE_INSERT;
import static local.hal.st42.android.todo90727.Consts.UNFINISH;


public class MainActivity extends AppCompatActivity {

    private int _menuCategory;

    private static final String PREFS_NAME = "PSPrefsFile";

    private static final int DEFAULT_SELECT = 1;

    private String titleName = "";

    private RecyclerView _rvToDo;

    private ToDoListAdapter _adapter;

    private MainViewModel _mainViewModel;

    private TasksListObserver _tasksListObserver;

    private LiveData<List<Tasks>> _tasksListLiveData;

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

        _rvToDo = findViewById(R.id.rvToDo);
        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        _rvToDo.setLayoutManager(layout);
        DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
        _rvToDo.addItemDecoration(decoration);

        List<Tasks> tasksList = new ArrayList<>();
        _adapter = new ToDoListAdapter(tasksList);
        _rvToDo.setAdapter(_adapter);

        ViewModelProvider provider = new ViewModelProvider(MainActivity.this);
        _mainViewModel = provider.get(MainViewModel.class);
        _tasksListObserver = new TasksListObserver();
        _tasksListLiveData = new MutableLiveData<>();

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

    private class TasksListObserver implements Observer<List<Tasks>> {
        @Override
        public void onChanged(List<Tasks> tasksList) {
            _adapter.changeTasksList(tasksList);
        }
    }


//    private class ToDoViewHolder extends RecyclerView.ViewHolder {
//        public TextView _tvNameRow;
//
//        public ToDoViewHolder(View itemView){
//            super(itemView);
//            _tvNameRow = itemView.findViewById(R.id.tvNameRow);
//        }
//    }

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
        _tasksListLiveData.removeObserver(_tasksListObserver);
        _tasksListLiveData = _mainViewModel.getTodoList(_menuCategory);
        _tasksListLiveData.observe(MainActivity.this,_tasksListObserver);
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

        public void changeTasksList(List<Tasks> listData){
            _listData = listData;
            notifyDataSetChanged();
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

            String tempDateStr = toDoEditActivity.dateGetTimeInMillis(item.deadline.getTime(),"yyyy年MM月dd日");
            String setText = "期限：";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = new Date(System.currentTimeMillis());
            try {
                date = (Date) sdf.parse(tempDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date nowDate = new Date(System.currentTimeMillis());
            Calendar nowCal = Calendar.getInstance();
            nowCal.setTime(nowDate);
            nowCal.set(Calendar.HOUR_OF_DAY,0);
            nowCal.set(Calendar.MINUTE,0);
            nowCal.set(Calendar.SECOND,0);
            nowCal.set(Calendar.MILLISECOND,0);
            nowDate = (Date) nowCal.getTime();
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
                holder._tvFixedDateRow.setTextColor(Color.GRAY);
            }
            if(item.done == 1){
                checked = true;
                rColor = androidx.appcompat.R.drawable.abc_list_selector_disabled_holo_dark;
//                setText = "期限："+ tempDateStr;
                setText = "タスク完了";
                holder._tvFixedDateRow.setTextColor(Color.BLACK);
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
            CheckBox cbTaskCheck = (CheckBox) view;
            int isChecked = -1;
            if(cbTaskCheck.isChecked()){
                isChecked = 1;
            }else{
                isChecked = 0;
            }
            int id = (int) cbTaskCheck.getTag();
            long result = 0;

            ViewModelProvider provider = new ViewModelProvider(MainActivity.this);
            _mainViewModel = provider.get(MainViewModel.class);
            result = _mainViewModel.checkedDone(id,isChecked);

            if(result <= 0){
                Toast.makeText(MainActivity.this , R.string.msg_save_error, Toast.LENGTH_SHORT).show();
            }
            onResume();
        }
    }
}