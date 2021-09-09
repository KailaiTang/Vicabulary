package com.laioffer.vicabulary.ui.select;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.vicabulary.R;
import com.laioffer.vicabulary.model.Movie;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder> {
    interface PlayListener {
        void onClick(Movie movie);
    }

    private List<Movie> movies = new ArrayList<>();
    private PlayListener playListener;

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
    }

    @Override
    public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item, parent, false);
        return new SelectViewHolder(view);
    }

/*    This is set up for internal test purposes.    */

//    @Override
//    public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {
//        int[] cover_set = {R.drawable.lion_king, R.drawable.ice_age, R.drawable.coco,
//                R.drawable.mickey_mouse, R.drawable.frozen_2};
//        String[] title_set = {"Lion King", "Ice Age", "Coco", "Mickey Mouse", "Frozen II"};
//        holder.Cover.setImageResource(cover_set[position % cover_set.length]);
//        holder.Button.setImageResource(R.drawable.ic_play_circle_filled_shallow_60dp);
//        holder.Duration.setText("2:00");
//        holder.Publisher.setText("Disney");
//        holder.Title.setText(title_set[position % title_set.length]);
//    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {
        Movie movie = movies.get(position);
        if (movie.getCover() == null) {
            holder.Cover.setImageResource(R.drawable.ice_age);
        } else {
            Picasso.get().load(movie.getCover()).into(holder.Cover);
        }
        holder.Button.setImageResource(R.drawable.ic_play_circle_filled_shallow_60dp);
        holder.Duration.setText(movie.getDuration());
        holder.Publisher.setText(movie.getPublisher());
        holder.Title.setText(movie.getName());
        holder.Button.setOnClickListener(
                v -> {
                    playListener.onClick(movie);
                });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public static class SelectViewHolder extends RecyclerView.ViewHolder {
       ImageView Cover;
       ImageView Button;
       TextView Title;
       TextView Duration;
       TextView Publisher;

       public SelectViewHolder(View itemView) {
           super(itemView);
           Cover = itemView.findViewById(R.id.cover);
           Button = itemView.findViewById(R.id.button);
           Title = itemView.findViewById(R.id.title);
           Duration = itemView.findViewById(R.id.duration);
           Publisher = itemView.findViewById(R.id.publisher);
       }
   }
}
