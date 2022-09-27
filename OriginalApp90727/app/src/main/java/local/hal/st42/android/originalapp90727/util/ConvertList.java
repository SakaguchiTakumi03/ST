package local.hal.st42.android.originalapp90727.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ConvertList {
    public String DateToString(Date value){
        String pattern = "yyyy年[]M月[]d日";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String stringDate = df.format(value);
        return stringDate;
    }

    public String LocalDateToString(LocalDate value){
        return value.format(DateTimeFormatter.ofPattern("yyyy年[]M月[]d日"));
    }

    public Date StringToDate(String value){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

//    public Date intToDate(int year,int month, int dayOfMonth){
//        String strYear = String.valueOf(year);
//        String strMonth = String.valueOf(month);
//        String strDayOfMonth = String.valueOf(dayOfMonth);
//    }
}
