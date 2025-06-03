package com.example.tageatproject.api;

import android.util.Log;

import com.example.tageatproject.firebase.FirestoreUploader;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.*;

public class TagBasedPlaceCollector {
    private static final String TAG = "PlaceCollector";

    private static final String CLIENT_ID = "YOUR_NAVER_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_NAVER_CLIENT_SECRET";

    public static void collectPlacesFromTags(String region) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("tags")
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (var doc : snapshot.getDocuments()) {
                        String tag = doc.getId();
                        searchAndSave(region + " " + tag);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "태그 불러오기 실패", e));
    }

    private static void searchAndSave(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String url = "https://openapi.naver.com/v1/search/local.json?query=" + encodedQuery + "&display=5&sort=random";

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-Naver-Client-Id", "5qHeGmwXtdnpnsmPTy5c")
                    .addHeader("X-Naver-Client-Secret", "3umgTFWw6l")
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "API 요청 실패", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        JSONArray items = jsonObj.getJSONArray("items");

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject obj = items.getJSONObject(i);

                            String title = obj.getString("title").replaceAll("<.?>", "");
                            String address = obj.optString("roadAddress", obj.getString("address"));
                            String category = obj.getString("category");
                            String link = obj.getString("link");

                            // 임시 태그: 지역 + 검색어
                            List<String> tags = List.of(query.split(" "));

                            // (선택) 위도/경도 변환 로직 추가 가능
                            double lat = 0.0;
                            double lng = 0.0;

                            FirestoreUploader uploader = new FirestoreUploader();
                            uploader.uploadPlace(title, address, category, tags, "", link, lat, lng);  // 이미지 추후 추가
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "JSON 파싱 실패", e);
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "검색 요청 실패", e);
        }
    }
}