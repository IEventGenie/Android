package com.beacons.app.beaconsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by aman on 3/1/16.
 */
public class MenuDetails extends BaseActivity {

    RelativeLayout actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_detail);

        actionBar = (RelativeLayout)findViewById(R.id.actionbar);
        actionBar.findViewById(R.id.menu_icon).setVisibility(View.GONE);

        actionBar.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDetails.this.finish();
            }
        });

        ((TextView)actionBar.findViewById(R.id.title)).setText("Details");

    }


}
