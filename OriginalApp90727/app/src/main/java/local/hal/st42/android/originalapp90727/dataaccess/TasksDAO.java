package local.hal.st42.android.originalapp90727.dataaccess;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TasksDAO {
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    LiveData<List<Tasks>> findAll();

    @Query("SELECT * FROM tasks WHERE done = 1 ORDER BY deadline DESC")
    LiveData<List<Tasks>> findFinished();

    @Query("SELECT * FROM tasks WHERE done = 0 ORDER BY deadline DESC")
    LiveData<List<Tasks>> findUnFinished();

    @Query("SELECT * FROM tasks WHERE id = :id")
    ListenableFuture<Tasks>findByPK(long id);

    @Insert
    ListenableFuture<Long> insert(Tasks tasks);

    @Update
    ListenableFuture<Integer> update(Tasks tasks);

    @Query("UPDATE tasks SET done = :done WHERE id = :id")
    ListenableFuture<Integer> changeTaskChecked(int id,int done);

    @Delete
    ListenableFuture<Integer> delete(Tasks tasks);

}
