package local.hal.st42.android.livedatasample.viewmodel;

/**
 * ST42 Androidサンプル12 LiveData
 *
 * 長方形に関するデータを保持するエンティティクラス。
 *
 * @author Shinzo SAITO
 */
public class Rectangle {
        /**
         * 縦の長さフィールド。
         */
        private int _height = 0;
        /**
         * 横の長さフィールド。
         */
        private int _width = 0;
    
        /**
         * 長方形の面積を得るメソッド。
         *
         * @return 長方形の面積。
         */
        public int getArea() {
                return _height * _width;
        }
    
        /**
         * 長方形の面積の文字列を得るメソッド。
         *
         * @return 長方形の面積の文字列。
         */
        public String getAreaStr() {
                return String.valueOf(getArea());
        }
    
        /**
         * 縦の長さの文字列を得るメソッド。
         *
         * @return 縦の長さの文字列。
         */
        public String getHeightStr() {
                return String.valueOf(_height);
        }
    
        /**
         * 横の長さの文字列を得るメソッド。
         *
         * @return 横の長さの文字列。
         */
        public String getWidthStr() {
                return String.valueOf(_width);
        }
    
        //以下アクセサメソッド。
    
        public int getHeight() {
                return _height;
        }
        public void setHeight(int height) {
                _height = height;
        }
        public int getWidth() {
                return _width;
        }
        public void setWidth(int width) {
                _width = width;
        }
}
