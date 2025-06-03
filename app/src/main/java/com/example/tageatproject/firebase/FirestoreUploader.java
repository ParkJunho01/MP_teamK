package com.example.tageatproject.firebase;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FirestoreUploader {
    private static final String TAG = "FirestoreUploader";
    private static final String COLLECTION_NAME = "places";
    private final FirebaseFirestore db;
    public FirestoreUploader() {
        db = FirebaseFirestore.getInstance();
    }

    public void uploadPlace(
            @NonNull String name,
            @NonNull String address,
            @NonNull String category,
            @NonNull List<String> tags,
            @NonNull String imageUrl,
            @NonNull String link,
            double latitude,
            double longitude
    ) {
        Map<String, Object> place = new HashMap<>();
        place.put("name", name);
        place.put("address", address);
        place.put("category", category);
        place.put("tags", tags);
        place.put("imageUrl", imageUrl);
        place.put("link", link);
        place.put("latitude", latitude);
        place.put("longitude", longitude);

        db.collection(COLLECTION_NAME)
                .add(place)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding document", e);
                });
    }

}
