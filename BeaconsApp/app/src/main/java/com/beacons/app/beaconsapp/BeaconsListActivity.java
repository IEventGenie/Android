package com.beacons.app.beaconsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.beacons.BeaconAdapter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aman on 2/12/16.
 */
public class BeaconsListActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private ArrayList<MSBeacon> beacons = new ArrayList<MSBeacon>();

    private BeaconAdapter beaconAdapter;
    private TextView bCount;
    private TextView testCamped;
    Beaconstac bstacInstance;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    private boolean registered = false;
    private boolean isPopupVisible = false;
    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacons_list);

        beaconSetup();

        if (savedInstanceState == null) {
            initList();
        }
    }

    public void beaconSetup()
    {

        // Use this check to determine whether BLE is supported on the device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            Toast.makeText(this, "Unable to obtain a BluetoothAdapter", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // set region parameters (UUID and unique region identifier)
        bstacInstance = Beaconstac.getInstance(this);
        bstacInstance.setRegionParams("F94DBB23-2266-7822-3782-57BEAC0952AC","com.beacons.app.beaconsapp");
        bstacInstance.syncRules();

        // if location is enabled
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            bstacInstance.syncPlaces();

            new PlaceSyncReceiver() {

                @Override
                public void onSuccess(Context context) {
                    bstacInstance.enableGeofences(true);

                    try {
                        bstacInstance.startRangingBeacons();
                    } catch (MSException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Context context) {
                    MSLogger.error("Error syncing geofence");
                }
            };

        } else {
            // if location disabled, start ranging beacons
            try {
                bstacInstance.startRangingBeacons();
            } catch (MSException e) {
                // handle for older devices
                TextView rangedView = (TextView) findViewById(R.id.RangedView);
                rangedView.setText(R.string.ble_not_supported);
                bCount.setVisibility(View.GONE);
                testCamped.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }
    }

    private void initList() {
        ListView beaconList = (ListView) findViewById(R.id.beaconListView);
        beaconAdapter   = new BeaconAdapter(beacons, this);
        beaconList.setAdapter(beaconAdapter);

        bCount = (TextView) findViewById(R.id.beaconCount);
        testCamped = (TextView) findViewById(R.id.CampedView);
        registerBroadcast();
    }


    @Override
    protected void onPause() {
        super.onPause();
        beaconAdapter.clear();
        beaconAdapter.notifyDataSetChanged();
        bCount.setText("" + beacons.size());
        unregisterBroadcast();
        isPopupVisible = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
        bCount.setText("" + beacons.size());
        registerBroadcast();
        isPopupVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();

        // stop scanning when the app closes
        if (bstacInstance != null) {
            try {
                bstacInstance.stopRangingBeacons();
            } catch (MSException e) {
                // handle for older devices
                e.printStackTrace();
            }
        }

    }

    // Callback intent results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
        if (bstacInstance != null) {
            try {
                bstacInstance.startRangingBeacons();
            } catch (MSException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerBroadcast() {
        if (!registered) {
            // register beaconstac receiver
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RANGED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_CAMPED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RULE_TRIGGERED);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_ENTERED_REGION);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_REGION);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_ENTERED_GEOFENCE);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_GEOFENCE);
            registerReceiver(beaconstacReceiver, intentFilter);

            //register place sync receiver
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(MSConstants.BEACONSTAC_INTENT_PLACE_SYNC_SUCCESS);
            iFilter.addAction(MSConstants.BEACONSTAC_INTENT_PLACE_SYNC_FAILURE);
            registerReceiver(placeSyncReceiver, iFilter);

            registered = true;
        }
    }

    private void unregisterBroadcast() {
        if (registered) {
            // unregister beaconstac receiver
            unregisterReceiver(beaconstacReceiver);
            // unregister place sync receiver
            unregisterReceiver(placeSyncReceiver);
            registered = false;
        }
    }

    PlaceSyncReceiver placeSyncReceiver = new PlaceSyncReceiver() {

        @Override
        public void onSuccess(Context context) {
            bstacInstance.enableGeofences(true);

            try {
                bstacInstance.startRangingBeacons();
            } catch (MSException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Context context) {
            MSLogger.error("Error syncing geofence");
        }
    };


    BeaconstacReceiver beaconstacReceiver = new BeaconstacReceiver() {
        @Override
        public void exitedBeacon(Context context, MSBeacon beacon) {
            testCamped.setText("Exited: " + beacon.getMajor() + ":" + beacon.getMinor());
            beaconAdapter.notifyDataSetChanged();
        }

        @Override
        public void rangedBeacons(Context context, ArrayList<MSBeacon> rangedBeacons) {
            beaconAdapter.clear();
            bCount.setText("" + rangedBeacons.size());
            beacons.addAll(rangedBeacons);
            beaconAdapter.notifyDataSetChanged();
        }

        @Override
        public void campedOnBeacon(Context context, MSBeacon beacon) {
            testCamped.setText("Camped: " + beacon.getMajor() + ":" + beacon.getMinor());
            beaconAdapter.addBeacon(beacon);
            beaconAdapter.notifyDataSetChanged();
        }

        @Override
        public void triggeredRule(Context context, String ruleName, ArrayList<MSAction> actions) {
            HashMap<String, Object> messageMap;
            AlertDialog.Builder dialogBuilder;

            if (!isPopupVisible) {
                for (MSAction action : actions) {

                    messageMap = action.getMessage();

                    switch (action.getType()) {
                        // handle action type Popup
                        case MSActionTypePopup:
                            dialogBuilder = new AlertDialog.Builder(context);
                            messageMap = action.getMessage();
                            dialogBuilder.setTitle(action.getName()).setMessage((String) messageMap.get("text"));
                            AlertDialog dialog = dialogBuilder.create();
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    isPopupVisible = false;
                                }
                            });
                            dialog.show();
                            isPopupVisible = true;
                            break;

                        // handle the action type Card
                        case MSActionTypeCard:
                            /*MSCard card = (MSCard) messageMap.get("card");

                            switch (card.getType()) {
                                case MSCardTypePhoto:
                                case MSCardTypeMedia:
                                    ArrayList<MSMedia> mediaArray = card.getMediaArray();
                                    if (mediaArray.size() > 0) {
                                        MSMedia m = mediaArray.get(0);

                                        String src = m.getMediaUrl().toString();

                                        dialogBuilder = new AlertDialog.Builder(context);
                                        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                isPopupVisible = false;
                                            }

                                        });

                                        final WebView webView = new WebView(context);
                                        webView.loadUrl(src);

                                        dialogBuilder.setView(webView);
                                        dialogBuilder.setPositiveButton("Close", null);
                                        dialogBuilder.show();

                                        isPopupVisible = true;
                                    }
                            }*/

                            if (!isPopupVisible) {
                                isPopupVisible = true;
                                MSCard card = (MSCard) messageMap.get("card");
                                MSMedia m;
                                String src;
                                AlertDialog.Builder dialogbuilder;

                                String title = card.getTitle();

                                switch (card.getType()) {
                                    case MSCardTypePhoto:
                                        ArrayList<String> urls = new ArrayList<>();
                                        for (int i = 0; i < card.getMediaArray().size(); i++) {
                                            m = card.getMediaArray().get(i);
                                            src = m.getMediaUrl().toString();
                                            urls.add(src);
                                        }
                                        String ok_label = (String) messageMap.get("notificationOkLabel");
                                        String ok_action = (String) messageMap.get("notificationOkAction");
                                        showPopupDialog(title, null, urls, ok_label, ok_action);
                                        break;

                                    case MSCardTypeSummary:
                                        ArrayList<String> cardUrls = new ArrayList<>();
                                        for (int i = 0; i < card.getMediaArray().size(); i++) {
                                            m = card.getMediaArray().get(i);
                                            src = m.getMediaUrl().toString();
                                            cardUrls.add(src);
                                        }
                                        ok_label = (String) messageMap.get("notificationOkLabel");
                                        ok_action = (String) messageMap.get("notificationOkAction");
                                        showPopupDialog(card.getTitle(), card.getBody(), cardUrls, ok_label, ok_action);
                                        break;

                                    case MSCardTypeMedia:
                                        m = card.getMediaArray().get(0);
                                        src = m.getMediaUrl().toString();

                                        // handle custom url types
                                        String ytId = extractYTId(src);
                                        if (ytId != null) {

                                            ok_label = (String) messageMap.get("notificationOkLabel");
                                            ok_action = (String) messageMap.get("notificationOkAction");
                                            showYoutubePopup(ytId, ok_label, ok_action);
                                            //showPopupDialog(title, null, "", ok_label, ok_action);
                                        } else {

                                            dialogbuilder = new AlertDialog.Builder(context);
                                            dialogbuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    isPopupVisible = false;
                                                }
                                            });
                                            final WebView webView = new WebView(context);
                                            webView.getSettings().setJavaScriptEnabled(true);
                                            webView.setWebViewClient(new WebViewClient());
                                            webView.loadUrl(src);

                                            ok_label = (String) messageMap.get("notificationOkLabel");
                                            final String ok_actionForWebDialog = (String) messageMap.get("notificationOkAction");
                                            if (ok_label != null && !ok_label.equals("") && ok_label.trim().length() != 0) {

                                                dialogbuilder.setPositiveButton(ok_label, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        Uri uri = Uri.parse(ok_actionForWebDialog); // missing 'http://' will cause crashed
                                                        Intent openUrl = new Intent(Intent.ACTION_VIEW, uri);
                                                        openUrl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(openUrl);
                                                    }
                                                });
                                            }

                                            dialogbuilder.setView(webView);
                                            dialogbuilder.setNeutralButton("Close", null);
                                            dialogbuilder.show();

                                            isPopupVisible = true;
                                        }

                                        break;
                                }
                            }
                            break;

                        // handle action type webpage
                        case MSActionTypeWebpage:
                            if (!isPopupVisible) {
                                dialogBuilder = new AlertDialog.Builder(context);
                                dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        isPopupVisible = false;
                                    }

                                });

                                final WebView webView = new WebView(context);
                                webView.loadUrl(messageMap.get("url").toString());

                                dialogBuilder.setView(webView);
                                dialogBuilder.setPositiveButton("Close", null);
                                dialogBuilder.show();

                                isPopupVisible = true;

                            }
                            break;
                    }
                }
                Toast.makeText(getApplicationContext(), "Rule " + ruleName, Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Opens a dialogFragment to display offers
         *
         * @param title Title of dialog (pass null to hide title)
         * @param text  Summary of dialog (pass null to hide summary)
         * @param url   ArrayList containing URLs of images (pass null to hide images)
         */
        private void showPopupDialog(String title, String text, ArrayList<String> url, String... ok_data) {
            String ok_label = "";
            String ok_action = "";

            if (ok_data.length == 2) {
                if (ok_data[0] != null && ok_data[1] != null) {
                    ok_label = ok_data[0];
                    ok_action = ok_data[1];
                }
            }

            FragmentManager fragmentManager = getFragmentManager();
            //ImageCarouselDialog imageCarouselDialog =
            //        ImageCarouselDialog.newInstance(title, text, url, ok_label, ok_action);
            //imageCarouselDialog.setRetainInstance(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(BeaconsListActivity.this);
            builder.setTitle(title);
            builder.setMessage(text);

            alertDialog = builder.create();
            alertDialog.show();

            isPopupVisible = true;

            //imageCarouselDialog.show(fragmentManager, "Dialog Fragment");
        }

        private void showYoutubePopup(String youTubeID, String... ok_data) {
            String ok_label = "";
            String ok_action = "";

            if (ok_data.length == 2) {
                if (ok_data[0] != null && ok_data[1] != null) {
                    ok_label = ok_data[0];
                    ok_action = ok_data[1];
                }
            }

            //FragmentManager fragmentManager = getSupportFragmentManager();
            /*YoutubePlayerDialog youtubePlayerDialog =
                    YoutubePlayerDialog.newInstance(youTubeID, ok_label, ok_action);
            youtubePlayerDialog.setRetainInstance(true);
            isPopupVisible = true;
            youtubePlayerDialog.show(fragmentManager, "Dialog Fragment");*/
            AlertDialog.Builder builder = new AlertDialog.Builder(BeaconsListActivity.this);
            builder.setTitle("You tube Player");
            builder.setMessage("YTID : "+youTubeID);

            alertDialog = builder.create();
            alertDialog.show();

            isPopupVisible = true;

        }

        @Override
        public void enteredRegion(Context context, String region) {
            beaconAdapter.clear();
            beaconAdapter.notifyDataSetChanged();
            bCount.setText("" + beacons.size());
            Toast.makeText(getApplicationContext(), "Entered region", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exitedRegion(Context context, String region) {
            beaconAdapter.clear();
            beaconAdapter.notifyDataSetChanged();
            bCount.setText("" + beacons.size());
            Toast.makeText(getApplicationContext(), "Exited region", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void enteredGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getApplicationContext(), "Entered Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exitedGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getApplicationContext(), "Exited Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
        }
    };

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*");
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }
}
