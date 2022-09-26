package local.hal.st42.android.originalapp90727.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertList {
    public String DateToString(Date value){
        String pattern = "yyyy年MM月[DD日";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String stringDate = df.format(value);
        return stringDate;
    }
}
