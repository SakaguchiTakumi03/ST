package local.hal.st42.android.originalapp90727.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import local.hal.st42.android.originalapp90727.dataaccess.AppDatabase;
import local.hal.st42.android.originalapp90727.dataaccess.Books;
import local.hal.st42.android.originalapp90727.dataaccess.BooksDAO;

public class DetailViewModel extends AndroidViewModel {
    private AppDatabase _db;

    public DetailViewModel(Application application){
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    public Books getBooks(int id){
        BooksDAO booksDAO = _db.createBooksDAO();
        ListenableFuture<Books> future = booksDAO.findByPK(id);
        Books books = new Books();
        try {
            books = future.get();
        } catch (ExecutionException ex) {
            Log.e("EditViewModel","データ取得処理失敗");
        } catch (InterruptedException ex) {
            Log.e("EditViewModel","データ取得処理失敗");
        }
        return books;
    }
}
