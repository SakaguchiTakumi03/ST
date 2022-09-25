package local.hal.st42.android.originalapp90727;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import static local.hal.st42.android.originalapp90727.Consts.*;

public class MainActivity extends AppCompatActivity {

    private int _menuCategory;

    private static final String PREFS_NAME = "PSPrefsFile";

    private static final int DEFAULT_SELECT = 1;

    private String titleName = "";

    private RecyclerView _rvToDo;

//    private ToDoListAdapter _adapter;

//    private MainViewModel _mainViewModel;

//    private TasksListObserver _tasksListObserver;

//    private LiveData<List<Tasks>> _tasksListLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





    }
}