package com.dp.meshinisp.view.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.FragmentMainBinding;
import com.dp.meshinisp.service.repository.remotes.MainRepository;
import com.dp.meshinisp.utility.utils.RequestBottomSheetDialog;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.activity.MainActivity;
import com.dp.meshinisp.view.ui.activity.NoInternetConnectionActivity;
import com.dp.meshinisp.viewmodel.MainActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    public static DrawerLayout drawer;
    private Lazy<MainActivityViewModel> mainActivityViewModelLazy = inject(MainActivityViewModel.class);
    private Lazy<MainRepository> mainRepositoryLazy = inject(MainRepository.class);
    private RequestBottomSheetDialog bottomSheet;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            if (!bottomSheet.isAdded()) {
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
            listenToMakeRequestAgain();
        } else {
            Intent intent = new Intent(getActivity(), NoInternetConnectionActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().finishAffinity();
//            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void listenToMakeRequestAgain() {
        mainRepositoryLazy.getValue().setConnectionTimeoutListener((connectionTimeout, errorMessage) -> {
            if (connectionTimeout) {
                SharedUtils.getInstance().cancelDialog();
                showSnackbar(errorMessage);
                setCountrySpinner();
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

}
