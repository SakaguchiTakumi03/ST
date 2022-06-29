package local.hal.st42.android.todo90727.dataaccess;

import androidx.room.TypeConverter;

import java.sql.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long value) {
        Date returnVal = null;
        if(value != null) {
            returnVal = new Date(value);
        }
        return returnVal;
    }
    @TypeConverter
    public static Long toLong(Date value) {
        Long returnVal = null;
        if(value != null) {
            returnVal = value.getTime();
        }
        return returnVal;
    }
}
