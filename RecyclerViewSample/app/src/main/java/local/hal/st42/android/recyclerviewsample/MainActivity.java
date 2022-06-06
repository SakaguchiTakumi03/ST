package local.hal.st42.android.recyclerviewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ST42 Androidサンプル08 リサイクラービュー
 *
 * 画面表示用アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
        /**
         * リサイクラービューを表すフィールド。
         */
        private RecyclerView _rvSukiya;
        /**
         * メニューリストの種類を表すフィールド。
         */
        private int _menuCategory;
        /**
         * 牛丼を表す定数フィールド。
         */
        private static final int DON = 1;
        /**
         * カレーを表す定数フィールド。
         */
        private static final int CURRY = 2;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
                toolbarLayout.setTitle(getString(R.string.app_name));
                toolbarLayout.setExpandedTitleColor(Color.WHITE);
                toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);
        
                _menuCategory = DON;
        
                _rvSukiya = findViewById(R.id.rvSukiya);
                LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
                _rvSukiya.setLayoutManager(layout);
                DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
                _rvSukiya.addItemDecoration(decoration);
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
                MenuItem menuListOptionTitle = menu.findItem(R.id.menuListOptionTitle);
                switch(_menuCategory) {
                        case DON:
                            menuListOptionTitle.setTitle(R.string.menu_list_don);
                            break;
                        case CURRY:
                            menuListOptionTitle.setTitle(R.string.menu_list_curry);
                            break;
                }
                return super.onPrepareOptionsMenu(menu);
        }
    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                boolean returnVal = true;
                int itemId = item.getItemId();
                switch(itemId) {
                        case R.id.menuListOptionDon:
                            _menuCategory = DON;
                            break;
                        case R.id.menuListOptionCurry:
                            _menuCategory = CURRY;
                            break;
                        default:
                            returnVal = super.onOptionsItemSelected(item);
                            break;
                }
                if(returnVal) {
                        createRecyclerView();
                        invalidateOptionsMenu();
                }
                return returnVal;
        }
    
        /**
         * リストビューを表示させるメソッド。
         */
        private void createRecyclerView() {
                List<SukiyaMenu> menuList;
                switch(_menuCategory) {
                        case CURRY:
                            menuList = createCurryList();
                            break;
                        default:
                            menuList = createDonList();
                            break;
                }
                SukiyaListAdapter adapter = new SukiyaListAdapter(menuList);
                _rvSukiya.setAdapter(adapter);
        }
    
        /**
         * リストビューに表示させる牛丼リストデータを生成するメソッド。
         *
         * @return 生成された牛丼リストデータ。
         */
        private List<SukiyaMenu> createDonList() {
                List<SukiyaMenu> list = new ArrayList<>();
        
                SukiyaMenu menu = new SukiyaMenu();
                menu.setName("牛丼");
                menu.setPrice(250);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("おろしポン酢牛丼");
                menu.setPrice(370);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("山かけオクラ牛丼");
                menu.setPrice(380);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("ねぎ玉牛丼");
                menu.setPrice(370);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("とろ～り3種のチーズ牛丼");
                menu.setPrice(390);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("コクみそ野菜牛丼");
                menu.setPrice(420);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("キムチ牛丼");
                menu.setPrice(350);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("高菜明太マヨ牛丼");
                menu.setPrice(370);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("チャプチェ牛丼");
                menu.setPrice(400);
                list.add(menu);
        
                return list;
        }
    
        /**
         * リストビューに表示させるカレーリストデータを生成するメソッド。
         *
         * @return 生成されたカレーリストデータ。
         */
        private List<SukiyaMenu> createCurryList() {
                List<SukiyaMenu> list = new ArrayList<>();
        
                SukiyaMenu menu = new SukiyaMenu();
                menu.setName("牛あいがけカレー");
                menu.setPrice(520);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("旨ポークカレー");
                menu.setPrice(420);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("ハンバーグカレー");
                menu.setPrice(620);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("とろ～りチーズカレー");
                menu.setPrice(560);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("とろ～りチーズハンバーグカレー");
                menu.setPrice(760);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("おんたまカレー");
                menu.setPrice(480);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("おんたま牛あいがけカレー");
                menu.setPrice(580);
                list.add(menu);
                menu = new SukiyaMenu();
                menu.setName("からあげカレー");
                menu.setPrice(540);
                list.add(menu);
        
                return list;
        }
    
        /**
         * リサイクラービューで利用するビューホルダクラス。
         */
        private class SukiyaViewHolder extends RecyclerView.ViewHolder {
                /**
                 * メニュー名表示用TextViewフィールド。
                 */
                public TextView _tvMenuNameRow;
                /**
                 * 金額表示用TextViewフィールド。
                 */
                public TextView _tvMenuPriceRow;
        
                /**
                 * コンストラクタ。
                 *
                 * @param itemView リスト1行分の画面部品。
                 */
                public SukiyaViewHolder(View itemView) {
                        super(itemView);
                        _tvMenuNameRow = itemView.findViewById(R.id.tvMenuNameRow);
                        _tvMenuPriceRow = itemView.findViewById(R.id.tvMenuPriceRow);
                }
        }
    
        /**
         * リサイクラービューで利用するアダプタクラス。
         */
        private class SukiyaListAdapter extends RecyclerView.Adapter<SukiyaViewHolder> {
                /**
                 * リストデータを表すフィールド。
                 */
                private List<SukiyaMenu> _listData;
        
                /**
                 * コンストラクタ。
                 *
                 * @param listData リストデータ。
                 */
                public SukiyaListAdapter(List<SukiyaMenu> listData) {
                        _listData = listData;
                }
        
                @Override
                public SukiyaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View row = inflater.inflate(R.layout.row_activity_main, parent, false);
                        row.setOnClickListener(new ListItemClickListener());
                        SukiyaViewHolder holder = new SukiyaViewHolder(row);
                        return holder;
                }
        
                @Override
                public void onBindViewHolder(SukiyaViewHolder holder, int position) {
                        SukiyaMenu item = _listData.get(position);
                        String menuPriceStr = String.valueOf(item.getPrice());
                        holder._tvMenuNameRow.setText(item.getName());
                        holder._tvMenuPriceRow.setText(menuPriceStr);
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
                        TextView tvMenuNameRow = view.findViewById(R.id.tvMenuNameRow);
                        TextView tvMenuPriceRow = view.findViewById(R.id.tvMenuPriceRow);
                        String name = tvMenuNameRow.getText().toString();
                        String price = tvMenuPriceRow.getText().toString();
                        Bundle extras = new Bundle();
                        extras.putString("name", name);
                        extras.putString("price", price);
                        OrderConfirmDialog dialog = new OrderConfirmDialog();
                        dialog.setArguments(extras);
                        FragmentManager manager = getSupportFragmentManager();
                        dialog.show(manager, "OrderConfirmDialog");
                }
        }
}
