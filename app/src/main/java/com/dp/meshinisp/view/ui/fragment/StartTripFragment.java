package com.dp.meshinisp.view.ui.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Lazy;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityStartTripBinding;
import com.dp.meshinisp.databinding.FragmentStartTripBinding;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.view.ui.activity.MainActivity;
import com.dp.meshinisp.view.ui.adapter.DestinationsRecyclerViewAdapter;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import static org.koin.java.standalone.KoinJavaComponent.inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartTripFragment extends FragmentActivity implements OnMapReadyCallback {

    FragmentStartTripBinding binding;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private GoogleMap mMap;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;
    private View v;
    private ArrayList<StartTripResponseModel> data;
    RecyclerView destinationsRecyclerView;
    DestinationsRecyclerViewAdapter destinationsRecyclerViewAdapter;

    public StartTripFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_trip);
        data = getIntent().getParcelableArrayListExtra(ConfigurationFile.Constants.TRIPS_DATA);
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        setupToolbar();
        initializeMap();
        initializeShowStateDialog();

    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_trip, container, false);
    }
*/
    private void setupToolbar() {
        binding.startTripToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.startTripToolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.ivMenu.setOnClickListener(v -> MainActivity.drawer.openDrawer(GravityCompat.START));
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void initializeShowStateDialog() {
        layoutBottomSheet = findViewById(R.id.bottom_sheet_destination_list);
        makeActionOnLayoutComponents(layoutBottomSheet.getRootView());
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        binding.btDirections.setVisibility(View.VISIBLE);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED: {
                        binding.btDirections.setVisibility(View.INVISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        binding.btDirections.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        binding.btDirections.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        binding.btDirections.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void makeActionOnLayoutComponents(View v) {
        this.v = v;
        ImageView ivClose = v.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v1 -> sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        destinationsRecyclerView = v.findViewById(R.id.rv_destinations);
        destinationsRecyclerViewAdapter = new DestinationsRecyclerViewAdapter(data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        destinationsRecyclerView.setLayoutManager(linearLayoutManager);
        destinationsRecyclerView.setAdapter(destinationsRecyclerViewAdapter);
        makeActionOnClickOnRecyclerViewItem();
    }

    private void makeActionOnClickOnRecyclerViewItem() {
        destinationsRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                opnDetailsActivity(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
//                removeItem(position);
            }
        });
    }

    public void openLocations(View view) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

}
