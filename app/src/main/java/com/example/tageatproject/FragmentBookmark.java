package com.example.tageatproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tageatproject.adapter.PlaceAdapter;
import com.example.tageatproject.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentBookmark extends Fragment {

    public FragmentBookmark() {
        // 기본 생성자

    }

    private RecyclerView bookmarkRecyclerView;
    private PlaceAdapter adapter;
    private List<Place> bookmarkList;
    private FirebaseFirestore db;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.activity_fragment_bookmark), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bookmarkRecyclerView = view.findViewById(R.id.bookmark_list);
        bookmarkList = new ArrayList<>();
        adapter = new PlaceAdapter(bookmarkList);
        bookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookmarkRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadBookmarks();

        return view;
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
