package local.hal.st31.android.itarticlecollection90727;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleDetailActivity extends AppCompatActivity {
    private Class<MainActivity> _mode = MainActivity.class;
    private int _selectedArticleId = 0 ;

    private String _articleURL = "";

    static final int MODE_INSERT = 1;

    private static final String DEBUG_TAG = "ReceiveArticl";

    private static final String ACCESS_URL = "https://hal.architshin.com/st31/getOneArticle.php";

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_article_detail);

        Log.v("onCreate_get_id",Integer.toString(intentInfoId()));

        receiveArticleList(ACCESS_URL,intentInfoId());

        Log.v("onCreate",Integer.toString(intentInfoId()));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void log(String ti,String me){
        Log.v(ti,me);
    }

    private void replaceTv(String id,String title,String url,String comment,String studentId,String seatNo,String lastName,String firstName, String date){
        String msg = "hogehoge";
        TextView textView1 = findViewById(R.id.tvMainTitle);
        textView1.setText("～選択されたID「"+id+"」～");
        TextView textView2 = findViewById(R.id.tvTitle);
        textView2.setText("タイトル："+title);
        TextView textView3 = findViewById(R.id.tvURL);
        textView3.setText("URL："+url);
        TextView textView4 = findViewById(R.id.tvComment);
        textView4.setText("コメント："+comment);
        TextView textView5 = findViewById(R.id.tvCommentOnArticle);
        textView5.setText("学籍番号："+studentId);
        TextView textView6 = findViewById(R.id.tvSeatNo);
        textView6.setText("出席番号："+seatNo);
        TextView textView7 = findViewById(R.id.tvArticleTitle);
        textView7.setText("名字："+lastName);
        TextView textView8 = findViewById(R.id.tvURLArticle);
        textView8.setText("名前："+firstName);
        TextView textView9 = findViewById(R.id.tvDate);
        textView9.setText("日付時刻："+date);
    }

    private int intentInfoId(){
        Intent intent = getIntent();
        int selectIndex;
        if(intent == null){
            return selectIndex = 0;
        }
        selectIndex =  intent.getIntExtra("id",0);
        return selectIndex;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @UiThread
    private class ArticleListBackgroundReceiver implements Runnable{

        private final Handler _handler;
        private final String _url;
        private final int _id;

        public ArticleListBackgroundReceiver(Handler handler,String url, int id){
            _handler = handler;
            _url = url;
            _id = id;
        }

        @Override
        public void run() {
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            try{
                URL url = new URL(_url +"?id="+ String.valueOf(_id));
                log("con",url.toString());
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
            ArticleDetailActivity.articleInfoPostExcutor postExcutor = new ArticleDetailActivity.articleInfoPostExcutor(result);
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
        @UiThread
        @Override
        public void run() {
            String id = "";
            String title = "";
            String url = "";
            String comment = "";
            String studentId = "";
            String seatNo = "";
            String firstName = "";
            String lastName = "";
            String date = "";
            try{
                JSONObject rootJSON = new JSONObject(_result).getJSONArray("article").getJSONObject(0);
                id = rootJSON.getString("id");
                title = rootJSON.getString("title");
                url = rootJSON.getString("url");
                comment = rootJSON.getString("comment");
                studentId = rootJSON.getString("student_id");
                seatNo = rootJSON.getString("seat_no");
                //性と名を結合し格納
                lastName = rootJSON.getString("last_name");
                firstName = rootJSON.getString("first_name");
                date = rootJSON.getString("created_at");
                } catch(JSONException e) {
                Log.e(DEBUG_TAG, "JSON解析失敗", e);
            }
            log("run","kakuninn");
            replaceTv(id,title,url,comment,studentId,seatNo,lastName,firstName,date);

            log("connection_URL",url);
            _articleURL = url;
        }
    }
    @UiThread
    private void receiveArticleList(final String url, final int id){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        ArticleListBackgroundReceiver backgroundReceiver = new ArticleListBackgroundReceiver(handler, url, id);
        ExecutorService executorService  = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                Uri uri = Uri.parse(_articleURL);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                log("uri",uri.toString());
        }
        return super.onOptionsItemSelected(item);
    }
}