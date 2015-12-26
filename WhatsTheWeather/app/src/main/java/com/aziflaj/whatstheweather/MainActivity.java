package com.aziflaj.whatstheweather;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText cityEditText;
    TextView resultTextView;
    FetchWeatherTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = (EditText) findViewById(R.id.city_edit_text);
        resultTextView = (TextView) findViewById(R.id.result_text_view);
        task = new FetchWeatherTask(resultTextView);
    }

    public void getWeather(View view) {
        String city = cityEditText.getText().toString();

        String url = Uri.parse("http://api.openweathermap.org/data/2.5")
                .buildUpon()
                .appendPath("weather")
                .appendQueryParameter("appid", "2de143494c0b295cca9337e1e96b00e0")
                .appendQueryParameter("q", city)
                .toString();

        task.execute(url);
    }
}
