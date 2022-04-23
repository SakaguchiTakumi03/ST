package local.hal.st42.android.todo90727;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    static final int MODE_INSERT = 1;
    static final int MODE_EDIT = 2;

    private ListView _lvToDoList;

    private DatabaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _lvToDoList = findViewById(R.id.lvToDoList);
        _lvToDoList.setOnItemClickListener(new ListItemClickListener());

        _helper = new DatabaseHelper(MainActivity.this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        SQLiteDatabase db = _helper.getWritableDatabase();
        Cursor cursor = DataAccess.findAll(db);
        String[] from = {"name"};
        int[] to = { android.R.id.text1 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_1,cursor,from,to,0);
        _lvToDoList.setAdapter(adapter);
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
        Intent intent = new Intent(MainActivity.this,ToDoEditActivity.class);
//        Intent intent = new Intent(getApplication(),ToDoEditActivity.class);
        intent.putExtra("mode",MODE_INSERT);
        startActivity(intent);
        Log.d("debug","MainActivity_menuButton");
        return super.onOptionsItemSelected(item);
//        return true;
    }

    @UiThread
    private void receiveToDoList(){

    }

}