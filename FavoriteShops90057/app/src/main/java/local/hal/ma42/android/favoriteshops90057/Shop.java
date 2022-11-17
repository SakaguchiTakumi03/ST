package local.hal.ma42.android.favoriteshops90057;

import java.sql.Timestamp;

public class Shop {
    /**
     * 主キーのID値。
     */
    private long _id;
    /**
     * お店名。
     */
    private String _name;
    /**
     * 電話番号。
     */
    private String _tel;
    /**
     * URL。
     */
    private String _url;
    /**
     * メモ内容。
     */
    private String _note;

    //以下アクセサメソッド。

    public long getId() {
        return _id;
    }
    public void setId(long id) {
        _id = id;
    }
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }
    public String getTel() {
        return _tel;
    }
    public void setTel(String tel) {
        _tel = tel;
    }
    public String getUrl() {
        return _url;
    }
    public void setUrl(String url) {
        _url = url;
    }
    public String getNote() {
        return _note;
    }
    public void setNote(String note) {
        _note = note;
    }
}
