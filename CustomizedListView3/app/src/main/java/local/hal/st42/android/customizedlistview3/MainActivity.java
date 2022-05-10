package local.hal.st42.android.customizedlistview3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
        /**
         * 電話番号リストビュー。
         */
        private ListView _lvPhones;
        /**
         * DBヘルパーオブジェクト。
         */
        private DatabaseHelper _helper;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                _lvPhones = findViewById(R.id.lvPhones);
                _helper = new DatabaseHelper(getApplicationContext());
        
                String[] from = {"phone_type", "phone_no", "sex", "checked"};
                int[] to = {R.id.imPhoneTypeRow, R.id.tvPhoneNoRow, R.id.tvSexRow, R.id.cbPhoneCheckRow};
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.row, null, from, to, 0);
                adapter.setViewBinder(new CustomViewBinder());
                _lvPhones.setAdapter(adapter);
        }
    
        @Override
        public void onResume() {
                super.onResume();
                setNewCursor();
        }
    
        @Override
        protected void onDestroy() {
                _helper.close();
                super.onDestroy();
        }
    
        /**
         * カーソルアダプタ内のカーソルを更新するメソッド。
         */
        private void setNewCursor() {
                SQLiteDatabase db = _helper.getWritableDatabase();
                Cursor cursor = DataAccess.findAll(db);
                SimpleCursorAdapter adapter = (SimpleCursorAdapter) _lvPhones.getAdapter();
                adapter.changeCursor(cursor);
        }
    
        /**
         * リストビューのカスタムビューバインダークラス。
         */
        private class CustomViewBinder implements SimpleCursorAdapter.ViewBinder {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        int viewId = view.getId();
                        switch(viewId) {
                                case R.id.imPhoneTypeRow:
                                    ImageView imPhoneType = (ImageView) view;
                                    int phoneType = cursor.getInt(columnIndex);
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
                                    int sex = cursor.getInt(columnIndex);
                                    String sexStr = "♂";
                                    if(sex == 0) {
                                            sexStr = "♀";
                                    }
                                    tvSex.setText(sexStr);
                                    return true;
                                case R.id.cbPhoneCheckRow:
                                    int idIdx = cursor.getColumnIndex("_id");
                                    long id = cursor.getLong(idIdx);
                                    CheckBox cbPhoneCheck = (CheckBox) view;
                                    int phoneCheck = cursor.getInt(columnIndex);
                                    boolean checked = false;
                                    LinearLayout row = (LinearLayout) cbPhoneCheck.getParent();
                                    int rColor = androidx.appcompat.R.drawable.abc_list_selector_holo_light;
                                    if(phoneCheck == 1) {
                                            checked = true;
                                            rColor = androidx.appcompat.R.drawable.abc_list_selector_disabled_holo_dark;
                                    }
                                    row.setBackgroundResource(rColor);
                                    cbPhoneCheck.setChecked(checked);
                                    cbPhoneCheck.setTag(id);
                                    cbPhoneCheck.setOnClickListener(new OnCheckBoxClickListener());
                                    return true;
                        }
                        return false;
                }
        }
    
        /**
         * チェックボックスのチェック状態が変更されたときのリスナクラス。
         */
        private class OnCheckBoxClickListener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                        CheckBox cbPhoneCheck = (CheckBox) view;
                        boolean isChecked = cbPhoneCheck.isChecked();
                        long id = (Long) cbPhoneCheck.getTag();
                        SQLiteDatabase db = _helper.getWritableDatabase();
                        DataAccess.changePhoneChecked(db, id, isChecked);
                        setNewCursor();
                }
        }
}
