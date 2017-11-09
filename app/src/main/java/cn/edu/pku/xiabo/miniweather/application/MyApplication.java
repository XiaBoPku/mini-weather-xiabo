package cn.edu.pku.xiabo.miniweather.application;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import cn.edu.pku.xiabo.miniweather.DB.City;
import cn.edu.pku.xiabo.miniweather.DB.CityDB;


/**
 * Created by xiabo on 2017/11/1.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private static String TAG = "MyApplication";
    private ArrayList<City> mCityArrayList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d(TAG,"MyApplication - > onCreate");

        new Thread(){
            @Override
            public void run() {
                super.run();
                CityDB.copyCityDBToDataDir(getApplicationContext());
                mCityArrayList = new CityDB(getApplicationContext()).getAllCity();
            }
        }.start();
    }

    public static synchronized MyApplication getInstance(){
        return instance;
    }

    public ArrayList<City> getAllCitys(){
        return mCityArrayList;
    }
}
