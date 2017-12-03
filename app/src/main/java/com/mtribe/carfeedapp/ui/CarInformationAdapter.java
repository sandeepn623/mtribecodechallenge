package com.mtribe.carfeedapp.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mtribe.carfeedapp.R;
import com.mtribe.carfeedapp.databinding.CarinfoItemBinding;
import com.mtribe.carfeedapp.model.CarInformation;

import java.util.List;
import java.util.Objects;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class CarInformationAdapter extends RecyclerView.Adapter<CarInformationAdapter.CarInformationHolder> {
    private List<? extends CarInformation> mCarInformationList;

    @Nullable
    private final CarInfoItemClickCallback mCarInfoItemClickCallback;

    public CarInformationAdapter(@Nullable CarInfoItemClickCallback clickCallback) {
        mCarInfoItemClickCallback = clickCallback;
    }

    @Override
    public CarInformationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CarinfoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.carinfo_item,
                        parent, false);
        binding.setCallback(mCarInfoItemClickCallback);
        return new CarInformationHolder(binding);
    }

    public void setCarInformationList(List<? extends CarInformation> carInformationList) {
        if(mCarInformationList == null) {
            mCarInformationList = carInformationList;
            notifyItemRangeChanged(0, carInformationList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mCarInformationList.size();
                }

                @Override
                public int getNewListSize() {
                    return mCarInformationList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mCarInformationList.get(oldItemPosition).getId() ==
                            mCarInformationList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    CarInformation newCarInformation = mCarInformationList.get(newItemPosition);
                    CarInformation oldCarInformation = mCarInformationList.get(oldItemPosition);
                    return newCarInformation.getId() == oldCarInformation.getId()
                            && newCarInformation.getVin().equals(oldCarInformation.getVin());
                }
            });
            mCarInformationList = carInformationList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onBindViewHolder(CarInformationHolder holder, int position) {
        holder.binding.setCardinformation(mCarInformationList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mCarInformationList == null ? 0 : mCarInformationList.size();
    }

    static class CarInformationHolder extends RecyclerView.ViewHolder {

        final CarinfoItemBinding binding;

        public CarInformationHolder(CarinfoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
