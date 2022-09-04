package local.hal.st42.android.memopad2kai.viewmodel;

import android.app.Application;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import androidx.lifecycle.AndroidViewModel;
import local.hal.st42.android.memopad2kai.dataaccess.AppDatabase;
import local.hal.st42.android.memopad2kai.dataaccess.Memo;
import local.hal.st42.android.memopad2kai.dataaccess.MemoDAO;

/**
 * ST42 Androidサンプル13 メモ帳2ViewModel+LiveData版
 *
 * メモ情報編集アクティビティ用データを管理するビューモデルクラス。
 *
 * @author Shinzo SAITO
 */
public class MemoEditViewModel extends AndroidViewModel {
        /**
         * データベースオブジェクト。
         */
        private AppDatabase _db;
    
        /**
         * コンストラクタ。
         *
         * @param application アプリケーションオブジェクト。
         */
        public MemoEditViewModel(Application application) {
                super(application);
                _db = AppDatabase.getDatabase(application);
        }
    
        /**
         * 引数の主キーに該当するメモ情報を取得するメソッド。
         *
         * @param id 主キー値。
         * @return 引数に該当するMemoオブジェクト。該当データが存在しない場合は、空のMemoオブジェクト。
         */
        public Memo getMemo(int id) {
                MemoDAO memoDAO = _db.createMemoDAO();
                ListenableFuture<Memo> future = memoDAO.findByPK(id);
                Memo memo = new Memo();
                try {
                        memo = future.get();
                }
                catch(ExecutionException ex) {
                        Log.e("MemoListViewModel", "データ取得処理失敗", ex);
                }
                catch(InterruptedException ex) {
                        Log.e("MemoListViewModel", "データ取得処理失敗", ex);
                }
                return memo;
        }
    
        /**
         * メモ情報登録メソッド。
         *
         * @param memo 登録するメモ情報。
         * @return 登録されたメモ情報の主キー値。登録に失敗した場合は0。
         */
        public long insert(Memo memo) {
                MemoDAO memoDAO = _db.createMemoDAO();
                ListenableFuture<Long> future = memoDAO.insert(memo);
                long result = 0;
                try {
                        result = future.get();
                }
                catch(ExecutionException ex) {
                        Log.e("MemoListViewModel", "データ登録処理失敗", ex);
                }
                catch(InterruptedException ex) {
                        Log.e("MemoListViewModel", "データ登録処理失敗", ex);
                }
                return result;
        }
    
        /**
         * メモ情報更新メソッド。
         *
         * @param memo 更新するメモ情報。
         * @return 更新件数。更新に失敗した場合は0。
         */
        public int update(Memo memo) {
                MemoDAO memoDAO = _db.createMemoDAO();
                ListenableFuture<Integer> future = memoDAO.update(memo);
                int result = 0;
                try {
                        result = future.get();
                }
                catch(ExecutionException ex) {
                        Log.e("MemoListViewModel", "データ更新処理失敗", ex);
                }
                catch(InterruptedException ex) {
                        Log.e("MemoListViewModel", "データ更新処理失敗", ex);
                }
                return result;
        }
    
        /**
         * メモ情報削除メソッド。
         *
         * @param id 削除対象メモの主キー値。
         * @return 削除件数。削除に失敗した場合は0。
         */
        public int delete(int id) {
                Memo memo = new Memo();
                memo.id = id;
                MemoDAO memoDAO = _db.createMemoDAO();
                ListenableFuture<Integer> future = memoDAO.delete(memo);
                int result = 0;
                try {
                        result = future.get();
                }
                catch(ExecutionException ex) {
                        Log.e("MemoListViewModel", "データ削除処理失敗", ex);
                }
                catch(InterruptedException ex) {
                        Log.e("MemoListViewModel", "データ削除処理失敗", ex);
                }
                return result;
        }
}
