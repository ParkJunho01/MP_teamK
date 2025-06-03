package com.example.tageatproject;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tageatproject.adapter.PlaceAdapter;
import com.example.tageatproject.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class fragment_bookmark extends AppCompatActivity {

    private RecyclerView bookmarkRecyclerView;
    private PlaceAdapter adapter;
    private List<Place> bookmarkList;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment_bookmark);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_fragment_bookmark), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bookmarkRecyclerView = findViewById(R.id.bookmark_list);
        bookmarkList = new ArrayList<>();
        adapter = new PlaceAdapter(bookmarkList);
        bookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookmarkRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadBookmarks();
    }

    private void loadBookmarks() {
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    bookmarkList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Place place = doc.toObject(Place.class);
                        bookmarkList.add(place);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Bookmark", "불러오기 실패", e));
    }
}
