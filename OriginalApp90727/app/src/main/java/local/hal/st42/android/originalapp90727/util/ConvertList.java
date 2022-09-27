package local.hal.st42.android.originalapp90727.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ConvertList {
    public String DateToString(Date value, String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        String stringDate = df.format(value);
        return stringDate;
    }

    public Date longToDate(long value){
        Date date = new Date(value);
        return date;
    }

    public String longToString(long longTimeInMillis, String format){
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(format);
        ZonedDateTime zoneDate = Instant.ofEpochMilli(longTimeInMillis).atZone(ZoneId.systemDefault());
        String strDate = zoneDate.format(dtFormat);
        return strDate;
    }

//    public String LocalDateToString(LocalDate value){
//        return value.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
//    }

    public Date StringToDate(String value){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("StringToDate",DateToString(date,"yyyy-MM-dd HH:mm:ss"));
        return date;
    }
}
