package com.aziflaj.whatstheweather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        task = new FetchWeatherTask(getApplicationContext(), resultTextView);
    }

    public void getWeather(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

        String city = cityEditText.getText().toString();

        if (TextUtils.isEmpty(city)) {
            Toast.makeText(getApplicationContext(), "Please enter a city name", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Uri.parse("http://api.openweathermap.org/data/2.5")
                .buildUpon()
                .appendPath("weather")
                .appendQueryParameter("appid", "2de143494c0b295cca9337e1e96b00e0")
                .appendQueryParameter("q", city)
                .toString();

        task.execute(url);
    }
}
