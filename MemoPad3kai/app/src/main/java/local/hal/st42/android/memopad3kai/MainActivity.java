package local.hal.st42.android.memopad3kai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import local.hal.st42.android.memopad3kai.dataaccess.Memo;
import local.hal.st42.android.memopad3kai.viewmodel.MainViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ST42 Androidサンプル14 メモ帳3ViewModel+LiveData版
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
         * 重要メモ情報リストのみに絞り込むかどうかを表すフィールド。trueの場合は絞り込む。
         */
        private boolean _onlyImportant = false;
        /**
         * メインアクティビティ用ビューモデルオブジェクト。
         */
        private MainViewModel _mainViewModel;
        /**
         * メモ情報リストLiveDataオブジェクト。
         */
        private LiveData<List<Memo>> _memoListLiveData;
        /**
         * メモ情報リストLiveData変更時に対応するオブザーバーオブジェクト。
         */
        private MemoListObserver _memoListObserver;
    
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
                _memoListObserver = new MemoListObserver();
                _memoListLiveData = new MutableLiveData<>();
        
                createRecyclerView();
        }
    
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_options_list, menu);
                return true;
        }
    
        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
                MenuItem menuTitle = menu.findItem(R.id.menuTitle);
                menuTitle.setTitle(R.string.menu_list_all);
                if(_onlyImportant) {
                        menuTitle.setTitle(R.string.menu_list_important);
                }
                return super.onPrepareOptionsMenu(menu);
        }
    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                boolean returnVal = true;
                switch(id) {
                        case R.id.menuListImportant:
                            _onlyImportant = true;
            
                            break;
                        case R.id.menuListAll:
                            _onlyImportant = false;
                            break;
                        default:
                            returnVal = super.onOptionsItemSelected(item);
                }
                if(returnVal) {
                        invalidateOptionsMenu();
                        createRecyclerView();
                }
                return returnVal;
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
         * リスト画面表示用のデータを生成するメソッド。
         * フィールド_onlyImportantの値に合わせて生成するデータを切り替える。
         */
        private void createRecyclerView() {
                _memoListLiveData.removeObserver(_memoListObserver);
                _memoListLiveData = _mainViewModel.getMemoList(_onlyImportant);
                _memoListLiveData.observe(MainActivity.this, _memoListObserver);
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
                 * 重要マーク表示用ImageViewフィールド。
                 */
                public ImageView _imStarRow;
        
                /**
                 * コンストラクタ。
                 *
                 * @param itemView リスト1行分の画面部品。
                 */
                public MemoViewHolder(View itemView) {
                        super(itemView);
                        _tvTitleRow = itemView.findViewById(R.id.tvTitleRow);
                        _imStarRow = itemView.findViewById(R.id.imStarRow);
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
                        holder._imStarRow.setVisibility(View.INVISIBLE);
                        if(item.getImportantBool()) {
                                holder._imStarRow.setVisibility(View.VISIBLE);
                        }
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
