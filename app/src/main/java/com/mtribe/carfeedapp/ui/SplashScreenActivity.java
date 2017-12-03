package com.mtribe.carfeedapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.mtribe.carfeedapp.R;
import com.mtribe.carfeedapp.datastore.DataStoreManager;
import com.mtribe.carfeedapp.utils.Constants;
import com.mtribe.carfeedapp.utils.NetworkUtils;

/**
 * Created by Sandeepn on 03-12-2017.
 */

public class SplashScreenActivity extends AppCompatActivity{
    private DataStoreManager mDataStoreManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot())
        {
            Intent intent = getIntent();
            String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN))
            {
                finish();
                return;
            }
        }
        setContentView(R.layout.splash_screen_layout);
        mDataStoreManager = DataStoreManager.getInstance();
        mDataStoreManager.setUIHandler(mHandler);
        checkIfDataExists();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NOTIFY_UI_SUCCESS:
                    launchMainActivity();
                    break;
                case Constants.NOTIFY_UI_FAILED:
                    AlertDialog alertDialog = showAlert(getResources().getString(R.string.requesterror));
                    alertDialog.show();
                    break;
                case Constants.NUMBER_OF_RECORDS:
                    int dbRecordsCount = msg.arg1;
                    fetchCarFeed(dbRecordsCount);
                    break;
            }
        }
    };

    private void fetchCarFeed(int recordsCount) {
        Context context = this.getApplicationContext();
        if(NetworkUtils.isNetworkAvailable(context)) {
            if(recordsCount <= 0) {
                mDataStoreManager.getCarFeeds(context);
            } else {
                launchMainActivity();
            }
        } else {
            //create an alertDialog.
            AlertDialog alertDialog = showAlert(getResources().getString(R.string.networkError));
            alertDialog.show();
        }
    }

    private void checkIfDataExists() {
        mDataStoreManager.getRecordsCount(getApplicationContext());
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        mDataStoreManager.setUIHandler(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private AlertDialog showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        mDataStoreManager.setUIHandler(null);
                        finish();
                    }
                });

        return builder.create();
    }
}
