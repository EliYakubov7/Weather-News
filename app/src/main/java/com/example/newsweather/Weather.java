package com.example.newsweather;

import java.io.Serializable;

class Weather implements Serializable {


    private String time_date;
    private String description;
    private String celsius;
    private String fahrenheit;
    private String image;

    Weather(String time_and_date, String description, String celsius, String fahrenheit, String image) {

        this.time_date = time_and_date;
        this.description = description;
        this.celsius = celsius;
        this.fahrenheit=fahrenheit;
        this.image = "https://openweathermap.org/img/w/" + image + ".png";

    }

    String getTime_date() { return time_date; }

    String getDescription() { return description; }

    String getCelsius() { return celsius; }

    String getFahrenheit() { return fahrenheit; }

    String getImage() { return image; }
}
