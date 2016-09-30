package com.comp.img.app.imagecomparison;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    int width = 0;
    int height = 0;
    ImageView overlayImg,baseImg;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findInitViews();
        //Bitmap firstBmp = BitmapFactory.decodeResource(getResources(),R.drawable.first_im);
        //Bitmap secondBmp = BitmapFactory.decodeResource(getResources(),R.drawable.second_im);
    }

    private void findInitViews()
    {
        overlayImg = (ImageView) findViewById(R.id.imageb);
        baseImg = (ImageView) findViewById(R.id.base_image);
        frame = (FrameLayout) findViewById(R.id.target);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //int height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        int pad = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);

        width -= pad;

        setWidthProgOfComponents();
        setImageComparator();
    }

    public void setWidthProgOfComponents()
    {
        seekBar.setProgress(width/2);
        seekBar.setMax(width);

        ViewGroup.LayoutParams lp = overlayImg.getLayoutParams();
        lp.width = width;
        overlayImg.setLayoutParams(lp);

        ViewGroup.LayoutParams lp1 = frame.getLayoutParams();
        lp1.width = width/2;
        frame.setLayoutParams(lp1);
    }

    private void setImageComparator()
    {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;

                ViewGroup.LayoutParams lp = frame.getLayoutParams();
                lp.width = progress;
                frame.setLayoutParams(lp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
