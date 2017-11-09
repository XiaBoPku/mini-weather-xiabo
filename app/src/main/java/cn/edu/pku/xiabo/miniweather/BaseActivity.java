package cn.edu.pku.xiabo.miniweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by xiabo on 2017/11/9.
 */

public class BaseActivity extends Activity {
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
