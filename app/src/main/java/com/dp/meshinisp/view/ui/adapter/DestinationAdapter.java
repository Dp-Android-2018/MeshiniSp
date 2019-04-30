package com.dp.meshinisp.view.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.dp.meshinisp.R;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.repository.remotes.Register2Repository;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.view.ui.callback.OnLanguageItemClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestantionHolder> {

    private ObservableInt recyclerViewSize;
    private Lazy<Register2Repository> registerRepositoryLazy = inject(Register2Repository.class);
    private int selectedPlace = -1;
    HashSet<Integer> selectedPlaces = new HashSet<>();
    List<Integer> selectLanguages = new ArrayList<>();
    List<CountryCityResponseModel> allPlaces;
    List<CountryCityResponseModel> stillPlaces;

    public DestinationAdapter() {
        recyclerViewSize = new ObservableInt(1);
    }

    @NonNull
    @Override
    public DestantionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.destination_list_item_layout, viewGroup, false);
        return new DestantionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DestantionHolder destantionHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return recyclerViewSize.get();
    }

    public void incrementRecyclerViewSize() {
        int temp = this.recyclerViewSize.get();
//        selectedPlaces.add(selectedPlace);
        this.recyclerViewSize.set(temp + 1);

    }

    public void decrementRecyclerViewSize() {
        int temp = this.recyclerViewSize.get();
        this.recyclerViewSize.set(temp - 1);
    }

    public void deleteItem(int position) {
        decrementRecyclerViewSize();
        if (!selectedPlaces.contains(selectedPlace) && selectedPlace != -1) {
            selectedPlaces.add(selectedPlace);
        }
        selectedPlaces.remove(stillPlaces.get(position).getId());
        allPlaces.add(stillPlaces.get(position));
        this.notifyDataSetChanged();
      /*  mRecentlyDeletedItem = mListItems.get(position);
        mRecentlyDeletedItemPosition = position;
        mListItems.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();*/
    }


    class DestantionHolder extends RecyclerView.ViewHolder {
        Spinner spinner;
        LanguageSpinnerAdapter spinnerAdapter;

        public DestantionHolder(@NonNull View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.sp_Places);
            SharedUtils.getInstance().showProgressDialog(itemView.getContext());
            getPlaces().observeForever((List<CountryCityResponseModel> places) -> {
                SharedUtils.getInstance().cancelDialog();
                /*stillPlaces = places;
                for (int i = 0; i < places.size(); i++) {
                    if (!selectedPlaces.isEmpty()) {
                        if (selectedPlaces.contains(places.get(i).getId())) {
                            places.remove(places.get(i));
                        }
                    }
                }

                for (int i = 0; i < places.size(); i++) {
                    if (selectedPlace != -1) {
                        if (places.get(i).getId() == selectedPlace) {
                            places.remove(places.get(i));
                        }
                    }
                }
                allPlaces = places;*/

                spinnerAdapter = new LanguageSpinnerAdapter(itemView.getContext(), places);
                spinner.setAdapter(spinnerAdapter);
                spinner.setPrompt("Choose languages you speak");
                /*spinnerAdapter.setOnItemClickListener(new OnLanguageItemClickListener() {
                    @Override
                    public void onCheckboxChecked(int position, View convertView) {
                        selectLanguages.add(places.get(position).getId());
                        System.out.println("this language checked : " + places.get(position).getName());
                    }

                    @Override
                    public void onCheckboxUnChecked(int position, View convertView) {
                        System.out.println("this language unckecked : " + places.get(position).getName());
                        if (selectLanguages.contains(places.get(position).getId())) {
                            selectLanguages.remove(places.get(position).getId());
                        }
                    }
                });*/
                /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (selectedPlaces.contains(places.get(position).getId())) {
                            Snackbar.make(view.getRootView(), "this language selected before", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        selectedPlace = places.get(position).getId();
                       *//* for (int i = 0; i < selectedPlaces.size(); i++) {
                            if (selectedPlaces.contains(places.get(position).getId())) {
                                selectedPlaces.remove(i);
                            }
                        }*//*
//                            selectedPlaces.add(places.get(position).getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/
            });
        }

    }

    public LiveData<List<CountryCityResponseModel>> getPlaces() {
        return registerRepositoryLazy.getValue().getLanguages();
    }

    public List<CountryCityResponseModel> getAllPlaces() {
        return allPlaces;
    }

    public List<Integer> getSelectedPlaces() {
       /* if (!selectedPlaces.contains(selectedPlace) && selectedPlace != -1) {
            selectedPlaces.add(selectedPlace);
        }*/
        /*List<Integer> integers = new ArrayList<>();
        integers.addAll(selectedPlaces);*/
        return selectLanguages;
    }
}
