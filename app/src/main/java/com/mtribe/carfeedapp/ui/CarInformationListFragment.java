package com.mtribe.carfeedapp.ui;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mtribe.carfeedapp.R;
import com.mtribe.carfeedapp.databinding.CarinfoListFragmentBinding;
import com.mtribe.carfeedapp.datastore.DataStoreManager;
import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;
import com.mtribe.carfeedapp.model.CarInformation;
import com.mtribe.carfeedapp.utils.NetworkUtils;
import com.mtribe.carfeedapp.viewmodel.CarInformationListViewModel;

import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class CarInformationListFragment extends Fragment {

    public static final String TAG = "CarInformationListFragment";

    private CarInformationAdapter mCarInformationAdapter;

    private CarinfoListFragmentBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.carinfo_list_fragment, container, false);

        mCarInformationAdapter = new CarInformationAdapter(mCarInfoItemClickCallback);
        mBinding.carsInfoList.setAdapter(mCarInformationAdapter);

        return mBinding.getRoot();

    }

    private final CarInfoItemClickCallback mCarInfoItemClickCallback = new CarInfoItemClickCallback() {
        @Override
        public void onClick(CarInformation carInformation) {
            //Nothing done here
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    ((MainActivity) getActivity()).showMap();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CarInformationListViewModel viewModel = ViewModelProviders.of(this).get(CarInformationListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(CarInformationListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCarInformationEntities().observe(this, new Observer<List<CarInformationEntity>>() {
            @Override
            public void onChanged(@Nullable List<CarInformationEntity> carInformationEntityList) {
                if (carInformationEntityList != null) {
                    mBinding.setIsLoading(false);
                    mCarInformationAdapter.setCarInformationList(carInformationEntityList);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (NetworkUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
                    DataStoreManager.getInstance().getCarFeeds(getActivity().getApplicationContext());
                } else {
                    showAlert(getResources().getString(R.string.networkError));
                }
                return true;
            default:
        }       return super.onOptionsItemSelected(item);
    }

    private AlertDialog showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
