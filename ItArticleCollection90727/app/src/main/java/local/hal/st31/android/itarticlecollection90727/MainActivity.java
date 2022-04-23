package local.hal.st31.android.itarticlecollection90727;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ReceiveArticl";

    private static final String ACCESS_URL = "https://hal.architshin.com/st31/getItArticlesList.php";


    private ListView _lvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _lvArticle = findViewById(R.id.lvArticle);
        Log.d("onCreate","ここまではいってるよ");
        _lvArticle.setOnItemClickListener(new ListItemClickListener());
    }

    private void log(String ti,String me){
        Log.v(ti,me);
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiveArticleList(ACCESS_URL);
    }

    @UiThread
    private class ArticleListBackgroundReceiver implements Runnable{

        private final Handler _handler;
        private final String _url;

        public ArticleListBackgroundReceiver(Handler handler,String url){
            _handler = handler;
            _url = url;
        }

        @Override
        public void run() {
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            try{
                URL url = new URL(_url);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(1000);
                con.setReadTimeout(1000);
                con.setRequestMethod("GET");
                con.connect();
                int status = con.getResponseCode();
                if(status != 200){
                    throw new IOException();
                }
                is = con.getInputStream();
                result = is2String(is);

            } catch(MalformedURLException e) {
                Log.e(DEBUG_TAG, "URL変換失敗！！！！！！！", e);
            } catch(SocketTimeoutException e) {
                Log.w(DEBUG_TAG, "通信タイムアウト！！！！！！！", e);
            } catch(IOException e) {
                Log.e(DEBUG_TAG, "通信失敗！！！！！！！", e);
            }finally {
                if(con != null){
                    con.disconnect();
                }
                if(is != null){
                    try{
                        is.close();
                    } catch(IOException e) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗！！！！！！！", e);
                    }
                }
            }
            articleInfoPostExcutor postExcutor = new articleInfoPostExcutor(result);
            _handler.post(postExcutor);
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }

    private class articleInfoPostExcutor implements Runnable{
        private final String _result;

        public articleInfoPostExcutor(String result){
            _result = result;
        }
        @Override
        public void run() {
            List<Map<String,String>> articleList = new ArrayList<>();
            try{
                JSONObject rootJSON = new JSONObject(_result);
                JSONArray listJsonArray = rootJSON.getJSONArray("list");
                for(int i = 0; i < listJsonArray.length(); i++){
                    Map<String,String > map = new HashMap<>();
                    map.put("title",listJsonArray.getJSONObject(i).getString("title"));
                    //性と名を結合し格納
                    String name = listJsonArray.getJSONObject(i).getString("last_name") + " " + listJsonArray.getJSONObject(i).getString("first_name");
                    map.put("name",name);
                    map.put("id",listJsonArray.getJSONObject(i).getString("id"));
                    articleList.add(map);
                }
            } catch(JSONException e) {
                Log.e(DEBUG_TAG, "JSON解析失敗", e);
            }

            ListView lvArticle = findViewById(R.id.lvArticle);
            String[] from = {"title","name"};
            int[] to = {android.R.id.text1,android.R.id.text2};
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),articleList, android.R.layout.simple_list_item_2,from,to);

            lvArticle.setAdapter(adapter);
        }
    }

    @UiThread
    private void receiveArticleList(final String url){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        ArticleListBackgroundReceiver backgroundReceiver = new ArticleListBackgroundReceiver(handler, url);
        ExecutorService executorService  = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundReceiver);
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Map<String, String> article = (Map<String, String>) parent.getItemAtPosition(position);
            String msg = "Item Click" + article.get("id");
            Log.v("article to val",msg);
            Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
            intent.putExtra("id", Integer.parseInt(article.get("id")));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_add_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //アクションボタンが押された際の処理
        //メイン処理
        Intent intent = new Intent(MainActivity.this,ArticleAddActivity.class);
        log("onOptionsItemSelected_push","hogehogehoge");
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}