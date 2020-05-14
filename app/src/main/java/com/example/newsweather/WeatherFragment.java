package com.example.newsweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class WeatherFragment extends Fragment {

    private static List<Weather> weathersList;

    static WeatherFragment newInstance(List<Weather> i_weatherList){
        WeatherFragment fragment = new WeatherFragment();
        weathersList = i_weatherList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.weather_fragment, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.forecast_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        WeatherAdapter adapter = new WeatherAdapter(weathersList);
        recyclerView.setAdapter(adapter);
        return root;
    }
}
