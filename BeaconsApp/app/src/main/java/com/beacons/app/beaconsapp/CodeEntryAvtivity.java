package com.beacons.app.beaconsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.qrcodescanner.ScannerActivity;
import com.beacons.app.webservices.WebServiceHandler;


public class CodeEntryAvtivity extends BaseActivity {

    EditText codeEd,lastEd;
    public final int requestCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_entry_screen);

        findViewsApplyAction();
    }

    public void findViewsApplyAction()
    {
        codeEd = (EditText) findViewById(R.id.code_ed);
        lastEd = (EditText) findViewById(R.id.last_name_ed);

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

        String code = ""+codeEd.getText().toString();
        if(code.length() == 0){
            codeEd.setError(getResources().getString(R.string.confirm_code_req_error));
        }else {
            String lastN = lastEd.getText().toString();
            if(lastN.length() == 0){
                lastEd.setError(getResources().getString(R.string.last_name_req_error));
            }else {
                new ConfirmationCodeService(code,lastN).execute();
            }
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
            pd = ProgressDialog.show(CodeEntryAvtivity.this,"","Verifying...");
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
                startActivity(new Intent(CodeEntryAvtivity.this, MyEventsActivity.class));
                //CodeEntryAvtivity.this.finish();
            }else if(status == GlobalConstants.ResponseStatus.AuthorisationRequired) {

            }else if(status == GlobalConstants.ResponseStatus.Fail){
                Toast.makeText(CodeEntryAvtivity.this, getResources().getString(R.string.lastname_code_wrong),Toast.LENGTH_LONG).show();
            }
        }
    }
}