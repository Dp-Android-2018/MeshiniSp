package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegister2Binding;
import com.dp.meshinisp.databinding.LanguageSpinnerListItemBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.service.repository.remotes.Register2Repository;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.DestinationAdapter;
import com.dp.meshinisp.view.ui.adapter.LanguageRecyclerViewAdapter;
import com.dp.meshinisp.view.ui.adapter.SpinnerAdapter;
import com.dp.meshinisp.view.ui.callback.OnLanguageItemClickListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RegisterActivity2 extends BaseActivity {

    ActivityRegister2Binding binding;
    private Lazy<Register2Repository> registerRepositoryLazy = inject(Register2Repository.class);
    Lazy<RegisterRequest> registerRequestLazy = inject(RegisterRequest.class);
    RegisterRequest register1Request;
    SpinnerAdapter languageSpinnerAdapter;

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private StorageReference storageRef;
    private StorageReference riversRef;
    Bitmap bitmap;
    private boolean photoUploaded;
    private boolean selectLanguage;
    private URL uploadedFileUrl;
    int startedSessionId;
    List<Integer> languageIds;
    AppCompatSpinner spinner1;
    ConstraintLayout childLayout;
    int selectedLanguageId;
    private DestinationAdapter destinationAdapter;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;
    LanguageRecyclerViewAdapter languageRecyclerViewAdapter;
    List<CountryCityResponseModel> allLanguages = new ArrayList<>();
    List<Integer> selectLanguages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register2);
        register1Request = registerRequestLazy.getValue();
        register1Request = getIntent().getParcelableExtra(ConfigurationFile.Constants.REGISTER1DATA);
        languageIds = new ArrayList<>();
        getLanguages();
        makeActionToUploadImage();
        makeActionOnClickOnBtnSignUP();

    }

    private void initizeShowStateDialog() {
        layoutBottomSheet = findViewById(R.id.bottom_sheet_language_list);
        makeActionOnLayoutComponents(layoutBottomSheet.getRootView());
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
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

    private void makeActionOnLayoutComponents(View rootView) {
        ImageView ivClose = rootView.findViewById(R.id.iv_language_close);
        ivClose.setOnClickListener(v1 -> sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        initializeRecyclerView(rootView);
    }

    private void initializeRecyclerView(View rootView) {
        RecyclerView destinationsRecyclerView = rootView.findViewById(R.id.rv_language);
        if (!allLanguages.isEmpty()) {
            languageRecyclerViewAdapter = new LanguageRecyclerViewAdapter(allLanguages);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false);
            destinationsRecyclerView.setLayoutManager(linearLayoutManager);
            destinationsRecyclerView.setAdapter(languageRecyclerViewAdapter);
            makeActionOnClickOnRecyclerViewItem();
        } else {
        }
    }

    private void makeActionOnClickOnRecyclerViewItem() {
        languageRecyclerViewAdapter.setOnItemClickListener(new OnLanguageItemClickListener() {
            @Override
            public void onCheckboxChecked(int position, LanguageSpinnerListItemBinding convertView) {
                selectLanguages.add(allLanguages.get(position).getId());
            }

            @Override
            public void onCheckboxUnChecked(int position, LanguageSpinnerListItemBinding convertView) {
                for (int i = 0; i < selectLanguages.size(); i++) {
                    if (selectLanguages.get(i) == allLanguages.get(position).getId()) {
                        selectLanguages.remove(i);
                    }
                }
            }
        });
    }

    private void getLanguages() {
        SharedUtils.getInstance().showProgressDialog(this);
        registerRepositoryLazy.getValue().getLanguages().observeForever(places -> {
            SharedUtils.getInstance().cancelDialog();
            allLanguages = places;
            initizeShowStateDialog();
            setLanguageSpinner();
        });

    }


    private void makeActionOnClickOnBtnSignUP() {
        binding.btSignUp.setOnClickListener(v -> {
//            languageIds = destinationAdapter.getSelectedPlaces();
            languageIds = selectLanguages;
            register1Request.setLanguageIds(languageIds);
            if (filePath != null && !languageIds.isEmpty()) {
                uploadFile();

            } else {
                showErrors();
            }
        });
    }

    private void showErrors() {
        if (filePath == null) {
            showSnackbar(getString(R.string.please_upload_your_photo));
            return;
        }
        if (languageIds.isEmpty()) {
            showSnackbar(getString(R.string.please_choose_languages));
        }
    }

    private void makeActionToUploadImage() {
        binding.ivUpload.setOnClickListener(v -> showFileToChoose());
    }

    public void setLanguageSpinner() {
        binding.tvAddAnotherLanguage.setOnClickListener(v -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                layoutBottomSheet.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        return super.dispatchTouchEvent(event);
    }

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
                putFileToStorageReference();
            } else {
                Snackbar.make(binding.getRoot(), "There is no pictures!!", Snackbar.LENGTH_SHORT).show();
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
                            register1Request.setProfilePictureUrl(uploadedFileUrl.toString());
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
                        binding.tvProgressNumber.setText(getResources().getString(R.string.operation_completed));
                    }
                });
    }

    private void openNextActivity() {
        Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
        intent.putExtra(ConfigurationFile.Constants.REGISTER1DATA, new Gson().toJson(register1Request));
        startActivity(intent);
        finish();
    }

}
