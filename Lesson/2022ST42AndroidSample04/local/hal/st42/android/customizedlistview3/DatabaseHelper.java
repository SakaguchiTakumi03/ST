package local.hal.st42.android.customizedlistview3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * ST42 Androidサンプル04 DBデータに応じたリストビューのカスタマイズ
 *
 * データベースのヘルパークラス。
 *
 * @author Shinzo SAITO
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * データベースファイル名の定数フィールド。
         */
        private static final String DATABASE_NAME = "phones.db";
        /**
         * バージョン情報の定数フィールド。
         */
        private static final int DATABASE_VERSION = 1;
    
        /**
         * コンストラクタ。
         *
         * @param context コンテキスト。
         */
        public DatabaseHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    
        @Override
        public void onCreate(SQLiteDatabase db) {
                StringBuffer sb = new StringBuffer();
                sb.append("CREATE TABLE phones (");
                sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
                sb.append("phone_type INTEGER NOT NULL,");
                sb.append("phone_no TEXT NOT NULL,");
                sb.append("sex INTEGER DEFAULT 0,");
                sb.append("checked INTEGER DEFAULT 0");
                sb.append(");");
                String sql = sb.toString();
        
                db.execSQL(sql);
        
                sql = "INSERT INTO phones (phone_type, phone_no, sex, checked) VALUES (?, ?, ?, ?)";
                SQLiteStatement stmt = db.compileStatement(sql);
        
                db.beginTransaction();
                for (int i = 1; i <= 30; i++) {
                        int phoneType = (int) (3 * Math.random()) + 1;
                        int phoneNoInt = (int) (99999999 * Math.random());
                        int sexInt = (int) (10 * Math.random() + 1);
                        int phoneCheckedInt = (int) (10 * Math.random() + 1);
            
                        String phoneNo = "090" + String.format("%08d", phoneNoInt);
            
                        int sex = 1;
                        if(sexInt <= 5) {
                                sex = 0;
                        }
            
                        int phoneChecked = 1;
                        if(phoneCheckedInt <= 5) {
                                phoneChecked = 0;
                        }
            
                        stmt.bindLong(1, phoneType);
                        stmt.bindString(2, phoneNo);
                        stmt.bindLong(3, sex);
                        stmt.bindLong(4, phoneChecked);
            
                        stmt.executeInsert();
                        stmt.clearBindings();
                }
        
                db.setTransactionSuccessful();
                db.endTransaction();
        }
    
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
}
