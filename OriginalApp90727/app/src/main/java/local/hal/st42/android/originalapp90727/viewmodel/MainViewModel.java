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

public class MainViewModel extends AndroidViewModel {
    private AppDatabase _db;

    public MainViewModel(Application application){
        super(application);
        _db = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Books>> getBooksList(int selectMenuCategory){
        BooksDAO booksDAO = _db.createBooksDAO();

        LiveData<List<Books>> booksList = null;

        if(selectMenuCategory == 1){
            booksList = booksDAO.findTitleAsc();
        }else if(selectMenuCategory == 2){
            booksList = booksDAO.findTitleDesc();
        }else if(selectMenuCategory == 3){
            booksList = booksDAO.findArtistAsc();
        }else if(selectMenuCategory == 4){
            booksList = booksDAO.findArtistDesc();
        }else{
            booksList = booksDAO.findBookmark();
        }
        return booksList;
    }

//    public int checkedDone(int id , int done){
//        BooksDAO booksDAO = _db.createBooksDAO();
//        ListenableFuture<Integer> future = booksDAO.changeTaskChecked(id, done);
//        int result = 0;
//        try {
//            result = future.get();
//        } catch (ExecutionException e) {
//            Log.e("EditViewModel","データ処理失敗");
//        } catch (InterruptedException e) {
//            Log.e("EditViewModel","データ処理失敗");
//        }
//        return result;
//    }
}
