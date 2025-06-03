package com.example.tageatproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tageatproject.adapter.PlaceAdapter;
import com.example.tageatproject.model.Place;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentTagSearch extends Fragment {

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView placeListView;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;

    private FirebaseFirestore db;

    public FragmentTagSearch() {
        // 기본 생성자
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_search, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.activity_tag_search), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = view.findViewById(R.id.search_input);
        searchButton = view.findViewById(R.id.search_button);
        placeListView = view.findViewById(R.id.recent_search_list);

        db = FirebaseFirestore.getInstance();
        placeList = new ArrayList<>();
        placeAdapter = new PlaceAdapter(placeList);

        placeListView.setLayoutManager(new LinearLayoutManager(getContext()));
        placeListView.setAdapter(placeAdapter);

        searchButton.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();
            if (!keyword.isEmpty()) {
                fetchPlacesByTag(keyword);
            }
        });

        return view;
    }

    private void fetchPlacesByTag(String tag) {
        Log.d("tag_search", "태그 검색 시작: " + tag);
        placeList.clear();

        db.collection("places")
                .whereArrayContains("tags", tag)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Log.d("tag_search", "검색 결과 개수: " + querySnapshot.size());
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Place place = doc.toObject(Place.class);
                        placeList.add(place);
                        Log.d("tag_search", "가져온 장소: " + place.getName());
                    }
                    placeAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("tag_search", "Firestore 태그 검색 실패", e));
    }
}
