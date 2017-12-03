package com.mtribe.carfeedapp;

import android.app.Application;
import android.util.Log;

import com.mtribe.carfeedapp.datastore.CarFeedsDatabase;
import com.mtribe.carfeedapp.datastore.DataStoreManager;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class CarFeedApplication extends Application {

    private static final String TAG = "CarFeedApplication";

    private BackgroundJobExecutor mBgExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mBgExecutors = new BackgroundJobExecutor();
    }

    public CarFeedsDatabase getDatabase() {
        Log.d(TAG, "getDatabase called");
        CarFeedsDatabase carFeedsDatabase = DataStoreManager.getInstance().initializeDatabase(this, mBgExecutors);
        carFeedsDatabase.checkAndUpdateIfDatabaseExists(this);
        return carFeedsDatabase;
    }

    public DataRepository getRepository() {
        Log.d(TAG, "getRepository called");
        return DataRepository.getInstance(getDatabase());
    }


}
