package com.garfield_chou.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public void queryWeather (View view) {

        DownloadJSONTask task = new DownloadJSONTask();
        EditText cityEditText = (EditText) findViewById(R.id.cityEditText);

        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" 
            + cityEditText.getText().toString()
            + "&appid=9c436ed4e80da1d03390836a27581377");

    }

    public class DownloadJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground (String... urls) {

            URL url;
            HttpURLConnection urlConnection;
            String result = "";

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (-1 != data) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String weatherStr = "";

            try {
                JSONObject webContent = new JSONObject(result);
                String weatherInfo = webContent.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);
                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);
                    weatherStr += (jsonPart.getString("main") + " : " + jsonPart.getString("description") + "\r\n");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView weatherInfoTextView = (TextView) findViewById(R.id.weatherInfoTextView);
            weatherInfoTextView.setText(weatherStr);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
