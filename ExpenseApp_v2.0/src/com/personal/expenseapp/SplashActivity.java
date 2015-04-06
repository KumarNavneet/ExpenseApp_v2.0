package com.personal.expenseapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.salesforce.androidsdk.rest.RestClient;

public class SplashActivity extends Activity {
	boolean haveConnectedWifi = false;
	boolean haveConnectedMobile = false;
	public static RestClient client;
	private String[] recentEntries = null;
	private String apiVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		apiVersion = getString(R.string.api_version);
		GifWebView view = new GifWebView(this,
				"file:///android_asset/splash.gif");
		setContentView(view);

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (haveConnectedWifi == false && haveConnectedMobile == false) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(SplashActivity.this,
							"No Network Connectivity..Exiting!!",
							Toast.LENGTH_LONG).show();
					finish();
				}
			}, 2000);
		} else {
			final Intent i = new Intent();
			i.setClass(this, MainActivity.class);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(i);
					finish();
				}
			}, 2000);

		}
	}
}
