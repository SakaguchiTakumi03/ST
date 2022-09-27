package local.hal.st42.android.originalapp90727;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.DataFormatException;

import local.hal.st42.android.originalapp90727.dataaccess.Books;
import local.hal.st42.android.originalapp90727.util.ConvertList;
import local.hal.st42.android.originalapp90727.viewmodel.EditViewModel;

import static local.hal.st42.android.originalapp90727.Consts.*;

public class EditActivity extends AppCompatActivity {
    private int _mode = MODE_INSERT;
    private long _idNo = 0;

    private long longTimeInMillis = System.currentTimeMillis();

    private EditViewModel _editViewModel;

    private String strNowDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode", MODE_INSERT);

        ViewModelProvider provider = new ViewModelProvider(EditActivity.this);
        _editViewModel = provider.get(EditViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarToDoEdit);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView tvTitleEdit = findViewById(R.id.tvTitleEdit);
        tvTitleEdit.setText(R.string.tv_title_edit);

        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        strNowDate = nowDate.format(formatter);

        TextView tvClickDate = findViewById(R.id.tvClickDate);
        tvClickDate.setText(strNowDate);

        Button sBookmark = findViewById(R.id.switchBookmark);
        sBookmark.setText(R.string.switch_button_text);

        ConvertList cList = new ConvertList();

        if(_mode == MODE_INSERT){
//            insertの処理
            sBookmark.setEnabled(false);
        }else{
//            editの処理
            _idNo = intent.getLongExtra("idNo",0);

            Books books = _editViewModel.getBooks((int) _idNo);

            EditText etInputTitle = findViewById(R.id.etInputTitle);
            etInputTitle.setText(books.title);

            EditText etInputArtist = findViewById(R.id.etInputArtist);
            etInputArtist.setText(books.artist);

            EditText etInputNote = findViewById(R.id.etInputNote);
            etInputNote.setText(books.note);

            tvClickDate.setText(cList.DateToString(books.purchaseDate));

            sBookmark.setEnabled(true);
            Switch bookmark = findViewById(R.id.switchBookmark);
            if(books.bookmark == 1){
                bookmark.setChecked(true);
            }else{
                bookmark.setChecked(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if(_mode == MODE_INSERT){
            inflater.inflate(R.menu.option_create_menu,menu);
        }else{
            inflater.inflate(R.menu.option_edit_menu,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menuSave:
                EditText etInputTitle = findViewById(R.id.etInputTitle);
                String inputTitle = etInputTitle.getText().toString();
                EditText etInputArtist = findViewById(R.id.etInputArtist);
                String inputArtist = etInputArtist.getText().toString();
                EditText etInputNote = findViewById(R.id.etInputNote);
                String inputNote = etInputNote.getText().toString();
                Switch bSwitch = findViewById(R.id.switchBookmark);

                if(inputTitle.equals("")){
                    Toast.makeText(EditActivity.this,"", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    ConvertList cList = new ConvertList();
                    Books books = new Books();
                    books.title = inputTitle;
                    books.artist = inputArtist;
                    books.note = inputNote;
                    if(bSwitch.isChecked() == true){
                        books.bookmark = 1;
                    }else{
                        books.bookmark = 0;
                    }
                    TextView tvClickDate = findViewById(R.id.tvClickDate);
                    books.purchaseDate = cList.StringToDate(tvClickDate.getText().toString());
                    long result = 0;
                    if(_mode == MODE_INSERT){
                        books.registrationDate = cList.StringToDate(strNowDate);
                        books.updateDate = null;
                        result = _editViewModel.insert(books);
                        Log.d("future_tag","_insert");
                    }else{
                        books.updateDate = cList.StringToDate(strNowDate);
                        result = _editViewModel.update(books);
                    }
                    if(result <= 0){
                        Toast.makeText(EditActivity.this, "内容",Toast.LENGTH_SHORT).show();
                        Log.e("errorLog","insertまたはupdateにてエラー");
                    }else {
                        finish();
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    //tvDateのonClickメソッド
    public void tvClickDate(View view) {
        TextView tvDate = findViewById(R.id.tvClickDate);
        String strDate = tvDate.getText().toString();
        strDate = strDate.replace("年","");
        strDate = strDate.replace("月","");
        strDate = strDate.replace("日","");

        int year =  Integer.parseInt(strDate.substring(0,4));
        int month  = Integer.parseInt(strDate.substring(4,6));
        int dayOfMonth = Integer.parseInt(strDate.substring(6,8));

        DatePickerDialog dateDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialogDateSetListener(), year, month, dayOfMonth);
        dateDialog.show();
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate dt = LocalDate.of(year,month,dayOfMonth);

            //Viewに表示する処理
            TextView tvClickDate = findViewById(R.id.tvClickDate);
            ConvertList cList = new ConvertList();
            tvClickDate.setText(cList.LocalDateToString(dt));
        }
    }
}