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

                // 주소 → 위도/경도 변환 후 이미지 검색 및 Firestore 업로드
                GeoCoder.fetchCoordinates(address, new GeoCoder.GeoCallback() {
                    @Override
                    public void onResult(double lat, double lng) {
                        ImageFetcher.fetchImage(title, imageUrl -> {
                            FirestoreUploader uploader = new FirestoreUploader();
                            uploader.uploadPlace(title, address, category, tags, imageUrl, link, lat, lng);
                        });
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "주소 → 좌표 변환 실패");
                    }
                });

            } else {
                Log.w(TAG, "API 응답에 장소 없음");
            }

        } catch (Exception e) {
            Log.e(TAG, "결과 JSON 파싱 오류", e);
        }
    }
}
