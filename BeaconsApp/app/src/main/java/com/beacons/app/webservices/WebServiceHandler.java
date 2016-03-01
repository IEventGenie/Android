package com.beacons.app.webservices;

import android.content.Context;
import android.util.Log;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.beaconsapp.Globals;
import com.beacons.app.constants.GlobalConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceHandler {



    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(stream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }


    public static GlobalConstants.ResponseStatus getEventsListByAccessCode(Context c,String confirmationCode,String lastname){
        URL url;
        HttpURLConnection urlConnection = null;
        GlobalConstants.ResponseStatus returnStatus = GlobalConstants.ResponseStatus.Fail;
        InputStream is;
        Globals global = (Globals) c.getApplicationContext();

        try {
            String mkUrl = GlobalConstants.BASE_URL+ GlobalConstants.GET_ALL_EVENT_DETAILS_CONFIRMATION_CODE_URL+
                            confirmationCode+"&"+ GlobalConstants.LAST_NAME_KEY+"="+lastname;

            Log.e("mkurl", "" + mkUrl);

            url = new URL(mkUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("response : ","The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);

            Log.e("Service response : ", ":   " + contentAsString);

            EventDetailMainModel resultModel = ResponseParser.parseResponseOfEventDetails(contentAsString);
            global.setEventDetailMainModel(resultModel);

            returnStatus = GlobalConstants.ResponseStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            returnStatus = GlobalConstants.ResponseStatus.Fail;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return returnStatus;
    }

}