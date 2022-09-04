package local.hal.st42.android.livedatasample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import local.hal.st42.android.livedatasample.viewmodel.MainViewModel;
import local.hal.st42.android.livedatasample.viewmodel.Rectangle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * ST42 Androidサンプル12 LiveData
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
        /**
         * 長方形のデータを保持するLiveDataオブジェクト。
         */
        private LiveData<Rectangle> _liveData;
        /**
         * 開始ボタンに関して、開始済みかどうかを表すフラグ。
         */
        private boolean _isStarted = false;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                ViewModelProvider viewModelProvider = new ViewModelProvider(MainActivity.this);
                _mainViewModel = viewModelProvider.get(MainViewModel.class);
                _liveData = _mainViewModel.getLiveRectangle();
                Rectangle rectangle = _liveData.getValue();
                showNewData(rectangle);
        
                RectangleObserver observer = new RectangleObserver();
                _liveData.observe(MainActivity.this, observer);
        }
    
        /**
         * ボタンがクリックされた時の処理メソッド。
         *
         * @param view クリックされた画面部品。
         */
        public void onButtonClick(View view) {
                Button btStart = findViewById(R.id.btStart);
                if(_isStarted) {
                        _mainViewModel.stopCreateNewDataTimer();
                        btStart.setText(R.string.bt_start);
                        _isStarted = false;
                }
                else {
                        _mainViewModel.startCreateNewDataTimer();
                        btStart.setText(R.string.bt_stop);
                        _isStarted = true;
                }
        }
    
        /**
         * 長方形に関するデータを保持するエンティティオブジェクト内のデータを画面に表示するメソッド。
         *
         * @param rectangle 長方形に関するデータを保持するエンティティオブジェクト。
         */
        private void showNewData(Rectangle rectangle) {
                TextView tvHeight = findViewById(R.id.tvHeight);
                TextView tvWidth = findViewById(R.id.tvWidth);
                TextView tvArea = findViewById(R.id.tvArea);
                tvHeight.setText(rectangle.getHeightStr());
                tvWidth.setText(rectangle.getWidthStr());
                tvArea.setText(rectangle.getAreaStr());
        }
    
        /**
         * 長方形のデータを保持するLiveDataオブジェクトのオブザーバクラス。
         */
        private class RectangleObserver implements Observer<Rectangle> {
                @Override
                public void onChanged(Rectangle rectangle) {
                        showNewData(rectangle);
                }
        }
}
