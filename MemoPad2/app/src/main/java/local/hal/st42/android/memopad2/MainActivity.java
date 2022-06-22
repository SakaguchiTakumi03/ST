package local.hal.st42.android.memopad2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import local.hal.st42.android.memopad2.dataaccess.AppDatabase;
import local.hal.st42.android.memopad2.dataaccess.Memo;
import local.hal.st42.android.memopad2.dataaccess.MemoDAO;

/**
 * ST42 Androidサンプル09 Room
 *
 * リスト画面表示用アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
        /**
         * リサイクラービューを表すフィールド。
         */
        private RecyclerView _rvMemo;
        /**
         * データベースオブジェクト。
         */
        private AppDatabase _db;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                _rvMemo = findViewById(R.id.rvMemo);
                LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
                _rvMemo.setLayoutManager(layout);
                DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
                _rvMemo.addItemDecoration(decoration);
        
                _db = AppDatabase.getDatabase(MainActivity.this);
        }
    
        @Override
        protected void onResume() {
                super.onResume();
                MemoDAO memoDAO = _db.createMemoDAO();
                ListenableFuture<List<Memo>> future = memoDAO.findAll();
                List<Memo> memoList = new ArrayList<>();
                try {
                        memoList = future.get();
                }
                catch(ExecutionException ex) {
                        Log.e("MainActivity", "データ取得処理失敗", ex);
                }
                catch(InterruptedException ex) {
                        Log.e("MainActivity", "データ取得処理失敗", ex);
                }
                MemoListAdapter adapter = new MemoListAdapter(memoList);
                _rvMemo.setAdapter(adapter);
        }
    
        /**
         * 新規ボタンが押されたときのイベント処理用メソッド。
         *
         * @param view 画面部品。
         */
        public void onNewButtonClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemoEditActivity.class);
                intent.putExtra("mode", Consts.MODE_INSERT);
                startActivity(intent);
        }
    
        /**
         * リサイクラービューで利用するビューホルダクラス。
         */
        private class MemoViewHolder extends RecyclerView.ViewHolder {
                /**
                 * メモタイトル表示用TextViewフィールド。
                 */
                public TextView _tvTitleRow;
        
                /**
                 * コンストラクタ。
                 *
                 * @param itemView リスト1行分の画面部品。
                 */
                public MemoViewHolder(View itemView) {
                        super(itemView);
                        _tvTitleRow = itemView.findViewById(R.id.tvTitleRow);
                }
        }
    
        /**
         * リサイクラービューで利用するアダプタクラス。
         */
        private class MemoListAdapter extends RecyclerView.Adapter<MemoViewHolder> {
                /**
                 * リストデータを表すフィールド。
                 */
                private List<Memo> _listData;
        
                /**
                 * コンストラクタ。
                 *
                 * @param listData リストデータ。
                 */
                public MemoListAdapter(List<Memo> listData) {
                        _listData = listData;
                }
        
                @Override
                public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View row = inflater.inflate(R.layout.row_activity_main, parent, false);
                        row.setOnClickListener(new ListItemClickListener());
                        MemoViewHolder holder = new MemoViewHolder(row);
                        return holder;
                }
        
                @Override
                public void onBindViewHolder(MemoViewHolder holder, int position) {
                        Memo item = _listData.get(position);
                        holder._tvTitleRow.setText(item.title);
                        holder._tvTitleRow.setTag(item.id);
                }
        
                @Override
                public int getItemCount() {
                        return _listData.size();
                }
        }
    
        /**
         * リストをタップした時の処理が記述されたメンバクラス。
         */
        private class ListItemClickListener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                        int idNo = (int) view.getTag();
                        Intent intent = new Intent(MainActivity.this, MemoEditActivity.class);
                        intent.putExtra("mode", Consts.MODE_EDIT);
                        intent.putExtra("idNo", idNo);
                        startActivity(intent);
                }
        }
}
