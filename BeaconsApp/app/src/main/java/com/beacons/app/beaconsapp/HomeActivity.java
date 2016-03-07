package com.beacons.app.beaconsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.slidingmenu.SlidingMenuSetup;
import com.beacons.app.utilities.CircleTransform;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;


public class HomeActivity extends BaseActivity {


    SlidingMenu menu;
    RelativeLayout actionBar;
    WebView webView;
    Globals global;
    ImageView eventImg;
    TextView titleTxt,location,date;
    EventDetailMainModel dataModel;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu != null)
                    menu.showMenu(true);
            }
        });

        actionBar.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.finish();
            }
        });

        menu = new SlidingMenuSetup(HomeActivity.this).setSlidingMenu();

        global = (Globals) getApplicationContext();
        dataModel = global.getEventDetailMainModel();

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);

        setWebView();
        setEventData();
    }

    public void setWebView(){
        webView = (WebView)findViewById(R.id.web_view);
        //webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new MyBrowser());

        String url = ""+dataModel.detailModel.Ev_Web_Url;
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);

        progress.setProgress(0);
    }

    private class MyBrowser extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            HomeActivity.this.progress.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
            if(newProgress < 100){
                HomeActivity.this.progress.setVisibility(View.VISIBLE);
            }else{
                HomeActivity.this.progress.setVisibility(View.GONE);
            }
        }
    }

    public void setEventData(){
        try {
            eventImg = (ImageView) findViewById(R.id.event_img);
            titleTxt = (TextView) findViewById(R.id.title_sec);
            location = (TextView) findViewById(R.id.location);
            date = (TextView) findViewById(R.id.date);

            titleTxt.setText("" + dataModel.detailModel.Ev_Nm);

            location.setText("" + dataModel.detailModel.Ev_Addr_1_Txt + "," + dataModel.detailModel.Ev_City_Txt);
            Picasso.with(HomeActivity.this)
                    .load("" + dataModel.detailModel.Ev_Img_Url)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.icon)
                    .into(eventImg);
            try {
                String dd = "" + dataModel.detailModel.Ev_Chk_In_Strt_Dttm;
                dd = dd.split("T")[0];
                date.setText(dd);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                date.setText("" + dataModel.detailModel.Ev_Chk_In_Strt_Dttm);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}