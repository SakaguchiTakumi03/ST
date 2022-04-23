package local.hal.st31.android.calcbmi90727;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ST31 Android課題６ BMI計算
 *
 * メインアクティビティクラス。
 *
 * @author takumi sakaguchi
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btCalc = findViewById(R.id.btCalc);
        btCalc.setOnClickListener(new ButtonClickListener());
        Button btClear = findViewById(R.id.btClear);
        btClear.setOnClickListener(new ButtonClickListener());
    }

    /**
     * ボタンが押されたときの処理が記述されたメンバクラス。
     */
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //身長
            EditText etHeight = findViewById(R.id.etheigt);
            //体重
            EditText etBodyWeight = findViewById(R.id.etweight);
            //答え
            TextView tvAnswer = findViewById(R.id.tvAnswer);
            //リザルト
            TextView tvResult = findViewById(R.id.tvResult);
            //アドバイス
            TextView tvAdvice = findViewById(R.id.tvAdvice);

            int id = view.getId();
            switch(id) {
                case R.id.btCalc:
                    String strHeight = etHeight.getText().toString();
                    String strBodyWeight = etBodyWeight.getText().toString();
                    if(strHeight.equals("") || strBodyWeight.equals("")) {
                        String msg = "数字を入力してください。";
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }else{
                        //数値をdoubleに変換
                        double height = Double.valueOf(strHeight);
                        double bodyWeight = Double.valueOf(strBodyWeight);
                        //値チェック
                        if(height == 0) {
                            String msg1 = "身長に「0ｃｍ」はダメ！！";
                            Toast.makeText(MainActivity.this, msg1, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(bodyWeight == 0) {
                            String msg2 = "体重「0ｋｇ」の人間なんて居ません！！";
                            Toast.makeText(MainActivity.this, msg2, Toast.LENGTH_SHORT).show();
                            break;
                        }else{
                            String advice = "";
                            String strResult = "";
                            String strIdealWeight = "";
                            //(cm)に値を変更
                            height = height / 100.0;
                            height = height * height;
                            double idealWeight = height * 22.0;
                            double doubleAns = bodyWeight / height;
                            //BigDecimalの中にはdouble型を渡す
                            BigDecimal bigAns = new BigDecimal(doubleAns);
                            BigDecimal bigIdealWeight = new BigDecimal(idealWeight);
                            bigIdealWeight = bigIdealWeight.setScale(0, RoundingMode.HALF_UP);
                            bigAns = bigAns.setScale(1, RoundingMode.HALF_UP);
                            strIdealWeight = bigIdealWeight.toString();
                            if(doubleAns < 18.5){
                                strResult = ("やせています。");
                                advice = ("体重"+strIdealWeight+"kgを目指しましょう。");
                            }else if(doubleAns < 25){
                                strResult = ("ちょうどいいです");
                                advice = ("現状を維持しましょう。");
                            }else{
                                strResult = ("肥満です。");
                                advice = ("体重"+strIdealWeight+"kgを目指しましょう。");
                            }
                            //setScaleにて四捨五入など...ゴニョゴニョ
                            /**
                             * 第１引数：切り上げたい桁数を指定
                             * 第２引数：切り上げ、切り捨て、四捨五入
                             */
                            //BigDecimal型では使えないから型変換を行う
                            String strAns = bigAns.toString();
                            tvAnswer.setText("BMI値：「"+strAns+"」です。");
                            tvResult.setText("結果："+strResult);
                            tvAdvice.setText("一言コメント："+advice);
                        }
                    }
                    break;
                //出力文字を消す処理
                case R.id.btClear:
                    etBodyWeight.setText("");
                    etHeight.setText("");
                    tvAnswer.setText("");
                    tvResult.setText("");
                    tvAdvice.setText("");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + id);
            }
        }
    }
}
