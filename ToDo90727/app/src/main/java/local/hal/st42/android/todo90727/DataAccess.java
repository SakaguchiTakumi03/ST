package local.hal.st42.android.todo90727;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataAccess {
    public static Cursor findAll(SQLiteDatabase db){
        Log.d("DataAccess","findAll");
        String sql = "SELECT * FROM tasks";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    //ぷらいまりーきー取得
    public static ToDo findByPK(SQLiteDatabase db,long id){
        Log.d("DataAccess","findByPK");
        String sql = "SELECT * FROM tasks WHERE _id = " + id;
        Cursor cursor = db.rawQuery(sql,null);
        ToDo result = null;
        if(cursor.moveToFirst()){
            int idxName = cursor.getColumnIndex("name");
            int idxDeadline = cursor.getColumnIndex("deadline");
            int idxDone = cursor.getColumnIndex("done");
            int idxNote = cursor.getColumnIndex("note");

            result = new ToDo();
            result.setId(id);
            result.setName(cursor.getString(idxName));
            result.setDeadline(cursor.getLong(idxDeadline));
            result.setDone(cursor.getLong(idxDone));
            result.setNote(cursor.getString(idxNote));
        }
        return result;
    }

    //いんさーと
    public static long insert(SQLiteDatabase db, String name, long deadline, long done, String note){
        Log.d("DataAccess","insert");
        String sql = "INSERT INTO tasks ( name, deadline, done, note) VALUES (?,?,?,?)";
        SQLiteStatement SQLstmt = db.compileStatement(sql);
        SQLstmt.bindString(1,name);
        SQLstmt.bindLong(2,deadline);
        SQLstmt.bindLong(3,done);
        SQLstmt.bindString(4,note);
        long id = SQLstmt.executeInsert();
        return id;
    }

    //あっぷでーと
    public static int update(SQLiteDatabase db, long id, String name, long deadline, long done, String note){
        Log.d("DataAccess","update");
        String sql = "UPDATE tasks SET name = ?, deadline = ?, done = ?, note = ? WHERE _id = " + id;
        SQLiteStatement SQLstmt = db.compileStatement(sql);
        SQLstmt.bindString(1,name);
        SQLstmt.bindLong(2,deadline);
        SQLstmt.bindLong(3,done);
        SQLstmt.bindString(4,note);
        int result = SQLstmt.executeUpdateDelete();
        return result;
    }

    //さくじょ
    public static int delete(SQLiteDatabase db, long id){
        Log.d("DataAccess","delete");
        String sql = "DELETE FROM tasks WHERE _id = " + id;
        SQLiteStatement SQLstmt = db.compileStatement(sql);
        SQLstmt.bindLong(1,id);
        int result = SQLstmt.executeUpdateDelete();
        return result;
    }
}
