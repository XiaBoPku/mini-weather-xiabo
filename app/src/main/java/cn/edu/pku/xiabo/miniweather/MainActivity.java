package cn.edu.pku.xiabo.miniweather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.xiabo.miniweather.bean.TodayWeather;
import cn.edu.pku.xiabo.miniweather.utils.NetUtil;

import static cn.edu.pku.xiabo.miniweather.R.id.city;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv,
            pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView imageView, pmImg;
    private static final int UPDATE_TODAY_WEATHER = 1;
    private String currentCityCode;
    private ViewPager viewPager;
    private ImageView[] dots2;//导航小圆点
    private int[] ids2 = {R.id.weatheriv1, R.id.weatheriv2};//小圆点的iamgeview值
    private ViewPagerAdapter vpAdapter2;
    private ViewPager vp2;
    private List<View> views2 = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);



        initView();
        updateView();
    }

    private void refreshCityCode() {
        String cityCode = sharedPreferences.getString("main_city_code","101010100");
        if (NetUtil.getNetworkState(MainActivity.this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            queryWeatherCode(cityCode);
        }else
        {
            Log.d("myWeather", "网络挂了");
        }
    }

    private void updateView() {
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            refreshCityCode();
            Toast.makeText(MainActivity.this,"网络OK！", Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this,"网络挂了！", Toast.LENGTH_LONG).show();
        }
    }

    void initView(){
        findViewById(R.id.title_city_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,SelectActivity.class),1);
            }
        });
        findViewById(R.id.title_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCityCode();
            }
        });
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        imageView = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        //以下是初始化viewPager
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View one_page = inflater.inflate(R.layout.weatherpage1, null);
        View two_page = inflater.inflate(R.layout.weatherpage1, null);
        views2.add(one_page);
        views2.add(two_page);
        vpAdapter2 = new ViewPagerAdapter(views2, MainActivity.this);
        viewPager = (ViewPager) findViewById(R.id.mViewpager);
        viewPager.setAdapter(vpAdapter2);
        viewPager.setOnPageChangeListener(this);
        //增加页面变化的监听事件，动态修改导航小圆点的属性
        dots2 = new ImageView[views2.size()];
        for (int i = 0; i < views2.size(); i++) {
            dots2[i] = (ImageView) findViewById(ids2[i]);
        }
    }

    /**
     * *viewpager加载数据
     */
    private void updateViewPager(ArrayList<TodayWeather.Day> forecastDaysArrayList) {
        View page1 = views2.get(0);
        TodayWeather.Day day0 = forecastDaysArrayList.get(0);
        TextView data1 = (TextView) page1.findViewById(R.id.weather01_data);
        TextView tpye1 = (TextView) page1.findViewById(R.id.weather01_type);
        TextView wendu1 = (TextView) page1.findViewById(R.id.weather01_wendu);
        ImageView typeIV1 = (ImageView) page1.findViewById(R.id.weather01_pic);
        data1.setText(day0.getDate());
        tpye1.setText(day0.getType());
        wendu1.setText(day0.getLow()+"~"+day0.getHigh());
        updateWeatherImg(typeIV1,day0.getType());


        TodayWeather.Day day1 = forecastDaysArrayList.get(1);
        TextView data2 = (TextView) page1.findViewById(R.id.weather02_data);
        TextView tpye2 = (TextView) page1.findViewById(R.id.weather02_type);
        TextView wendu2 = (TextView) page1.findViewById(R.id.weather02_wendu);
        ImageView typeIV2 = (ImageView) page1.findViewById(R.id.weather02_pic);
        data2.setText(day1.getDate());
        tpye2.setText(day1.getType());
        wendu2.setText(day1.getLow()+"~"+day1.getHigh());
        updateWeatherImg(typeIV2,day1.getType());

        TodayWeather.Day day2 = forecastDaysArrayList.get(2);
        TextView data3 = (TextView) page1.findViewById(R.id.weather03_data);
        TextView tpye3 = (TextView) page1.findViewById(R.id.weather03_type);
        TextView wendu3 = (TextView) page1.findViewById(R.id.weather03_wendu);
        ImageView typeIV3 = (ImageView) page1.findViewById(R.id.weather03_pic);
        data3.setText(day2.getDate());
        tpye3.setText(day2.getType());
        wendu3.setText(day2.getLow()+"~"+day2.getHigh());
        updateWeatherImg(typeIV3,day2.getType());

        View page2 = views2.get(1);
        TodayWeather.Day day3 = forecastDaysArrayList.get(3);
        TextView page2_data1 = (TextView) page2.findViewById(R.id.weather01_data);
        TextView page2_tpye1 = (TextView) page2.findViewById(R.id.weather01_type);
        TextView page2_wendu1 = (TextView) page2.findViewById(R.id.weather01_wendu);
        ImageView page2_typeIV1 = (ImageView) page2.findViewById(R.id.weather01_pic);
        page2_data1.setText(day3.getDate());
        page2_tpye1.setText(day3.getType());
        page2_wendu1.setText(day3.getLow()+"~"+day3.getHigh());
        updateWeatherImg(page2_typeIV1,day3.getType());

        TodayWeather.Day day4 = forecastDaysArrayList.get(4);
        TextView page2_data2 = (TextView) page2.findViewById(R.id.weather02_data);
        TextView page2_tpye2 = (TextView) page2.findViewById(R.id.weather02_type);
        TextView page2_wendu2 = (TextView) page2.findViewById(R.id.weather02_wendu);
        ImageView page2_typeIV2 = (ImageView) page2.findViewById(R.id.weather02_pic);
        page2_data2.setText(day4.getDate());
        page2_tpye2.setText(day4.getType());
        page2_wendu2.setText(day4.getLow()+"~"+day4.getHigh());
        updateWeatherImg(page2_typeIV2,day4.getType());

        TodayWeather.Day day5 = forecastDaysArrayList.get(5);
        TextView page2_data3 = (TextView) page2.findViewById(R.id.weather03_data);
        TextView page2_tpye3 = (TextView) page2.findViewById(R.id.weather03_type);
        TextView page2_wendu3 = (TextView) page2.findViewById(R.id.weather03_wendu);
        ImageView page2_typeIV3 = (ImageView) page2.findViewById(R.id.weather03_pic);
        page2_data3.setText(day5.getDate());
        page2_tpye3.setText(day5.getType());
        page2_wendu3.setText(day5.getLow()+"~"+day5.getHigh());
        updateWeatherImg(page2_typeIV3,day5.getType());
    }

    //动态修改小圆点的属性，即可实现相应的导航效果
    @Override
    public void onPageSelected(int i) {
        for(int a=0;a<ids2.length;a++){
            if(a == i){//设置选中效果
                dots2[a].setImageResource(R.drawable.indicator_focused);
            }else{//设置未选中效果
                dots2[a].setImageResource(R.drawable.indicator_unfocued);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    //更新天气图标
    private void updateWeatherImg(ImageView imageView,String weather) {
        switch (weather) {
            case "暴雪":
                imageView.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                break;
            case "暴雨":
                imageView.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                break;
            case "大暴雨":
                imageView.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                break;
            case "大雪":
                imageView.setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            case "大雨":
                this.imageView.setImageResource(R.drawable.biz_plugin_weather_dayu);
                break;
            case "多云":
                imageView.setImageResource(R.drawable.biz_plugin_weather_yin);
                break;
            case "雷阵雨":
                imageView.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            case "雷阵雨冰雹":
                this.imageView.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                break;
            case "晴":
                imageView.setImageResource(R.drawable.biz_plugin_weather_qing);
                break;
            case "沙尘暴":
                imageView.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                break;
            case "特大暴雨":
                imageView.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                break;
            case "雾":
               imageView.setImageResource(R.drawable.biz_plugin_weather_wu);
                break;
            case "小雪":
                imageView.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                break;
            case "小雨":
                imageView.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                break;
            case "阴":
                imageView.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                break;
            case "雨夹雪":
                imageView.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                break;
            case "阵雪":
                imageView.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                break;
            case "阵雨":
                imageView.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
                break;
            case "中雪":
                imageView.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                break;
            case "中雨":
                imageView.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                break;
            default:
                break;
        }
    }
    /**
            * *
    @param cityCode
*/
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr=response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather = parseXML(responseStr);
                    if(todayWeather != null){
                        Log.d("myWeather" ,todayWeather.toString());
                        Message message = Message.obtain();
                        message.what = UPDATE_TODAY_WEATHER;
                        message.obj = todayWeather;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr=response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather = parseXML(responseStr);
                    if(todayWeather != null){
                        Log.d("myWeather" ,todayWeather.toString());
                        Message message = Message.obtain();
                        message.what = UPDATE_TODAY_WEATHER;
                        message.obj = todayWeather;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        TodayWeather.Day day = null;
        boolean firstDay = false;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                            day = new TodayWeather.Day();
                        }else if(xmlPullParser.getName().equals("weather")){
                            day = new TodayWeather.Day();
                        }else if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setShidu(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setWendu(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("date")) {
                            day = new TodayWeather.Day();
                            eventType = xmlPullParser.next();
                            day.setDate(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengxiang")) {
                            eventType = xmlPullParser.next();
                            day.setFengxiang(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengli")) {
                            eventType = xmlPullParser.next();
//                            day.setFengli(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("high")) {
                            eventType = xmlPullParser.next();
                            day.setHigh(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("low")) {
                            eventType = xmlPullParser.next();
                            day.setLow(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("type")) {
                            eventType = xmlPullParser.next();
                            day.setType(xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("date_1")) {
                            day = new TodayWeather.Day();
                            eventType = xmlPullParser.next();
                            day.setDate(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fx_1")) {
                            eventType = xmlPullParser.next();
                            day.setFengxiang(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fl_1")) {
                            eventType = xmlPullParser.next();
                            day.setFengli(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("high_1")) {
                            eventType = xmlPullParser.next();
                            day.setHigh(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("low_1")) {
                            eventType = xmlPullParser.next();
                            day.setLow(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("type_1")) {
                            eventType = xmlPullParser.next();
                            day.setType(xmlPullParser.getText());
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("yesterday") || xmlPullParser.getName().equals("weather")) {
                            eventType = xmlPullParser.next();
                            todayWeather.addDay(day);
                        }
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    void updateTodayWeather(TodayWeather todayWeather){
        TodayWeather.Day day = todayWeather.getForecastDaysArrayList().get(1);
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(day.getDate());
        temperatureTv.setText(day.getHigh()+"~"+day.getLow());
        climateTv.setText(day.getType());
        windTv.setText(day.getFengxiang());
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();

        updateViewPager(todayWeather.getForecastDaysArrayList());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            String newCityCode= data.getStringExtra("cityCode");
//            Log.d("myWeather", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(sharedPreferences.getString("main_city_code","101010100"));
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
}