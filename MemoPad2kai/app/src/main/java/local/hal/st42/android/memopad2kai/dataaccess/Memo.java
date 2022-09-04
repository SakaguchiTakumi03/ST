package local.hal.st42.android.memopad2kai.dataaccess;

import java.sql.Timestamp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * ST42 Androidサンプル13 メモ帳2ViewModel+LiveData版
 *
 * メモ情報のエンティティクラス。
 *
 * @author Shinzo SAITO
 */
@Entity
public class Memo {
        /**
         * 主キーのid。
         */
        @PrimaryKey(autoGenerate = true)
        public int id;
        /**
         * タイトル。
         */
        @NonNull
        public String title;
        /**
         * 内容。
         */
        public String content;
        /**
         * 更新日時。
         */
        @NonNull
        public Timestamp updatedAt;
}
