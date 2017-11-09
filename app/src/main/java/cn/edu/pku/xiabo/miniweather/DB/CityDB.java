package cn.edu.pku.xiabo.miniweather.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by xiabo on 2017/11/1.
 */

public class CityDB {
    private static String TAG = "CityDB";
    public static String CITY_TABLE_NAME = "city";
    public static String CITY_DB_NAME = "city.db";
    public static String CITY_ASSERT_DB_NAME = "city.db";
    private static String PROVINCE = "province";
    private static String NUMBER = "number";
    private static String CITY = "city";
    private static String FIRST_PY = "firstpy";
    private static String ALL_FIRST_PY = "allfirstpy";
    private static String ALL_PY = "allpy";
    private static String SELECT_ALL_CITY_SQL = "select * from "+CITY_TABLE_NAME;
    ArrayList<City> cityArrayList = new ArrayList<City>();
    private SQLiteDatabase mCityDB;
    private Context mContext;

    public CityDB(Context context){
        mContext = context;
        mCityDB = context.openOrCreateDatabase(CITY_DB_NAME,Context.MODE_PRIVATE,null);
    }

    public ArrayList<City> getAllCity(){
        Cursor cityCursor = mCityDB.rawQuery(SELECT_ALL_CITY_SQL, null);
        ArrayList<City> cityArrayList = new ArrayList<City>();
        while (cityCursor.moveToNext()){
            String pro = cityCursor.getString(cityCursor.getColumnIndexOrThrow(PROVINCE));
            String num = cityCursor.getString(cityCursor.getColumnIndexOrThrow(NUMBER));
            String city = cityCursor.getString(cityCursor.getColumnIndexOrThrow(CITY));
            String firstPY = cityCursor.getString(cityCursor.getColumnIndexOrThrow(FIRST_PY));
            String allFirstPY = cityCursor.getString(cityCursor.getColumnIndexOrThrow(ALL_FIRST_PY));
            String allPY = cityCursor.getString(cityCursor.getColumnIndexOrThrow(ALL_PY));
            cityArrayList.add(new City(pro,city,firstPY,allFirstPY,allPY,num));
        }
        return cityArrayList;
    }

    public static void copyCityDBToDataDir(Context mContext) {
        File citydatabasePath = mContext.getDatabasePath(CityDB.CITY_DB_NAME);
        if(!citydatabasePath.exists()){
            Log.d(TAG,"--->>>>>>>>>>cityDB not exist");
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try{
                inputStream = mContext.getAssets().open(CityDB.CITY_ASSERT_DB_NAME);
                fileOutputStream = new FileOutputStream(citydatabasePath.getAbsolutePath());
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buffer))!= -1){
                    fileOutputStream.write(buffer);
                    fileOutputStream.flush();
                }
            }catch (Exception e){
            }finally {
                try {
                    inputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Log.d(TAG,"--->>>>>>>>>>cityDB  exist");
        }
        Log.d(TAG,"--->>>>>>>>>>");
    }
}
