package local.hal.st42.android.memopad2kai;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import local.hal.st42.android.memopad2kai.dataaccess.Memo;
import local.hal.st42.android.memopad2kai.viewmodel.MainViewModel;

/**
 * ST42 Androidサンプル13 メモ帳2ViewModel+LiveData版
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
         * リサイクラービューで利用するアダプタオブジェクト。
         */
        private MemoListAdapter _adapter;
        /**
         * メインアクティビティ用ビューモデルオブジェクト。
         */
        private MainViewModel _mainViewModel;
        /**
         * メモ情報リストLiveDataオブジェクト。
         */
        private LiveData<List<Memo>> _memoListLiveData;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                _rvMemo = findViewById(R.id.rvMemo);
                LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
                _rvMemo.setLayoutManager(layout);
                DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
                _rvMemo.addItemDecoration(decoration);
                List<Memo> memoList = new ArrayList<>();
                _adapter = new MemoListAdapter(memoList);
                _rvMemo.setAdapter(_adapter);
        
                ViewModelProvider provider = new ViewModelProvider(MainActivity.this);
                _mainViewModel = provider.get(MainViewModel.class);
                _memoListLiveData = _mainViewModel.getMemoList();
                MemoListObserver memoListObserver = new MemoListObserver();
                _memoListLiveData.observe(MainActivity.this, memoListObserver);
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
         * ビューモデル中のメモ情報リストに変更があった際に、画面の更新を行う処理が記述されたクラス。
         */
        private class MemoListObserver implements Observer<List<Memo>> {
                @Override
                public void onChanged(List<Memo> memoList) {
                        _adapter.changeMemoList(memoList);
                }
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
                        holder._tvTitleRow.setTag(item.id);
                        holder._tvTitleRow.setText(item.title);
                }
        
                @Override
                public int getItemCount() {
                        return _listData.size();
                }
        
                /**
                 * 内部で保持しているリストデータを丸ごと入れ替えるメソッド。
                 *
                 * @param listData 新しいリストデータ。
                 */
                public void changeMemoList(List<Memo> listData) {
                        _listData = listData;
                        notifyDataSetChanged();
                }
        }
    
        /**
         * リストをタップした時の処理が記述されたメンバクラス。
         */
        private class ListItemClickListener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                        TextView tvTitleRow = view.findViewById(R.id.tvTitleRow);
                        int idNo = (int) tvTitleRow.getTag();
                        Intent intent = new Intent(MainActivity.this, MemoEditActivity.class);
                        intent.putExtra("mode", Consts.MODE_EDIT);
                        intent.putExtra("idNo", idNo);
                        startActivity(intent);
                }
        }
}
