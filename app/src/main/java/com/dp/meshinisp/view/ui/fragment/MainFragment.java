package com.dp.meshinisp.view.ui.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.FragmentMainBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.RequestBottomSheetDialog;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.activity.MainActivity;
import com.dp.meshinisp.view.ui.adapter.SpinnerAdapter;
import com.dp.meshinisp.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    public static DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private Lazy<MainActivityViewModel> mainActivityViewModelLazy = inject(MainActivityViewModel.class);
    private EditText fromEditText;
    private EditText toEditText;
    private AppCompatSpinner countrySpinner;
    private Dialog dialog;
    private SpinnerAdapter countrySpinnerAdapter;
    private CountryCityResponseModel selectedCountry;
    private int countryId;
    private View v;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;
    private RequestBottomSheetDialog bottomSheet;
    private List<CountryCityResponseModel> countryCityPojosdata;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        makeActionOnClickOnBtSearch();
    }

    private void setupToolbar() {
        binding.mainFragmentToolbar.setNavigationIcon(R.drawable.ic_menu);
        binding.mainFragmentToolbar.setNavigationOnClickListener(v -> MainActivity.drawer.openDrawer(GravityCompat.START));
        bottomSheet = new RequestBottomSheetDialog();
        setCountrySpinner();
    }

    public void makeActionOnClickOnBtSearch() {
        if (bottomSheet.isHidden()) {
            binding.btSearch.setVisibility(View.VISIBLE);
        }

        binding.btSearch.setOnClickListener(v -> {
            if (!bottomSheet.isAdded()){
                bottomSheet.show(getActivity().getSupportFragmentManager(), "exampleBottomSheet");
            }
        });
    }


    private void setCountrySpinner() {
        if (ValidationUtils.isConnectingToInternet(getActivity())) {
            SharedUtils.getInstance().showProgressDialog(getActivity());
            mainActivityViewModelLazy.getValue().getCountries().observe(this, countryCityPojos -> {
                SharedUtils.getInstance().cancelDialog();
                bottomSheet.setCountries(countryCityPojos);
            });
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

}
