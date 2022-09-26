package local.hal.st42.android.originalapp90727.dataaccess;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

@Entity
public class Books {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

//    @NonNull
    public String artist;

    public String note;

    @NonNull
    @ColumnInfo(defaultValue = "0")
    public int bookMark;

    public Date purchaseDate;

    public Date registrationDate;

    public Date updateDate;
}