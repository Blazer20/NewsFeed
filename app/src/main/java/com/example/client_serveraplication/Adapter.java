package com.example.client_serveraplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client_serveraplication.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Article> articles;
    private OnItemClickListener mOnItemClickListener;

    public Adapter() {
        articles = new ArrayList<>();
    }

    public void setItems(List<Article> items) {
        articles.clear();
        articles.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<Article> items) {
        articles.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new MyViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Article model = articles.get(position);
        holder.bindView(model);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, desc, author, published_at, source, time;
        ImageView imageView;
        OnItemClickListener onItemClickListener;

        Article article;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            published_at = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.img);

            this.onItemClickListener = onItemClickListener;
        }


        public void bindView(Article model) {
            article = model;

            Picasso.get().load(model.getUrlToImage()).into(imageView);

            title.setText(model.getTitle());
            desc.setText(model.getDescriprion());
            source.setText(model.getSource().getName());
            time.setText("\u2022" + Utils.DateToTimeFormat(model.getPublishedAt()));
            published_at.setText(Utils.DateFormat(model.getPublishedAt()));
            author.setText(model.getAuthor());
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(article);
        }
    }
}
