package local.hal.st42.android.todo90727.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

import local.hal.st42.android.todo90727.dataaccess.AppDatabase;
import local.hal.st42.android.todo90727.dataaccess.Tasks;
import local.hal.st42.android.todo90727.dataaccess.TasksDAO;

public class MainViewModel extends AndroidViewModel {
    private AppDatabase _db;

    public MainViewModel(Application application){
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Tasks>> getTodoList(int selectMenuCategory){
        TasksDAO tasksDAO = _db.createTasksDAO();

        LiveData<List<Tasks>> tasksList;

        if(selectMenuCategory == 1){
            tasksList = tasksDAO.findAll();

        }else if(selectMenuCategory == 2){
            tasksList = tasksDAO.findFinished();

        }else{
            tasksList = tasksDAO.findUnFinished();
        }
        return tasksList;
    }

    public int checkedDone(int id , int done){
        TasksDAO tasksDAO = _db.createTasksDAO();
        ListenableFuture<Integer> future = tasksDAO.changeTaskChecked(id, done);
        int result = 0;
        try {
            result = future.get();
        } catch (ExecutionException e) {
            Log.e("ToDoEditViewModel","データ処理失敗");
        } catch (InterruptedException e) {
            Log.e("ToDoEditViewModel","データ処理失敗");
        }
        return result;
    }

}
