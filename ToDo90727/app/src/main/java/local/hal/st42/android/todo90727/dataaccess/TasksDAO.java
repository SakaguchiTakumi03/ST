package local.hal.st42.android.todo90727.dataaccess;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface TasksDAO {
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    ListenableFuture<List<Tasks>> findAll();

    @Query("SELECT * FROM tasks WHERE done = 1 ORDER BY deadline DESC")
    ListenableFuture<List<Tasks>> findFinished();

    @Query("SELECT * FROM tasks WHERE done = 0 ORDER BY deadline DESC")
    ListenableFuture<List<Tasks>> findUnFinished();

    @Insert
    ListenableFuture<Long> insert(Tasks tasks);

    @Update
    ListenableFuture<Integer> update(Tasks tasks);

    @Query("UPDATE tasks SET done = :done WHERE id = :id")
    ListenableFuture<Integer> changeTaskChecked(long id,int done);

    @Delete
    ListenableFuture<Integer> delete(Tasks tasks);

}
