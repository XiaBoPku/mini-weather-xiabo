package cn.edu.pku.xiabo.miniweather.DB;

/**
 * Created by xiabo on 2017/11/1.
 */

public class City {
    String province;
    String city;
    String firstPY;
    String allFirstPY;
    String allPY;
    String num;


    public City(String province, String city, String firstPY, String allFirstPY, String allPY, String num) {
        this.province = province;
        this.city = city;
        this.firstPY = firstPY;
        this.allFirstPY = allFirstPY;
        this.allPY = allPY;
        this.num = num;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstPY() {
        return firstPY;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }

    public String getAllFirstPY() {
        return allFirstPY;
    }

    public void setAllFirstPY(String allFirstPY) {
        this.allFirstPY = allFirstPY;
    }

    public String getAllPY() {
        return allPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isRelative(String str){
        if(province.indexOf(str) != -1 || city.indexOf(str) != -1 ||firstPY.indexOf(str) != -1 ||allFirstPY.indexOf(str) != -1 ||allPY.indexOf(str) != -1 || num.indexOf(str) != -1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "City{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", firstPY='" + firstPY + '\'' +
                ", allFirstPY='" + allFirstPY + '\'' +
                ", allPY='" + allPY + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
