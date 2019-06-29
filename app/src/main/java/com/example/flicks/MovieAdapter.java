package com.example.flicks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flicks.models.Config;
import com.example.flicks.models.Movie;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    List<Movie> movies;
    Context context;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    Config config;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);

        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie movie = movies.get(i);
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        //TODO: set image

        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPoster_Path());

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .bitmapTransform(new RoundedCornersTransformation(context, 30, 0))
                .into(viewHolder.ivPosterImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    // view Holder static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosterImage = itemView.findViewById(R.id.tvPosterImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview  = itemView.findViewById(R.id.tvOverview);


        }
    }
}
