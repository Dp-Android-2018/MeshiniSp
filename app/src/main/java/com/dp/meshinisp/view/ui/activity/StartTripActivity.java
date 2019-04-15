package com.dp.meshinisp.view.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityStartTripBinding;
import com.dp.meshinisp.service.model.global.RequestDetailsModel;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.service.model.request.StartDestinationRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.DestinationsRecyclerViewAdapter;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.dp.meshinisp.viewmodel.StartTripViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Lazy;
import ng.max.slideview.SlideView;
import okhttp3.ResponseBody;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class StartTripActivity extends FragmentActivity implements OnMapReadyCallback {
    ActivityStartTripBinding binding;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    Lazy<StartTripViewModel> startTripViewModelLazy = inject(StartTripViewModel.class);
    private GoogleMap mMap;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;
    private View v;
    private ArrayList<StartTripResponseModel> data;
    private RequestDetailsModel requestDetailsModel;
    private int requestId;
    private ArrayList<String> destinationsList;
    private Dialog dialog;
    RecyclerView destinationsRecyclerView;
    DestinationsRecyclerViewAdapter destinationsRecyclerViewAdapter;
    SwipeMenu menu;

    private static final String TAG = "START";
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    LatLng pickupLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_trip);
        data = getIntent().getParcelableArrayListExtra(ConfigurationFile.Constants.TRIPS_DATA);
        requestDetailsModel = getIntent().getParcelableExtra(ConfigurationFile.Constants.REQUEST_DATA);
        requestId = getIntent().getIntExtra(ConfigurationFile.Constants.REQUEST_ID, 0);
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        setupToolbar();
        initializeUi();
        initializeMap();
        initializeShowStateDialog();
        makeActionOnBtnFinishTrip();
    }

    private void initializeUi() {
        binding.tvLocationTv.setText(requestDetailsModel.getPickupAddress());
        pickupLocation = new LatLng(requestDetailsModel.getPickupLat(), requestDetailsModel.getPickupLong());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void setupToolbar() {
        binding.startTripToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.startTripToolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.ivMenu.setOnClickListener(v -> MainActivity.drawer.openDrawer(GravityCompat.START));
    }

    private void initializeMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            getAndDrawMyWay(pickupLocation, requestDetailsModel.getPickupAddress());
        }

    }

    private void getAndDrawMyWay(LatLng nextLocation, String nextLocationTitle) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality() + ":";
                    result += addresses.get(0).getCountryName();
                    LatLng yourLocation = new LatLng(latitude, longitude);
                    if (marker != null) {
                        marker.remove();
                        MarkerOptions markerWithIconCar = new MarkerOptions().position(yourLocation).title(result);
                        markerWithIconCar.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_marker_small));
                        marker = mMap.addMarker(markerWithIconCar);
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation, 12.0f));
                        drawLineBetweenTwoLocations(yourLocation, nextLocation, nextLocationTitle);
                    } else {
                        MarkerOptions markerWithIconCar = new MarkerOptions().position(yourLocation).title(result);
                        markerWithIconCar.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_marker_small));
                        marker = mMap.addMarker(markerWithIconCar);
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation, 21.0f));
                        drawLineBetweenTwoLocations(yourLocation, nextLocation, nextLocationTitle);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      /*  // Enabling MyLocation Layer of Google Map
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        mMap.setMyLocationEnabled(true);*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    private void drawLineBetweenTwoLocations(LatLng userLocation, LatLng nextLocation, String nextLocationTitle) {
        mMap.addMarker(new MarkerOptions().position(nextLocation).title(nextLocationTitle));

        GoogleDirection.withServerKey("AIzaSyBmQpNW53XMiXH3Hxzn7iKQRtBe88sAxQE")
                .from(userLocation)
                .to(nextLocation)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            // Do something
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(StartTripActivity.this, directionPositionList, 5, Color.BLUE);
                            mMap.addPolyline(polylineOptions);
                        } else {
                            Snackbar.make(binding.getRoot(), "Error direction :" + direction.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Snackbar.make(binding.getRoot(), "Error onDirectionFailure :" + t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
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
        SwipeMenuListView listView = (SwipeMenuListView) v.findViewById(R.id.rv_destinations);
//        destinationsRecyclerViewAdapter = new DestinationsRecyclerViewAdapter(data);
        destinationsList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            destinationsList.add(data.get(i).getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(StartTripActivity.this, R.layout.item_tv_destination, destinationsList);

        listView.setAdapter(adapter);
        SwipeMenuCreator creator = (SwipeMenu menu) -> {
            this.menu = menu;
            // create "open" item
            createSwipeMenuItem(ContextCompat.getColor(this, R.color.start_trip_color), R.drawable.ic_drop_down_arrow_start
                    , (int) getResources().getDimension(R.dimen.dp51w), getResources().getString(R.string.start));

            createSwipeMenuItem(ContextCompat.getColor(this, R.color.done_trip_color), R.drawable.ic_done
                    , (int) getResources().getDimension(R.dimen.dp50h), getResources().getString(R.string.done));

        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener((position, menu, index) -> {
            switch (index) {
                case 0:
                    startGoingToDestination(position);
                    break;
                case 1:
                    doneThisDestination(position);
                    break;
            }
            // false : close the menu; true : not close the menu
            return false;
        });
//        destinationsRecyclerView = v.findViewById(R.id.rv_destinations);
        /*destinationsRecyclerViewAdapter = new DestinationsRecyclerViewAdapter(data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        destinationsRecyclerView.setLayoutManager(linearLayoutManager);
        destinationsRecyclerView.setAdapter(destinationsRecyclerViewAdapter);
        makeActionOnClickOnRecyclerViewItem();
      */
    }

    private void doneThisDestination(int position) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        SharedUtils.getInstance().showProgressDialog(this);
        startTripViewModelLazy.getValue().setDoneDestination(requestId, getStartDestinationRequest(data.get(position).getId()))
                .observe(this, voidResponse -> {
                    SharedUtils.getInstance().cancelDialog();
                    if (voidResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > voidResponse.code()) {
                        mMap.clear();
                    } else {
                        showStartTripErrorMessage(voidResponse.errorBody());
                    }
                });
    }

    private void startGoingToDestination(int position) {
        binding.tvLocationTv.setText(data.get(position).getName());
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        SharedUtils.getInstance().showProgressDialog(this);
        startTripViewModelLazy.getValue().setNextDestination(requestId, getStartDestinationRequest(data.get(position).getId()))
                .observe(this, voidResponse -> {
                    SharedUtils.getInstance().cancelDialog();
                    if (voidResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > voidResponse.code()) {
                        mMap.clear();
                        getAndDrawMyWay(new LatLng(data.get(position).getLocationLat(), data.get(position).getLocationLong())
                                , data.get(position).getName());
                    } else {
                        showStartTripErrorMessage(voidResponse.errorBody());

                    }
                });

    }

    private void showStartTripErrorMessage(ResponseBody errorResponseBody) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(errorResponseBody.string(), ErrorResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String error = "";
        for (String string : errorResponse.getErrors()) {
            error += string;
            error += "\n";
        }
        showSnackbar(error);
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private StartDestinationRequest getStartDestinationRequest(int id) {
        StartDestinationRequest startDestinationRequest = new StartDestinationRequest();
        startDestinationRequest.setDestinationId(id);
        return startDestinationRequest;
    }

    private void createSwipeMenuItem(int color, int iconDrawable, int dimension, String title) {
        SwipeMenuItem startTipItem = new SwipeMenuItem(
                getApplicationContext());
        // set item background
        startTipItem.setBackground(new ColorDrawable(color));
        startTipItem.setIcon(iconDrawable);
        // set item width
        startTipItem.setWidth(dimension);
        // set item title
        startTipItem.setTitle(title);
        // set item title fontsize
        startTipItem.setTitleSize(17);
        // set item title font color
        startTipItem.setTitleColor(Color.WHITE);
        // add to menu
        menu.addMenuItem(startTipItem);
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

    private void makeActionOnBtnFinishTrip() {
        SlideView slideView = binding.btSlideTofinishTrip;
        slideView.setOnSlideCompleteListener(slideView1 -> {
            // vibrate the device
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            if (ValidationUtils.isConnectingToInternet(this)) {
                makeFinishTripRequest();
            } else {
                showSnackbar(getString(R.string.there_is_no_internet_connection));
            }
        });

    }

    private void makeFinishTripRequest() {
        SharedUtils.getInstance().showProgressDialog(this);
        startTripViewModelLazy.getValue().finishTrip(requestId).observe(this, startTripResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (startTripResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > startTripResponseResponse.code()) {
                showRateTripDialog();
                if (startTripResponseResponse.body() != null) {
                    showSnackbar(startTripResponseResponse.body().getMessage());
                }
            } else {
                showStartTripErrorMessage(startTripResponseResponse.errorBody());
            }
        });
    }

    private void showRateTripDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View v = View.inflate(this, R.layout.rate_trip_dialog, null);
        builder.setView(v);
        builder.setCancelable(false);
        dialog = builder.create();
        Window window = dialog.getWindow();
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.show();
    }

}
