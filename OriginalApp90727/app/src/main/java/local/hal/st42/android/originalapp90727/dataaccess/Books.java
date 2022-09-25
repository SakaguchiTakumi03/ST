package local.hal.st42.android.originalapp90727.dataaccess;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    public int done;

    public String purchaseDate;

    public String registrationDate;

    public String updateDate;
}
