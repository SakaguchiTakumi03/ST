package local.hal.ma42.android.favoriteshops90057;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.sql.Timestamp;

public class DataAccess {
    /**
     * 全データ検索メソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @return 検索結果のCursorオブジェクト。
     */
    public static Cursor findAll(SQLiteDatabase db) {
        String sql = "SELECT * FROM shops";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    /**
     * 主キーによる検索。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @return 主キーに対応するデータを格納したMemoオブジェクト。対応するデータが存在しない場合はnull。
     */
    public static Shop findByPK(SQLiteDatabase db, long id) {
        String sql = "SELECT * FROM shops WHERE _id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        Shop result = null;
        if(cursor.moveToFirst()) {
            int idxName = cursor.getColumnIndex("name");
            int idxTel = cursor.getColumnIndex("tel");
            int idxUrl = cursor.getColumnIndex("url");
            int idxNote = cursor.getColumnIndex("note");
            String name = cursor.getString(idxName);
            String tel = cursor.getString(idxTel);
            String url = cursor.getString(idxUrl);
            String note = cursor.getString(idxNote);

            result = new Shop();
            result.setId(id);
            result.setName(name);
            result.setTel(tel);
            result.setUrl(url);
            result.setNote(note);
        }
        return result;
    }

    /**
     * お店情報を更新するメソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @param name お店名。
     * @param tel 電話番号。
     * @param url URL。
     * @param note メモ。
     * @return 更新件数。
     */
    public static int update(SQLiteDatabase db, long id, String name, String tel, String url, String note) {
        String sql = "UPDATE shops SET name = ?, tel = ?, url = ?, note = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, tel);
        stmt.bindString(3, url);
        stmt.bindString(4, note);
        stmt.bindLong(5, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }

    /**
     * お店情報を新規登録するメソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param name お店名。
     * @param tel 電話番号。
     * @param url URL。
     * @param note メモ。
     * @return 登録されたレコードの主キー値。
     */
    public static long insert(SQLiteDatabase db, String name, String tel, String url, String note) {
        String sql = "INSERT INTO shops (name, tel, url, note) VALUES (?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, name);
        stmt.bindString(2, tel);
        stmt.bindString(3, url);
        stmt.bindString(4, note);
        long id = stmt.executeInsert();
        return id;
    }

    /**
     * お店情報を削除するメソッド。
     *
     * @param db SQLiteDatabaseオブジェクト。
     * @param id 主キー値。
     * @return 削除件数。
     */
    public static int delete(SQLiteDatabase db, long id) {
        String sql = "DELETE FROM shops WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1, id);
        int result = stmt.executeUpdateDelete();
        return result;
    }
}
