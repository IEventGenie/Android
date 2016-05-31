/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package com.beacons.app.qrcodescanner;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Button;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import android.widget.RelativeLayout;
import android.widget.TextView;

/* Import ZBar Class files */

import com.beacons.app.beaconsapp.R;
import com.beacons.app.constants.GlobalConstants;
import com.beacons.app.qrcodescanner.CameraPreview;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

public class ScannerActivity extends Activity
{
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private boolean isFlashOn = false;
    RelativeLayout flashBtn;


    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scanner_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = (TextView)findViewById(R.id.scanText);

        scanButton = (Button)findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    scanText.setText("Scanning...");
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });

        flashBtn = (RelativeLayout)findViewById(R.id.flash_btn);

        if(!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            flashBtn.setVisibility(View.GONE);
        }

        flashBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isFlashOn) {
                        mCamera.stopPreview();
                        mPreview.getHolder().removeCallback(mPreview);
                        //mCamera.setPreviewCallback(null);

                        Parameters params = mCamera.getParameters();
                        params.setFlashMode(Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(params);
                        mCamera.setPreviewCallback(previewCb);
                        mCamera.startPreview();
                        previewing = true;
                        //mCamera.autoFocus(autoFocusCB);
                        isFlashOn = false;
                    }else{
                        mCamera.stopPreview();
                        mPreview.getHolder().removeCallback(mPreview);
                        //mCamera.setPreviewCallback(null);

                        Parameters params = mCamera.getParameters();
                        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(params);
                        mCamera.setPreviewCallback(previewCb);
                        mCamera.startPreview();
                        previewing = true;
                        //mCamera.autoFocus(autoFocusCB);
                        isFlashOn = true;
                    }
                }
                catch(Exception e){
                    Log.e("ex on flash",""+e.getStackTrace());
                    previewing = false;
                    mPreview.getHolder().removeCallback(mPreview);
                    //mCamera.setPreviewCallback(null);
                    mCamera.release();
                    mCamera = null;

                    restartCamera();
                }
            }
        });
    }

    public void restartCamera(){
        mCamera = getCameraInstance();
        Parameters params = mCamera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
        mCamera.setPreviewCallback(previewCb);
        mCamera.startPreview();
        previewing = true;
        mCamera.autoFocus(autoFocusCB);
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
            Log.e("camera","error : "+e.getStackTrace());
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mPreview.getHolder().removeCallback(mPreview);
            //mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mPreview.getHolder().removeCallback(mPreview);
                //mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                String res = "";
                for (Symbol sym : syms) {
                    scanText.setText(""+sym.getData());
                    res = sym.getData();
                    Log.e("Scanned Data : " ,""+res);
                    barcodeScanned = true;
                }

                Intent intent=new Intent();
                intent.putExtra(GlobalConstants.SCANNED_CODE_RESULT,res);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}
