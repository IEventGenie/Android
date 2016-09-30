package com.comp.img.app.imagecomparison;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by aman on 8/8/16.
 */
public class Utilities {

    public static int dpToPX(Activity act,int dps)
    {
        Resources r = act.getResources();
        int px = (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dps, r.getDisplayMetrics()));
        return px;
    }

}
