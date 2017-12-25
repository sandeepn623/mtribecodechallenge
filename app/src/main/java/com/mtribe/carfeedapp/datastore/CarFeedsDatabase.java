package com.mtribe.carfeedapp.datastore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mtribe.carfeedapp.datastore.dao.CarFeedDao;
import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;

import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */
@Database(entities = {CarInformationEntity.class}, version = 1)
public abstract class CarFeedsDatabase extends RoomDatabase {

    private static final String TAG = "CarFeedsDatabase";

    private static final String DATABASE_NAME = "carfeedapp-db";

    private static CarFeedsDatabase sInstance;

    public abstract CarFeedDao daoAccess();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();


    private static final String URL = "http://data.m-tribes.com/locations.json";

    public static CarFeedsDatabase getInstance(Context context) {
        Log.d(TAG, "CarFeedsDatabase getInstance called: " + (sInstance == null));
        if(sInstance == null) {
            synchronized (CarFeedsDatabase.class) {
                if(sInstance == null) {
                    Log.d(TAG, "CarFeedsDatabase getInstance buildDatabase called");
                    sInstance = buildDatabase(context);
                }
            }
        }

        return sInstance;
    }

    private static CarFeedsDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), CarFeedsDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        sInstance.setDatabaseCreated();
                    }
                }).build();
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public void insertData(final CarFeedsDatabase database, final List<CarInformationEntity> carInformationEntities) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                database.daoAccess().inserMultipleCarInfo(carInformationEntities);
            }
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
