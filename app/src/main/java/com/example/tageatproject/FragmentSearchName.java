package com.example.tageatproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentSearchName extends Fragment {

    public FragmentSearchName() {
        // 기본 생성자

    }

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView recentSearchList;
    private RecentSearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.activty_search), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = view.findViewById(R.id.search_input);
        searchButton = view.findViewById(R.id.search_button);
        recentSearchList = view.findViewById(R.id.recent_search_list);
        recentSearchList.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> recentSearches = RecentSearchManager.getRecentSearches(getContext());
        adapter = new RecentSearchAdapter(recentSearches);
        recentSearchList.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();
            if (!keyword.isEmpty()) {
                RecentSearchManager.saveSearch(getContext(), keyword);
                searchInput.setText("");

                // 리스트 갱신
                List<String> updatedSearches = RecentSearchManager.getRecentSearches(getContext());
                adapter = new RecentSearchAdapter(updatedSearches);
                recentSearchList.setAdapter(adapter);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<String> recentSearches = RecentSearchManager.getRecentSearches(getContext());
        adapter = new RecentSearchAdapter(recentSearches);
        recentSearchList.setAdapter(adapter);
    }
}
