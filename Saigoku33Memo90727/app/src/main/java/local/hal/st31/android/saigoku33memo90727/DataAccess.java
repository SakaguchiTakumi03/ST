package local.hal.st31.android.saigoku33memo90727;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DataAccess {
    public static Memo findByPK(SQLiteDatabase db, int selectedTempleNo) {
        String sql = "SELECT * FROM temples WHERE _id = " + selectedTempleNo;
        Cursor cursor = db.rawQuery(sql, null);
        Memo memo = null;
        if(cursor.moveToFirst()) {
            memo = new Memo();
            int idxName = cursor.getColumnIndex("name");
            String name = cursor.getString(idxName);

            int idxHonzon = cursor.getColumnIndex("honzon");
            String Honzon = cursor.getString(idxHonzon);

            int idxShushi = cursor.getColumnIndex("shushi");
            String shushi = cursor.getString(idxShushi);

            int idxAddress = cursor.getColumnIndex("address");
            String address = cursor.getString(idxAddress);

            int idxURL = cursor.getColumnIndex("url");
            String URL = cursor.getString(idxURL);

            int idxNote = cursor.getColumnIndex("note");
            String note = cursor.getString(idxNote);

            memo.setId(selectedTempleNo);
            memo.setName(name);
            memo.setHonzon(Honzon);
            memo.setShushi(shushi);
            memo.setAddress(address);
            memo.setURL(URL);
            memo.setNote(note);
        }
        return memo;
    }

    public static boolean findRowByPK(SQLiteDatabase db, int selectedTempleNo) {
        String sql = "SELECT COUNT(*) AS count FROM temples WHERE _id = " + selectedTempleNo;
        Cursor cursor = db.rawQuery(sql, null);
        boolean result = false;
        if(cursor.moveToFirst()) {
            int idxCount = cursor.getColumnIndex("count");
            int count = cursor.getInt(idxCount);
            if(count >= 1) {
                result = true;
            }
        }
        return result;
    }

    public static int update(SQLiteDatabase db, long selectedTempleNo, String honzon, String shushi, String address, String url, String note ) {
        String sql = "UPDATE temples SET honzon = ?, shushi = ?, address = ? , url = ? , note = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, honzon);
        stmt.bindString(2, shushi);
        stmt.bindString(3, address);
        stmt.bindString(4, url);
        stmt.bindString(5, note);
        stmt.bindLong(6,selectedTempleNo);
        int result = stmt.executeUpdateDelete();
        return result;
    }

    public static long insert(SQLiteDatabase db, long selectedTempleNo, String selectedTempleName, String honzon, String shushi, String address, String url, String note) {
        String sql = "INSERT INTO temples (_id, name, honzon, shushi, address, url, note ) VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, selectedTempleNo);
        stmt.bindString(2, selectedTempleName);
        stmt.bindString(3, honzon);
        stmt.bindString(4, shushi);
        stmt.bindString(5, address);
        stmt.bindString(6, url);
        stmt.bindString(7, note);
        long insertedId = stmt.executeInsert();
        return insertedId;
    }
}
