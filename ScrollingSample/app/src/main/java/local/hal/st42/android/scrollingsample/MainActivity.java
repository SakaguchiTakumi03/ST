package local.hal.st42.android.scrollingsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * ST42 Androidサンプル07 スクローリングアクティビティ
 *
 * 画面表示用アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class MainActivity extends AppCompatActivity {
        /**
         * 表示フォントを表すフィールド。
         */
        Typeface _fontType = Typeface.DEFAULT;
        /**
         * 表示字体を表すフィールド。
         */
        int _fontStyle = Typeface.NORMAL;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        
                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setLogo(R.drawable.ic_zeon);
                setSupportActionBar(toolbar);
                CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
                toolbarLayout.setTitle(getString(R.string.app_name));
                toolbarLayout.setExpandedTitleColor(Color.WHITE);
                toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);
        }
    
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_options_main, menu);
                return true;
        }
    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                boolean returnVal = true;
                int itemId = item.getItemId();
                switch(itemId) {
                        case R.id.menuFonttypeSerif:
                            _fontType = Typeface.SERIF;
                            break;
                        case R.id.menuFonttypeSansserif:
                            _fontType = Typeface.SANS_SERIF;
                            break;
                        case R.id.menuFonttypeMonospace:
                            _fontType = Typeface.MONOSPACE;
                            break;
                        case R.id.menuFontstyleNormal:
                            _fontStyle = Typeface.NORMAL;
                            break;
                        case R.id.menuFontstyleItalic:
                            _fontStyle = Typeface.ITALIC;
                            break;
                        case R.id.menuFontstyleBold:
                            _fontStyle = Typeface.BOLD;
                            break;
                        case R.id.menuFontstyleBolditalic:
                            _fontStyle = Typeface.BOLD_ITALIC;
                            break;
                        default:
                            returnVal = super.onOptionsItemSelected(item);
                }
                if(returnVal) {
                        TextView tvSpeech = findViewById(R.id.tvSpeech);
                        tvSpeech.setTypeface(_fontType, _fontStyle);
                }
                return returnVal;
        }
    
        /**
         * 字体リセットFABがクリックされたときの処理メソッド。
         *
         * @param view クリックされた画面部品。
         */
        public void onFabResetClicked(View view) {
                _fontType = Typeface.DEFAULT;
                _fontStyle = Typeface.NORMAL;
                TextView tvSpeech = findViewById(R.id.tvSpeech);
                tvSpeech.setTypeface(_fontType, _fontStyle);
        }
}
