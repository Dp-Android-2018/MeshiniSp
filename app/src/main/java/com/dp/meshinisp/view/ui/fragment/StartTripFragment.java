package com.dp.meshinisp.view.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityStartTripBinding;
import com.dp.meshinisp.databinding.ItemDestinationRvLayoutBinding;
import com.dp.meshinisp.service.model.global.ActiveTripResponseModel;
import com.dp.meshinisp.service.model.global.RequestDetailsModel;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.service.model.request.RateTripRequest;
import com.dp.meshinisp.service.model.request.StartDestinationRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.utility.utils.firebase.classes.ActiveTripFirebase;
import com.dp.meshinisp.view.ui.activity.LoginActivity;
import com.dp.meshinisp.view.ui.activity.MainActivity;
import com.dp.meshinisp.view.ui.adapter.DestinationsRecyclerViewAdapter;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.dp.meshinisp.viewmodel.MainActivityViewModel;
import com.dp.meshinisp.viewmodel.StartTripViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Lazy;
import ng.max.slideview.SlideView;
import okhttp3.ResponseBody;

import static android.content.Context.LOCATION_SERVICE;
import static org.koin.java.standalone.KoinJavaComponent.inject;

public class StartTripFragment extends Fragment implements OnMapReadyCallback {
    ActivityStartTripBinding binding;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private Lazy<StartTripViewModel> startTripViewModelLazy = inject(StartTripViewModel.class);
    private Lazy<MainActivityViewModel> mainActivityViewModelLazy = inject(MainActivityViewModel.class);
    private ActiveTripFirebase activeTripFirebase;
    private GoogleMap mMap;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;
    private View v;
    private Dialog dialog;
    private ArrayList<StartTripResponseModel> data;
    private RequestDetailsModel requestDetailsModel;
    private int requestId;
    private float ratingValue;
    private EditText reviewEditText;
    private RecyclerView destinationsRecyclerView;
    private DestinationsRecyclerViewAdapter destinationsRecyclerViewAdapter;
    private ActiveTripResponseModel activeTripResponseModel;
    private final int CALL_REQUEST = 100;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Marker marker1;
    private Marker marker2;
    private LocationListener locationListener;
    private LatLng pickupLocation;
    private Polyline polyline;
    private int firebaseNextDestinationId;
    private LatLng yourLocation;
    private boolean mLocationPermissionGranted;
    public Context activityContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activityContext=context;
    }

    public StartTripFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_start_trip, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        data = getArguments().getParcelableArrayList(ConfigurationFile.Constants.TRIPS_DATA);
        requestDetailsModel = getArguments().getParcelable(ConfigurationFile.Constants.REQUEST_DATA);
        activeTripResponseModel = getArguments().getParcelable(ConfigurationFile.Constants.ACTIVE_REQUEST_DATA);
        requestId = getArguments().getInt(ConfigurationFile.Constants.REQUEST_ID, 0);
        activeTripFirebase = new Gson().fromJson(getArguments().getString(ConfigurationFile.Constants.FIREBASE_REQUEST_DATA), ActiveTripFirebase.class);
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        setupToolbar();
        initializeUi();
        initializeMap();
        initializeShowStateDialog();
        makeActionOnBtnFinishTrip();
        openLocations();
        makeClickOnButtonsCall();
    }

    private void makeClickOnButtonDirections(LatLng nextLocation, String nextLocationTitle) {
        binding.btDirections.setOnClickListener(v -> openMapsIntent(nextLocation, nextLocationTitle));
    }

    private void openMapsIntent(LatLng nextLocation, String nextLocationTitle) {
        Uri gmIntentUri;
        gmIntentUri = Uri.parse(ConfigurationFile.Constants.GOOGLE_MAPS_URI_DATA + nextLocation.latitude
                + "," + nextLocation.longitude + "(" + nextLocationTitle + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
        mapIntent.setPackage(ConfigurationFile.Constants.GOOGLE_MAPS_PACKAGE_NAME);
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void makeClickOnButtonsCall() {
        binding.ivCall.setOnClickListener(v -> {
            callPhoneNumber();
        });
        binding.ivZoomToMyLocation.setOnClickListener(v ->
                {
                    if (mMap != null && yourLocation != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocation, 12.0f));
                    }
                }
        );
    }

    public void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }
            String telephone = requestDetailsModel.getClient().getPhone();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + telephone.trim()));
            startActivity(callIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeUi() {
        if (activeTripResponseModel != null) {
            if (activeTripFirebase != null) {
                if (activeTripFirebase.getNext_destination() != -1) {
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getId() == activeTripFirebase.getNext_destination()) {
                            firebaseNextDestinationId = i;
                        }
                    }
                    pickupLocation = new LatLng(data.get(firebaseNextDestinationId).getLocationLat(),
                            data.get(firebaseNextDestinationId).getLocationLong());
                    binding.tvLocationTv.setText(data.get(firebaseNextDestinationId).getName());
                } else {
                    binding.tvLocationTv.setText(activeTripResponseModel.getPickupAddress());
                    pickupLocation = new LatLng(activeTripResponseModel.getPickupLat(), activeTripResponseModel.getPickupLong());
                }
            }
        } else {
            binding.tvLocationTv.setText(requestDetailsModel.getPickupAddress());
            pickupLocation = new LatLng(requestDetailsModel.getPickupLat(), requestDetailsModel.getPickupLong());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void setupToolbar() {
        binding.ivMenuIcon.setOnClickListener(v -> MainActivity.drawer.openDrawer(GravityCompat.START));
        if (customUtilsLazy.getValue().getSavedLanguageType().equals(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC)) {
            binding.ivBack.setRotation(180);

        }
        binding.ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(activityContext, MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void initializeMap() {
        SharedUtils.getInstance().showProgressDialog(getActivity());
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (activeTripResponseModel != null) {
            if (activeTripFirebase != null) {
                if (activeTripFirebase.getNext_destination() != -1) {
                    getAndDrawMyWay(pickupLocation
                            , data.get(firebaseNextDestinationId).getName());
                } else {
                    getAndDrawMyWay(pickupLocation, activeTripResponseModel.getPickupAddress());
                }
            }
        } else {
            getAndDrawMyWay(pickupLocation, requestDetailsModel.getPickupAddress());
        }

    }

    private void getAndDrawMyWay(LatLng nextLocation, String nextLocationTitle) {
        makeClickOnButtonDirections(nextLocation, nextLocationTitle);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(activityContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //get the location name from latitude and longitude
                    Geocoder geocoder = new Geocoder(activityContext);
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(latitude, longitude, 1);
                        String result = addresses.get(0).getLocality() + ":";
                        result += addresses.get(0).getCountryName();
                        yourLocation = new LatLng(latitude, longitude);
                        mMap.clear();
                        if (marker1 != null) {
                            marker1.remove();
                            addMarker1ToMap(yourLocation, result, nextLocation, nextLocationTitle);
                            marker1.setRotation(location.getBearing());
                        } else {
                            addMarker1ToMap(yourLocation, result, nextLocation, nextLocationTitle);
                            marker1.setRotation(location.getBearing());
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
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void addMarker1ToMap(LatLng yourLocation, String result, LatLng nextLocation, String nextLocationTitle) {
        marker1 = mMap.addMarker(new MarkerOptions().position(yourLocation).title(result)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_marker_small)).flat(true));

        mMap.setMaxZoomPreference(20);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocation, 12.0f));
        drawLineBetweenTwoLocations(yourLocation, nextLocation, nextLocationTitle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        SharedUtils.getInstance().cancelDialog();
        mMap = googleMap;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private void drawLineBetweenTwoLocations(LatLng userLocation, LatLng nextLocation, String nextLocationTitle) {
        if (marker2 != null) {
            marker2.remove();
            marker2 = mMap.addMarker(new MarkerOptions().position(nextLocation).title(nextLocationTitle));
        } else {
            marker2 = mMap.addMarker(new MarkerOptions().position(nextLocation).title(nextLocationTitle));
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker1.getPosition());
        builder.include(marker2.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 110; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

        GoogleDirection.withServerKey("AIzaSyCKzPmrsBgCHmWgpucrwovaZV4vRTZvnMs")
                .from(userLocation)
                .to(nextLocation)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            if (polyline != null) {
                                polyline.remove();
                            }
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(activityContext, directionPositionList, 5, Color.BLUE);
                            // Do something
                            polyline = mMap.addPolyline(polylineOptions);
                            String distance = leg.getDistance().getText();
                            String duration = leg.getDuration().getText();
                            binding.tvLocationDistance.setText(ConfigurationFile.Constants.BRACKET_BEFORE + distance + ConfigurationFile.Constants.BRACKET_AFTER);
                            binding.tvArriveTime.setText(ConfigurationFile.Constants.BRACKET_BEFORE + duration + ConfigurationFile.Constants.BRACKET_AFTER);
                        } else {
                            showSnackbar("Error direction :" + direction.getErrorMessage());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        showSnackbar("Error onDirectionFailure :" + t.getMessage());
                    }
                });
    }


    private void initializeShowStateDialog() {
        layoutBottomSheet = getView().findViewById(R.id.bottom_sheet_destination_list);
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
        destinationsRecyclerViewAdapter = new DestinationsRecyclerViewAdapter(data, activeTripFirebase);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activityContext, RecyclerView.VERTICAL, false);
        destinationsRecyclerView.setLayoutManager(linearLayoutManager);
        destinationsRecyclerView.setAdapter(destinationsRecyclerViewAdapter);
        makeActionOnClickOnRecyclerViewItem();
    }

    private void makeActionOnClickOnRecyclerViewItem() {
        destinationsRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }

            @Override
            public void onStartClick(int position, ItemDestinationRvLayoutBinding binding) {
                startGoingToDestination(position, binding);

            }

            @Override
            public void onEndClick(int position, ItemDestinationRvLayoutBinding binding) {
                doneThisDestination(position, binding);
            }
        });
    }

    private void doneThisDestination(int position, ItemDestinationRvLayoutBinding binding) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        SharedUtils.getInstance().showProgressDialog(activityContext);
        startTripViewModelLazy.getValue().setDoneDestination(requestId, getStartDestinationRequest(data.get(position).getId()))
                .observe(this, voidResponse -> {
                    SharedUtils.getInstance().cancelDialog();
                    if (voidResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > voidResponse.code()) {
                        mMap.clear();
                        marker1 = null;
                        marker2 = null;
                        polyline = null;
                        binding.getRoot().setBackgroundColor(activityContext.getResources().getColor(R.color.slide_to_finish_color));
                        binding.btDoneDestination.setVisibility(View.GONE);
                        binding.ivDone.setVisibility(View.VISIBLE);
                    } else if (voidResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                        logout();
                    } else {
                        if (voidResponse.errorBody() != null) {
                            showStartTripErrorMessage(voidResponse.errorBody());
                        }
                    }
                });
    }

    public void logout() {
        SharedUtils.getInstance().showProgressDialog(getActivity());
        mainActivityViewModelLazy.getValue().logout().observe(this, voidResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (voidResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > voidResponse.code()) {
                goToLoginPage();
            } else if (voidResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                goToLoginPage();
            } else {
                if (voidResponse.errorBody() != null) {
                    showStartTripErrorMessage(voidResponse.errorBody());
                }
            }
        });
    }

    private void goToLoginPage() {
        String languageType = customUtilsLazy.getValue().getSavedLanguageType();
        customUtilsLazy.getValue().clearSharedPref();
        customUtilsLazy.getValue().saveLanguageTypeToPrefs(languageType);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void startGoingToDestination(int position, ItemDestinationRvLayoutBinding binding) {
        this.binding.tvLocationTv.setText(data.get(position).getName());
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        SharedUtils.getInstance().showProgressDialog(activityContext);
        startTripViewModelLazy.getValue().setNextDestination(requestId, getStartDestinationRequest(data.get(position).getId()))
                .observe(this, voidResponse -> {
                    SharedUtils.getInstance().cancelDialog();
                    if (voidResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > voidResponse.code()) {
                        mMap.clear();
                        marker1 = null;
                        marker2 = null;
                        polyline = null;
                        getAndDrawMyWay(new LatLng(data.get(position).getLocationLat(), data.get(position).getLocationLong())
                                , data.get(position).getName());
                        binding.btStartDestination.setVisibility(View.GONE);
                        binding.btDoneDestination.setVisibility(View.VISIBLE);
                    } else {
                        if (voidResponse.errorBody() != null) {
                            showStartTripErrorMessage(voidResponse.errorBody());
                        }

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


    public void openLocations() {
        binding.ivLocations.setOnClickListener(v -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
    }

    private void makeActionOnBtnFinishTrip() {
        SlideView slideView = binding.btSlideTofinishTrip;
        slideView.setOnSlideCompleteListener(slideView1 -> {
            // vibrate the device
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            if (ValidationUtils.isConnectingToInternet(Objects.requireNonNull(activityContext))) {
                makeFinishTripRequest();
            } else {
                showSnackbar(getString(R.string.there_is_no_internet_connection));
            }
        });

    }

    private void makeFinishTripRequest() {
        if (ValidationUtils.isConnectingToInternet(Objects.requireNonNull(activityContext))) {
            SharedUtils.getInstance().showProgressDialog(activityContext);
            startTripViewModelLazy.getValue().finishTrip(requestId).observe(this, startTripResponseResponse -> {
                SharedUtils.getInstance().cancelDialog();
                if (startTripResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                        && ConfigurationFile.Constants.SUCCESS_CODE_TO > startTripResponseResponse.code()) {
                    new Handler().postDelayed(this::showRateTripDialog, 1000);
                    if (startTripResponseResponse.body() != null) {
                        showSnackbar(startTripResponseResponse.body().getMessage());
                    }
                } else if (startTripResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                    logout();
                } else {
                    if (startTripResponseResponse.errorBody() != null) {
                        showStartTripErrorMessage(startTripResponseResponse.errorBody());
                    }
                }
            });
        } else {
            showSnackbar(getResources().getString(R.string.there_is_no_internet_connection));
        }
    }

    private void showRateTripDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(activityContext));
        v = View.inflate(activityContext, R.layout.rate_trip_dialog, null);
        builder.setView(v);
        builder.setCancelable(false);
        dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        makeActionOnTripDialog();
        dialog.show();
    }

    private void makeActionOnTripDialog() {
        RatingBar ratingBar = v.findViewById(R.id.ratingBar);
        reviewEditText = v.findViewById(R.id.et_review_comment);
        Button sendReviewButton = v.findViewById(R.id.bt_send_review);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> ratingValue = rating);
        sendReviewButton.setOnClickListener(v1 -> {
            if (ValidationUtils.isConnectingToInternet(Objects.requireNonNull(activityContext))) {
                rateTrip();
            } else {
                showDialogSnackBar(getResources().getString(R.string.there_is_no_internet_connection));
            }
        });

    }

    private void showDialogSnackBar(String message) {
        Snackbar.make(v.getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void rateTrip() {
        if (ratingValue != 0.0) {
            SharedUtils.getInstance().showProgressDialog(activityContext);
            startTripViewModelLazy.getValue().rateTrip(requestId, getRateTripRequest()).observe(this, startTripResponseResponse -> {
                SharedUtils.getInstance().cancelDialog();
                if (startTripResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                        && ConfigurationFile.Constants.SUCCESS_CODE_TO > startTripResponseResponse.code()) {
                    if (startTripResponseResponse.body() != null) {
                        showDialogSnackBar(startTripResponseResponse.body().getMessage());
                    }
                    new Handler().postDelayed(this::openHomeActivity, 1000);
                } else if (startTripResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                    logout();
                } else {
                    if (startTripResponseResponse.errorBody() != null) {
                        showStartTripErrorMessage(startTripResponseResponse.errorBody());
                    }
                }
            });
        } else {
            showDialogSnackBar(getResources().getString(R.string.please_rate_the_trip));
        }
    }

    private void openHomeActivity() {
        Intent intent = new Intent(activityContext, MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        getActivity().finishAffinity();
    }

    private RateTripRequest getRateTripRequest() {
        RateTripRequest rateTripRequest = new RateTripRequest();
        rateTripRequest.setRatingValue(ratingValue);
        if (!reviewEditText.getText().toString().isEmpty()) {
            rateTripRequest.setReviewText(reviewEditText.getText().toString());
        }
        return rateTripRequest;
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activityContext);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}
