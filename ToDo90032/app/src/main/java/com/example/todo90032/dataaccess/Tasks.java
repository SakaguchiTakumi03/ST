package com.example.todo90032.dataaccess;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity
public class Tasks {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    public Timestamp deadline;

    @ColumnInfo(defaultValue = "0")
    public int done;

    public String note;

    public boolean getDoneBool(){
        return done == 1;
    }
}
