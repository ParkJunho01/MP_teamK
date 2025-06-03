package com.example.tageatproject.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class ImageFetcher {
    private static final String TAG = "ImageFetcher";

    private static final String CLIENT_ID = "YOUR_NAVER_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_NAVER_CLIENT_SECRET";

    public interface ImageCallback {
        void onResult(String imageUrl);
    }

    public static void fetchImage(String query, ImageCallback callback) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String url = "https://openapi.naver.com/v1/search/image?query=" + encodedQuery + "&display=1&sort=sim";

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-Naver-Client-Id", CLIENT_ID)
                    .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Image API request failed", e);
                    callback.onResult("https://default-image.com/default.jpg"); // 실패 시 기본 이미지
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray items = jsonObject.getJSONArray("items");
                        if (items.length() > 0) {
                            String imageUrl = items.getJSONObject(0).getString("link");
                            callback.onResult(imageUrl);
                        } else {
                            callback.onResult("https://default-image.com/default.jpg");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing image result", e);
                        callback.onResult("https://default-image.com/default.jpg");
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Encoding error", e);
            callback.onResult("https://default-image.com/default.jpg");
        }
    }
}
