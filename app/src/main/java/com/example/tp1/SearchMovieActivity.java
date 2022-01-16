package com.example.tp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class SearchMovieActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.search);
        EditText queryEditText = findViewById(R.id.query_edit_text);
        ImageView querySearchButton = findViewById(R.id.query_search_button);
        RecyclerView resultsRecycleView = findViewById(R.id.results_recycle_view);
        resultsRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        querySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryEditText.getText() != null) {
                    String query = queryEditText.getText().toString();
                    if (query.equals("") || query.equals(" ")) {
                        Toast.makeText(SearchMovieActivity.this, "Please enter any text...", Toast.LENGTH_SHORT).show();
                    } else {
                        queryEditText.setText("");
                        String finalQuery = query.replaceAll(" ", "+");
                        MovieService movieService = new Retrofit.Builder()
                                .baseUrl(MovieService.ENDPOINT)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(MovieService.class);

                        Call<PopularMovie> movieResponseCall = movieService.getMoviesByQuery("dbe38ae140b51dbf69c6827c755d3fbe", finalQuery);
                        movieResponseCall.enqueue(new Callback<PopularMovie>() {

                            @Override
                            public void onResponse(@NonNull Call<PopularMovie> call, @NonNull Response<PopularMovie> response) {
                                PopularMovie movieResponse = response.body();
                                if (movieResponse != null) {
                                    List<Movie> movieResponseResults = movieResponse.getResults();
                                    MovieListAdapter moviesAdapter = new MovieListAdapter(SearchMovieActivity.this);
                                    moviesAdapter.addAll(movieResponseResults);
                                    resultsRecycleView.setAdapter(moviesAdapter);
                                    resultsRecycleView.setLayoutManager(new GridLayoutManager(SearchMovieActivity.this, 2));
                                } else {
                                    Toast.makeText(SearchMovieActivity.this, "movieResponse.getResults().get(0).getTitle()", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(@NonNull Call<PopularMovie> call, @NonNull Throwable t) {

                            }
                        });
                    }
                }

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_popular:
                startActivity(new Intent(getApplicationContext(), PopularMovieActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_upcoming:
                startActivity(new Intent(getApplicationContext(), UpcomingMovieActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.search:
               return true;

            case R.id.favoris:
                startActivity(new Intent(getApplicationContext(),FavorisActivity.class));
                overridePendingTransition(0,0);
                return true;
        }
        return false;
    }
}
