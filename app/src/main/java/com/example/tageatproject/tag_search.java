package com.example.tageatproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tageatproject.adapter.PlaceAdapter;
import com.example.tageatproject.model.Place;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class tag_search extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView placeListView;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_fragment_search);

        View rootView = findViewById(R.id.activity_tag_search);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        placeListView = findViewById(R.id.recent_search_list);

        db = FirebaseFirestore.getInstance();
        placeList = new ArrayList<>();
        placeAdapter = new PlaceAdapter(placeList);

        placeListView.setLayoutManager(new LinearLayoutManager(this));
        placeListView.setAdapter(placeAdapter);

        searchButton.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();

            if (!keyword.isEmpty()) {
                fetchPlacesByTag(keyword);
            }
        });
    }

    private void fetchPlacesByTag(String tag) {
        placeList.clear();

        db.collection("places")
                .whereArrayContains("tags", tag)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        Place place = doc.toObject(Place.class);
                        placeList.add(place);
                    }
                    placeAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("tag_search", "Firestore 태그 검색 실패", e));
    }
}
