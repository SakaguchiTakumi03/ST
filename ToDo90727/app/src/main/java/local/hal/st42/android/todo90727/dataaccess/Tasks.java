package local.hal.st42.android.todo90727.dataaccess;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tasks {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    public long deadline;

    @NonNull
    @ColumnInfo(defaultValue = "0")
    public int done;

    public String note;

    public boolean getDoneBool(){
        return done == 1;
    }

}
