package local.hal.st42.android.livedatasample.viewmodel;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ST42 Androidサンプル12 LiveData
 *
 * メインアクティビティ用ViewModelクラス。
 *
 * @author Shinzo SAITO
 */
public class MainViewModel extends ViewModel {
        /**
         * 長方形のデータを保持するLiveDataオブジェクト。
         */
        private MutableLiveData<Rectangle> _liveData = new MutableLiveData<>();
        /**
         * タイマーオブジェクト。
         */
        private Timer _timer;
    
        /**
         * 長方形のデータを保持するLiveDataオブジェクトを取得するメソッド。
         *
         * @return 長方形のデータを保持するLiveDataオブジェクト。
         */
        public LiveData<Rectangle> getLiveRectangle() {
                Rectangle rectangle = new Rectangle();
                _liveData.setValue(rectangle);
                return _liveData;
        }
    
        /**
         * 長方形のデータを1秒ごとにバックグラウンドで新規に生成するタイマーを開始するメソッド。
         */
        @UiThread
        public void startCreateNewDataTimer() {
                CreateNewDataTimerTask timerTask = new CreateNewDataTimerTask(_liveData);
                _timer = new Timer();
                _timer.schedule(timerTask, 0, 1000);
        }
    
        /**
         * タイマーを修了するメソッド。
         */
        @UiThread
        public void stopCreateNewDataTimer() {
                if(_timer != null) {
                        _timer.cancel();
                        _timer = null;
                }
        }
    
        /**
         * 長方形のデータを、タイマーの設定に従ってバックグラウンドで新規に生成するクラス。
         */
        private class CreateNewDataTimerTask extends TimerTask {
                /**
                 * 長方形のデータを保持するLiveDataオブジェクト。
                 */
                private MutableLiveData<Rectangle> _liveData;
        
                /**
                 * コンストラクタ。
                 *
                 * @param liveData 長方形のデータを保持するLiveDataオブジェクト。
                 */
                public CreateNewDataTimerTask(MutableLiveData<Rectangle> liveData) {
                        _liveData = liveData;
                }
        
                @WorkerThread
                @Override
                public void run() {
                        Rectangle rectangle = new Rectangle();
                        int height = (int) (Math.random() * 10) + 1;
                        int width = (int) (Math.random() * 10) + 1;
                        rectangle.setHeight(height);
                        rectangle.setWidth(width);
                        _liveData.postValue(rectangle);
                }
        }
}
