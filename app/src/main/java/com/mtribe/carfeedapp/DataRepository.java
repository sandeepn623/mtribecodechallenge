package com.mtribe.carfeedapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mtribe.carfeedapp.datastore.CarFeedsDatabase;
import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;

import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class DataRepository {

    private static final String TAG = "DataRepository";
    private static DataRepository sInstance;

    private final CarFeedsDatabase mDatabase;

    private MediatorLiveData<List<CarInformationEntity>> mObservableCarEntities;
    public DataRepository(final CarFeedsDatabase database) {
        Log.d(TAG, "DataRepository constructor called");
        this.mDatabase = database;
        mObservableCarEntities = new MediatorLiveData<>();
        LiveData<List<CarInformationEntity>> carInformationLiveData = mDatabase.daoAccess().fetchAllRecords();
        mObservableCarEntities.addSource(carInformationLiveData, new Observer<List<CarInformationEntity>>() {
            @Override
            public void onChanged(@Nullable List<CarInformationEntity> carInformationEntities) {
                Log.d(TAG, "DataRepository onChanged called");
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableCarEntities.postValue(carInformationEntities);
                }
            }
        });
    }

    public static DataRepository getInstance(final CarFeedsDatabase database) {
        Log.d(TAG, "DataRepository getInstance called");
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    Log.d(TAG, "DataRepository DataRepository Instance created");
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }


    public LiveData<List<CarInformationEntity>> getCarInformationEntities() {
        return mObservableCarEntities   ;
    }
}
