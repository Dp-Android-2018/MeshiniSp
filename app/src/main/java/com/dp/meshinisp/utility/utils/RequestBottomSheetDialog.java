package com.dp.meshinisp.utility.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.dp.meshinisp.R;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.SearchRequestsResponse;
import com.dp.meshinisp.view.ui.activity.RequestsActivity;
import com.dp.meshinisp.view.ui.adapter.SpinnerAdapter;
import com.dp.meshinisp.view.ui.callback.OnDateTimeSelected;
import com.dp.meshinisp.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Lazy<MainActivityViewModel> mainActivityViewModelLazy = inject(MainActivityViewModel.class);
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private EditText fromEditText;
    private EditText toEditText;
    private AppCompatSpinner countrySpinner;
    private SpinnerAdapter countrySpinnerAdapter;
    private int countryId;
    private View v;
    private Button btnSearch;
    private List<CountryCityResponseModel> countryCityPojos;


    public void setCountries(List<CountryCityResponseModel> countryCityPojos) {
        this.countryCityPojos = countryCityPojos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.request_guide_dialog, container, false);
        makeActionOnLayoutComponents(v.getRootView());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!countryCityPojos.isEmpty()) {
            setCountrySpinner();
        }
        makeSearch(btnSearch);
    }

    private void makeActionOnLayoutComponents(View rootView) {
        fromEditText = (EditText) rootView.findViewById(R.id.et_dialog_from);
        toEditText = (EditText) rootView.findViewById(R.id.et_dialog_to);
        countrySpinner = rootView.findViewById(R.id.sp_country_request);
        btnSearch = rootView.findViewById(R.id.bt_search_for_requests);
        pickDateAndTime(fromEditText);
        pickDateAndTime(toEditText);

    }


    private void setCountrySpinner() {
        countrySpinnerAdapter = new SpinnerAdapter(getActivity(), countryCityPojos);
        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = countryCityPojos.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showSnackbarOnDialogView(getString(R.string.please_select_country));
            }
        });
    }

    private void makeSearch(Button btnSearch) {
        btnSearch.setOnClickListener(v -> {
            if (!fromEditText.getText().toString().isEmpty()
                    && !toEditText.getText().toString().isEmpty()) {
                if (ValidationUtils.isConnectingToInternet(v.getContext())) {
                    SharedUtils.getInstance().showProgressDialog(v.getContext());
                    makeSearchRequest();
                } else {
                    showSnackbarOnDialogView(getString(R.string.there_is_no_internet_connection));
                }
            } else {
                showErrors();
            }
        });
    }

    private void makeSearchRequest() {
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        mainActivityViewModelLazy.getValue().searchForRequests(1,
                countryId, fromEditText.getText().toString(), toEditText.getText().toString())
                .observe(this, searchRequestsResponseResponse -> {
                    SharedUtils.getInstance().cancelDialog();
                    if (searchRequestsResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > searchRequestsResponseResponse.code()) {
                        if (searchRequestsResponseResponse.body() != null) {
                            if (!searchRequestsResponseResponse.body().getData().isEmpty()) {
                                openActivityRequests();
                            } else {
                                showSnackbarOnDialogView(getString(R.string.there_is_no_requests_available));
                            }
                        }
                    } else {
                        showError(searchRequestsResponseResponse);
                    }
                });
    }

    private void showError(Response<SearchRequestsResponse> searchRequestsResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(searchRequestsResponseResponse.errorBody().string(), ErrorResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String error = "";
        for (String string : errorResponse.getErrors()) {
            error += string;
            error += "\n";
        }
        showSnackbarOnDialogView(error);
    }

    public void showSnackbarOnDialogView(String message) {
        Snackbar.make(v.getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void openActivityRequests() {
        Intent intent = new Intent(getActivity(), RequestsActivity.class);
        intent.putExtra(ConfigurationFile.Constants.COUNTRY_ID, countryId);
        intent.putExtra(ConfigurationFile.Constants.DATE_FROM, fromEditText.getText().toString());
        intent.putExtra(ConfigurationFile.Constants.DATE_TO, toEditText.getText().toString());
        startActivity(intent);
        clearAllFields();
    }

    private void clearAllFields() {
        fromEditText.setText("");
        toEditText.setText("");
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    private void showErrors() {
        if (fromEditText.getText().toString().isEmpty()) {
            showSnackbarOnDialogView(getString(R.string.please_select_start_date));
            return;
        }

        if (toEditText.getText().toString().isEmpty()) {
            showSnackbarOnDialogView(getString(R.string.please_select_end_date));
        }
    }

    private void pickDateAndTime(EditText editText) {
        editText.setOnClickListener(v -> DateTimePicker.getInstance().showDatePickerDialog(v.getRootView().getContext(), new OnDateTimeSelected() {
            @Override
            public void onDateReady(String date) {
                editText.setText(date);
            }
        }));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}