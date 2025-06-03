package com.example.tageatproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class fragment_search extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView recentSearchList;
    private RecentSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activty_search), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        recentSearchList = findViewById(R.id.recent_search_list);
        recentSearchList.setLayoutManager(new LinearLayoutManager(this));

        List<String> recentSearches = RecentSearchManager.getRecentSearches(this);
        adapter = new RecentSearchAdapter(recentSearches);
        recentSearchList.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchInput.getText().toString().trim();

                if (!keyword.isEmpty()) {
                    RecentSearchManager.saveSearch(fragment_search.this, keyword);
                    Intent intent = new Intent(fragment_search.this, fragment_search.class);
                    intent.putExtra("search_keyword", keyword);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> recentSearches = RecentSearchManager.getRecentSearches(this);
        adapter = new RecentSearchAdapter(recentSearches);
        recentSearchList.setAdapter(adapter);
    }
}
