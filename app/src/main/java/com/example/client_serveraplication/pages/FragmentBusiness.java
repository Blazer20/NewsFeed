package com.example.client_serveraplication.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.client_serveraplication.Adapter;
import com.example.client_serveraplication.NewsDetailActivity;
import com.example.client_serveraplication.R;
import com.example.client_serveraplication.Utils;
import com.example.client_serveraplication.api.ApiClient;
import com.example.client_serveraplication.models.Article;
import com.example.client_serveraplication.models.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBusiness extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "950191008b904f3686e6f444b5e1aa32";
    private RecyclerView recyclerView;
    private Adapter adapter;
    private TextView topHeadLine;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentBusiness newInstance() {
        FragmentBusiness fragment = new FragmentBusiness();
        return fragment;
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);

        topHeadLine = view.findViewById(R.id.topHeadLines);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        LoadJson(true);
        return view;
    }



    public void LoadJson(final boolean refresh) {

        topHeadLine.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        final String country = Utils.getCountry();

        ApiClient.getInstance()
                .getApiInterface()
                .getBusinessNews(country, API_KEY)
                .enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getArticles() != null) {
                            if (refresh) {
                                adapter.setItems(response.body().getArticles());
                            } else {
                                adapter.addItems(response.body().getArticles());
                            }
                            topHeadLine.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            topHeadLine.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        onItemClickListener();
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        topHeadLine.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public void onItemClickListener() {

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("author", article.getAuthor());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        LoadJson(true);
    }
}
