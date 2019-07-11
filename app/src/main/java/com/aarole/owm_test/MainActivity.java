package com.aarole.owm_test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "193c825225dd24406594a6cc9dac2069";
    public String city;
    public String country, key, weather, temp, humidity, windSpeed, windDirection;
    private Button back;
    public Double windSp, windK, windM;
    public String tempM, tempI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = getIntent().getStringExtra("city");
        back = findViewById(R.id.backBtn);

        final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;

        final TextView cityTxt = findViewById(R.id.txtCity);
        final TextView countryTxt= findViewById(R.id.txtCountry);
        final TextView keyTxt= findViewById(R.id.txtKey);
        final TextView tempTxt= findViewById(R.id.txtTemp);
        final TextView weatherTxt= findViewById(R.id.txtWeather);
        final TextView humidTxt= findViewById(R.id.txtHumid);
        final TextView windSTxt= findViewById(R.id.txtWindS);
        final TextView windDTxt= findViewById(R.id.txtWindD);
        final RadioButton radKm = findViewById(R.id.radkmh);
        final RadioButton radMi = findViewById(R.id.radmh);
        final RadioButton radMe = findViewById(R.id.radmps);
        final RadioButton radC = findViewById(R.id.radC);
        final RadioButton radF = findViewById(R.id.radF);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        });

        cityTxt.setText(city);

        @SuppressLint("StaticFieldLeak") final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("task", "Async Task started");

                try {
                    country = getJSONObjectFromURL(API_URL).getJSONObject("sys").getString("country");
                    key = Integer.toString(getJSONObjectFromURL(API_URL).getInt("id"));
                    weather = getJSONObjectFromURL(API_URL).getJSONArray("weather").getJSONObject(0).getString("main") + " (" + getJSONObjectFromURL(API_URL).getJSONArray("weather").getJSONObject(0).getString("description") +  ")";
                    humidity = Integer.toString(getJSONObjectFromURL(API_URL).getJSONObject("main").getInt("humidity"));
                    Double tempK = getJSONObjectFromURL(API_URL).getJSONObject("main").getDouble("temp");
                    Double tempC = (tempK-273);
                    Double tempF = ((9*tempC/5)+32);
                    tempM = String.format("%.2f", tempC);
                    tempI = String.format("%.2f", tempF);
                    windSp = getJSONObjectFromURL(API_URL).getJSONObject("wind").getDouble("speed");
                    int deg = getJSONObjectFromURL(API_URL).getJSONObject("wind").getInt("deg");
                    windDirection = getWindDirection(deg);
                    Log.d("complete", "Task complete.");
                }
                catch (IOException eIO){
                    eIO.printStackTrace();
                }
                catch (JSONException eJSON){
                    //Do nothing
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                    weather = "null";
                    humidity = "null";
                    temp="null";
                    windSpeed = "null";
                    windDirection = "null";
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                Log.d("Country", country);
//                Log.d("Key", key);
//                Log.d("Weather", weather);
//                Log.d("Temp(F)", tempI);
//                Log.d("Temp(C)", tempM);
//                Log.d("Humidity", humidity);
//                Log.d("Wind Speed", windSpeed);
//                Log.d("Wind Direction", windDirection);

                if(country.equalsIgnoreCase("us")){
                    radF.setChecked(true);
                    radMi.setChecked(true);
                }

                if(radKm.isChecked()){
                    windK = windSp * 3.6;
                    windSpeed = String.format("%.2f", windK);
                }
                else if(radMi.isChecked()){
                    windM = windSp * 2.237;
                    windSpeed = String.format("%.2f", windM);
                }
                else{
                    windSpeed = String.format("%.2f", windSp);
                }

                if(radF.isChecked()){
                    temp = tempI;
                }
                else{
                    temp = tempM;
                }

                windK = windSp * 3.6;
                windM = windSp * 2.237;
                countryTxt.setText(country);
                keyTxt.setText(key);
                weatherTxt.setText(weather);
                tempTxt.setText(temp);
                humidTxt.setText(humidity);
                windSTxt.setText(windSpeed);
                windDTxt.setText(windDirection);

                radKm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Double temp = Double.parseDouble((String)windSTxt.getText());
//                        if(windM == null){
//                            temp *= 3.6;
//                        }
//                        else if(windSp == null){
//                            temp *= 1.609;
//                        }
//
                        windSTxt.setText(String.format("%.2f", windK));
//                        windSp = null;
//                        windM = null;
                    }
                });

                radMi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Double temp = Double.parseDouble((String)windSTxt.getText());
//                        if(windSp == null){
//                            temp /= 1.609;
//                        }
//                        else if(windK == null){
//                            temp *= 2.237;
//                        }
//
                        windSTxt.setText(String.format("%.2f", windM));
//                        windK = null;
//                        windSp = null;
                    }
                });

                radMe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Double temp = Double.parseDouble((String)windSTxt.getText());
//                        if(windM == null){
//                            temp /= 3.6;
//                        }
//                        else if(windK == null){
//                            temp /= 2.237;
//                        }
//
                        windSTxt.setText(String.format("%.2f", windSp));
//                        windK = null;
//                        windM = null;
                    }
                });

                radC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tempTxt.setText(tempM);
                    }
                });

                radF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tempTxt.setText(tempI);
                    }
                });
            }
        };

        asyncTask.execute();

    }

    public JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }

    public String getWindDirection(double degrees){

        int degInt = (int) ((degrees/22.5)+0.5);

        String[] dir = {"N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};

        return dir[(degInt%16)];
    }
}
