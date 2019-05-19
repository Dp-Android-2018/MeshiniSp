package com.dp.meshinisp.view.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegister3Binding;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import kotlin.Lazy;

import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.BIKE_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.CAR_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.CAR_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.GOLFCAR_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.GOLFCAR_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.JETSKI_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.JETSKI_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.MOTORBIKE_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.ONFOOT_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.ON_FOOT_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.STEAGECOACH_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.STEAGECOACH_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.TUKTUK_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.TUKTUK_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.VAN_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.VAN_TYPE;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.YACHAT_ID;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.YACHAT_TYPE;
import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RegisterActivity3 extends BaseActivity {

    ActivityRegister3Binding binding;
    Lazy<RegisterRequest> registerRequestLazy = inject(RegisterRequest.class);
    RegisterRequest register1Request;
    private Uri filePath;
    private ProgressDialog progressDialog;
    private StorageReference storageRef;
    private StorageReference riversRef;
    private UploadTask uploadTask;
    Bitmap bitmap;
    private boolean photoUploaded;
    private boolean tabSelected;
    private URL uploadedFileUrl;
    private int selectedTab = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register3);
        register1Request = registerRequestLazy.getValue();
        Gson gson = new Gson();
        register1Request = gson.fromJson(getIntent().getStringExtra(ConfigurationFile.Constants.REGISTER1DATA), RegisterRequest.class);
        makeActionToUploadImage();
        makeClickListenerOnClickOnBtnNext();
    }


    private void makeActionOnIcon(ImageView imageView, boolean iconState, boolean iconClicked) {

    }

    private void makeClickListenerOnClickOnBtnNext() {
        binding.btNext.setOnClickListener(v -> {
            if (tabSelected && filePath != null) {
                uploadFile();
            } else {
                showErrors();
            }
        });
    }

    private void showErrors() {
        if (!tabSelected) {
            showSnackbar("vehicle type :" + register1Request.getVehicleType());
            showSnackbar(getString(R.string.please_choose_your_vehicle_type));
            return;
        }

        if (filePath == null) {
            showSnackbar(getString(R.string.please_upload_your_vehicle_licence));
        }
    }

    public void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

   /* private void setVehicleType() {
        tabSelected = true;
        register1Request.setVehicleType(ConfigurationFile.Constants.ON_FOOT_TYPE);
        binding.bottomBar.setOnNavigationItemSelectedListener(item -> {
            tabSelected = true;
            switch (item.getItemId()) {
                case R.id.onfoot_white:
                    register1Request.setVehicleType(ConfigurationFile.Constants.ON_FOOT_TYPE);
                    break;

                case R.id.car_white:
                    register1Request.setVehicleType(ConfigurationFile.Constants.CAR_TYPE);
                    break;

                case R.id.motorbike:
                    register1Request.setVehicleType(ConfigurationFile.Constants.MOTORBIKE_TYPE);
                    break;

                default:
                    register1Request.setVehicleType(ConfigurationFile.Constants.ON_FOOT_TYPE);
                    break;

            }
            return true;
        });

    }*/

    private void makeActionToUploadImage() {
        binding.ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        ImagePicker.Companion.with(this).start();
//                .crop(1f, 1f)       //Crop Square image(Optional)
//                .compress(1024)   //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(620, 620) //Final image resolution will be less than 620 x 620(Optional)
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.ivUpload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //this method will upload the file
    private void uploadFile() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            if (filePath != null) {
                storageRef = FirebaseStorage.getInstance().getReference();
                putFileToStorageReference();
            } else {
                Snackbar.make(binding.getRoot(), "There is no pictures!!", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void putFileToStorageReference() {
        riversRef = storageRef.child("vehicle_pic" + ConfigurationFile.Constants.SERVICEPROVIDER_DIRECTORY_NAME);
        riversRef.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadedFileUrl = null;
                        try {
                            uploadedFileUrl = new URL(uri.toString());
                            register1Request.setVehicleLicenseUrl(uploadedFileUrl.toString());
                            photoUploaded = true;
                            openNextActivity();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });
                })
                .addOnFailureListener(exception -> {
                    Snackbar.make(binding.getRoot(), exception.getMessage(), Snackbar.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.tvProgressNumber.setVisibility(View.VISIBLE);
                    binding.progressBar.setProgress((int) progress);
                    binding.tvProgressNumber.setText((int) progress + ConfigurationFile.Constants.PERCENT);
                    if (progress == 100) {
                        // Set a message of completion
                        binding.tvProgressNumber.setText(getResources().getString(R.string.operation_completed));
                    }
                });
    }

    private void openNextActivity() {
        Intent intent = new Intent(RegisterActivity3.this, RegisterActivity4.class);
        intent.putExtra(ConfigurationFile.Constants.REGISTER1DATA, new Gson().toJson(register1Request));
        startActivity(intent);
        finish();
    }

    public void setVehicleType(View view) {
        tabSelected = true;
        switch (view.getId()) {
            case R.id.iv_car:
                selectedTab = CAR_ID;
                break;
            case R.id.iv_van:
                selectedTab = VAN_ID;
                break;
            case R.id.iv_golf_car:
                selectedTab = GOLFCAR_ID;
                break;
            case R.id.iv_tuktuk:
                selectedTab = TUKTUK_ID;
                break;
            case R.id.iv_stagecoach:
                selectedTab = STEAGECOACH_ID;
                break;
            case R.id.iv_yacht:
                selectedTab = YACHAT_ID;
                break;
            case R.id.iv_jet_ski:
                selectedTab = JETSKI_ID;
                break;
            case R.id.iv_motorbike:
                selectedTab = BIKE_ID;
                break;
            case R.id.iv_onfoot:
                selectedTab = ONFOOT_ID;
                break;

        }
        handleImageResource();
        setVehicleTypToRegister();
    }

    private void handleImageResource() {
        binding.ivCar.setImageResource(selectedTab == CAR_ID ? R.drawable.car_black : R.drawable.car_white);
        binding.ivVan.setImageResource(selectedTab == VAN_ID ? R.drawable.van_black : R.drawable.van_white);
        binding.ivGolfCar.setImageResource(selectedTab == GOLFCAR_ID ? R.drawable.golf_car_black : R.drawable.golf_car_white);
        binding.ivTuktuk.setImageResource(selectedTab == TUKTUK_ID ? R.drawable.tuktuk_black : R.drawable.tuktuk_white);
        binding.ivStagecoach.setImageResource(selectedTab == STEAGECOACH_ID ? R.drawable.stagecoach_black : R.drawable.stagecoach_white);
        binding.ivYacht.setImageResource(selectedTab == YACHAT_ID ? R.drawable.boat_black : R.drawable.boat_white);
        binding.ivJetSki.setImageResource(selectedTab == JETSKI_ID ? R.drawable.jet_ski_black : R.drawable.jet_ski_white);
        binding.ivMotorbike.setImageResource(selectedTab == BIKE_ID ? R.drawable.motorcycle_black : R.drawable.motorcycle_white);
        binding.ivOnfoot.setImageResource(selectedTab == ONFOOT_ID ? R.drawable.trekking_black : R.drawable.onfoot_white);
    }

    private void setVehicleTypToRegister() {
        switch (selectedTab) {
            case CAR_ID:
                register1Request.setVehicleType(CAR_TYPE);
                break;
            case VAN_ID:
                register1Request.setVehicleType(VAN_TYPE);
                break;
            case GOLFCAR_ID:
                register1Request.setVehicleType(GOLFCAR_TYPE);
                break;
            case TUKTUK_ID:
                register1Request.setVehicleType(TUKTUK_TYPE);
                break;
            case STEAGECOACH_ID:
                register1Request.setVehicleType(STEAGECOACH_TYPE);
                break;
            case YACHAT_ID:
                register1Request.setVehicleType(YACHAT_TYPE);
                break;
            case JETSKI_ID:
                register1Request.setVehicleType(JETSKI_TYPE);
                break;
            case BIKE_ID:
                register1Request.setVehicleType(MOTORBIKE_TYPE);
                break;
            case ONFOOT_ID:
                register1Request.setVehicleType(ON_FOOT_TYPE);
                break;

        }
    }

}
