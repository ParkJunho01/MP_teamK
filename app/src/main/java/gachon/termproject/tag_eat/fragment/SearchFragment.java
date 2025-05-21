package gachon.termproject.tag_eat.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gachon.termproject.tag_eat.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SearchFragment extends Fragment {
    private EditText searchInput;
    private Button searchButton;
    private RecyclerView recyclerView;
    private RecentSearchAdapter adapter;
    private LinearLayout tagContainer;
    private SharedPreferences prefs;

    private static final String PREFS_NAME = "recent_searches";
    private static final String KEY_SEARCHES = "search_list";
    private final List<String> tags = Arrays.asList("데이트", "혼밥", "한식", "양식", "디저트");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = view.findViewById(R.id.search_input);
        searchButton = view.findViewById(R.id.search_button);
        tagContainer = view.findViewById(R.id.tag_container);
        recyclerView = view.findViewById(R.id.recent_search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        adapter = new RecentSearchAdapter(getSearchList());
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> handleSearch(searchInput.getText().toString()));

        for (String tag : tags) {
            Button tagBtn = new Button(getContext());
            tagBtn.setText("#" + tag);
            tagBtn.setOnClickListener(v -> handleSearch(tag));
            tagContainer.addView(tagBtn);
        }

        return view;
    }

    private void handleSearch(String keyword) {
        keyword = keyword.trim();
        if (!TextUtils.isEmpty(keyword)) {
            saveSearch(keyword);
            adapter.updateData(getSearchList());
            searchInput.setText("");
        }
    }

    private void saveSearch(String keyword) {
        List<String> list = getSearchList();
        list.remove(keyword);
        list.add(0, keyword);
        if (list.size() > 10) list = list.subList(0, 10);
        prefs.edit().putStringSet(KEY_SEARCHES, new HashSet<>(list)).apply();
    }

    private List<String> getSearchList() {
        return new ArrayList<>(prefs.getStringSet(KEY_SEARCHES, new HashSet<>()));
    }
}

