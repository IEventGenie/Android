package com.beacons.app.beaconsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.slidingmenu.SlidingMenuSetup;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mobstac.beaconstac.core.Beaconstac;
import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSConstants;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.core.PlaceSyncReceiver;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;
import com.mobstac.beaconstac.models.MSCard;
import com.mobstac.beaconstac.models.MSMedia;
import com.mobstac.beaconstac.utils.MSException;
import com.mobstac.beaconstac.utils.MSLogger;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeActivity extends BaseActivity {


    SlidingMenu menu;
    RelativeLayout actionBar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu != null)
                menu.showMenu(true);
            }
        });

        menu = new SlidingMenuSetup(HomeActivity.this).setSlidingMenu();

        webView = (WebView)findViewById(R.id.web_view);
        webView.setWebViewClient(new MyBrowser());

        String url = "http://www.google.com";

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}