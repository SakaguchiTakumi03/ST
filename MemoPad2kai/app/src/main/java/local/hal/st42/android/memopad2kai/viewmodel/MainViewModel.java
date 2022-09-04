package local.hal.st42.android.memopad2kai.viewmodel;

import android.app.Application;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import local.hal.st42.android.memopad2kai.dataaccess.AppDatabase;
import local.hal.st42.android.memopad2kai.dataaccess.Memo;
import local.hal.st42.android.memopad2kai.dataaccess.MemoDAO;

/**
 * ST42 Androidサンプル13 メモ帳2ViewModel+LiveData版
 *
 * メインアクティビティ用のデータを管理するビューモデルクラス。
 *
 * @author Shinzo SAITO
 */
public class MainViewModel extends AndroidViewModel {
        /**
         * データベースオブジェクト。
         */
        private AppDatabase _db;
    
        /**
         * コンストラクタ。
         *
         * @param application アプリケーションオブジェクト。
         */
        public MainViewModel(Application application) {
                super(application);
                _db = AppDatabase.getDatabase(application);
        }
    
        /**
         * メモ情報リストを取得するメソッド。
         *
         * @return メモ情報リストに関連するLiveDateオブジェクト。
         */
        public LiveData<List<Memo>> getMemoList() {
                MemoDAO memoDAO = _db.createMemoDAO();
                LiveData<List<Memo>> memoList = memoDAO.findAll();
                return memoList;
        }
}
