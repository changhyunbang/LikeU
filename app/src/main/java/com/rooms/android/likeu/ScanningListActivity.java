package com.rooms.android.likeu;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by changhyun on 2017. 9. 5..
 */

public class ScanningListActivity extends Activity {

    @Bind(R.id.BTN_REFRESH)
    Button btnRefresh;
    @Bind(R.id.RV_LIST)
    RecyclerView rvList;

    private DeviceAdapter adapter;

    private static String TAG = ScanningListActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 200;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanninglist);

        ButterKnife.bind(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();

        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceAdapter(null);

        enableBluetooth();
    }

    public void enableBluetooth() {
        Log.i(TAG, "enableBluetooth()");

        if (mBluetoothAdapter.isEnabled()) {
            startScanning();
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("This app needs location access");
//            builder.setMessage("Please grant location access so this app can detect peripherals.");
//            builder.setPositiveButton(android.R.string.ok, null);
//            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
                    if( Build.VERSION.SDK_INT >= 23 ) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
//                }
//            });
//            builder.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "coarse location permission granted");
                    startScanning();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT :
                if (resultCode == Activity.RESULT_OK) {
                    startScanning();
                } else {

                }
                break;
            default:
                break;
        }
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.i(TAG, "onScanResult : " + result.getDevice().getAddress());
        }
    };

    public void startScanning() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mBluetoothScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mBluetoothScanner.stopScan(leScanCallback);
            }
        });
    }

    @OnClick(R.id.BTN_REFRESH)
    public void clickRefresh() {
        enableBluetooth();
    }
}
