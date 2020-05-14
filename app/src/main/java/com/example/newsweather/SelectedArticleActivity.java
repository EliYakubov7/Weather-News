package com.example.newsweather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class SelectedArticleActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = "מידע אודות המאמר";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Objects.requireNonNull(getSupportActionBar()).setTitle(s);

        setContentView(R.layout.activity_selected_article);

        final Article article = (Article) getIntent().getSerializableExtra("article");

        ImageView articleImageView = findViewById(R.id.selected_article_iv);
        TextView titleTv = findViewById(R.id.selected_article_title_tv);
        TextView authorAndSourceNameTv = findViewById(R.id.selected_article_author_and_source_name_tv);
        TextView timeDateTv = findViewById(R.id.selected_article_time_date_tv);
        TextView descriptionTv = findViewById(R.id.selected_article_description_tv);
//        TextView contentTv = findViewById(R.id.selected_article_content_tv);


        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(800);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        Button readFullArticleBtn = findViewById(R.id.watch_full_article_button);
        readFullArticleBtn.startAnimation(animation);

        readFullArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.clearAnimation();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Objects.requireNonNull(article).getUrl()));
                startActivity(browserIntent);
            }
        });



//        Glide.with(articleImageView.getContext()).
//                load(Objects.requireNonNull(article).getImageUrl()).into(articleImageView);

        Picasso.get().load(Objects.requireNonNull(article).getImageUrl()).into(articleImageView);
        titleTv.setText(article.getTitle());
        timeDateTv.setText(article.getDateTime());


        if(article.getAuthor().contentEquals("null")) {
            authorAndSourceNameTv.setText("שם הכתב לא נמצא" + ", " + article.getSourceName());
        }
        else {
            authorAndSourceNameTv.setText(article.getAuthor() + ", " + article.getSourceName());
        }


        if(article.getDescription().contentEquals("null")) {
            descriptionTv.setText("תיאור המאמר לא נמצא");
        }
        else {
            descriptionTv.setText(article.getDescription());
        }

    }
}
