package local.hal.st42.android.originalapp90727.dataaccess;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.google.common.base.Converter;

@Database(entities = {Books.class}, version = 1 , exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase _instance;

    public static AppDatabase getDatabase(Context context){
        if(_instance == null){
            _instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "books_db").build();
        }
        return _instance;
    }

    public abstract BooksDAO createBooksDAO();

}
