package com.example.todo90032.dataaccess;

import androidx.room.TypeConverter;

import java.sql.Timestamp;

public class TimestampConverter {
    @TypeConverter
    public static Timestamp toTimestamp(Long value){
        Timestamp returnVal = null;
        if(value != null){
            returnVal = new Timestamp(value);
        }
        return returnVal;
    }

    @TypeConverter
    public static Long toLong(Timestamp value){
        Long returnVal = null;
        if(value != null){
            returnVal = value.getTime();
        }
        return returnVal;
    }
}
