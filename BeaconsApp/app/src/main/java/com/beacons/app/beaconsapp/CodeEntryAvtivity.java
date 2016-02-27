package com.beacons.app.beaconsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beacons.app.constants.AppConstants;
import com.beacons.app.qrcodescanner.ScannerActivity;
import com.beacons.app.webservices.WebServiceHandler;


public class CodeEntryAvtivity extends BaseActivity {

    EditText codeEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_entry_screen);

        findViewsApplyAction();
    }

    public void findViewsApplyAction()
    {
        codeEd = (EditText) findViewById(R.id.code_ed);

        findViewById(R.id.activate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            codeEd.setError(null);
            String code = ""+codeEd.getText().toString();
            if(code.length() == 0){
                codeEd.setError("Code required!");
            }else {
                new ConfirmationCodeService(code).execute();
            }
            }
        });

        findViewById(R.id.scanner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeEntryAvtivity.this, ScannerActivity.class));
            }
        });
    }

    public class ConfirmationCodeService extends AsyncTask<String,Integer,AppConstants.ResponseStatus>{

        ProgressDialog pd;
        String confirmationCode;

        public ConfirmationCodeService(String confirmationCode) {
            this.confirmationCode = confirmationCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(CodeEntryAvtivity.this,"","Verifying...");
        }

        @Override
        protected AppConstants.ResponseStatus doInBackground(String... params) {

            AppConstants.ResponseStatus res = WebServiceHandler.getEventsListByAccessCode(CodeEntryAvtivity.this,confirmationCode, "Aditya");

            return res;
        }

        @Override
        protected void onPostExecute(AppConstants.ResponseStatus status) {
            super.onPostExecute(status);
            pd.dismiss();

            startActivity(new Intent(CodeEntryAvtivity.this, MyEventsActivity.class));
            CodeEntryAvtivity.this.finish();
        }
    }

}