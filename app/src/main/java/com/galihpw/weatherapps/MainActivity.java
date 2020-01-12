package com.galihpw.weatherapps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llBackground;
    private TextView tvSapa,tvNama,tvSuhu, tvCuaca, tvHari, tvKota;
    private RecyclerView rvWeather;
    private ImageView ivIcon;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Weather> weatherList;
    private RecyclerView.Adapter adapter;

    private String API = "7b45f0f74816633b5c92ada71ce4df34";
    private String JSON_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String nama = null;
        String kodePos = null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                nama = null;
                kodePos = null;
            } else {
                nama = extras.getString("nama");
                kodePos = extras.getString("kodepos");
            }
        }

        JSON_URL = "http://api.openweathermap.org/data/2.5/forecast?zip=" + kodePos + "&appid=" + API;

        llBackground = findViewById(R.id.llBackground);
        tvSapa = findViewById(R.id.tvSapa);
        tvNama = findViewById(R.id.tvNama);
        ivIcon = findViewById(R.id.ivIcon);
        tvSuhu = findViewById(R.id.tvSuhu);
        tvCuaca = findViewById(R.id.tvCuaca);
        tvHari = findViewById(R.id.tvHari);
        tvKota = findViewById(R.id.tvKota);
        rvWeather = findViewById(R.id.rvWeather);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 4){
            tvSapa.setText("Selamat Malam,");
            llBackground.setBackgroundResource(R.drawable.bg_malam);
        }else if(timeOfDay >= 4 && timeOfDay < 10){
            tvSapa.setText("Selamat Pagi,");
            llBackground.setBackgroundResource(R.drawable.bg_pagi);
        }else if(timeOfDay >= 10 && timeOfDay < 14){
            tvSapa.setText("Selamat Siang,");
            llBackground.setBackgroundResource(R.drawable.bg_siang);
        }else if(timeOfDay >= 14 && timeOfDay < 18){
            tvSapa.setText("Selamat Sore,");
            llBackground.setBackgroundResource(R.drawable.bg_sore);
        }else if(timeOfDay >= 18 && timeOfDay < 24){
            tvSapa.setText("Selamat Malam,");
            llBackground.setBackgroundResource(R.drawable.bg_malam);
        }

        tvNama.setText(nama);

        weatherList = new ArrayList<>();
        adapter = new WeatherAdapter(getApplicationContext(),weatherList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(rvWeather.getContext(), linearLayoutManager.getOrientation());

        rvWeather.setHasFixedSize(true);
        rvWeather.setLayoutManager(linearLayoutManager);
        rvWeather.addItemDecoration(dividerItemDecoration);
        rvWeather.setAdapter(adapter);

        loadWeather();
    }

    private void loadWeather() {
        //getting the progressbar
//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //making the progressbar visible
//        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
//                        progressBar.setVisibility(View.INVISIBLE);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar cal = Calendar.getInstance();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray weatherArray = obj.getJSONArray("list");

//                            Toast.makeText(MainActivity.this, ""+weatherArray, Toast.LENGTH_LONG).show();

                            JSONObject weatherObjectMain = weatherArray.getJSONObject(0);
                            JSONObject tempMain = weatherObjectMain.getJSONObject("main");

                            Double kelvinMain = tempMain.getDouble("temp");
                            Double celciusMain = kelvinMain - 273.15;
                            tvSuhu.setText(""+Math.round(celciusMain));

                            JSONArray arrayOfWeatherMain = weatherObjectMain.getJSONArray("weather");
                            JSONObject arrayofWeatherObjectMain = arrayOfWeatherMain.getJSONObject(0);

                            Glide.with(MainActivity.this)
                                    .load("http://openweathermap.org/img/w/" + arrayofWeatherObjectMain.getString("icon") + ".png")
                                    .into(ivIcon);

                            String kondisiCuacaUtama = arrayofWeatherObjectMain.getString("main");

                            if(kondisiCuacaUtama.equals("Clear")){
                                tvCuaca.setText("Cerah");
                            }else if(kondisiCuacaUtama.equals("Clouds")){
                                tvCuaca.setText("Berawan");
                            }else if(kondisiCuacaUtama.equals("Rain")){
                                tvCuaca.setText("Hujan");
                            }

                            String dateStringMain = weatherObjectMain.getString("dt_txt");
                            Date dateMain = null;
                            try {
                                dateMain = format.parse(dateStringMain);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String dayOfTheWeekMain = (String) DateFormat.format("EEEE", dateMain);
                            tvHari.setText(dayOfTheWeekMain);

                            cal.setTime(dateMain);
                            int hoursMain = cal.get(Calendar.HOUR_OF_DAY);

                            JSONObject city = obj.getJSONObject("city");
                            String namaKota = city.getString("name");
                            tvKota.setText(namaKota);

//                            Toast.makeText(MainActivity.this, ""+hours, Toast.LENGTH_SHORT).show();

                            //now looping through all the elements of the json array
                            for (int i = 1; i < weatherArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject weatherObject = weatherArray.getJSONObject(i);
                                JSONObject temp = weatherObject.getJSONObject("main");

                                Double kelvin = temp.getDouble("temp");
                                Double celcius = kelvin - 273.15;

                                JSONArray arrayOfWeather = weatherObject.getJSONArray("weather");
                                JSONObject arrayofWeatherObject = arrayOfWeather.getJSONObject(0);

                                String icon = arrayofWeatherObject.getString("icon");

                                String kondisi = arrayofWeatherObject.getString("main");
                                String kondisiCuaca = null;
                                if(kondisi.equals("Clear")){
                                    kondisiCuaca = "Cerah";
                                }else if(kondisi.equals("Clouds")){
                                    kondisiCuaca = "Berawan";
                                }else if(kondisi.equals("Rain")){
                                    kondisiCuaca = "Hujan";
                                }

                                String dateString = weatherObject.getString("dt_txt");
                                Date date = null;
                                try {
                                    date = format.parse(dateString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String dayOfTheWeek = (String) DateFormat.format("EEEE", date);

                                cal.setTime(date);
                                int hours = cal.get(Calendar.HOUR_OF_DAY);

                                if(hoursMain == hours){
                                    Weather weather = new Weather(dayOfTheWeek, icon, kondisiCuaca, celcius);

                                    weatherList.add(weather);
                                }

                                //creating a hero object and giving them the values from json object
//                                Hero hero = new Hero(heroObject.getString("name"), heroObject.getString("imageurl"));
//
//                                //adding the hero to herolist
//                                heroList.add(hero);
                            }
//
//                            //creating custom adapter object
//                            ListViewAdapter adapter = new ListViewAdapter(heroList, getApplicationContext());
//
//                            //adding the adapter to listview
//                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
