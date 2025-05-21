package gachon.termproject.tag_eat.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import gachon.termproject.tag_eat.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private TextInputEditText searchInput;
    private RecyclerView recentList;
    private RecentSearchAdapter adapter;
    private SharedPreferences prefs;

    private static final String PREFS_NAME = "recent_searches";
    private static final String KEY_SEARCHES = "search_list";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchInput = view.findViewById(R.id.home_search_input);
        recentList = view.findViewById(R.id.home_recent_search_list);
        recentList.setLayoutManager(new LinearLayoutManager(getContext()));

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        adapter = new RecentSearchAdapter(getSearchList());
        recentList.setAdapter(adapter);
        recentList.setVisibility(View.GONE);

        // 검색창 클릭 시 최근 검색어 표시
        searchInput.setOnClickListener(v -> recentList.setVisibility(View.VISIBLE));

        // 입력 중에도 최근 검색어 보이게 유지
        searchInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recentList.setVisibility(View.VISIBLE);
            }
            public void afterTextChanged(Editable s) {}
        });

        // 키보드 엔터로 검색
        searchInput.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_NULL) {
                String keyword = searchInput.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    saveSearch(keyword);
                    adapter.updateData(getSearchList());
                    recentList.setVisibility(View.GONE);
                    searchInput.setText("");
                }
                return true;
            }
            return false;
        });

        // 네이버 지도 세팅
        NaverMapSdk.getInstance(requireContext()).setClient(
                new NaverMapSdk.NcpKeyClient("ykv01w7r7u")
        );

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.home_map_container);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.home_map_container, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);
        return view;
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

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.5666102, 126.9783881));
        marker.setMap(naverMap);
    }
}
