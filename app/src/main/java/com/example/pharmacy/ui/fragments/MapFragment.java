package com.example.pharmacy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentMapBinding;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Geometry;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.Session;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private MapObjectCollection mapObjects;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            MapKitFactory.initialize(getContext());
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMapBinding.inflate(getLayoutInflater());
        Map map = binding.mapview.getMapWindow().getMap();
        mapObjects = map.getMapObjects();
        CameraPosition cameraPosition = new CameraPosition(
                new Point(55.670016, 37.480147),
                14.0f,
                0,
                0.0f
        );
        SearchManager searchManager = SearchFactory.getInstance()
                .createSearchManager(SearchManagerType.COMBINED);
        SearchOptions searchOptions = new SearchOptions();
        searchOptions.setSearchTypes(SearchType.BIZ.value);
        searchOptions.setResultPageSize(32);

        map.addCameraListener((map1, cameraPosition1, cameraUpdateReason, b) ->
                searchManager.submit(
                        "аптеки",
                        VisibleRegionUtils.toPolygon(map1.visibleRegion(cameraPosition1)),
                        searchOptions,
                        new Session.SearchListener() {
                            @Override
                            public void onSearchResponse(@NonNull Response response) {
                                mapObjects.clear();
                                addPlacemarksToMap(response);
                            }

                            @Override
                            public void onSearchError(@NonNull Error error) {
                            }
                        }
                ));
        map.move(cameraPosition);
        binding.btnRecenter.setOnClickListener(v -> map.move(cameraPosition));
        return binding.getRoot();
    }

    private void addPlacemarksToMap(Response response) {
        for (GeoObjectCollection.Item item : response.getCollection().getChildren()) {
            if (item.getObj() != null) {
                for (Geometry geometry : item.getObj().getGeometry()) {
                    if (geometry.getPoint() != null) {
                        try {
                            mapObjects.addPlacemark(
                                    geometry.getPoint(),
                                    ImageProvider.fromResource(
                                            requireContext(),
                                            R.drawable.ic_pin
                                    )
                            );
                        } catch (Exception e){
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        binding.mapview.onStart();

    }

    @Override
    public void onStop() {
        binding.mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}