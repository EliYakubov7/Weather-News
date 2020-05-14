package com.example.newsweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ArticlesListFragment.OnArticleFragmentListener {

    public static String title;
    public static String urlToImage;
    public static String author;
    public static String description;
    public static String lastTitle;

    public static JSONObject currentTitle;

    private ArrayList<Weather> weatherForecastsList = new ArrayList<>();
    private FusedLocationProviderClient client;
    final int LOCATION_PERMISSION_REQUEST = 1;
    private String WEATHER_FORECAST_LINK = "https://api.openweathermap.org/data/2.5/forecast?APPID=61fdf72da95b0807f12f775d7d474e6c";
    final String WEATHER_LIST_FRAGMENT_TAG = "weather_fragment";


    TextView cityTv;
    AlarmManager alarmManager;
    private ArrayList<Article> articlesList = new ArrayList<>();
    private String notificationCategory = "all";
    private String[] notificationFrequencies = {"דקה 1", "שעה 1", "יום 1"};
    private String HEADLINES_NEWS_LINK = "https://newsapi.org/v2/top-headlines?country=il&apiKey=eef605b5cecf4b309878ce8d0af89b52";
    final String ARTICLES_LIST_FRAGMENT_TAG = "register_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        cityTv=findViewById(R.id.country_tv);
        //NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //mNotificationManager.cancel(NotifierService.NOTIF_ID);
        String category = "";
        if(getIntent().hasExtra("category_link"))
        {
            category = getIntent().getStringExtra("category_link");
            refreshNews(HEADLINES_NEWS_LINK + category);
        }
        else
        {
            getNews(HEADLINES_NEWS_LINK + category);
        }


        if(Build.VERSION.SDK_INT>=23) {
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if(hasLocationPermission!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
            }
            else getLocationAndWeatherForecast();
        }
        else getLocationAndWeatherForecast();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "אין אפשרות להציג את תחזית מזג האוויר", Toast.LENGTH_SHORT).show();
            }
            else getLocationAndWeatherForecast();
        }
    }

    public void getLocationAndWeatherForecast(){
        client = LocationServices.getFusedLocationProviderClient(this);

        LocationCallback callback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location lastLocation = locationResult.getLastLocation();
                getWeatherForecast(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        //request.setInterval(60000);
        //request.setFastestInterval(500);

        if(Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            client.requestLocationUpdates(request,callback,null);
        else if(Build.VERSION.SDK_INT<=22)
            client.requestLocationUpdates(request,callback,null);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }

    public void getNews(String link){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray articles = response.getJSONArray("articles");
                    for(int i = 0; i < articles.length(); i++){
                        JSONObject currentArticle = articles.getJSONObject(i);
                        JSONObject source = currentArticle.getJSONObject("source");
                        String sourceName = source.getString("name");
                        author = currentArticle.getString("author");
                        title = currentArticle.getString("title");
                        description = currentArticle.getString("description");
                        String url = currentArticle.getString("url");
                        urlToImage = currentArticle.getString("urlToImage");
                        String date_time = currentArticle.getString("publishedAt");


                        currentTitle = articles.getJSONObject(0);


                        if(date_time != null){
                            date_time = date_time.substring(0, date_time.length() - 1);
                            String[] publishedAtArr = date_time.split("T");
                            date_time = publishedAtArr[1] + "  " + publishedAtArr[0];
                        }

                        Article article = new Article(sourceName, title, author, description, url,
                                urlToImage, date_time);
                        articlesList.add(article);

                    }

                    ArticlesListFragment articlesListFragment = ArticlesListFragment.newInstance(articlesList);
                    getSupportFragmentManager().beginTransaction().add(R.id.root_container, articlesListFragment, ARTICLES_LIST_FRAGMENT_TAG).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
        //queue.start();
    }

    public void onClicked(Article article) {
        Intent intent = new Intent(this, SelectedArticleActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.all_category_action:
                refreshNews(HEADLINES_NEWS_LINK);
                notificationCategory = "all";
                try {
                    lastTitle = currentTitle.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.health_category_action:
                refreshNews(HEADLINES_NEWS_LINK + "&category=health");
                notificationCategory = "Health";
                try {
                    lastTitle = currentTitle.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sports_category_action:
                refreshNews(HEADLINES_NEWS_LINK + "&category=sports");
                notificationCategory = "Sports";
                try {
                    lastTitle = currentTitle.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.technology_category_action:
                refreshNews(HEADLINES_NEWS_LINK + "&category=technology");
                notificationCategory = "Technology";
                try {
                    lastTitle = currentTitle.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  R.id.action_notify:

                //HERE SET NOTIFICATION
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setTitle("אתה תקבל התראה על קטגוריית"+ " " + notificationCategory + " " + "במשך כל :")
                builder.setTitle("בחר את זמן ההתראה עבור הקטגוריה הנוכחית :")
                        // .setMessage("If you want to get notification for another category, please change it in the main_menu and tap on the bell again.")
                        .setItems(notificationFrequencies, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                Integer freqMillis = 0;
                                switch (i){
                                    //case 0: freqMillis = 60 * 1000; break;
                                    case 0: freqMillis = 60 * 1000; break;
                                    case 1: freqMillis = 3600 * 1000; break;
                                    case 2: freqMillis = 24 * 3600 * 1000; break;
                                }

                                Intent intent = new Intent(MainActivity.this, NotifierService.class);
                                intent.putExtra("notification_freq", freqMillis);
                                intent.putExtra("notification_category", notificationCategory);
                                PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + freqMillis,pendingIntent);
                                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + freqMillis,pendingIntent);
                                else                                     alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + freqMillis,pendingIntent);

                                Toast.makeText(MainActivity.this, "התראה הופעלה", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("ביטול", null).show();

                break;
            case R.id.action_notify_off:

                AlertDialog.Builder builder2=new AlertDialog.Builder(MainActivity.this);
                builder2.setTitle("האם אתה בטוח ?").setCancelable(true).setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });
                builder2.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this,NotifierService.class);

                        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        stopService(intent);
                        dialogInterface.cancel();

                        Toast.makeText(MainActivity.this, "התראה בוטלה", Toast.LENGTH_SHORT).show();

                    }
                }).show();



        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshNews(String link){

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ARTICLES_LIST_FRAGMENT_TAG);
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        articlesList.clear();
        getNews(link);
    }



    public void getWeatherForecast(Double latitude, Double longitude) {
        String linkToWeather = WEATHER_FORECAST_LINK + "&lat=" + latitude + "&lon=" + longitude + "&units=metric" + "&lang=he";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, linkToWeather, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray forecasts = response.getJSONArray("list");
                    String city = response.getJSONObject("city").getString("name");
                    cityTv.setText("מזג האוויר" + " " + " - " + " " + city);
                    for(int i = 0; i < forecasts.length(); i++){
                        JSONObject currentForecast = forecasts.getJSONObject(i);
                        JSONObject main = currentForecast.getJSONObject("main");
                        String fahrenheit =String.format( "%.2f",Double.valueOf(main.getString("temp"))*1.8 + 32)+ " \u2109";
                        String temperature = main.getString("temp")+ " \u2103";
                        String description = currentForecast.getJSONArray("weather").getJSONObject(0).getString("description");
                        String iconCode = currentForecast.getJSONArray("weather").getJSONObject(0).getString("icon");
                        String time = convertTime(currentForecast.getString("dt_txt"));


                        Weather weather = new Weather(time, description, temperature,fahrenheit,iconCode);
                        weatherForecastsList.add(weather);
                    }

                    if(weatherForecastsList.size() > 0){

                        WeatherFragment weathersListFragment = WeatherFragment.newInstance(weatherForecastsList);
                        getSupportFragmentManager().beginTransaction().add(R.id.weather_container, weathersListFragment, WEATHER_LIST_FRAGMENT_TAG).commit();



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);

    }


    private String convertTime(String time) {

        Locale locale = new Locale ( "iw" , "IL" );
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("EEEE HH:mm\n" + "dd MMMM yyyy", locale);
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }

    public static String optString(JSONObject json, String key)
    {
        if (json.isNull(key))
            return null;
        else
            return json.optString(key, null);
    }

}