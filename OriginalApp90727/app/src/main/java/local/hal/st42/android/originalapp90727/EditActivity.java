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

//    private String dateGetTimeInMillis(long longTimeInMillis, String format){
//        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(format);
//        ZonedDateTime zoneDate = Instant.ofEpochMilli(longTimeInMillis).atZone(ZoneId.systemDefault());
//        String strDate = zoneDate.format(dtFormat);
//        return strDate;
//    }

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
//
//        TextView tvNote = findViewById(R.id.tvInputContent);
//        tvNote.setText(R.string.tv_input_note);

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
                EditText etInput = findViewById(R.id.etInputTitle);
                String input = etInput.getText().toString();
                if(input.equals("")){
                    Toast.makeText(EditActivity.this,"", Toast.LENGTH_SHORT).show();
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
        int month  = Integer.parseInt(strDate.substring(4,6)) -1;
        int dayOfMonth = Integer.parseInt(strDate.substring(6,8));

        DatePickerDialog dateDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialogDateSetListener(), year, month, dayOfMonth);
        dateDialog.show();
    }

    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
//        後で変更。
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy年[]M月[]d日 HH:mm:ss").withZone(ZoneId.systemDefault());
            ZonedDateTime dt = ZonedDateTime.parse(year+"年"+(month+1)+"月"+dayOfMonth+"日 23:23:23", parseFormatter);

            Instant instant = dt.toInstant();
            Date date = Date.from(instant);

            //Viewに表示する処理
            TextView tvClickDate = findViewById(R.id.tvClickDate);
            ConvertList cList = new ConvertList();
            tvClickDate.setText(cList.DateToString(date));
        }
    }

}