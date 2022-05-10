package local.hal.st42.android.customizedlistview3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * ST42 Androidサンプル04 DBデータに応じたリストビューのカスタマイズ
 *
 * データベース上のデータを処理するクラス。
 *
 * @author Shinzo SAITO
 */
public class DataAccess {
        /**
         * 全データ検索メソッド。
         *
         * @param db SQLiteDatabaseオブジェクト。
         * @return 検索結果のCursorオブジェクト。
         */
        public static Cursor findAll(SQLiteDatabase db) {
                String sql = "SELECT * FROM phones";
                Cursor cursor = db.rawQuery(sql, null);
                return cursor;
        }
    
        /**
         * checkedフラグのon/offを変更するメソッド。
         *
         * @param db SQLiteDatabaseオブジェクト。
         * @param id 変更対象レコードの主キー値。
         * @param isChecked trueの場合フラグをon(値を1)に、falseの場合フラグをoff(値を0)に変更する。
         */
        public static void changePhoneChecked(SQLiteDatabase db, long id, boolean isChecked) {
                String sql = "UPDATE phones SET checked = ? WHERE _id = ?";
                SQLiteStatement stmt = db.compileStatement(sql);
                if(isChecked) {
                        stmt.bindLong(1, 1);
                }
                else {
                        stmt.bindLong(1, 0);
                }
                stmt.bindLong(2, id);
                stmt.executeUpdateDelete();
        }
}
