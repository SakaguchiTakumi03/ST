package local.hal.st42.android.todo90727;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {

    private static List<ToDo> convertList(Cursor cursor){
        List<ToDo> result = new ArrayList<>();
        while(cursor.moveToNext()){
            ToDo todo = new ToDo();
            int idxId = cursor.getColumnIndex("_id");
            int idxName = cursor.getColumnIndex("name");
            int idxDeadline = cursor.getColumnIndex("deadline");
            int idxDone = cursor.getColumnIndex("done");
            int idxNote = cursor.getColumnIndex("note");
            todo.setId(cursor.getLong(idxId));
            todo.setName(cursor.getString(idxName));
            todo.setDeadline(cursor.getLong(idxDeadline));
            todo.setDone(cursor.getLong(idxDone));
            todo.setNote(cursor.getString(idxNote));
            result.add(todo);
        }
        return result;
    }

    public static List<ToDo> findAll(SQLiteDatabase db){
        Log.d("DataAccess","findAll");
        String sql = "SELECT * FROM tasks ORDER BY deadline ASC";
        Cursor cursor = db.rawQuery(sql,null);
        List<ToDo> result = convertList(cursor);
        return result;
    }

    public static List<ToDo> findFinished(SQLiteDatabase db){
        Log.d("DataAccess","finishedList");
        String sql = "SELECT * FROM tasks WHERE done = 1 ORDER BY deadline DESC";
        Cursor cursor = db.rawQuery(sql,null);
        List<ToDo> result = convertList(cursor);
        return result;
    }

    public static List<ToDo> findUnFinished(SQLiteDatabase db){
        Log.d("DataAccess","unfinishedList");
        String sql = "SELECT * FROM tasks WHERE done = 0 ORDER BY deadline ASC";
        Cursor cursor = db.rawQuery(sql,null);
        List<ToDo> result = convertList(cursor);
        return result;
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
        String sql = "DELETE FROM tasks WHERE _id = ?";
        SQLiteStatement SQLstmt = db.compileStatement(sql);
        SQLstmt.bindLong(1,id);
        int result = SQLstmt.executeUpdateDelete();
        return result;
    }

    public static int changeTaskChecked(SQLiteDatabase db,long id,boolean isChecked){
        String sql = "UPDATE tasks SET done = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        if(isChecked){
            Log.d("debug","change_true");
            stmt.bindLong(1,1);
        }else {
            Log.d("debug","change_false");
            stmt.bindLong(1,0);
        }
        stmt.bindLong(2, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }
}
