package local.hal.st42.android.recyclerviewsample;

/**
 * ST42 Androidサンプル08 リサイクラービュー
 *
 * メニュー情報を格納するエンティティクラス。
 *
 * @author Shinzo SAITO
 */
public class SukiyaMenu {
        /**
         * メニュー名。
         */
        private String _name;
        /**
         * 金額。
         */
        private Integer _price;
    
        //以下アクセサメソッド。
    
        public String getName() {
                return _name;
        }
        public void setName(String name) {
                _name = name;
        }
        public Integer getPrice() {
                return _price;
        }
        public void setPrice(Integer price) {
                _price = price;
        }
}
