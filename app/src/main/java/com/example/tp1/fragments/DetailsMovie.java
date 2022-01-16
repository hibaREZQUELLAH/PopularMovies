package com.example.tp1.fragments;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tp1.FavorisActivity;
import com.example.tp1.R;
import com.example.tp1.model.Genre;
import com.example.tp1.model.MovieDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailsMovie extends AppCompatActivity {

    MovieDetail movie_detail = new MovieDetail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        if(getIntent().getExtras() != null) {
            movie_detail = (MovieDetail) getIntent().getSerializableExtra("detail");
        }

        Button button = (Button) findViewById(R.id.ajouter);

        ImageView image = findViewById(R.id.image_movie);
        TextView description = findViewById(R.id.description);
        TextView date = findViewById(R.id.date_value);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        TextView title = findViewById(R.id.title);

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie_detail.getPoster_path()).into(image);
        description.setText(movie_detail.getOverview());
        date.setText(movie_detail.getRelease_date());
        title.setText(movie_detail.getTitle());
        for (Genre genre : movie_detail.getGenres()) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(genre.getName());
            if (linearLayout != null) {
                linearLayout.addView(textView);
            }
        }

        button.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FavorisActivity.class);
            intent.putExtra("favoris", movie_detail);
            v.getContext().startActivity(intent);
        });
    }
}
