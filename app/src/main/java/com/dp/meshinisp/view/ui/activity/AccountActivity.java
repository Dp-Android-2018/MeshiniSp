package com.dp.meshinisp.view.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityAccountBinding;
import com.dp.meshinisp.service.model.request.ProfileInfoRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.AccountActivityViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class AccountActivity extends AppCompatActivity {

    ActivityAccountBinding binding;
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    Lazy<AccountActivityViewModel> accountActivityViewModelLazy = inject(AccountActivityViewModel.class);

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private ProgressDialog progressDialog;
    private StorageReference storageRef;
    private StorageReference riversRef;
    private UploadTask uploadTask;
    Bitmap bitmap;
    private boolean photoUploaded;
    private boolean selectLanguage;
    private URL uploadedFileUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        setupToolbar();
        initializeUiWithData();

    }

    private void setupToolbar() {
        binding.accountToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.accountToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initializeUiWithData() {
        ImageView ivFeedPhoto = binding.circleImageView;
        Picasso.get().load(customUtilsLazy.getValue().getSavedMemberData().getProfilePictureUrl()).into(ivFeedPhoto);
        binding.etFirstName.setText(customUtilsLazy.getValue().getSavedMemberData().getFirstName());
        binding.etLastName.setText(customUtilsLazy.getValue().getSavedMemberData().getLastName());
        binding.etEmail.setText(customUtilsLazy.getValue().getSavedMemberData().getEmail());
        binding.etPhone.setText(customUtilsLazy.getValue().getSavedMemberData().getPhone());
    }

    public void saveChanges(View view) {
        uploadFile();

        if ((
                !binding.etFirstName.getText().toString().isEmpty()
                        && !binding.etLastName.getText().toString().isEmpty()
                        && !binding.etEmail.getText().toString().isEmpty()
                        && !binding.etPhone.getText().toString().isEmpty()
                        &&( !customUtilsLazy.getValue().getSavedMemberData().getFirstName().equals(binding.etFirstName.getText().toString())
                        || !customUtilsLazy.getValue().getSavedMemberData().getLastName().equals(binding.etLastName.getText().toString())
                        || !customUtilsLazy.getValue().getSavedMemberData().getEmail().equals(binding.etEmail.getText().toString())
                        || !customUtilsLazy.getValue().getSavedMemberData().getPhone().equals(binding.etPhone.getText().toString()) )
        ) || photoUploaded
        ) {
            if (ValidationUtils.isConnectingToInternet(this)) {
                SharedUtils.getInstance().showProgressDialog(this);
                accountActivityViewModelLazy.getValue().updateProfileInfo(getProfileInfoRequest()).observe(this, new Observer<Response<MessageResponse>>() {
                    @Override
                    public void onChanged(Response<MessageResponse> offerResponseResponse) {
                        SharedUtils.getInstance().cancelDialog();
                        if (offerResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE
                                || offerResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE_SECOND) {
                            if (offerResponseResponse.body() != null) {
                                showSnackbar(offerResponseResponse.body().getMessage());
                            }
                            new Handler().postDelayed(() -> onBackPressed(), ConfigurationFile.Constants.WAIT_VALUE);

                        } else {
                            showErrorMessage(offerResponseResponse);
                        }
                    }
                });
            } else {
                showSnackbar(getString(R.string.there_is_no_internet_connection));
            }
        } else {
            showErrors();
        }
    }

    private void showErrorMessage(Response<MessageResponse> offerResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(offerResponseResponse.errorBody().string(), ErrorResponse.class);
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

    private ProfileInfoRequest getProfileInfoRequest() {
        ProfileInfoRequest profileInfoRequest = new ProfileInfoRequest();
        profileInfoRequest.setEmail(binding.etEmail.getText().toString());
        profileInfoRequest.setFirstName(binding.etFirstName.getText().toString());
        profileInfoRequest.setLastName(binding.etLastName.getText().toString());
        profileInfoRequest.setPhone(binding.etPhone.getText().toString());
        if (photoUploaded) {
            profileInfoRequest.setProfilePictureUrl(uploadedFileUrl.toString());
        }
        return profileInfoRequest;
    }

    private void showErrors() {
        if (binding.etFirstName.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_first_name_error_message));
            return;
        }
        if (binding.etLastName.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_last_name_error_message));
            return;
        }
        if (binding.etEmail.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_mail_error_message));
            return;
        }
        if (binding.etPhone.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_phone_error_message));
            return;
        }
        if (binding.etFirstName.getText().toString().equals(customUtilsLazy.getValue().getSavedMemberData().getFirstName())
                || binding.etLastName.getText().toString().equals(customUtilsLazy.getValue().getSavedMemberData().getLastName())
                || binding.etEmail.getText().toString().equals(customUtilsLazy.getValue().getSavedMemberData().getEmail())
                || binding.etPhone.getText().toString().equals(customUtilsLazy.getValue().getSavedMemberData().getPhone())
        ) {
            showSnackbar(getString(R.string.nothing_changed));
        }

    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    public void changePassword(View view) {
        Intent intent = new Intent(this, ChangePasswordProfileActivity.class);
        startActivity(intent);
    }

    public void showFileToChoose(View view) {
        showFileToChoose();
    }

    private void showFileToChoose() {
        ImagePicker.Companion.with(this).start();
//                .crop(1f, 1f)       //Crop Square image(Optional)
//                .compress(1024)   //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(620, 620) //Final image resolution will be less than 620 x 620(Optional)
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //this method will upload the file
    private void uploadFile() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            if (filePath != null) {
                storageRef = FirebaseStorage.getInstance().getReference();
                initializeProgressDialog();
                putFileToStorageReference();
            } else {
//                Snackbar.make(binding.getRoot(), "There is no pictures!!", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void putFileToStorageReference() {
        riversRef = storageRef.child("profile_pic" + ConfigurationFile.Constants.SERVICEPROVIDER_DIRECTORY_NAME);
        riversRef.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadedFileUrl = null;
                        try {
                            uploadedFileUrl = new URL(uri.toString());
                            photoUploaded = true;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });
                    progressDialog.dismiss();
                    Snackbar.make(binding.getRoot(), "Uploaded Successfully", Snackbar.LENGTH_SHORT).show();
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
