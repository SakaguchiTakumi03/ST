package local.hal.st42.android.originalapp90727.dataaccess;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface BooksDAO {
//    @Query("SELECT * FROM books ORDER BY  ASC")
//    LiveData<List<Books>> findAll();
//
//    @Query("SELECT * FROM books WHERE done = 1 ORDER BY deadline DESC")
//    LiveData<List<Books>> findFinished();
//
//    @Query("SELECT * FROM books WHERE done = 0 ORDER BY deadline DESC")
//    LiveData<List<Books>> findUnFinished();

    @Query("SELECT * FROM books WHERE id = :id")
    static ListenableFuture<Books> findByPK(long id) {
        return null;
    }

    @Insert
    ListenableFuture<Long> insert(Books tasks);

    @Update
    ListenableFuture<Integer> update(Books tasks);

    @Query("UPDATE books SET done = :done WHERE id = :id")
    ListenableFuture<Integer> changeTaskChecked(int id,int done);

    @Delete
    ListenableFuture<Integer> delete(Books tasks);

}
