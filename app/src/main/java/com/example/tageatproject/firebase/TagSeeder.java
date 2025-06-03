package com.example.tageatproject.firebase;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TagSeeder {
    private static final String TAG = "TagSeeder";

    public static void seedInitialTags() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<String> initialTags = Arrays.asList(
                "coffee", "dessert", "korean", "japanese", "chinese", "western",
                "spicy", "vegan", "brunch", "quiet", "study", "romantic",
                "pet_friendly", "group_meal", "takeaway", "night_open"
        );

        for (String tag : initialTags) {
            Map<String, Object> tagData = new HashMap<>();
            tagData.put("name", tag);  // 필요하면 태그 이름 외에 다른 정보도 추가 가능

            db.collection("tags").document(tag)
                    .set(tagData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "태그 추가됨: " + tag))
                    .addOnFailureListener(e -> Log.e(TAG, "태그 추가 실패: " + tag, e));
        }
    }
}