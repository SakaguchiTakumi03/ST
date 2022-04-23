package local.hal.st31.android.saigoku33memo90727;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TempleEditActivity extends AppCompatActivity {
    private int _selectedTempleNo = 0;

    private String _selectedTempleName = "";

    private DatabaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_edit);

        Intent intent = getIntent();
        _selectedTempleNo = intent.getIntExtra("selectedTempleNo", 0);
        _selectedTempleName = intent.getStringExtra("selectedTempleName");

        _helper = new DatabaseHelper(getApplicationContext());

        TextView tvTemple = findViewById(R.id.tvTemple);
        tvTemple.setText(_selectedTempleName);

        SQLiteDatabase db = _helper.getWritableDatabase();
        Memo memo = DataAccess.findByPK(db, _selectedTempleNo);
        if(memo != null) {
            String honzon = memo.getHonzon();
            EditText etHonzon = findViewById(R.id.etHonzon);
            etHonzon.setText(honzon);

            String shushi = memo.getShushi();
            EditText etShushi = findViewById(R.id.etShushi);
            etShushi.setText(shushi);

            String address = memo.getAddress();
            EditText etAddress = findViewById(R.id.etAddress);
            etAddress.setText(address);

            String url = memo.getURL();
            EditText etURL = findViewById(R.id.etURL);
            etURL.setText(url);

            String note = memo.getNote();
            EditText etNote = findViewById(R.id.etNote);
            etNote.setText(note);
        }
    }

    @Override
    protected void onDestroy() {
        _helper.close();
        super.onDestroy();
    }

    /**
     * 保存ボタンがタップされた時の処理メソッド。
     * @param view タップされた画面部品。
     */
    public void onSaveButtonClick(View view) {
        EditText etHonzon = findViewById(R.id.etHonzon);
        String honzon = etHonzon.getText().toString();

        EditText etShushi = findViewById(R.id.etShushi);
        String shushi = etShushi.getText().toString();

        EditText etAddress = findViewById(R.id.etAddress);
        String address = etAddress.getText().toString();

        EditText etURL = findViewById(R.id.etURL);
        String url = etURL.getText().toString();

        EditText etNote = findViewById(R.id.etNote);
        String note = etNote.getText().toString();

        SQLiteDatabase db = _helper.getWritableDatabase();
        boolean exist = DataAccess.findRowByPK(db, _selectedTempleNo);
        if(exist) {
            DataAccess.update(db, _selectedTempleNo, honzon,shushi,address,url,note);

        }
        else {
            DataAccess.insert(db, _selectedTempleNo, _selectedTempleName, honzon,shushi,address,url,note);
//            insert(SQLiteDatabase db, long selectedTempleNo, String selectedTempleName, String honzon, String shushi, String address, String url, String note)
        }
        finish();
    }
}
