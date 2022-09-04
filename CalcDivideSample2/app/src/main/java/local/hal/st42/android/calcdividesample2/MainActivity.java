package local.hal.st42.android.calcdividesample2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import local.hal.st42.android.calcdividesample2.viewmodel.MainViewModel;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ST42 Androidサンプル11 割り算計算機ViewModel版
 *
 * メインアクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
        /**
         * この画面用のViewModelオブジェクト。
         */
        private MainViewModel _mainViewModel;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                ViewModelProvider viewModelProvider = new ViewModelProvider(MainActivity.this);
                _mainViewModel = viewModelProvider.get(MainViewModel.class);
        
                EditText etDenomi = findViewById(R.id.etDenomi);
                EditText etNume = findViewById(R.id.etNume);
                TextView tvAnswer = findViewById(R.id.tvAnswer);
                etDenomi.setText(_mainViewModel.getStrDenomi());
                etNume.setText(_mainViewModel.getStrNume());
                tvAnswer.setText(_mainViewModel.getAns());
        }
    
        /**
         * ボタンがクリックされた時の処理メソッド。
         *
         * @param view クリックされた画面部品。
         */
        public void onButtonClick(View view) {
                EditText etDenomi = findViewById(R.id.etDenomi);
                EditText etNume = findViewById(R.id.etNume);
                TextView tvAnswer = findViewById(R.id.tvAnswer);
        
                int id = view.getId();
                switch(id) {
                        case R.id.btCalc:
                            String strDenomi = etDenomi.getText().toString();
                            String strNume = etNume.getText().toString();
                            if(strDenomi.equals("") || strNume.equals("")) {
                                    String msg = "何か数字を入力せなあきまへんがな!";
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                    double denomi = Double.valueOf(strDenomi);
                                    if(denomi == 0) {
                                            String msg = "分母に0はあきまへんがな!";
                                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                            double nume = Double.valueOf(strNume);
                                            _mainViewModel.setDenomi(denomi);
                                            _mainViewModel.setNume(nume);
                                    }
                            }
                            break;
                        case R.id.btClear:
                            _mainViewModel.setNume(0);
                            _mainViewModel.setDenomi(0);
                            break;
                }
                etDenomi.setText(_mainViewModel.getStrDenomi());
                etNume.setText(_mainViewModel.getStrNume());
                tvAnswer.setText(_mainViewModel.getAns());
        }
}
