package local.hal.st42.android.memopad3kai.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import local.hal.st42.android.memopad3kai.dataaccess.AppDatabase;
import local.hal.st42.android.memopad3kai.dataaccess.Memo;
import local.hal.st42.android.memopad3kai.dataaccess.MemoDAO;

/**
 * ST42 Androidサンプル14 メモ帳3ViewModel+LiveData版
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
         * @param onlyImportant 重要メモ情報のみに絞り込むかどうかの値。trueならば絞り込む。
         * @return メモ情報リストに関連するLiveDateオブジェクト。
         */
        public LiveData<List<Memo>> getMemoList(boolean onlyImportant) {
                MemoDAO memoDAO = _db.createMemoDAO();
                LiveData<List<Memo>> memoList;
                if(onlyImportant) {
                        memoList = memoDAO.findAllImportant();
                }
                else {
                        memoList = memoDAO.findAll();
                }
                return memoList;
        }
}
