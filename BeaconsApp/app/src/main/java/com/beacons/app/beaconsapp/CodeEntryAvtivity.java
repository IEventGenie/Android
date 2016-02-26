package com.beacons.app.beaconsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class CodeEntryAvtivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_entry_screen);

        findViewsApplyAction();

    }

    public void findViewsApplyAction()
    {
       findViewById(R.id.activate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeEntryAvtivity.this,MyEventsActivity.class));
                CodeEntryAvtivity.this.finish();
            }
        });

        findViewById(R.id.scanner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeEntryAvtivity.this,ScannerActivity.class));
            }
        });
    }

}