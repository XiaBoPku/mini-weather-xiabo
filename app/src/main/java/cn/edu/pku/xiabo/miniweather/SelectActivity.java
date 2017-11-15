package cn.edu.pku.xiabo.miniweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.pku.xiabo.miniweather.DB.City;
import cn.edu.pku.xiabo.miniweather.application.MyApplication;

/**
 * Created by xiabo on 2017/10/18.
 */

public class SelectActivity extends BaseActivity {
    ListView cityLV;
    private ClearEditText clearEditText;
    private SelectCityAdapter displayCityAdapter;
    private ArrayList<City> allCities;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        initView();
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

    }

    private void initView() {
        cityLV = (ListView) findViewById(R.id.lv_select_city);
        displayCityAdapter = new SelectCityAdapter();
        cityLV.setAdapter(displayCityAdapter);
        cityLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("main_city_code",((City)displayCityAdapter.getItem(position)).getNum());
                editor.commit();
                Intent intent = new Intent();
//                intent.putExtra("cityCode",cities.get(position).getNum());
                setResult(RESULT_OK,null);
                finish();
            }
        });
        clearEditText = (ClearEditText) findViewById(R.id.ET_search_city);
        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    displayCityAdapter.updateListView(allCities);

                }else {
                    ArrayList<City> filterArr = new ArrayList<City>();
                    for(City city:allCities){
                        if (city.isRelative(s.toString())){
                            filterArr.add(city);
                        }
                    }
                    displayCityAdapter.updateListView(filterArr);
                }
            }
        });
    }

    class SelectCityAdapter extends BaseAdapter{
        private ArrayList<City> filterCityList;

        public SelectCityAdapter(){
            allCities = MyApplication.getInstance().getAllCitys();
            filterCityList = allCities;
        }

        public void updateListView(ArrayList<City> newfilter){
            filterCityList  = newfilter;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return filterCityList.size();
        }

        @Override
        public Object getItem(int position) {
            return filterCityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            City city = filterCityList.get(position);
            View view  = View.inflate(SelectActivity.this,R.layout.select_city_item,null);
            TextView cityName = (TextView) view.findViewById(R.id.select_city_item_cityName);
            TextView cityCode = (TextView) view.findViewById(R.id.select_city_item_cityCode);
            cityName.setText(city.getCity());
            cityCode.setText(city.getNum());
            return view;
        }
    }
}
