package com.galihpw.weatherapps;

public class Weather {

    public String date;
    public String icon;
    public String cuaca;
    public double temp;

    public Weather() {

    }

    public Weather(String date, String icon, String cuaca, double temp) {
        this.date = date;
        this.icon = icon;
        this.cuaca = cuaca;
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCuaca() {
        return cuaca;
    }

    public void setCuaca(String cuaca) {
        this.cuaca = cuaca;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
