package local.hal.st42.android.customizedlistview1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ST42 Androidサンプル02 リストビュー各行のカスタマイズ
 *
 * 画面表示用アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
        /**
         * リストビューに表示させるリストデータ。
         */
        private List<Map<String, Object>> _list;
        /**
         * リストビューを表すフィールド。
         */
        private ListView _lvSukiya;
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
        
                _menuCategory = DON;
                _lvSukiya = findViewById(R.id.lvSukiya);
                _list = createDonList();
        
                createListView();
                _lvSukiya.setOnItemClickListener(new ListItemClickListener());
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
                        createListView();
                        invalidateOptionsMenu();
                }
                return returnVal;
        }
    
        /**
         * リストビューを表示させるメソッド。
         */
        private void createListView() {
                switch(_menuCategory) {
                        case CURRY:
                            _list = createCurryList();
                            break;
                        default:
                            _list = createDonList();
                            break;
                }
        
                String[] from = {"name", "price"};
                int[] to = {R.id.tvMenuNameRow, R.id.tvMenuPriceRow};
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _list, R.layout.row, from, to);
                _lvSukiya.setAdapter(adapter);
        }
    
        /**
         * リストビューに表示させる牛丼リストデータを生成するメソッド。
         *
         * @return 生成された牛丼リストデータ。
         */
        private List<Map<String, Object>> createDonList() {
                List<Map<String, Object>> list = new ArrayList<>();
        
                Map<String, Object> map = new HashMap<>();
                map.put("name", "牛丼");
                map.put("price", 250);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "おろしポン酢牛丼");
                map.put("price", 370);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "山かけオクラ牛丼");
                map.put("price", 380);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "ねぎ玉牛丼");
                map.put("price", 370);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "とろ～り3種のチーズ牛丼");
                map.put("price", 390);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "コクみそ野菜牛丼");
                map.put("price", 420);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "キムチ牛丼");
                map.put("price", 350);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "高菜明太マヨ牛丼");
                map.put("price", 370);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "チャプチェ牛丼");
                map.put("price", 400);
                list.add(map);
        
                return list;
        }
    
        /**
         * リストビューに表示させるカレーリストデータを生成するメソッド。
         *
         * @return 生成されたカレーリストデータ。
         */
        private List<Map<String, Object>> createCurryList() {
                List<Map<String, Object>> list = new ArrayList<>();
        
                Map<String, Object> map = new HashMap<>();
                map.put("name", "牛あいがけカレー");
                map.put("price", 520);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "旨ポークカレー");
                map.put("price", 420);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "ハンバーグカレー");
                map.put("price", 620);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "とろ～りチーズカレー");
                map.put("price", 560);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "とろ～りチーズハンバーグカレー");
                map.put("price", 760);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "おんたまカレー");
                map.put("price", 480);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "おんたま牛あいがけカレー");
                map.put("price", 580);
                list.add(map);
                map = new HashMap<>();
                map.put("name", "からあげカレー");
                map.put("price", 540);
                list.add(map);
        
                return list;
        }
    
        /**
         * リストをタップした時の処理が記述されたメンバクラス。
         */
        private class ListItemClickListener implements AdapterView.OnItemClickListener {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Map<String, Object> item = _list.get(position);
                        String name = (String) item.get("name");
                        Integer price = (Integer) item.get("price");
                        Bundle extras = new Bundle();
                        extras.putString("name", name);
                        extras.putInt("price", price);
                        OrderConfirmDialog dialog = new OrderConfirmDialog();
                        dialog.setArguments(extras);
                        FragmentManager manager = getSupportFragmentManager();
                        dialog.show(manager, "OrderConfirmDialog");
                }
        }
}
