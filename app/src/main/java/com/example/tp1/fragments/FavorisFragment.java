package com.example.tp1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.tp1.R;
import com.example.tp1.model.MovieDetail;

public class FavorisFragment extends Fragment {

    public MovieDetail movieDetail = new MovieDetail();

    public FavorisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favoris, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity().getIntent().getExtras() != null){
            movieDetail = (MovieDetail) getActivity().getIntent().getSerializableExtra("favoris");
            LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
            ImageView image = new ImageView(view.getContext());
            Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movieDetail.getPoster_path()).into(image);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
            image.setMaxHeight(20);
            image.setMaxWidth(20);

            // Adds the view to the layout
            linearLayout.addView(image);

        }
    }
}