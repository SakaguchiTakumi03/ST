package local.hal.st42.android.originalapp90727.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import local.hal.st42.android.originalapp90727.dataaccess.AppDatabase;
import local.hal.st42.android.originalapp90727.dataaccess.Books;
import local.hal.st42.android.originalapp90727.dataaccess.BooksDAO;

public class EditViewModel extends AndroidViewModel {
    private AppDatabase _db;

    public EditViewModel(Application application){
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    public Books getTasks(int id){
        BooksDAO booksDAO = _db.createTasksDAO();
        ListenableFuture<Books> future = BooksDAO.findByPK(id);
        Books books = new Books();
        try {
            books = future.get();
        } catch (ExecutionException ex) {
            Log.e("ToDoEditViewModel","データ取得処理失敗");
        } catch (InterruptedException ex) {
            Log.e("ToDoEditViewModel","データ取得処理失敗");
        }
        return books;
    }

    public long insert(Books tasks){
        BooksDAO booksDAO = _db.createTasksDAO();
        ListenableFuture<Long> future = booksDAO.insert(tasks);
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

    public int update(Books tasks){
        BooksDAO booksDAO = _db.createTasksDAO();
        ListenableFuture<Integer> future = booksDAO.update(tasks);
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
        Books tasks = new Books();
        tasks.id = id;
        BooksDAO booksDAO = _db.createTasksDAO();
        ListenableFuture<Integer> future = booksDAO.delete(tasks);
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