package com.beacons.app.beaconsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.beacons.app.slidingmenu.SlidingMenuSetup;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


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
    }

}