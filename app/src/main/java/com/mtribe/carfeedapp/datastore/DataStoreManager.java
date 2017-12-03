package com.mtribe.carfeedapp.datastore;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mtribe.carfeedapp.BackgroundJobExecutor;
import com.mtribe.carfeedapp.datastore.entity.CarInformationEntity;
import com.mtribe.carfeedapp.http.request.GetCarFeedRequest;
import com.mtribe.carfeedapp.http.response.model.CarFeeds;
import com.mtribe.carfeedapp.utils.Constants;
import com.mtribe.carfeedapp.utils.NetworkUtils;

import java.util.List;

/**
 * Created by Sandeepn on 02-12-2017.
 */

public class DataStoreManager {
    private static final String TAG = "DataStoreManager";
    private static DataStoreManager sInstance;

    private Handler mUIHandler;

    private BackgroundJobExecutor mBgExecutor = new BackgroundJobExecutor();

    private CarFeeds mCarFeeds;

    private DataStoreManager() {}

    public static DataStoreManager getInstance() {
        if(sInstance == null) {
            synchronized (DataStoreManager.class) {
                if(sInstance == null) {
                    sInstance = new DataStoreManager();
                }
            }
        }
            return sInstance;
    }

    public CarFeedsDatabase initializeDatabase(Context context, BackgroundJobExecutor bgExecutor) {
        return CarFeedsDatabase.getInstance(context.getApplicationContext());
    }

    public void getCarFeeds(Context context) {
        mBgExecutor.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                GetCarFeedRequest request = new GetCarFeedRequest();
                mCarFeeds = request.sendRequest(Constants.URL);
                if(mCarFeeds != null) {
                    Message msg = mHandler.obtainMessage(Constants.HTTP_OK_MSG, context);
                    mHandler.sendEmptyMessage(Constants.HTTP_OK_MSG);
                } else {
                    mHandler.sendEmptyMessage(Constants.HTTP_FAILED_MSG);
                }
            }
        });
    }

    public void insertCarRecordsIntoDB(Context context) {
        mBgExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<CarInformationEntity> carInformationEntities = DataEntityMapper.mapResponseWithFields(mCarFeeds);
                CarFeedsDatabase carFeedsDatabase = CarFeedsDatabase.getInstance(context);
                carFeedsDatabase.insertData(carFeedsDatabase, carInformationEntities);

                mHandler.sendEmptyMessage(Constants.INSERT_DB_SUCCESS);
            }
        });

    }

    public boolean isDataBaseCreated(Context context) {
        return CarFeedsDatabase.getInstance(context).checkAndUpdateIfDatabaseExists(context);
    }
    public void setUIHandler(Handler handler) {
        this.mUIHandler = handler;
    }

    public void getRecordsCount(Context context) {
        mBgExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int numberOfRecords = CarFeedsDatabase.getInstance(context).daoAccess().countCarFeeds();
                Message msg = mHandler.obtainMessage(Constants.NUMBER_OF_RECORDS);
                msg.arg1 = numberOfRecords;
                mHandler.sendMessage(msg);
            }
        });
    }
    private void notifyUI(Message msg) {
        if(mUIHandler != null) {
            mUIHandler.sendMessage(msg);
        } else {
            Log.d(TAG, "ui handler is null!");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Message message;
            switch (msg.what) {
                case Constants.HTTP_OK_MSG:
                    Context context = (Context) msg.obj;
                    insertCarRecordsIntoDB(context);
                    break;
                case Constants.HTTP_FAILED_MSG:
                    message = mHandler.obtainMessage(Constants.HTTP_FAILED_MSG);
                    notifyUI(message);
                    break;
                case Constants.INSERT_DB_SUCCESS:
                    message = mHandler.obtainMessage(Constants.NOTIFY_UI_SUCCESS);
                    notifyUI(message);
                    break;
                case Constants.NUMBER_OF_RECORDS:
                    message = mHandler.obtainMessage(Constants.NUMBER_OF_RECORDS);
                    message.arg1 = msg.arg1;
                    notifyUI(message);
                    break;
            }
        }
    };
}
