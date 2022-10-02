package local.hal.st42.android.originalapp90727.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
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

    public LiveData<Books> getBooks(int id){
        BooksDAO booksDAO = _db.createBooksDAO();
        LiveData<Books> book = booksDAO.findByPKLiveData(id);
        return book;
    }

}
