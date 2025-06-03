package com.example.tageatproject.api;

import android.util.Log;

import com.example.tageatproject.firebase.FirestoreUploader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class NaverPlaceFetcher {

    private static final String TAG = "NaverPlaceFetcher";

    public static void handlePlaceResult(String resultJson) {
        try {
            JSONObject jsonObject = new JSONObject(resultJson);
            JSONArray items = jsonObject.getJSONArray("items");

            if (items.length() > 0) {
                JSONObject place = items.getJSONObject(0);

                String rawTitle = place.getString("title");
                String title = rawTitle.replaceAll("<.*?>", "");

                String address = place.optString("roadAddress", place.getString("address"));
                String category = place.getString("category");
                String link = place.getString("link");

                List<String> tags = new ArrayList<>();
                if (category.contains("카페")) tags.add("카페");
                if (category.contains("디저트")) tags.add("디저트");


                ImageFetcher.fetchImage(title, imageUrl -> {
                    FirestoreUploader uploader = new FirestoreUploader();
                    uploader.uploadPlace(title, address, category, tags, imageUrl, link);
                });

            } else {
                Log.w(TAG, "No items found in API result");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing result JSON", e);
        }
    }
}
