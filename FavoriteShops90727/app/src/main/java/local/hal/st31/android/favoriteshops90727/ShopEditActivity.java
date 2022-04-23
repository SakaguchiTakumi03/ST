package local.hal.st31.android.favoriteshops90727;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;

public class ShopEditActivity extends AppCompatActivity {

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

    @Override
    protected void onDestroy(){
        _helper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(_mode == MainActivity.MODE_INSERT){
            inflater.inflate(R.menu.option_shop_edit_menu, menu);
        }else{
            inflater.inflate(R.menu.option_shop_create_menu, menu);
        }
        return true;
    }

    //処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //アクションバーに戻るボタンを追加
            case android.R.id.home:
                finish();
            return true;
            //各アクションボタン押下時の処理内容
            case R.id.menuSave:
                EditText etInputName = findViewById(R.id.etInputName);
                String inputName = etInputName.getText().toString();
                if(inputName.equals("")){
                    Toast.makeText(ShopEditActivity.this,R.string.msg_input_title,Toast.LENGTH_SHORT).show();
                }else{
                    EditText etInputTel = findViewById(R.id.etInputTel);
                    String inputTel = etInputTel.getText().toString();
                    EditText etInputUrl = findViewById(R.id.etInputUrl);
                    String inputUrl = etInputUrl.getText().toString();
                    EditText etInputNote = findViewById(R.id.etInputNote);
                    String inputNote = etInputNote.getText().toString();
                    SQLiteDatabase db = _helper.getWritableDatabase();
                    if(_mode == MainActivity.MODE_INSERT) {
                        DataAccess.insert(db, inputName, inputTel,inputUrl, inputNote);
                    } else {
                        DataAccess.update(db, _idNo, inputName, inputTel,inputUrl, inputNote);
                    }
                    finish();
                }
            return true;
            case R.id.menuDelete:
                DialogFragment dialog = new DialogFragment(_helper,_idNo);
                FragmentManager manager = getSupportFragmentManager();
                dialog.show(manager,"DialogFragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
