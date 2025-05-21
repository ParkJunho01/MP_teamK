package gachon.termproject.tag_eat.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import gachon.termproject.tag_eat.R;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.5666102, 126.9783881));
        marker.setMap(naverMap);
    }
}
