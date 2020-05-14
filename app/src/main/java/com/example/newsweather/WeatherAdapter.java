package com.example.newsweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<Weather> weathersList;


    WeatherAdapter(List<Weather> weathersList) {
        this.weathersList = weathersList;
    }


    static class WeatherViewHolder extends RecyclerView.ViewHolder  {

        TextView time_date_tv;
        ImageView icon_image_view;
        TextView words_description_tv;
        TextView celsius_temperature_with_sign_tv;
        TextView fahrenheit_temperature_with_sign_tv;


        WeatherViewHolder(View itemView) {
            super(itemView);

            icon_image_view = itemView.findViewById(R.id.wicon_iv);
            time_date_tv = itemView.findViewById(R.id.wtime_tv);
            words_description_tv = itemView.findViewById(R.id.wdescription_tv);
            celsius_temperature_with_sign_tv = itemView.findViewById(R.id.wtemp_tv);
            fahrenheit_temperature_with_sign_tv = itemView.findViewById(R.id.ftemp_tv);



        }
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_cell,parent,false);
        return new WeatherViewHolder(view);
    }


    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {

        Weather weather = weathersList.get(position);

        holder.time_date_tv.setText(weather.getTime_date());

//        Glide.with(holder.icon_image_view.getContext()).load(weather.getImage())
//                .override(300, 200).centerCrop().into(holder.icon_image_view);

        Picasso.get().load(weather.getImage()).resize(300,200).into(holder.icon_image_view);
        holder.words_description_tv.setText(weather.getDescription());
        holder.celsius_temperature_with_sign_tv.setText(weather.getCelsius());
        holder.fahrenheit_temperature_with_sign_tv.setText(weather.getFahrenheit());


    }


    @Override
    public int getItemCount() {
        if (weathersList!=null){
        return weathersList.size();}
        else return 0;
    }

}
