package local.hal.st42.android.calcdividesample2.viewmodel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.lifecycle.ViewModel;

/**
 * ST42 Androidサンプル11 割り算計算機ViewModel版
 *
 * メインアクティビティ用ViewModelクラス。
 *
 * @author Shinzo SAITO
 */
public class MainViewModel extends ViewModel {
        /**
         * 分母フィールド。
         */
        private double _denomi = 0;
        /**
         * 分子フィールド。
         */
        private double _nume = 0;
        /**
         * 割り算結果の文字列フィールド。
         */
        private String _ans = "";
    
        /**
         * 割り算結果を取得するメソッド。
         *
         * @return 割り算結果の文字列。
         */
        public String getAns() {
                if(_denomi == 0) {
                        _ans = "";
                }
                else {
                        BigDecimal bigAns = BigDecimal.valueOf(_nume / _denomi);
                        bigAns = bigAns.setScale(3, RoundingMode.HALF_UP);
                        _ans = bigAns.toString();
                }
                return _ans;
        }
    
        /**
         * 分母の文字列を取得するメソッド。
         *
         * @return 分母の文字列。0の場合は空文字。
         */
        public String getStrDenomi() {
                String strDenomi = "";
                if(_denomi != 0) {
                        strDenomi = String.valueOf(_denomi);
                }
                return strDenomi;
        }
    
        /**
         * 分子の文字列を取得するメソッド。
         *
         * @return 分子の文字列。0の場合は空文字。
         */
        public String getStrNume() {
                String strNume = "";
                if(_nume != 0) {
                        strNume = String.valueOf(_nume);
                }
                return strNume;
        }
    
        //以下アクセサメソッド。
    
        public double getDenomi() {
                return _denomi;
        }
    
        public void setDenomi(double denomi) {
                _denomi = denomi;
        }
    
        public double getNume() {
                return _nume;
        }
    
        public void setNume(double nume) {
                _nume = nume;
        }
}
