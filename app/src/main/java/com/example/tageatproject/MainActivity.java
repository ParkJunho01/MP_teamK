package com.example.tageatproject;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.tageatproject.api.TagBasedPlaceCollector;
import com.example.tageatproject.firebase.TagSeeder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selected = new FragmentHome();
            } else if (itemId == R.id.nav_search) {
                selected = new FragmentTagSearch();
            } else if (itemId == R.id.nav_bookmark) {
                selected = new FragmentBookmark();
            } else if (itemId == R.id.nav_setting) {
                selected = new FragmentSetting();
            } else if (itemId == R.id.nav_recommand) {
                selected = new FragmentRecommend();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selected)
                        .commit();
            }

            return true;
        });
        nav.setSelectedItemId(R.id.nav_home);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Log.d("Firebase", "Firebase Analytics Initialized");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            auth.signInAnonymously()
                    .addOnSuccessListener(result -> Log.d("Auth", "익명 로그인 성공"))
                    .addOnFailureListener(e -> Log.e("Auth", "익명 로그인 실패", e));
        }

//        TagSeeder.seedInitialTags();

//        TagBasedPlaceCollector.collectPlacesFromTags("가천대");

    }
}