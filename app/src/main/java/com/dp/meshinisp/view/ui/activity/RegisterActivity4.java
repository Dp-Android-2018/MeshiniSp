package com.dp.meshinisp.view.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegister4Binding;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.LoginRegisterResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.Register1ViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RegisterActivity4 extends AppCompatActivity {

    ActivityRegister4Binding binding;
    Lazy<Register1ViewModel> registerViewModelLazy = inject(Register1ViewModel.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, (R.layout.activity_register4));
        register1Request = registerRequestLazy.getValue();
        Gson gson = new Gson();
        register1Request = gson.fromJson(getIntent().getStringExtra(ConfigurationFile.Constants.REGISTER1DATA), RegisterRequest.class);
        makeActionToUploadImage();
        makeClickListenerOnClickOnBtnNext();
    }

    private void makeClickListenerOnClickOnBtnNext() {
        binding.btNext.setOnClickListener(v -> uploadFile());
    }

    private void makeRegister() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            registerViewModelLazy.getValue().register(register1Request).observe(this, new Observer<Response<LoginRegisterResponse>>() {
                @Override
                public void onChanged(Response<LoginRegisterResponse> loginRegisterResponseResponse) {
                    SharedUtils.getInstance().cancelDialog();
                    if (loginRegisterResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > loginRegisterResponseResponse.code()) {
                        openHomeActivity();
                    } else {
                        Gson gson = new GsonBuilder().create();
                        ErrorResponse errorResponse = new ErrorResponse();

                        try {
                            errorResponse = gson.fromJson(loginRegisterResponseResponse.errorBody().string(), ErrorResponse.class);
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
                }
            });
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void openHomeActivity() {
        Intent intent = new Intent(RegisterActivity4.this, RegisterActivity5.class);
        startActivity(intent);
        finish();
    }

    public void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void makeActionToUploadImage() {
        binding.ivUpload.setOnClickListener(v -> showFileChooser());
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
                initializeProgressDialog();
                putFileToStorageReference();
            } else {
                Snackbar.make(binding.getRoot(), "There is no pictures!!", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void putFileToStorageReference() {
        riversRef = storageRef.child("driving_licence_pic" + ConfigurationFile.Constants.SERVICEPROVIDER_DIRECTORY_NAME);
        riversRef.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadedFileUrl = null;
                        try {
                            uploadedFileUrl = new URL(uri.toString());
                            register1Request.setDrivingLicenseUrl(uploadedFileUrl.toString());
                            photoUploaded = true;
                            makeRegister();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });

                    progressDialog.dismiss();
//                    Snackbar.make(binding.getRoot(), "Uploaded Successfully", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    progressDialog.dismiss();
                    Snackbar.make(binding.getRoot(), exception.getMessage(), Snackbar.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                });
    }

    private void initializeProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            if (riversRef != null) {
                uploadTask = riversRef.getActiveUploadTasks().get(0);
                if (uploadTask != null) {
                    uploadTask.cancel();
                    progressDialog.dismiss();
                    Snackbar.make(binding.getRoot(), "Upload cancelled sussessfully :(", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        progressDialog.show();
    }
}
