package local.hal.st42.android.todo90727;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todolist.db";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE tasks (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL,");
        sb.append("deadline INTEGER,");
        sb.append("done INTEGER DEFAULT 0,");
        sb.append("note TEXT");
        sb.append(");");
        //どっちでもいい後に削除
        //sb.append("CREATE TABLE tasks ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, deadline INTEGER, done INTEGER DEFAULT 0, note TEXT);");
        String sql = sb.toString();
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
