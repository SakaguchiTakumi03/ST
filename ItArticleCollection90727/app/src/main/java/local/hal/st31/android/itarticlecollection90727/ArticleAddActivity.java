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
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleAddActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "PostDebug";
    private static final String ACCESS_URL = "https://hal.architshin.com/st31/insertItArticle.php";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_add);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void log(String ti,String me){ Log.v(ti,me);}

    private class PostExecutor implements Runnable{

        private final String _result;

        private final boolean _success;

        PostExecutor(String result,boolean success){
            _result = result;
            _success = success;
        }

        @UiThread
        @Override
        public void run(){
            String message = _result;
            log("try,catch入る前",message);
            if(_success){
                String title = "";
                String url = "";
                String comment = "";
                String name = "";
                String studentid = "";
                String seatno = "";
                String status = "";
                String msg = "";
                String timestamp = "";
                try{
                    JSONObject rootJSON = new JSONObject(_result);
                    title = rootJSON.getString("title");
                    url = rootJSON.getString("url");
                    comment = rootJSON.getString("comment");
                    name = rootJSON.getString("name");
                    studentid = rootJSON.getString("studentid");
                    seatno = rootJSON.getString("seatno");
                    status = rootJSON.getString("status");
                    msg = rootJSON.getString("msg");
                    timestamp = rootJSON.getString("timestamp");
                    message = "タイトル:"+title+"URL:"+url+"コメント:"+comment+"名前:"+name+"学籍番号:"+studentid+"出席番号:"+seatno+"結果:"+status+"メッセージ:"+msg+"タイムスタンプ:"+timestamp;
                    log("JSON データがレスポンス",message);

                }catch(JSONException ex){
                    message = getString(R.string.msg_err_parse);
                    log("00000000000000000000000000000000000000000000000000000000000000000000000000000000000","JSONデータレスポンス in catch");
                    Log.e(DEBUG_TAG,"JSON解析失敗",ex);
                }
                log("catch通過後",message);
                Dialog dialog = new Dialog();
                Bundle extras = new Bundle();

                extras.putString("title",title);
                extras.putString("url",url);
                extras.putString("comment",comment);
                extras.putString("name",name);
                extras.putString("studentid",studentid);
                extras.putString("seatno",seatno);
                extras.putString("status",status);
                extras.putString("msg",msg);
                extras.putString("timestamp",timestamp);
                dialog.setArguments(extras);
                FragmentManager manager = getSupportFragmentManager();
                if(Integer.parseInt(status) == 0){
                    dialog.show(manager,"Dialog");
                }else{
                    finish();
                }
            }
        }
    }

    private class BackgroundPostAccess implements Runnable {
        private final Handler _handler;
        private final String _accessURL;
        private final String _title;
        private final String _url;
        private final String _comment;
        private final String _lastName;
        private final String _firstName;
        private final String _studentId;
        private final String _seatNo;

        public BackgroundPostAccess(Handler handler, String accessURL,String title,String url,String comment,String lastName, String firstName, String studentId, String seatNo) {
            _handler = handler;
            _accessURL = accessURL;
            _title = title;
            _url = url;
            _comment = comment;
            _lastName = lastName;
            _firstName = firstName;
            _studentId = studentId;
            _seatNo = seatNo;
        }

        @WorkerThread
        @Override
        public void run() {
            String postData = "title=" + _title + "&url=" + _url + "&comment=" + _comment + "&lastname=" + _lastName + "&firstname=" + _firstName + "&studentid=" + _studentId + "&seatno=" + _seatNo ;
            log("POST_DATA",postData);
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            boolean success = false;
            try {
                URL url = new URL(_accessURL);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();
                int status = con.getResponseCode();
                if (status != 200) {
                    throw new IOException("ステータスコード:" + status);
                }
                is = con.getInputStream();

                result = is2String(is);
                success = true;
            } catch (SocketTimeoutException ex) {
                result = getString(R.string.msg_err_timeout);
                Log.e(DEBUG_TAG, "タイムアウト", ex);
            } catch (MalformedURLException ex) {
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                result = getString(R.string.msg_err_send);
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    result = getString(R.string.msg_err_send);
                    Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                }
            }
            PostExecutor postExecutor = new PostExecutor(result, success);
            _handler.post(postExecutor);
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            char[] b = new char[1042];
            int line;
            while (0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }

    @UiThread
//    private int postAccess(final String accessurl, final String title , final String url ,final String comment ,final String lastName, final String firstName, final String studentId, final String seatNo){
    private void postAccess(final String accessurl, final String title , final String url ,final String comment ,final String lastName, final String firstName, final String studentId, final String seatNo){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundPostAccess backgroundPostAccess = new BackgroundPostAccess(handler , accessurl, title, url, comment, lastName, firstName, studentId, seatNo);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        log("backgroundpostaccess",backgroundPostAccess.toString());
        executorService.submit(backgroundPostAccess);
//        return
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String title = "";
        String url = "";
        String comment = "";
//        EditText etArticleTitle = findViewById(R.id.etArticleTitle);
//        EditText etURLArticle = findViewById(R.id.etURLArticle);
//        EditText etCommentOnArticle = findViewById(R.id.etCommentOnArticle);
//        title = etArticleTitle.getText().toString();
//        url = etURLArticle.getText().toString();
//        comment = etCommentOnArticle.getText().toString();
        String lastName = "坂口";
        String firstName = "拓海";
        String studentid = "90727";
        String seatno = "19";
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                log("hiyoko","piyopiyo");
//                title = "【C#】引数の型を何でも List にしちゃう奴にそろそろ一言いっておくか";
                title = "ほげ";
                url = "https://qiita.com/lobin-z0x50/items/248db6d0629c7abe47dd";
                comment = "扱いやすいからって修正しにくかったらダメだね。";
                postAccess(ACCESS_URL,title,url,comment,lastName,firstName,studentid,seatno);
        }
        return super.onOptionsItemSelected(item);
    }
}
//コーディング時間16時間
