package com.example.tageatproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tageatproject.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceDetailActivity extends AppCompatActivity {

    private static final String TAG = "PlaceDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.place_detail_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.detail_image);
        TextView nameView = findViewById(R.id.detail_name);
        TextView addressView = findViewById(R.id.detail_address);
        TextView linkView = findViewById(R.id.detail_link);
        TextView tagsView = findViewById(R.id.detail_tags);

        Place place = (Place) getIntent().getSerializableExtra("place");

        if (place != null) {
            nameView.setText(place.getName());
            addressView.setText(place.getAddress());
            tagsView.setText(place.getTags().toString());

            Glide.with(this)
                    .load(place.getImageUrl())
                    .into(imageView);

            linkView.setText("방문하기");
            linkView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getLink()));
                startActivity(intent);
            });

            // ✅ 태그 기록 저장
            recordTagUsage(place.getTags());
        }
    }

    private void recordTagUsage(List<String> tags) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Long> tagHistory;

                    if (documentSnapshot.exists()) {
                        // 문서가 존재하면 기존 기록 가져오기
                        tagHistory = (Map<String, Long>) documentSnapshot.get("tagHistory");
                        if (tagHistory == null) {
                            tagHistory = new HashMap<>();
                        }
                    } else {
                        // 문서가 없으면 새로 생성
                        tagHistory = new HashMap<>();
                        db.collection("users").document(userId)
                                .set(new HashMap<String, Object>())
                                .addOnSuccessListener(unused -> Log.d(TAG, "새 사용자 문서 생성 완료"))
                                .addOnFailureListener(e -> Log.e(TAG, "새 사용자 문서 생성 실패", e));
                    }

                    // 태그 사용 기록 누적
                    for (String tag : tags) {
                        long count = tagHistory.getOrDefault(tag, 0L);
                        tagHistory.put(tag, count + 1);
                    }

                    // Firestore에 업데이트
                    db.collection("users").document(userId)
                            .update("tagHistory", tagHistory)
                            .addOnSuccessListener(unused -> Log.d(TAG, "태그 기록 업데이트 완료"))
                            .addOnFailureListener(e -> Log.e(TAG, "태그 기록 업데이트 실패", e));
                })
                .addOnFailureListener(e -> Log.e(TAG, "사용자 문서 로딩 실패", e));
    }
}