package com.mtribe.carfeedapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mtribe.carfeedapp.BackgroundJobExecutor;
import com.mtribe.carfeedapp.datastore.CarFeedsDatabase;
import com.mtribe.carfeedapp.datastore.DataEntityMapper;
import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;
import com.mtribe.carfeedapp.http.request.MTribeServiceApi;
import com.mtribe.carfeedapp.http.request.ServiceGenerator;
import com.mtribe.carfeedapp.http.response.CarFeeds;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class DataRepository {

    private static final String TAG = "DataRepository";
    private static DataRepository sInstance;
    private final CarFeedsDatabase mDatabase;
    private final MTribeServiceApi mTribeServiceApi;

    private MediatorLiveData<List<CarInformationEntity>> mObservableCarEntities;
    public DataRepository(final CarFeedsDatabase database, BackgroundJobExecutor appExecutors) {
        this.mDatabase = database;
        mObservableCarEntities = new MediatorLiveData<>();
        this.mTribeServiceApi = ServiceGenerator.createService(MTribeServiceApi.class);
        LiveData<List<CarInformationEntity>> carInformationLiveData = mDatabase.daoAccess().fetchAllRecords();
        mObservableCarEntities.addSource(carInformationLiveData, new Observer<List<CarInformationEntity>>() {
            @Override
            public void onChanged(@Nullable List<CarInformationEntity> carInformationEntities) {
                Log.d(TAG, "DataRepository onChanged called");
                if (carInformationEntities != null && carInformationEntities.size() > 0) {
                    mObservableCarEntities.postValue(carInformationEntities);
                } else {
                    Call<CarFeeds> call = mTribeServiceApi.getCarFeeds();
                    call.enqueue(new Callback<CarFeeds>() {
                        @Override
                        public void onResponse(Call<CarFeeds> call, Response<CarFeeds> response) {
                            if(response.isSuccessful()) {
                                CarFeeds carFeeds = response.body();
                                mObservableCarEntities.postValue(DataEntityMapper.mapResponseWithFields(carFeeds));
                            }
                        }

                        @Override
                        public void onFailure(Call<CarFeeds> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }


    public static DataRepository getInstance(final CarFeedsDatabase database, BackgroundJobExecutor appExecutors) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database, appExecutors);
                }
            }
        }
        return sInstance;
    }


    public LiveData<List<CarInformationEntity>> getCarInformationEntities() {
        return mObservableCarEntities;
    }
}
