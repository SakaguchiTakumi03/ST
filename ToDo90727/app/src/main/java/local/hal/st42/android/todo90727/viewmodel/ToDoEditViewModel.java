package local.hal.st42.android.todo90727.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import local.hal.st42.android.todo90727.dataaccess.AppDatabase;
import local.hal.st42.android.todo90727.dataaccess.Tasks;
import local.hal.st42.android.todo90727.dataaccess.TasksDAO;

public class ToDoEditViewModel extends AndroidViewModel {
    private AppDatabase _db;

    public ToDoEditViewModel(Application application){
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    public Tasks getTasks(int id){
        TasksDAO tasksDAO = _db.createTasksDAO();
        ListenableFuture<Tasks> future = tasksDAO.findByPK(id);
        Tasks tasks = new Tasks();
        try {
            tasks = future.get();
        } catch (ExecutionException ex) {
            Log.e("ToDoEditViewModel","データ取得処理失敗");
        } catch (InterruptedException ex) {
            Log.e("ToDoEditViewModel","データ取得処理失敗");
        }
        return tasks;
    }

    public long insert(Tasks tasks){
        TasksDAO tasksDAO = _db.createTasksDAO();
        ListenableFuture<Long> future = tasksDAO.insert(tasks);
        long result = 0;
        try {
            result = future.get();
        } catch (ExecutionException e) {
            Log.e("ToDoEditViewModel","データ登録処理失敗");
        } catch (InterruptedException e) {
            Log.e("ToDoEditViewModel","データ登録処理失敗");
        }
        return result;
    }

    public int update(Tasks tasks){
        TasksDAO tasksDAO = _db.createTasksDAO();
        ListenableFuture<Integer> future = tasksDAO.update(tasks);
        int result = 0;
        try {
            result = future.get();
        } catch (ExecutionException e) {
            Log.e("ToDoEditViewModel","データ更新処理失敗");
        } catch (InterruptedException e) {
            Log.e("ToDoEditViewModel","データ更新処理失敗");
        }
        return result;
    }

    public int delete(int id){
        Tasks tasks = new Tasks();
        tasks.id = id;
        TasksDAO tasksDAO = _db.createTasksDAO();
        ListenableFuture<Integer> future = tasksDAO.delete(tasks);
        int result = 0;
        try {
            result = future.get();
        } catch (ExecutionException e) {
            Log.e("ToDoEditViewModel","データ削除処理失敗");
        } catch (InterruptedException e) {
            Log.e("ToDoEditViewModel","データ削除処理失敗");
        }
        return result;
    }
}
