package local.hal.st42.android.originalapp90727;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import local.hal.st42.android.originalapp90727.dataaccess.Tasks;
import local.hal.st42.android.originalapp90727.viewmodel.EditViewModel;

import static local.hal.st42.android.originalapp90727.Consts.*;

public class EditActivity extends AppCompatActivity {
    private int _mode = MODE_INSERT;
    private long _idNo = 0;

    private long longTimeInMillis = System.currentTimeMillis();

    private EditViewModel _editViewModel;

    private String strNowDate;

    private String dateGetTimeInMillis(long longTimeInMillis, String format){
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(format);
        ZonedDateTime zoneDate = Instant.ofEpochMilli(longTimeInMillis).atZone(ZoneId.systemDefault());
        String strDate = zoneDate.format(dtFormat);
        return strDate;
    }

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月[dd日");
        strNowDate = nowDate.format(formatter);

        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText(strNowDate);

        TextView tvNote = findViewById(R.id.tvInputContent);
        tvNote.setText(R.string.tv_input_note);

        Button button = findViewById(R.id.switchButton);
        button.setText(R.string.switch_button_text);

        if(_mode == MODE_INSERT){
//            insertの処理
            button.setEnabled(false);
        }else{
//            editの処理
            _idNo = intent.getLongExtra("idNo",0);

            Tasks tasks = _editViewModel.getTasks((int) _idNo);

            EditText etInputText = findViewById(R.id.etInputTask);
            etInputText.setText(tasks.name);

            EditText etInputNote = findViewById(R.id.etInputNote);
            etInputNote.setText(tasks.note);

            tvDate = findViewById(R.id.tvDate);
            tvDate.setText(dateGetTimeInMillis(tasks.deadline.getTime(),"yyyy年MM月[dd日"));
            longTimeInMillis = tasks.deadline.getTime();

            Switch sButton = findViewById(R.id.switchButton);
            if(tasks.done == 1){
                sButton.setChecked(true);
            }else{
                sButton.setChecked(false);
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
                EditText etInput = findViewById(R.id.etInputTask);
                String input = etInput.getText().toString();
                if(input.equals("")){
                    Toast.makeText(EditActivity.this,"", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
