package com.example.tageatproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tageatproject.model.Place;

import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.place_detail_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.detail_image);
        TextView nameView = findViewById(R.id.detail_name);
        TextView addressView = findViewById(R.id.detail_address);
        TextView linkView = findViewById(R.id.detail_link);
        TextView tagsView = findViewById(R.id.detail_tags);
        Place place = (Place) getIntent().getSerializableExtra("place");

        if (place != null) {
            nameView.setText(place.getName());
            addressView.setText(place.getAddress());
            tagsView.setText(place.getTags().toString());

            Glide.with(this)
                    .load(place.getImageUrl())
                    .into(imageView);


            linkView.setText("방문하기");
            linkView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getLink()));
                startActivity(intent);
            });
        }
    }
}