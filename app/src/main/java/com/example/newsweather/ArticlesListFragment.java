package com.example.newsweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ArticlesListFragment extends Fragment {

    private static List<Article> articlesList;

    interface OnArticleFragmentListener{
        void onClicked(Article article);
    }

    private OnArticleFragmentListener callBack;

    static ArticlesListFragment newInstance(List<Article> i_articlesList){
        ArticlesListFragment fragment = new ArticlesListFragment();
        articlesList = i_articlesList;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_fragment, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArticleAdapter adapter = new ArticleAdapter(articlesList);
        adapter.setListener(new ArticleAdapter.ArticleListener() {
            @Override
            public void onArticleClicked(int position, View view) {
                callBack.onClicked(articlesList.get(position));
            }
        });

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callBack = (OnArticleFragmentListener)context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement OnArticleFragmentListener interface");
        }
    }

}
