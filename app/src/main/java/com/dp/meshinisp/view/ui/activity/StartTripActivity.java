package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityStartTripBinding;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class StartTripActivity extends FragmentActivity implements OnMapReadyCallback {
    ActivityStartTripBinding binding;
    private GoogleMap mMap;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;
    private View v;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_trip);
        ConfigurationFile.Constants.AUTHORIZATION=customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        setupToolbar();
        initializeMap();
        initializeShowStateDialog();
    }

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
//                        sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        View v = layoutBottomSheet.getRootView();
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

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
        ImageView ivClose=v.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v1 -> sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        /*fromEditText = v.findViewById(R.id.et_dialog_from);
        toEditText = v.findViewById(R.id.et_dialog_to);
        countrySpinner = v.findViewById(R.id.sp_country);
        Button btnSearch = v.findViewById(R.id.bt_search_for_requests);
        pickDateAndTime(fromEditText);
        pickDateAndTime(toEditText);
        setCountrySpinner();
        makeSearch(btnSearch);*/
    }

    public void openLocations(View view) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        /*if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

//            btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            btnBottomSheet.setText("Expand sheet");
        }*/
    }
}
