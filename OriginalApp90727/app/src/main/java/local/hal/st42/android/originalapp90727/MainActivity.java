package local.hal.st42.android.originalapp90727;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import local.hal.st42.android.originalapp90727.dataaccess.Books;
import local.hal.st42.android.originalapp90727.viewmodel.MainViewModel;

import static local.hal.st42.android.originalapp90727.Consts.*;

public class MainActivity extends AppCompatActivity {

    private int _menuCategory;
    private int _menuName;

    private static final String PREFS_NAME = "PSPrefsFile";

    private static final String MENU_NAME = "MNPrefsFile";

    private static final int DEFAULT_SELECT = 1;

    private String titleName = "";

    private RecyclerView _rvOriginApp;

    private OriginalAppListAdapter _adapter;

    private MainViewModel _mainViewModel;

    private BooksListObserver _booksListObserver;

    private LiveData<List<Books>> _booksListLiveData;

    private MenuInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _menuCategory = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).getInt("selectedMenu",DEFAULT_SELECT);
        _menuName = getSharedPreferences(MENU_NAME,MODE_PRIVATE).getInt("selectedMenuName",DEFAULT_SELECT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        titleName = "蔵書管理アプリ";
        toolbarLayout.setTitle(titleName);
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

        _rvOriginApp = findViewById(R.id.rvOriginApp);
        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        _rvOriginApp.setLayoutManager(layout);
        DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
        _rvOriginApp.addItemDecoration(decoration);

        List<Books> booksList = new ArrayList<>();
        _adapter = new OriginalAppListAdapter(booksList);
        _rvOriginApp.setAdapter(_adapter);

        ViewModelProvider provider = new ViewModelProvider(MainActivity.this);
        _mainViewModel = provider.get(MainViewModel.class);
        _booksListObserver = new BooksListObserver();
        _booksListLiveData = new MutableLiveData<>();

        createRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("mode",MODE_INSERT);
                startActivity(intent);
            }
        });
    }

    private class OriginalAppViewHolder extends RecyclerView.ViewHolder{
        public TextView _tvTitleRow;
        public TextView _tvArtistRow;

        public OriginalAppViewHolder(View itemView){
            super(itemView);
            _tvTitleRow = itemView.findViewById(R.id.tvTitleRow);
            _tvArtistRow = itemView.findViewById(R.id.tvArtistRow);
        }
    }

    private class OriginalAppListAdapter extends RecyclerView.Adapter<OriginalAppViewHolder> {
        private List<Books> _listData;

        public OriginalAppListAdapter(List<Books> listData) {
            _listData = listData;
        }

        public void changeBooksList(List<Books> listData) {
            _listData = listData;
            notifyDataSetChanged();
        }

        @Override
        public OriginalAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View row = inflater.inflate(R.layout.row, parent, false);
            OriginalAppViewHolder holder = new OriginalAppViewHolder(row);
            return holder;
        }

        @Override
        public void onBindViewHolder(OriginalAppViewHolder holder, int position) {
            Books item = _listData.get(position);

            holder.itemView.setOnClickListener(new ListItemClickListener(item.id));
            holder._tvTitleRow.setText("タイトル："+item.title);
            String artist = "";
            if(item.artist.equals("")){
                artist = "作者不定";
            }else{
                artist = item.artist;
            }
            holder._tvArtistRow.setText("作者："+artist);
        }

        @Override
        public int getItemCount(){
            return _listData.size();
        }
    }

    private class BooksListObserver implements Observer<List<Books>> {
        @Override
        public void onChanged(List<Books> booksList) {
            _adapter.changeBooksList(booksList);
        }
    }

    private class ListItemClickListener implements View.OnClickListener{
        private long _id;

        public ListItemClickListener(long id){
            _id = id;
        }

        @Override
        public void onClick(View view){
            Intent intent = new Intent(MainActivity.this,DetailActivity.class);
            intent.putExtra("mode",MODE_DETAIL);
            intent.putExtra("idNo",_id);
            startActivity(intent);
        }
    }

    private void createRecyclerView(){
        _booksListLiveData.removeObserver(_booksListObserver);
        _booksListLiveData = _mainViewModel.getBooksList(_menuCategory);
        _booksListLiveData.observe(MainActivity.this,_booksListObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences settingsMenu = getSharedPreferences(MENU_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        SharedPreferences.Editor editorMenu = settingsMenu.edit();
        boolean returnVal = true;

        switch (item.getItemId()){
            case R.id.menuTitleAsc:
                _menuCategory = TITLE_ASC;
                editor.putInt("selectedMenu",TITLE_ASC);
                break;
            case R.id.menuTitleDesc:
                _menuCategory = TITLE_DESC;
                editor.putInt("selectedMenu",TITLE_DESC);
                break;
            case R.id.menuArtistAsc:
                _menuCategory = ARTIST_ASC;
                editor.putInt("selectedMenu",ARTIST_ASC);
                break;
            case R.id.menuArtistDesc:
                _menuCategory = ARTIST_DESC;
                editor.putInt("selectedMenu",ARTIST_DESC);
                break;
            case R.id.menuBookmarkList:
                if(_menuName == 0){
                    _menuCategory = TITLE_ASC;
                    _menuName = 1;
                    editor.putInt("selectedMenu",TITLE_ASC);
                }else{
                    _menuCategory = BOOKMARK;
                    _menuName = 0;
                    editor.putInt("selectedMenu",BOOKMARK);
                }
                editorMenu.putInt("selectedMenuName",_menuName);
                break;
            default:
                returnVal = super.onOptionsItemSelected(item);
                break;
        }
        editor.apply();
        editorMenu.apply();
        if(returnVal){
            createRecyclerView();
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem menuOptionTitle = menu.findItem(R.id.selectMenuInfo);
        switch (_menuCategory){
            case TITLE_ASC:
                menuOptionTitle.setTitle("タイトル昇順");
                break;
            case TITLE_DESC:
                menuOptionTitle.setTitle("タイトル降順");
                break;
            case ARTIST_ASC:
                menuOptionTitle.setTitle("作者昇順");
                break;
            case ARTIST_DESC:
                menuOptionTitle.setTitle("作者降順");
                break;
            case BOOKMARK:
                menuOptionTitle.setTitle("ブックマーク");
                break;
        }
        Log.d("selectedMenu",Integer.toString(_menuCategory)+"+_menuCategory");
        Log.d("selectedMenu",Integer.toString(_menuName)+"+_menuName");
        return super.onPrepareOptionsMenu(menu);
    }
}