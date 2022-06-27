package com.example.todo90032.dataaccess;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface TasksDAO {
    @Query("SELECT * FROM tasks ORDER BY deadline DESC")
    ListenableFuture<List<Tasks>> findAll();

    @Query("SELECT * FROM tasks WHERE done = 0 ORDER BY deadline ASC")
    ListenableFuture<List<Tasks>>findTodo();

    @Query("SELECT * FROM tasks WHERE done = 1 ORDER BY deadline DESC")
    ListenableFuture<List<Tasks>>findDone();

    @Query("SELECT * FROM tasks WHERE id = :id")
    ListenableFuture<Tasks>findByPK(int id);

    @Query("UPDATE tasks SET done = :done WHERE id = :id")
    ListenableFuture<Integer> updateOnlyDone(int id, int done);

    @Insert
    ListenableFuture<Long> insert(Tasks task);

    @Update
    ListenableFuture<Integer> update(Tasks task);

    @Delete
    ListenableFuture<Integer> delete(Tasks task);


}
