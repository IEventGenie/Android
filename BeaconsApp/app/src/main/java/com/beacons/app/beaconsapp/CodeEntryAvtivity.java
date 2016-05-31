package com.beacons.app.beaconsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.notificationDb.DatabaseHandler;
import com.beacons.app.notificationDb.EventDetailDBModel;
import com.beacons.app.qrcodescanner.ScannerActivity;
import com.beacons.app.webservices.WebServiceHandler;
import com.mobstac.beaconstac.utils.MSException;


public class CodeEntryAvtivity extends BaseActivity {

    EditText codeEd,lastEd;
    TextView myEventBtn,nearMeEventBtn,allEventBtn;
    public final int requestCode = 100;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    Globals global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_entry_screen);

        findViewsApplyAction();

        checkBluetoothAdaptability();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(global.DoLogout){
            global.DoLogout = false;
        }
    }

    public void findViewsApplyAction()
    {
        global = (Globals) getApplicationContext();

        codeEd = (EditText) findViewById(R.id.code_ed);
        lastEd = (EditText) findViewById(R.id.last_name_ed);

        myEventBtn = (TextView) findViewById(R.id.my_event_btn);
        nearMeEventBtn = (TextView) findViewById(R.id.event_near_btn);
        allEventBtn = (TextView) findViewById(R.id.all_event_btn);

        findViewById(R.id.activate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        findViewById(R.id.scanner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CodeEntryAvtivity.this, ScannerActivity.class), requestCode);
            }
        });

        myEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeEntryAvtivity.this, MyEventsActivity.class));
            }
        });

        nearMeEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeEntryAvtivity.this, MyEventsActivity.class));
            }
        });

        allEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeEntryAvtivity.this, MyEventsActivity.class));
            }
        });
    }

    public void checkBluetoothAdaptability(){
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
            Log.e("Beacons", "Unable to obtain a BluetoothAdapter.");
            Toast.makeText(this, "Unable to obtain a BluetoothAdapter", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.requestCode){
            if(resultCode == RESULT_OK){
                try {
                    String result = "" + data.getStringExtra(GlobalConstants.SCANNED_CODE_RESULT);
                    codeEd.setText(result);
                }catch (Exception e){
                    System.out.println(e.getStackTrace());
                }
            }
        }
    }

    public void validate() {
        codeEd.setError(null);
        lastEd.setError(null);

        boolean isValid = true;

        String code = "" + codeEd.getText().toString();
        if (code.length() == 0) {
            codeEd.setError(getResources().getString(R.string.confirm_code_req_error));
            isValid = false;
        }

        String lastN = lastEd.getText().toString();
        if (lastN.length() == 0) {
            lastEd.setError(getResources().getString(R.string.last_name_req_error));
            isValid = false;
        }

        if (isValid) {
            new ConfirmationCodeService(code, lastN).execute();
        }
    }

    public class ConfirmationCodeService extends AsyncTask<String,Integer,GlobalConstants.ResponseStatus>{

        ProgressDialog pd;
        String confirmationCode = "",lastName = "";

        public ConfirmationCodeService(String confirmationCode,String lastN) {
            this.confirmationCode = confirmationCode;
            this.lastName = lastN;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(CodeEntryAvtivity.this,"","Loading...");
        }

        @Override
        protected GlobalConstants.ResponseStatus doInBackground(String... params) {
            GlobalConstants.ResponseStatus res = GlobalConstants.ResponseStatus.Fail;
            try {
                res = WebServiceHandler.getEventsListByAccessCode(CodeEntryAvtivity.this, confirmationCode, lastName);
            }catch (Exception e){
                Log.e("Exception : ",e.getStackTrace().toString());
            }
            return res;
        }

        @Override
        protected void onPostExecute(GlobalConstants.ResponseStatus status) {
            super.onPostExecute(status);
            pd.dismiss();
            if(status == GlobalConstants.ResponseStatus.OK) {
                try {
                    EventDetailMainModel dataModel = global.getEventDetailMainModel();

                    EventDetailDBModel dbModel = new EventDetailDBModel();
                    dbModel.setConfirmCode(confirmationCode);
                    dbModel.setLastName(lastName);
                    dbModel.setEvId(dataModel.detailModel.Ev_Id);
                    dbModel.setEvAddrTxt(dataModel.detailModel.Ev_Addr_1_Txt);
                    dbModel.setEvCityTxt(dataModel.detailModel.Ev_City_Txt);
                    dbModel.setEvImgUrl(dataModel.detailModel.Ev_Img_Url);
                    dbModel.setEvLocTxt(dataModel.detailModel.Ev_Loc_Txt);
                    dbModel.setEvName(dataModel.detailModel.Ev_Nm);
                    dbModel.setEnablePrechkIn("" + dataModel.detailModel.Enabl_PreCheckin);
                    dbModel.setAttendeeId(dataModel.attendeeDetail.Id);
                    dbModel.setChkinStartDate(dataModel.detailModel.Ev_Chk_In_Strt_Dttm);
                    dbModel.setChkInEndDate(dataModel.detailModel.Ev_Chk_In_End_Dttm);
                    dbModel.setPreChkInStrtDate(dataModel.detailModel.Ev_Early_Chk_In_Strt_Dttm);
                    dbModel.setPreChkInEndDate(dataModel.detailModel.Ev_Early_Chk_In_End_Dttm);
                    dbModel.setEnablCheckin(dataModel.detailModel.Enabl_Checkin);
                    dbModel.setEventStatus(dataModel.detailModel.Ev_Sts_Cd);
                    String preChkStatus = ""+dataModel.attendeeDetail.Status;
                    if(preChkStatus.length() == 0 || preChkStatus.equals("null")){
                        preChkStatus = "false";
                    }
                    dbModel.setEvPreChkinStatus(preChkStatus);
                    dbModel.setEvStartDate(dataModel.detailModel.Ev_Strt_Dt);
                    dbModel.setEvEndDate(dataModel.detailModel.Ev_End_Dt);
                    dbModel.setEventDetailJson(global.getEventDetailJson());

                    DatabaseHandler dbHandler = new DatabaseHandler(CodeEntryAvtivity.this);
                    dbHandler.addEventDetails(dbModel);
                }catch (Exception e){
                    Log.e("Inserting ev in db",""+e.getStackTrace());
                }

                codeEd.setText("");
                lastEd.setText("");

                startActivity(new Intent(CodeEntryAvtivity.this, MyEventsActivity.class));
                //CodeEntryAvtivity.this.finish();
            }else if(status == GlobalConstants.ResponseStatus.AuthorisationRequired) {

            }else if(status == GlobalConstants.ResponseStatus.Fail){
                Toast.makeText(CodeEntryAvtivity.this, getResources().getString(R.string.lastname_code_wrong),Toast.LENGTH_LONG).show();
            }
        }
    }
}