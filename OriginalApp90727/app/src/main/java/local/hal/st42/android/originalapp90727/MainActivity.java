package local.hal.st42.android.originalapp90727;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import local.hal.st42.android.originalapp90727.dataaccess.Tasks;
import local.hal.st42.android.originalapp90727.viewmodel.MainViewModel;

import static local.hal.st42.android.originalapp90727.Consts.*;

public class MainActivity extends AppCompatActivity {

    private int _menuCategory;

    private static final String PREFS_NAME = "PSPrefsFile";

    private static final int DEFAULT_SELECT = 1;

    private String titleName = "";

    private RecyclerView _rvOriginApp;

//    private OriginalAppListAdapter _adapter;

    private MainViewModel _mainViewModel;

//    private TasksListObserver _tasksListObserver;

    private LiveData<List<Tasks>> _tasksListLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _menuCategory = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).getInt("selectedMenu",DEFAULT_SELECT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        titleName = "hogehoge";
        toolbarLayout.setTitle(titleName);
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

        _rvOriginApp = findViewById(R.id.rvOriginApp);
        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        _rvOriginApp.setLayoutManager(layout);
        DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
        _rvOriginApp.addItemDecoration(decoration);

        List<Tasks> tasksList = new ArrayList<>();
//        _adapter = new OriginalAppListAdapter(tasksList);
//        _rvOriginApp.setAdapter(_adapter);

        ViewModelProvider provider = new ViewModelProvider(MainActivity.this);
        _mainViewModel = provider.get(MainViewModel.class);
//        _tasksListObserver = new TasksListObserver();
        _tasksListLiveData = new MutableLiveData<>();

//        createRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("mode",MODE_INSERT);
                startActivity(intent);
            }
        });
    }

//    private class OriginalAppListAdapter extends RecyclerView.Adapter<OriginalAppViewHolder> {
//        private List<Tasks> _listData;
//
//        public OriginalAppListAdapter(List<Tasks> listData) {
//            _listData = listData;
//        }
//
//        public void changeTasksList(List<Tasks> listData) {
//            _listData = listData;
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public OriginalAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//            View row = inflater.inflate(R.layout.row, parent, false);
//            OriginalAppViewHolder holder = new OriginalAppViewHolder(row);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(OriginalAppViewHolder holder, int position) {
//            boolean checked = false;
//            Tasks item = _listData.get(position);
//        }
//
//        private class OriginalAppViewHolder extends RecyclerView.ViewHolder {
//            public TextView _tvNameRow;
//            public TextView _tvFixedDateRow;
//            public CheckBox _cbTaskCheckRow;
//
//            public OriginalAppViewHolder(View itemView) {
//                super(itemView);
//                _tvNameRow = itemView.findViewById(R.id.tvNameRow);
//                _tvFixedDateRow = itemView.findViewById(R.id.tvFixedDateRow);
//                _cbTaskCheckRow = itemView.findViewById(R.id.cbTaskCheckRow);
//            }
//        }
//    }

//    private class TasksListObserver {
//    }
}