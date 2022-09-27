package local.hal.st42.android.originalapp90727;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.InvalidationTracker;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import local.hal.st42.android.originalapp90727.dataaccess.Books;
import local.hal.st42.android.originalapp90727.util.ConvertList;
import local.hal.st42.android.originalapp90727.viewmodel.DetailViewModel;

import static local.hal.st42.android.originalapp90727.Consts.MODE_DETAIL;
import static local.hal.st42.android.originalapp90727.Consts.MODE_EDIT;

public class DetailActivity extends AppCompatActivity {
    private int _mode = MODE_DETAIL;
    private  long _idNo = 0;
    private String titleName = "";
    private DetailViewModel _detailViewModel;
    private LiveData<Books> _liveData;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode",MODE_DETAIL);
        _idNo = intent.getLongExtra("idNo",0);

        ViewModelProvider provider = new ViewModelProvider(DetailActivity.this);
        _detailViewModel = provider.get(DetailViewModel.class);

        _liveData = _detailViewModel.getBooks((int) _idNo);

        DetailObserver observer = new DetailObserver();
        _liveData.observe(DetailActivity.this,observer);

        TextView tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailTitle.setText(R.string.tv_detail_title);

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menuEdit:
                Intent intent =  new Intent(DetailActivity.this,EditActivity.class);
                intent.putExtra("mode",MODE_EDIT);
                intent.putExtra("idNo",_idNo);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class DetailObserver implements Observer<Books>{
        @Override
        public void onChanged(Books books){
            ConvertList cList = new ConvertList();

            //取得のコーナー
            _idNo = books.id;
            TextView tvDetailId = findViewById(R.id.tvIdValue);
            tvDetailId.setText(Integer.toString(books.id));
            TextView tvTitle = findViewById(R.id.tvDetailTitleValue);
            tvTitle.setText(books.title);
            TextView tvArtist = findViewById(R.id.tvArtistValue);
            tvArtist.setText(books.artist);
            TextView tvBookmark = findViewById(R.id.tvDetailBookmarkValue);
            if(books.bookmark == 1){
                tvBookmark.setText("ブックマークしてるよ");
            }else{
                tvBookmark.setText("ブックマークしてないよ");
            }
            TextView tvPurchase = findViewById(R.id.tvDetailPurchaseDateValue);
            tvPurchase.setText(cList.DateToString(books.purchaseDate,"yyyy年MM月dd日 hh:mm:ss"));
            TextView tvRegistration = findViewById(R.id.tvDetailRegistrationDateValue);
            tvRegistration.setText(cList.DateToString(books.registrationDate,"yyyy年MM月dd日 hh:mm:ss"));
            TextView tvUpdate = findViewById(R.id.tvDetailUpdateDateValue);
            if(books.updateDate == null){
                tvUpdate.setText("更新されてないよ");
            }else{
                tvUpdate.setText(cList.DateToString(books.updateDate,"yyyy年MM月dd日 hh:mm:ss"));
            }
            TextView tvNote = findViewById(R.id.tvDetailNoteValue);
            tvNote.setText(books.note);
        }
    }
}
