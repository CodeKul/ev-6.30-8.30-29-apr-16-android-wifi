package com.codekul.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);

        findViewById(R.id.btnConfigured).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(!manager.isWifiEnabled()){
                    //startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    manager.setWifiEnabled(true);
                }
                else {
                    List<WifiConfiguration> listConfigured = manager.getConfiguredNetworks();
                    for (WifiConfiguration wifiConfiguration : listConfigured) {

                        Log.i("@codekul", "BSSID - " + wifiConfiguration.BSSID);
                        Log.i("@codekul", "SSID - " + wifiConfiguration.SSID);
                    }
                }
            }
        });

        findViewById(R.id.btnScan).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                manager.startScan();
            }
        });

        findViewById(R.id.btnConnect).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", "Umesh");
                wifiConfig.preSharedKey = String.format("\"%s\"", "123456789");

                WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
//remember id
                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();
            }
        });

        registerReceiver(wifiScanReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
            List<ScanResult> listScanRes = manager.getScanResults();

            for (ScanResult scanRes : listScanRes) {

                Log.i("@codekul","BSSID - "+scanRes.BSSID);
                Log.i("@codekul","SSID - "+scanRes.SSID);
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(wifiScanReceiver);
        super.onDestroy();
    }
}
