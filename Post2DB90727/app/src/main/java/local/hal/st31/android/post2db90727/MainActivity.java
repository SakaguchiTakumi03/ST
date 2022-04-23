package local.hal.st31.android.post2db90727;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "PostSample";

    //POST先のURL
    private static final String ACCESS_URL = "https://hal.architshin.com/st31/post2DB.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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
            if(_success){
                String name = "";
                String studentid = "";
                String seatno = "";
                String status = "";
                String msg = "";
                String serialno = "";
                String timestamp = "";
                try{
                    JSONObject rootJSON = new JSONObject(_result);
                    name = rootJSON.getString("name");
                    studentid = rootJSON.getString("studentid");
                    seatno = rootJSON.getString("seatno");
                    status = rootJSON.getString("status");
                    msg = rootJSON.getString("msg");
                    serialno = rootJSON.getString("serialno");
                    timestamp = rootJSON.getString("timestamp");
                }catch(JSONException ex){
                    message = getString(R.string.msg_err_parse);
                    Log.e(DEBUG_TAG,"JSON解析失敗",ex);
                }
                Post2DBDialog dialog = new Post2DBDialog();
                Bundle extras = new Bundle();
                extras.putString("name",name);
                extras.putString("studentid",studentid);
                extras.putString("seatno",seatno);
                extras.putString("status",status);
                extras.putString("msg",msg);
                extras.putString("serialno",serialno);
                extras.putString("timestamp",timestamp);
                dialog.setArguments(extras);
                FragmentManager manager = getSupportFragmentManager();
                dialog.show(manager,"Post2DBDialog");
            }
        }
    }

    private class BackgroundPostAccess implements Runnable {
        private final Handler _handler;
        private final String _url;
        private final String _lastName;
        private final String _firstName;
        private final String _studentId;
        private final String _seatNo;
        private final String _teacherMessage;

        public BackgroundPostAccess(Handler handler, String url, String lastName, String firstName, String studentId, String seatNo, String teacherMessage) {
            _handler = handler;
            _url = url;
            _lastName = lastName;
            _firstName = firstName;
            _studentId = studentId;
            _seatNo = seatNo;
            _teacherMessage = teacherMessage;
        }

        @WorkerThread
        @Override
        public void run() {
            String postData = "lastname=" + _lastName + "&firstname=" + _firstName + "&studentid=" + _studentId + "&seatno=" + _seatNo + "&message=" + _teacherMessage;
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            boolean success = false;
            try {
                URL url = new URL(_url);
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
    private void postAccess(final String url, final String lastName, final String firstName, final String studentId, final String seatNo, final String teacherMessage){
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundPostAccess backgroundPostAccess = new BackgroundPostAccess(handler ,url, lastName, firstName, studentId, seatNo, teacherMessage);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundPostAccess);
    }

    public void sendButtonPush(View view){
        EditText etLastName = findViewById(R.id.etLastName);
        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etStudentId = findViewById(R.id.etStudentId);
        EditText etSeatNo = findViewById(R.id.etSeatNo);
        EditText etTeacherMessage = findViewById(R.id.etTeacherMessage);

        String lastName = etLastName.getText().toString();
        String firstName = etFirstName.getText().toString();
        String studentId = etStudentId.getText().toString();
        String seatNo = etSeatNo.getText().toString();
        String teacherMessage = etTeacherMessage.getText().toString();

        postAccess(ACCESS_URL,lastName,firstName,studentId,seatNo,teacherMessage);
    }
}