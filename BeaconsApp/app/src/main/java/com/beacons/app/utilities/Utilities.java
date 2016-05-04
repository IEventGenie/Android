package com.beacons.app.utilities;


import android.content.Context;

/**
 * Created by aman on 3/1/16.
 */
public class Utilities {

    public static int dp2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }


}
