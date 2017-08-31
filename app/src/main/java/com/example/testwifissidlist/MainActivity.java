package com.example.testwifissidlist;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private String TAG = "MainActivity";
	
	private String wifiList = "";
	
	private ArrayAdapter<String> wifiListArrayAdapter = null;
	private ListView wifiListView = null;
	private TextView textView = null;
	private Button button = null;
	private ProgressBar progressBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		textView = (TextView)findViewById(R.id.textview_id);
		button = (Button)findViewById(R.id.button_id);
		progressBar = (ProgressBar)findViewById(R.id.progreassbar_id);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		scanWifi();
	}
	
	public void OnButtonClick(View view) {
		if(view == button) {
			progressBar.setVisibility(View.VISIBLE);
			scanWifi();
		}
	}
	
	private void scanWifi() {
		//wifiManagerのインスタンスを得る
		final WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				StringBuilder sb = new StringBuilder();
				List<ScanResult> results = wifiManager.getScanResults();
				Log.i(TAG, "### " + String.format("%d networks found.",  results.size()));
				for(ScanResult result : results) {
					Log.i(TAG, "### " + String.format("SSID: '%s' BSSID:'%s' LEVEL:'%d' FREQUENCY:'%d' CAPABILITIES:'%s' SIGNALLEVEL:'%d'"
							, result.SSID, result.BSSID, result.level, result.frequency, result.capabilities, wifiManager.calculateSignalLevel(result.level, 5)));
					sb.append("SSID\t\t\t\t\t\t\t\t\t\t:").append(result.SSID).append("\r\n");
					sb.append("BSSID\t\t\t\t\t\t\t\t\t:").append(result.BSSID).append("\r\n");
					sb.append("LEVEL\t\t\t\t\t\t\t\t:").append(result.level).append("\r\n");
//					sb.append("FREQUENCY\t\t\t:").append(result.frequency).append("\r\n");
					sb.append("SIGNALLEVEL\t:").append(wifiManager.calculateSignalLevel(result.level, 5)).append("\r\n");
					sb.append("--------------------").append("\r\n");
				}
				progressBar.setVisibility(View.INVISIBLE);
				textView.setText(sb.toString());
			}
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();

		//接続済みAPの電波レベルを取得する
		WifiInfo info = wifiManager.getConnectionInfo();
		int rssi = info.getRssi();
		int signalLevel = WifiManager.calculateSignalLevel(rssi, 5);
		Log.i(TAG, "### " +  String.format("getNetworkId:%d/4", info.getNetworkId()));
		Log.i(TAG, "### " +  "getSSID:"+ info.getSSID());
		Log.i(TAG, "### " +  "getBSSID:"+ info.getBSSID());
		Log.i(TAG, "### " +  String.format("Signal Level:%d/4", signalLevel));
		Log.i(TAG, "### " +  String.format("Mac Address:%s", info.getMacAddress()));
		
//		StringBuilder sb = new StringBuilder();
//		sb.append(String.format("getNetworkId:%d/4", info.getNetworkId())).append("\r\n");
//		sb.append("getSSID:"+ info.getSSID()).append("\r\n");
//		sb.append("getBSSID:"+ info.getBSSID()).append("\r\n");
//		sb.append(String.format("Signal Level:%d/4", signalLevel)).append("\r\n");
//		sb.append(String.format("Mac Address:%s", info.getMacAddress())).append("\r\n");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
		}
	};
}
