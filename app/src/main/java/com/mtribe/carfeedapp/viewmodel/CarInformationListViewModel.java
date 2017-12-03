package com.mtribe.carfeedapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mtribe.carfeedapp.CarFeedApplication;
import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;

import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class CarInformationListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<CarInformationEntity>> mObservableCarInfoEntities;

    public CarInformationListViewModel(@NonNull Application application) {
        super(application);

        mObservableCarInfoEntities = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableCarInfoEntities.setValue(null);

        LiveData<List<CarInformationEntity>> carInformationEntities = ((CarFeedApplication) application).getRepository().getCarInformationEntities();

        // observe the changes of the products from the database and forward them
        mObservableCarInfoEntities.addSource(carInformationEntities, new Observer<List<CarInformationEntity>>() {
            @Override
            public void onChanged(@Nullable List<CarInformationEntity> value) {
                mObservableCarInfoEntities.setValue(value);
            }
        });
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<CarInformationEntity>> getCarInformationEntities() { return mObservableCarInfoEntities;
    }
}
