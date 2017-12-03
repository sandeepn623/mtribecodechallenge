package com.mtribe.carfeedapp.datastore.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;

import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */
@Dao
public interface CarFeedDao {

    @Insert
    public void inserMultipleCarInfo(List<CarInformationEntity> carInformationEntities);

    @Query("SELECT * FROM carfeeds")
    public LiveData<List<CarInformationEntity>> fetchAllRecords();

    @Query("SELECT count(*) FROM carfeeds")
    public int countCarFeeds();
}
