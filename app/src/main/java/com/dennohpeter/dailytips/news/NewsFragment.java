package com.dennohpeter.dailytips.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    private ArrayList<NewsModel> newsModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsModels = new ArrayList<>();
        newsModels.add(new NewsModel("Macias Vs Shaka"));
        newsModels.add(new NewsModel("Holly Cow"));
        newsModels.add(new NewsModel("Breaking News"));
        newsModels.add(new NewsModel("Damn It"));
        newsModels.add(new NewsModel("Macias Vs Shaka"));
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.news_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NewsAdapter newsAdapter = new NewsAdapter();
        newsAdapter.addAll(newsModels);
        recyclerView.setAdapter(newsAdapter);
        return root;
    }
}