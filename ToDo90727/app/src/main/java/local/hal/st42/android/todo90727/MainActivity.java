package local.hal.st42.android.todo90727;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int MODE_INSERT = 1;
    static final int MODE_EDIT = 2;

    private ListView _lvToDoList;

    private int _menuCategory;

    private static final int ALL = 1;
    private static final int FINISH = 2;
    private static final int UNFINISH = 3;

    private static final String PREFS_NAME = "PSPrefsFile";

    private static final int DEFAULT_SELECT = 1;

    private DatabaseHelper _helper;

    private String titleName = "";

    private RecyclerView _rvToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _menuCategory = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).getInt("selectedMenu",DEFAULT_SELECT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        titleName = "ToDoリスト";
        toolbarLayout.setTitle(titleName);
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

//        _lvToDoList = findViewById(R.id.lvToDoList);
//        _lvToDoList.setOnItemClickListener(new ListItemClickListener());

        _helper = new DatabaseHelper(MainActivity.this);

        _rvToDo = findViewById(R.id.rvToDo);
        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        _rvToDo.setLayoutManager(layout);
        DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
        _rvToDo.addItemDecoration(decoration);
        createRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ToDoEditActivity.class);
                intent.putExtra("mode",MODE_INSERT);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
//        createListView();
    }

    private void setNewCursor(){
        SQLiteDatabase db = _helper.getWritableDatabase();
        Cursor cursor = null;
        switch (_menuCategory){
            case ALL:
                cursor = DataAccess.findAll(db);
                break;
            case FINISH:
                cursor = DataAccess.findFinished(db);
                break;
            case UNFINISH:
                cursor = DataAccess.findUnFinished(db);
                break;
        }
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) _lvToDoList.getAdapter();
        adapter.changeCursor(cursor);
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Cursor item = (Cursor) parent.getItemAtPosition(position);
            int idxId = item.getColumnIndex("_id");
            long idNo = item.getLong(idxId);

            Intent intent = new Intent(MainActivity.this, ToDoEditActivity.class);
            intent.putExtra("mode",MODE_EDIT);
            intent.putExtra("idNo",idNo);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        boolean returnVal = true;
        switch (item.getItemId()){
            case R.id.menuAllList:
                _menuCategory = ALL;
                editor.putInt("selectedMenu",ALL);
                break;
            case R.id.menuFinished:
                _menuCategory = FINISH;
                editor.putInt("selectedMenu",FINISH);
                break;
            case R.id.menuUnFinished:
                _menuCategory = UNFINISH;
                editor.putInt("selectedMenu",UNFINISH);
                break;
            case R.id.menuTransition:
                Intent intent = new Intent(MainActivity.this,ToDoEditActivity.class);
                intent.putExtra("mode",MODE_INSERT);
                startActivity(intent);
                break;
            default:
                returnVal = super.onOptionsItemSelected(item);
                break;
        }
        editor.apply();
        if(returnVal){
            createRecyclerView();
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem menuListOptionTitle = menu.findItem(R.id.menuListOptionTitle);
        switch (_menuCategory){
            case ALL:
                menuListOptionTitle.setTitle(R.string.menu_all_list);
                break;
            case FINISH:
                menuListOptionTitle.setTitle(R.string.menu_finish_list);
                break;
            case UNFINISH:
                menuListOptionTitle.setTitle(R.string.menu_unFinished_list);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void createRecyclerView() {
        SQLiteDatabase db = _helper.getWritableDatabase();
        List<ToDo> menuList;
        Cursor cursor = null;
        switch (_menuCategory){
            case ALL:
                cursor = DataAccess.findAll(db);
                break;
            case FINISH:
                cursor = DataAccess.findFinished(db);
                break;
            case UNFINISH:
                cursor = DataAccess.findUnFinished(db);
                break;
        }
        ToDoListAdapter adapter = new ToDoListAdapter(menuList);
        _rvToDo.setAdapter(adapter);
//        String[] from = {"name","deadline","done"};
//        int[] to = {R.id.tvNameRow,R.id.tvFixedDateRow,R.id.cbTaskCheckRow};
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.row,cursor,from,to,0);
//        adapter.setViewBinder(new CustomViewBinder());
//        _lvToDoList.setAdapter(adapter);
    }

    /**
     * リサイクラービューで利用するビューホルダクラス。
     */
    private class ToDoViewHolder extends RecyclerView.ViewHolder {
        /**
         * メニュー名表示用TextViewフィールド。
         */
        public TextView _tvMenuNameRow;
        /**
         * 金額表示用TextViewフィールド。
         */
        public TextView _tvMenuPriceRow;

        /**
         * コンストラクタ。
         *
         * @param itemView リスト1行分の画面部品。
         */
        public ToDoViewHolder(View itemView) {
            super(itemView);
            _tvMenuNameRow = itemView.findViewById(R.id.tvNameRow);
            _tvMenuPriceRow = itemView.findViewById(R.id.tvDate);
        }
    }

    /**
     * リサイクラービューで利用するアダプタクラス。
     */
    private class ToDoListAdapter extends RecyclerView.Adapter<ToDoViewHolder> {
        /**
         * リストデータを表すフィールド。
         */
        private List<ToDo> _listData;

        /**
         * コンストラクタ。
         *
         * @param listData リストデータ。
         */
        public ToDoListAdapter(List<ToDo> listData) {
            _listData = listData;
        }

        @Override
        public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View row = inflater.inflate(R.layout.row, parent, false);
            row.setOnClickListener(new ListItemClickListener());
            ToDoViewHolder holder = new ToDoViewHolder(row);
            return holder;
        }

        @Override
        public void onBindViewHolder(ToDoViewHolder holder, int position) {
            ToDo item = _listData.get(position);
//            String menuPriceStr = String.valueOf(item.getPrice());
            holder._tvMenuNameRow.setText(item.getName());
//            holder._tvMenuPriceRow.setText(menuPriceStr);
        }

        @Override
        public int getItemCount() {
            return _listData.size();
        }
    }

    private class ListItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            TextView tvMenuNameRow = view.findViewById(R.id.tvMenuNameRow);
//            TextView tvMenuPriceRow = view.findViewById(R.id.tvMenuPriceRow);
//            String name = tvMenuNameRow.getText().toString();
//            String price = tvMenuPriceRow.getText().toString();
//            Bundle extras = new Bundle();
//            extras.putString("name", name);
//            extras.putString("price", price);
//            OrderConfirmDialog dialog = new OrderConfirmDialog();
//            dialog.setArguments(extras);
//            FragmentManager manager = getSupportFragmentManager();
//            dialog.show(manager, "OrderConfirmDialog");
        }
    }

//    private class CustomViewBinder implements SimpleCursorAdapter.ViewBinder{
//        TextView tvName = null;
//        TextView tvDate = null;
//        CheckBox cbTaskCheck = null;
//        @Override
//        public boolean setViewValue(View view,Cursor cursor,int columnsIndex){
////            setNewCursor();
//            switch (view.getId()){
//                case R.id.tvNameRow:
//                    tvName = (TextView) view;
//                    String name = cursor.getString(columnsIndex);
//                    tvName.setText(name);
//                    tvName.setTextColor(Color.GRAY);
//                    TextPaint tvNamePaint = tvName.getPaint();
//                    tvNamePaint.setFlags(tvName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//                    tvNamePaint.setAntiAlias(false);
//                    return true;
//                case R.id.tvFixedDateRow:
//                    ToDoEditActivity toDoEditActivity = new ToDoEditActivity();
//                    tvDate = (TextView) view;
//                    int getDeadline = cursor.getColumnIndex("deadline");
//                    int getId = cursor.getColumnIndex("_id");
////                    int getName = cursor.getColumnIndex("name");
//                    long deadline = cursor.getLong(getDeadline);
//                    String setText = "期限： ";
//                    String tempDateStr = toDoEditActivity.dateGetTimeInMillis(deadline,"yyyy年MM月dd日");
//                    Log.d("log_date",tempDateStr+"tempDateDtr");
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//                    Date date = null;
//                    try {
//                        date = sdf.parse(tempDateStr);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    Date nowDate = new Date();
//                    Calendar nowCal = Calendar.getInstance();
//                    nowCal.setTime(nowDate);
//                    nowCal.set(Calendar.HOUR_OF_DAY,0);
//                    nowCal.set(Calendar.MINUTE,0);
//                    nowCal.set(Calendar.SECOND,0);
//                    nowCal.set(Calendar.MILLISECOND,0);
//                    nowDate = nowCal.getTime();
//                    Log.d("log_date",date.toString()+"=date");
//                    Log.d("log_date",nowDate.toString()+"=nowDate");
//                    if(date.before(nowDate)){
////                        setText += "過ぎてます！！！";
//                        setText += tempDateStr;
//                        tvDate.setText(setText);
//                        tvDate.setTextColor(Color.RED);
//                    }else if(date.equals(nowDate)){
//                        Log.d("log_date","equals");
//                        setText += "今日";
//                        tvDate.setText(setText);
//                        tvDate.setTextColor(Color.BLUE);
//                    }else{
//                        Log.d("log_date","after");
//                        setText += tempDateStr;
//                        tvDate.setText(setText);
//                    }
//
//                    Log.d("log_date",tempDateStr);
//
//                    return true;
//                case R.id.cbTaskCheckRow:
//                    int idIdx = cursor.getColumnIndex("_id");
//                    long id = cursor.getLong(idIdx);
//                    cbTaskCheck = (CheckBox) view;
//                    int taskCheck = cursor.getInt(columnsIndex);
//                    boolean checked = false;
//                    LinearLayout row = (LinearLayout) cbTaskCheck.getParent();
//                    int rColor = androidx.appcompat.R.drawable.abc_list_selector_holo_light;
//                    if(taskCheck == 1){
//                        checked = true;
//                        rColor = androidx.appcompat.R.drawable.abc_list_selector_disabled_holo_dark;
//                        tvName.setTextColor(Color.DKGRAY);
////                        TextPaint paint = tvName.getPaint();
////                        paint.setFlags(tvName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
////                        paint.setAntiAlias(true);
////                        tvDate.setText("タスク完了");
//                        tvDate.setTextColor(Color.DKGRAY);
//                    }
//                    row.setBackgroundResource(rColor);
//                    cbTaskCheck.setChecked(checked);
//                    cbTaskCheck.setTag(id);
//                    cbTaskCheck.setOnClickListener(new OnCheckBoxClickListener());
//                    return true;
//            }
//            return false;
//        }
//    }

    private class OnCheckBoxClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            CheckBox cbTaskCheck = (CheckBox) view;
            boolean isChecked = cbTaskCheck.isChecked();
            long id = (Long) cbTaskCheck.getTag();
            SQLiteDatabase db = _helper.getWritableDatabase();
            DataAccess.changeTaskChecked(db, id, isChecked);
            setNewCursor();
        }
    }
}