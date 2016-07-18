package com.beacons.app.beaconsapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.notificationDb.BeaconNotification;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.beacons.app.slidingmenu.SlidingMenuSetup;
import com.beacons.app.utilities.CircleTransform;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;
import com.pushwoosh.PushManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;


public class HomeActivity extends FragmentActivity {


    SlidingMenu menu;
    RelativeLayout actionBar;
    WebView webView;
    Globals global;
    ImageView eventImg;
    TextView titleTxt,location,date;
    EventDetailMainModel dataModel;
    ProgressBar progress;
    LinearLayout webviewLay;
    TextView noWebText;
    AlertDialog notificationDialog;
    LinearLayout noWebContent;
    ImageView evImgBig;
    TextView evNameText,evAddrText;
    WebView mapView;

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

        actionBar.findViewById(R.id.notification_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NotificationListAvticity.class));
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
        global.setFragActivity(this);

        webviewLay = (LinearLayout) findViewById(R.id.web_view_lay);
        noWebText = (TextView) findViewById(R.id.no_web_text);

        noWebContent = (LinearLayout) findViewById(R.id.no_web_content);
        evImgBig = (ImageView) findViewById(R.id.ev_img);
        evNameText = (TextView) findViewById(R.id.title_text);
        evAddrText = (TextView) findViewById(R.id.address_text);
        mapView = (WebView) findViewById(R.id.map_web_view);

        dataModel = global.getEventDetailMainModel();

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);

        setWebView();
        setEventData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(global.DoLogout){
            this.finish();
        }else{
            global.fragActivityResumed(this.getClass(),HomeActivity.this);
        }
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        global.fragActivityPaused();
        unregisterReceivers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        global.fragActivityDestroyed();
    }

    @Override
    public void onBackPressed() {
        if(menu.isMenuShowing()){
            menu.showContent();
        }else{
            super.onBackPressed();
        }
    }

    //Registration receiver
    BroadcastReceiver mBroadcastReceiver = new BaseRegistrationReceiver()
    {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent)
        {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {
            //JSON_DATA_KEY contains JSON payload of push notification.
            //showMessage("push message is json key --- " + intent.getExtras().getString(JSON_DATA_KEY));
            //Log.e("push msg json key",""+intent.getExtras().getString(JSON_DATA_KEY));
            try {
                String msg = ""+intent.getExtras().getString(JSON_DATA_KEY);
                JSONObject job = new JSONObject(msg);
                String pushTitle = job.getString("title");

                DatabaseHandler dbHandler = new DatabaseHandler(HomeActivity.this);
                BeaconNotification notification = new BeaconNotification();
                notification.setType(GlobalConstants.PUSHWOOSH_TYPE_PUSH_NOTIFICATION);
                notification.setTitle(pushTitle);
                dbHandler.addNotification(notification);

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Notification");
                builder.setMessage(pushTitle);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notificationDialog.dismiss();
                    }
                });
                notificationDialog = builder.create();
                notificationDialog.show();
            }catch (Exception e1){
                Log.e("exceptions : ",""+e1.getStackTrace());
            }
        }
    };

    //Registration of the receivers
    public void registerReceivers()
    {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");

        registerReceiver(mReceiver, intentFilter, getPackageName() + ".permission.C2D_MESSAGE", null);

        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers()
    {
        //Unregister receivers on pause
        try
        {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e)
        {
            // pass.
        }

        try
        {
            unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e)
        {
            //pass through
        }
    }

    private void checkMessage(Intent intent)
    {
        if (null != intent)
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                showMessage("push message is recv event ::: " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
                Log.e("push msg recv event", "" + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                //showMessage("register");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                //showMessage("unregister");
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                //showMessage("register error");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                //showMessage("unregister error");
            }

            resetIntentValues();
        }
    }

    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues()
    {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void setWebView(){
        String url = ""+dataModel.detailModel.Ev_Web_Url;
        if(url.length() > 0) {

            if(!url.contains("http")){
                url = "http://"+url;
            }

            if(!url.endsWith("/")){
                url = url+"/";
            }

            //url = url+"?username="+ dataModel.attendeeDetail.LastName+"&?password="+dataModel.attendeeDetail.ConfirmationCode;

            url = url+"?username="+ dataModel.attendeeDetail.LastName+"&password="+dataModel.attendeeDetail.ConfirmationCode+
                    "&phone="+dataModel.attendeeDetail.PhoneNumber+ "&fname="+dataModel.attendeeDetail.FirstName+"&lname="+
                    dataModel.attendeeDetail.LastName+"&email="+dataModel.attendeeDetail.Email+"&confirm_code=1234"+"&city="+
                    dataModel.attendeeDetail.City+"&country="+dataModel.attendeeDetail.State;

            Log.e("web url : ",""+url);
            webView = (WebView) findViewById(R.id.web_view);
            webView.setWebViewClient(new MyWebViewClient());
            webView.setWebChromeClient(new MyBrowser());

            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);

            progress.setProgress(0);

            noWebContent.setVisibility(View.GONE);
            webviewLay.setVisibility(View.VISIBLE);
        }else{
            noWebContent.setVisibility(View.VISIBLE);
            webviewLay.setVisibility(View.GONE);

            Picasso.with(HomeActivity.this).load("" + dataModel.detailModel.Ev_Img_Url).into(evImgBig);
            evNameText.setText("" + dataModel.detailModel.Ev_Nm);
            evAddrText.setText("" + dataModel.detailModel.Ev_Addr_1_Txt + "," + dataModel.detailModel.Ev_City_Txt);

            String mapurl = "https://www.google.co.in/maps/place/"+evAddrText.getText().toString();
            Log.e("Map URL",""+mapurl);
            mapView.setWebViewClient(new MyWebViewClient());
            mapView.setWebChromeClient(new MyBrowser());

            mapView.getSettings().setLoadsImagesAutomatically(true);
            mapView.getSettings().setJavaScriptEnabled(true);
            mapView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mapView.loadUrl(mapurl);
        }
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

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //if (Uri.parse(url).getHost().equals("www.example.com")) {
                // This is my web site, so do not override; let my WebView load the page

            //}
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;*/
            return false;
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