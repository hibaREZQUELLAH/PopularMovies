package com.example.tp1.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tp1.R;
import com.example.tp1.fragments.DetailsMovie;
import com.example.tp1.model.Movie;
import com.example.tp1.model.MovieDetail;
import com.example.tp1.services.MovieService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Movie> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    public MovieDetail movie_detail;

    public MovieListAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new MovieListAdapter.LoadingVH(v2);
                break;
        }
        return viewHolder;
    }



    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.movie_item_image, parent, false);
        viewHolder = new MovieListAdapter.MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*Movie movie = movieResults.get(position);
        MovieListAdapter.MovieVH movieVH = (MovieListAdapter.MovieVH) holder;
        Glide.with(holder.itemView).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(movieVH.mPosterImg);
        holder.itemView.setOnClickListener(v -> {
            MovieService movieService = new Retrofit.Builder()
                    .baseUrl(MovieService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MovieService.class);
            movieService.getMovie(movie.getId(),"dbe38ae140b51dbf69c6827c755d3fbe").enqueue(new Callback<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                    movie_detail = new MovieDetail();
                    movie_detail = response.body();
                    Intent intent = new Intent(v.getContext(), DetailsMovie.class);
                    intent.putExtra("detail", movie_detail);
                    v.getContext().startActivity(intent);
                }

                @Override
                public void onFailure(Call<MovieDetail> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
        });*/
        Movie result = movieResults.get(position); // Movie
        switch (getItemViewType(position)) {
            case ITEM:
                final MovieListAdapter.MovieVH movieVH = (MovieListAdapter.MovieVH) holder;

                Glide
                        .with(holder.itemView)
                        .load("https://image.tmdb.org/t/p/w500"+ result.getPoster_path())
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .into(movieVH.mPosterImg);
                holder.itemView.setOnClickListener(v -> {
                    MovieService movieService = new Retrofit.Builder()
                            .baseUrl(MovieService.ENDPOINT)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(MovieService.class);
                    movieService.getMovie(result.getId(),"dbe38ae140b51dbf69c6827c755d3fbe").enqueue(new Callback<MovieDetail>() {
                        @Override
                        public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                            movie_detail = new MovieDetail();
                            movie_detail = response.body();
                            Intent intent = new Intent(v.getContext(), DetailsMovie.class);
                            intent.putExtra("detail", movie_detail);
                            v.getContext().startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<MovieDetail> call, Throwable t) {
                            Log.e("error", t.getMessage());
                        }
                    });
                });

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Movie r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    public void remove(Movie r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movieResults.get(position);
    }


    public static class MovieVH extends RecyclerView.ViewHolder{

        private ImageView mPosterImg;
        private ProgressBar mProgress;

        public MovieVH(View itemView) {
            super(itemView);

            mPosterImg = (ImageView) itemView.findViewById(R.id.imageView);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}