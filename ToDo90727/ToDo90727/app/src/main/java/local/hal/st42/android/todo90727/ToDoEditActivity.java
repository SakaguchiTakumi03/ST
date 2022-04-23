package local.hal.st42.android.todo90727;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ToDoEditActivity extends AppCompatActivity {
    private int _mode = MainActivity.MODE_INSERT;
    private long _idNo = 0;
    private DatabaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_edit);

        _helper = new DatabaseHelper(ShopEditActivity.this);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode",MainActivity.MODE_INSERT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(_mode == MainActivity.MODE_INSERT){
            TextView tvTitleEdit = findViewById(R.id.tvTitleEdit);
            tvTitleEdit.setText(R.string.tv_title_insert);
        }else{
            _idNo = intent.getLongExtra("idNo",0);
            SQLiteDatabase db = _helper.getWritableDatabase();
            Shops shopsData = DataAccess.findByPK(db,_idNo);

            EditText etInputName = findViewById(R.id.etInputName);
            etInputName.setText(shopsData.getName());

            EditText etInputTell = findViewById(R.id.etInputTel);
            etInputTell.setText(shopsData.getTel());

            EditText etInputUrl = findViewById(R.id.etInputUrl);
            etInputUrl.setText(shopsData.getUrl());

            EditText etInputNote = findViewById(R.id.etInputNote);
            etInputNote.setText(shopsData.getNote());
        }
    }

}
