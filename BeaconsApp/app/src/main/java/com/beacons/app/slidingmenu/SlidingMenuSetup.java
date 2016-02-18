package com.beacons.app.slidingmenu;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.beacons.app.beaconsapp.BeaconsListActivity;
import com.beacons.app.beaconsapp.HomeActivity;
import com.beacons.app.beaconsapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by aman on 2/12/16.
 */
public class SlidingMenuSetup {

    Activity attachToAct;
    SlidingMenu menu;

    public SlidingMenuSetup(Activity act) {
        attachToAct = act;
    }

    public SlidingMenu setSlidingMenu()
    {
        menu = new SlidingMenu(attachToAct);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //menu.setShadowWidthRes(R.dimen.shadow_width);
        //menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(attachToAct, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu);

        menu.findViewById(R.id.beacons_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.showContent();
                Intent i = new Intent(attachToAct, BeaconsListActivity.class);
                attachToAct.startActivity(i);
            }
        });

        return menu;
    }
}