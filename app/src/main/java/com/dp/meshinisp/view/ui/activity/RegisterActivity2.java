package com.dp.meshinisp.view.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegister2Binding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.SpinnerAdapter;
import com.dp.meshinisp.viewmodel.Register2ViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RegisterActivity2 extends AppCompatActivity {

    ActivityRegister2Binding binding;
    Lazy<Register2ViewModel> registerViewModelLazy = inject(Register2ViewModel.class);
    Lazy<RegisterRequest> registerRequestLazy = inject(RegisterRequest.class);
    RegisterRequest register1Request;
    //    RegisterRequest register2Request;
    SpinnerAdapter languageSpinnerAdapter;

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
    int startedSessionId;
    ArrayList<Integer> languageIds;
    AppCompatSpinner spinner1;
    ConstraintLayout childLayout;
    int selectedLanguageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register2);
        register1Request = registerRequestLazy.getValue();
        register1Request = getIntent().getParcelableExtra(ConfigurationFile.Constants.REGISTER1DATA);

//        setDataToRegister2Request();

        languageIds = new ArrayList<>();

        setLanguageSpinner();
        makeActionToUploadImage();
        makeActionOnClickOnBtnSignUP();
    }

    private void makeActionOnClickOnBtnSignUP() {
        binding.btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filePath != null && selectLanguage) {
                    uploadFile();

                } else {
                    showErrors();

                }
            }
        });
    }

    private void showErrors() {
        if (filePath == null){
            showSnackbar(getString(R.string.please_upload_your_photo));
            return;
        }
        if (!selectLanguage){
            showSnackbar(getString(R.string.please_choose_languages));
        }
    }

    private void makeActionToUploadImage() {
        binding.ivUpload.setOnClickListener(v -> showFileToChoose());
    }

    public void setLanguageSpinner() {

        registerViewModelLazy.getValue().getLanguages().observe(this, (List<CountryCityResponseModel> countryCityPojos) -> {

//        addLanguageSpinner();
//            setLanguageAdapter();
//            childLayout = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.language_spinner_item, null, false);
//            spinner1= childLayout.findViewById(R.id.sp_language);
            languageSpinnerAdapter = new SpinnerAdapter(RegisterActivity2.this, countryCityPojos);
            binding.spLanguage.setAdapter(languageSpinnerAdapter);
            makeActonOnSelectLanguage();

        });
    }

    private void makeActonOnSelectLanguage() {
         binding.spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CountryCityResponseModel selectedLanguage = (CountryCityResponseModel) parent.getItemAtPosition(position);
                    selectedLanguageId = selectedLanguage.getId();
                    if (languageIds.isEmpty()){
                        languageIds.add(selectedLanguageId);
                    }else {
                        for (int i=0;i<languageIds.size();i++){
                            if (!languageIds.get(i).equals(selectedLanguageId)) {
                                languageIds.add(selectedLanguageId);
                            }
                        }
                    }

                    if (!languageIds.isEmpty()) {
                        register1Request.setLanguageIds(languageIds);
                        selectLanguage=true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    showSnackbar(getString(R.string.please_choose_languages));
                }
            });
    }


    public void addAnotherLanguage(View view) {
        /*if (!languageIds.isEmpty()){
            System.out.println("languageId : "+languageIds.get(0));
            languageIds.remove(selectedLanguageId);
            addLanguageSpinner();
        }*/
    }

   /* private void addLanguageSpinner() {
        LinearLayout linearLayout = findViewById(R.id.main_container);
        childLayout.setTag(linearLayout.getChildCount() + 1); // set some Id
        linearLayout.addView(childLayout); // Adding for first time
        for(int i=0; i<5; i++) {
            childLayout.setTag(linearLayout.getChildCount() + 1);
            linearLayout.addView(childLayout); //child_layout is your row layout design
        }
    }*/

    public void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
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
                binding.ivUpload.setImageBitmap(bitmap);
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
                Snackbar.make(binding.getRoot(), "There is no pictures!!", Snackbar.LENGTH_SHORT).show();
            }
        }else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void putFileToStorageReference() {
        riversRef = storageRef.child("profile_pic"+ConfigurationFile.Constants.SERVICEPROVIDER_DIRECTORY_NAME);
        riversRef.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadedFileUrl = null;
                        try {
                            uploadedFileUrl = new URL(uri.toString());
                            register1Request.setProfilePictureUrl(uploadedFileUrl.toString());
                            photoUploaded = true;
                            openNextActivity();
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

    private void openNextActivity() {
        Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
        intent.putExtra(ConfigurationFile.Constants.REGISTER1DATA, new Gson().toJson(register1Request));
        startActivity(intent);
    }

}
