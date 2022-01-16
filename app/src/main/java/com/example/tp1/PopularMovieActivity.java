package com.example.tp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tp1.adapter.MovieListAdapter;
import com.example.tp1.model.Movie;
import com.example.tp1.model.PopularMovie;
import com.example.tp1.services.MovieService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopularMovieActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    MovieListAdapter adapter;
    GridLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES=10;
    private int currentPage = PAGE_START;

    private MovieService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movie);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_popular);
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new MovieListAdapter(this);

        //rv.setLayoutManager(new GridLayoutManager(this, 2));
        linearLayoutManager = new GridLayoutManager(this, 2);

        //linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        movieService = new Retrofit.Builder()
                .baseUrl(MovieService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService.class);

        loadFirstPage();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_popular:
                return true;
            case R.id.action_upcoming:
                startActivity(new Intent(getApplicationContext(),UpcomingMovieActivity.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.search:
                startActivity(new Intent(getApplicationContext(),SearchMovieActivity.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.favoris:
                startActivity(new Intent(getApplicationContext(),FavorisActivity.class));
                overridePendingTransition(0,0);
                return true;
        }
        return false;
    }


    private void loadFirstPage() {
        //Log.d(TAG, "loadFirstPage: ");

        callTopRatedMoviesApi().enqueue(new Callback<PopularMovie>() {
            @Override
            public void onResponse(Call<PopularMovie> call, Response<PopularMovie> response) {
                // Got data. Send it to adapter

                List<Movie> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PopularMovie> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    /**
     * @param response extracts List<{@link Movie>} from response
     * @return
     */
    private List<Movie> fetchResults(Response<PopularMovie> response) {
        PopularMovie topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        //Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<PopularMovie>() {
            @Override
            public void onResponse(Call<PopularMovie> call, Response<PopularMovie> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Movie> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PopularMovie> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<PopularMovie> callTopRatedMoviesApi() {
        return movieService.getListPopularMovie(
                "dbe38ae140b51dbf69c6827c755d3fbe",
                currentPage
        );
    }


}