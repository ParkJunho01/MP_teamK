package com.example.tageatproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tageatproject.adapter.PlaceAdapter;
import com.example.tageatproject.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FragmentRecommend extends Fragment {

    private Button btnFrequentTags, btnUncommonTags;
    private RecyclerView recommendationList;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;

    private FirebaseFirestore db;
    private String userId;

    public FragmentRecommend() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnFrequentTags = view.findViewById(R.id.btn_frequent_tags);
        btnUncommonTags = view.findViewById(R.id.btn_uncommon_tags);
        recommendationList = view.findViewById(R.id.recommendation_list);

        placeList = new ArrayList<>();
        placeAdapter = new PlaceAdapter(placeList);
        recommendationList.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendationList.setAdapter(placeAdapter);

        // 위치 권한 요청
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }

        btnFrequentTags.setOnClickListener(v -> fetchRecommendations(true));
        btnUncommonTags.setOnClickListener(v -> fetchRecommendations(false));

        return view;
    }

    private void fetchRecommendations(boolean frequent) {
        placeList.clear();
        placeAdapter.notifyDataSetChanged();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Long> tagHistory = (Map<String, Long>) documentSnapshot.get("tagHistory");
                    if (tagHistory == null || tagHistory.isEmpty()) {
                        Toast.makeText(getContext(), "추천할 태그 기록이 없습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<String> selectedTags;

                    if (frequent) {
                        selectedTags = tagHistory.entrySet().stream()
                                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                                .limit(3)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());
                    } else {
                        List<String> allTags = Arrays.asList("coffee", "dessert", "korean", "japanese", "brunch", "spicy", "quiet", "study");
                        selectedTags = new ArrayList<>(allTags);
                        selectedTags.removeAll(tagHistory.keySet());
                    }

                    queryPlacesByTags(selectedTags);
                })
                .addOnFailureListener(e -> Log.e("TagRecommend", "태그 추천 로딩 실패", e));
    }

    private Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private void queryPlacesByTags(List<String> tags) {
        Location currentLocation = getCurrentLocation();
        if (currentLocation == null) {
            Toast.makeText(getContext(), "현재 위치를 가져올 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("places")
                .whereArrayContainsAny("tags", tags)
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (DocumentSnapshot doc : snapshot) {
                        Place place = doc.toObject(Place.class);
                        if (place != null) {
                            Location placeLocation = new Location("");
                            placeLocation.setLatitude(place.getLatitude());
                            placeLocation.setLongitude(place.getLongitude());

                            float distance = currentLocation.distanceTo(placeLocation); // 미터 단위
                            if (distance <= 3000) {
                                placeList.add(place);
                            }
                        }
                    }
                    placeAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("TagRecommend", "장소 추천 검색 실패", e));
    }
}
