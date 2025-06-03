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

public class GeoCoder {

    public interface GeoCallback {
        void onResult(double lat, double lng);
        void onFailure();
    }

    public static void fetchCoordinates(String address, GeoCallback callback) {
        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + encodedAddress;

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-NCP-APIGW-API-KEY-ID", "86zf15o9kd")
                    .addHeader("X-NCP-APIGW-API-KEY", "3m4eVIbSZ6j9Mks2FOheh9xrnDNJR5JF70MDXxTV")
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("GeoCoder", "지오코딩 요청 실패", e);
                    callback.onFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String json = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(json);
                            JSONArray addresses = obj.getJSONArray("addresses");
                            if (addresses.length() > 0) {
                                JSONObject addr = addresses.getJSONObject(0);
                                double lat = addr.getDouble("y");
                                double lng = addr.getDouble("x");
                                callback.onResult(lat, lng);
                            } else {
                                callback.onFailure();
                            }
                        } catch (Exception e) {
                            Log.e("GeoCoder", "지오코딩 결과 파싱 실패", e);
                            callback.onFailure();
                        }
                    } else {
                        callback.onFailure();
                    }
                }
            });

        } catch (Exception e) {
            Log.e("GeoCoder", "주소 인코딩 오류", e);
            callback.onFailure();
        }
    }
}
