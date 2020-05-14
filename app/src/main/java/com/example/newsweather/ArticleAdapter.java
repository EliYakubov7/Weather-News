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

import java.util.ArrayList;
import java.util.List;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articles;
    private ArticleListener listener;

    interface ArticleListener {
        void onArticleClicked(int position, View view);
    }

    void setListener(ArticleListener listener) {
        this.listener = listener;
    }

    ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImageIv;
        TextView articleTitleTv;
        TextView articleDateTimeTv;

        ArticleViewHolder(View itemView) {
            super(itemView);

            articleImageIv = itemView.findViewById(R.id.article_image_iv);
            articleTitleTv = itemView.findViewById(R.id.article_title_tv);
            articleDateTimeTv = itemView.findViewById(R.id.article_date_time_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        listener.onArticleClicked(getAdapterPosition(),view);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_cell,parent,false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.articleTitleTv.setText(article.getTitle());
        holder.articleDateTimeTv.setText(article.getDateTime());

        //String urlToImage = MainActivity.urlToImage;

        Picasso.get().load(article.getImageUrl()).resize(300, 200).into(holder.articleImageIv);

//        Glide.with(holder.articleImageIv.getContext()).load(article.getImageUrl())
//                .override(300, 200).centerCrop().into(holder.articleImageIv);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
