package local.hal.st42.android.todo90727.dataaccess;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Tasks {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    public Date deadline;

    @NonNull
    @ColumnInfo(defaultValue = "0")
    public int done;

    public String note;

}
