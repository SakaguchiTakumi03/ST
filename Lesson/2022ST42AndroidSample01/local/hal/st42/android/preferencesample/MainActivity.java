package local.hal.st42.android.preferencesample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * ST42 Androidサンプル01 プレファレンス
 *
 * 画面表示用アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
        /**
         * プレファレンスファイル名を表す定数フィールド。
         */
        private static final String PREFS_NAME = "PSPrefsFile";
        /**
         * 表示フォントでデフォルトを表す数値の定数フィールド。
         */
        private static final int FONT_TYPE_DEFAULT = 0;
        /**
         * 表示フォントで明朝体を表す数値の定数フィールド。
         */
        private static final int FONT_TYPE_SERIF = 1;
        /**
         * 表示フォントでゴシック体を表す数値の定数フィールド。
         */
        private static final int FONT_TYPE_SANS_SERIF = 2;
        /**
         * 表示フォントで等幅を表す数値の定数フィールド。
         */
        private static final int FONT_TYPE_MONOSPACE = 3;
        /**
         * 表示フォントを表すフィールド。
         */
        private Typeface _fontType;
        /**
         * 表示字体を表すフィールド。
         */
        private int _fontStyle;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                _fontStyle = settings.getInt("fontStyle", Typeface.NORMAL);
                int fontTypeCode = settings.getInt("fontType", FONT_TYPE_DEFAULT);
                _fontType = fontTypeInt2FontType(fontTypeCode);
                TextView tvSpeech = findViewById(R.id.tvSpeech);
                tvSpeech.setTypeface(_fontType, _fontStyle);
        }
    
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_options_main, menu);
                return true;
        }
    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
        
                boolean returnVal = true;
                int itemId = item.getItemId();
                switch(itemId) {
                        case R.id.menuFonttypeSerif:
                            editor.putInt("fontType", FONT_TYPE_SERIF);
                            _fontType = Typeface.SERIF;
                            break;
                        case R.id.menuFonttypeSansserif:
                            editor.putInt("fontType", FONT_TYPE_SANS_SERIF);
                            _fontType = Typeface.SANS_SERIF;
                            break;
                        case R.id.menuFonttypeMonospace:
                            editor.putInt("fontType", FONT_TYPE_MONOSPACE);
                            _fontType = Typeface.MONOSPACE;
                            break;
                        case R.id.menuFontstyleNormal:
                            editor.putInt("fontStyle", Typeface.NORMAL);
                            _fontStyle = Typeface.NORMAL;
                            break;
                        case R.id.menuFontstyleItalic:
                            editor.putInt("fontStyle", Typeface.ITALIC);
                            _fontStyle = Typeface.ITALIC;
                            break;
                        case R.id.menuFontstyleBold:
                            editor.putInt("fontStyle", Typeface.BOLD);
                            _fontStyle = Typeface.BOLD;
                            break;
                        case R.id.menuFontstyleBolditalic:
                            editor.putInt("fontStyle", Typeface.BOLD_ITALIC);
                            _fontStyle = Typeface.BOLD_ITALIC;
                            break;
                        case R.id.menuReset:
                            _fontType = Typeface.DEFAULT;
                            editor.putInt("fontType", FONT_TYPE_DEFAULT);
                            _fontStyle = Typeface.NORMAL;
                            editor.putInt("fontStyle", Typeface.NORMAL);
                            break;
                        default:
                            returnVal = super.onOptionsItemSelected(item);
                            break;
                }
                editor.apply();
        
                TextView tvSpeech = findViewById(R.id.tvSpeech);
                tvSpeech.setTypeface(_fontType, _fontStyle);
                return returnVal;
        }
    
        /**
         * 表示フォントを表す数値をそれが該当するTypefaceオブジェクトに変換するメソッド。
         *
         * @param fontTypeInt 表示フォントを表す数値。このクラスの定数を使用する。
         * @return 該当するTypefaceオブジェクト。
         */
        private Typeface fontTypeInt2FontType(int fontTypeInt) {
                Typeface fontType = Typeface.DEFAULT;
                switch(fontTypeInt) {
                        case FONT_TYPE_SERIF:
                            fontType = Typeface.SERIF;
                            break;
                        case FONT_TYPE_SANS_SERIF:
                            fontType = Typeface.SANS_SERIF;
                            break;
                        case FONT_TYPE_MONOSPACE:
                            fontType = Typeface.MONOSPACE;
                            break;
                }
                return fontType;
        }
}