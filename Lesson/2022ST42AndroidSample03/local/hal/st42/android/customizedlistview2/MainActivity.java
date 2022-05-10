package local.hal.st42.android.customizedlistview2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ST42 Androidサンプル03 データに応じたリストビューのカスタマイズ
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
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                _list = createList();
        
                String[] from = {"phoneType", "phoneNo", "sex"};
                int[] to = {R.id.imPhoneTypeRow, R.id.tvPhoneNoRow, R.id.tvSexRow};
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _list, R.layout.row, from, to);
                adapter.setViewBinder(new CustomViewBinder());
                ListView lvPhones = findViewById(R.id.lvPhones);
                lvPhones.setAdapter(adapter);
        }
    
        /**
         * リストビューに表示させるリストデータを生成するメソッド。
         *
         * @return 生成されたリストデータ。
         */
        private List<Map<String, Object>> createList() {
                List<Map<String, Object>> list = new ArrayList<>();
        
                for(int i = 1; i <= 30; i++) {
                        int phoneType = (int) (3 * Math.random()) + 1;
                        int phoneNoInt = (int) (99999999 * Math.random());
                        int sexInt = (int) (10*Math.random() + 1);
            
                        String phoneNo = "090" + String.format("%08d", phoneNoInt);
            
                        int sex = 1;
                        if(sexInt <= 5) {
                                sex = 0;
                        }
            
                        Map<String, Object> map = new HashMap<>();
                        map.put("phoneType", phoneType);
                        map.put("phoneNo", phoneNo);
                        map.put("sex", sex);
                        list.add(map);
                }
        
                return list;
        }
    
        /**
         * リストビューのカスタムビューバインダークラス。
         */
        private class CustomViewBinder implements SimpleAdapter.ViewBinder {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                        int viewId = view.getId();
                        switch(viewId) {
                                case R.id.imPhoneTypeRow:
                                    ImageView imPhoneType = (ImageView) view;
                                    int phoneType = (Integer) data;
                                    switch(phoneType) {
                                            case 2:
                                                imPhoneType.setImageResource(android.R.drawable.ic_menu_crop);
                                                break;
                                            case 3:
                                                imPhoneType.setImageResource(android.R.drawable.ic_menu_myplaces);
                                                break;
                                            default:
                                                imPhoneType.setImageResource(android.R.drawable.ic_menu_call);
                                                break;
                                    }
                                    return true;
                                case R.id.tvSexRow:
                                    TextView tvSex = (TextView) view;
                                    int phoneSex = (Integer) data;
                                    String sex = "♂";
                                    if(phoneSex == 0) {
                                            sex = "♀";
                                    }
                                    tvSex.setText(sex);
                                    return true;
                        }
                        return false;
                }
        }
}
