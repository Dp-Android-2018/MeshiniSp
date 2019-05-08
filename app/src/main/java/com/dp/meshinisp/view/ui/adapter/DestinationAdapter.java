package com.dp.meshinisp.view.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.repository.remotes.Register2Repository;
import com.dp.meshinisp.utility.utils.SharedUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestantionHolder> {

    private ObservableInt recyclerViewSize;
    private Lazy<Register2Repository> registerRepositoryLazy = inject(Register2Repository.class);
    private int selectedPlace = -1;
    private HashSet<Integer> selectedPlaces = new HashSet<>();
    private List<Integer> selectLanguages = new ArrayList<>();
    private List<CountryCityResponseModel> allPlaces;
    private List<CountryCityResponseModel> stillPlaces;

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

                spinnerAdapter = new LanguageSpinnerAdapter(itemView.getContext(), places);
                spinner.setAdapter(spinnerAdapter);
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
        return selectLanguages;
    }
}
