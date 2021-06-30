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
import com.example.client_serveraplication.api.ApiClient;
import com.example.client_serveraplication.models.Article;
import com.example.client_serveraplication.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBitcoin extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "950191008b904f3686e6f444b5e1aa32";
    private RecyclerView recyclerView;
    private Adapter adapter;
    private TextView everything2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Article> articles = new ArrayList<>();

    public static FragmentBitcoin newInstance() {
        FragmentBitcoin fragment = new FragmentBitcoin();
        return fragment;
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bitcoin, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);

        everything2 = view.findViewById(R.id.everything2);

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

        everything2.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        ApiClient.getInstance()
                .getApiInterface()
                .getBitcoinNews("bitcoin",API_KEY )
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
                            onItemClickListener();
                            everything2.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);

                        } else {
                            everything2.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        everything2.setVisibility(View.INVISIBLE);
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
