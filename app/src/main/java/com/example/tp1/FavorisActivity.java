package com.example.tp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.tp1.model.MovieDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavorisActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    public MovieDetail movieDetail = new MovieDetail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.favoris);
        if(getIntent().getExtras() != null){
            movieDetail = (MovieDetail) getIntent().getSerializableExtra("favoris");
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            ImageView image = new ImageView(this);
            Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movieDetail.getPoster_path()).into(image);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
            image.setMaxHeight(20);
            image.setMaxWidth(20);

            // Adds the view to the layout
            linearLayout.addView(image);

        }
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
                return true;
        }
        return false;
    }
}