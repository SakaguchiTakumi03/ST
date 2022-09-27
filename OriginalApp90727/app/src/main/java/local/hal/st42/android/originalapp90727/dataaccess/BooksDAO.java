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
    @Query("SELECT * FROM books ORDER BY title ASC")
    LiveData<List<Books>> findTitleAsc();

    @Query("SELECT * FROM books ORDER BY title DESC")
    LiveData<List<Books>> findTitleDesc();

    @Query("SELECT * FROM books ORDER BY artist ASC")
    LiveData<List<Books>> findArtistAsc();

    @Query("SELECT * FROM books ORDER BY artist DESC")
    LiveData<List<Books>> findArtistDesc();

    @Query("SELECT * FROM books WHERE bookmark = 1")
    LiveData<List<Books>> findBookmark();

    @Query("SELECT * FROM books WHERE id = :id")
    static ListenableFuture<Books> findByPK(long id) {
        return null;
    }

    @Insert
    ListenableFuture<Long> insert(Books books);

    @Update
    ListenableFuture<Integer> update(Books tasks);

    @Query("UPDATE books SET bookMark = :bookMark WHERE id = :id")
    ListenableFuture<Integer> changeTaskChecked(int id,int bookMark);

    @Delete
    ListenableFuture<Integer> delete(Books tasks);

}
