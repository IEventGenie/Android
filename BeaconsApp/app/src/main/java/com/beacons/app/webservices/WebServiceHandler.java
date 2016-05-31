package com.beacons.app.webservices;

import android.content.Context;
import android.util.Log;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.WebserviceDataModels.ResponseModel;
import com.beacons.app.beaconsapp.Globals;
import com.beacons.app.constants.GlobalConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceHandler {



    // Reads an InputStream and converts it to a String.
    public static String readResponse(InputStream stream) throws IOException, UnsupportedEncodingException {
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
            String mkUrl = GlobalConstants.BASE_URL+ GlobalConstants.GET_ALL_EVENT_DETAILS_CONFIRMATION_CODE_URL+"?"+
                            GlobalConstants.CONFIRMATION_CODE_PARAM+"="+confirmationCode+"&"+ GlobalConstants.LAST_NAME_PARAM+"="+lastname;

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
            String contentAsString = readResponse(is);

            Log.e("Service response : ", ":   " + contentAsString);

            if(contentAsString.length() > 0){
                JSONObject job = new JSONObject(contentAsString);
                JSONArray eventDet = job.getJSONArray(GlobalConstants.EVENT_DETAILS);
                if(eventDet != null && eventDet.length() > 0){
                    EventDetailMainModel resultModel = ResponseParser.parseResponseOfEventDetails(contentAsString);
                    global.setEventDetailMainModel(resultModel);
                    global.setEventDetailJson(contentAsString);
                    returnStatus = GlobalConstants.ResponseStatus.OK;
                }else{
                    returnStatus = GlobalConstants.ResponseStatus.Fail;
                }
            }else{
                returnStatus = GlobalConstants.ResponseStatus.Fail;
            }

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

    public static ResponseModel submitPreCheckinEvent(Context c,String eventId,String attendeeId){
        URL url;
        HttpURLConnection urlConnection = null;
        GlobalConstants.ResponseStatus returnStatus = GlobalConstants.ResponseStatus.Fail;
        InputStream is;
        Globals global = (Globals) c.getApplicationContext();
        ResponseModel returnModel = new ResponseModel();

        try {
            String mkUrl = GlobalConstants.BASE_URL+ GlobalConstants.SUBMIT_PRE_CHECKIN_URL+"?"+
                            GlobalConstants.EVENT_ID_PARAM+"="+eventId+"&"+ GlobalConstants.ATTENDEE_ID_PARAM+"="+attendeeId;
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
            Log.d("Prechkin response : ","The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readResponse(is);
            Log.e("Service response : ", ":   " + contentAsString);

            if(contentAsString.length() > 0){
                JSONObject job = new JSONObject(contentAsString);
                boolean result = job.getBoolean(GlobalConstants.SUCCESS);
                if(result){
                    returnModel.responseStatus = GlobalConstants.ResponseStatus.OK;
                }else{
                    returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
                    returnModel.message = ""+job.getBoolean(GlobalConstants.MESSAGE);
                }
            }else{
                returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
                returnModel.message = "Error in PreCheckin!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
            returnModel.message = "Error in PreCheckin!";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return returnModel;
    }


    public static ResponseModel checkInWithConfirmationCode(Context c,String confirmationCode,String eventId,String attendeeId){
        URL url;
        HttpURLConnection urlConnection = null;
        GlobalConstants.ResponseStatus returnStatus = GlobalConstants.ResponseStatus.Fail;
        InputStream is;
        Globals global = (Globals) c.getApplicationContext();
        ResponseModel returnModel = new ResponseModel();

        try {
            String mkUrl = GlobalConstants.BASE_URL+ GlobalConstants.CHECKIN_WITH_CONFIRMATION_CODE_URL+"?"+
                    GlobalConstants.CONFIRMATION_CODE_PARAM+"="+confirmationCode+"&"+
                    GlobalConstants.EVENT_ID_PARAM+"="+eventId+"&"+ GlobalConstants.ATTENDEE_ID_PARAM+"="+attendeeId;
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
            Log.d("Check in response : ","The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readResponse(is);
            Log.e("Service response : ", ":   " + contentAsString);

            if(contentAsString.length() > 0){
                JSONObject job = new JSONObject(contentAsString);
                boolean result = job.getBoolean(GlobalConstants.SUCCESS);
                if(result){
                    returnModel.responseStatus = GlobalConstants.ResponseStatus.OK;
                }else{
                    returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
                    returnModel.message = ""+job.getBoolean(GlobalConstants.MESSAGE);
                }
            }else{
                returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
                returnModel.message = "Error in Checkin!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
            returnModel.message = "Error in Checkin!";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return returnModel;
    }


    public static ResponseModel checkOutWithConfirmationCode(Context c,String confirmationCode,String eventId,String attendeeId){
        URL url;
        HttpURLConnection urlConnection = null;
        GlobalConstants.ResponseStatus returnStatus = GlobalConstants.ResponseStatus.Fail;
        InputStream is;
        Globals global = (Globals) c.getApplicationContext();
        ResponseModel returnModel = new ResponseModel();

        try {
            String mkUrl = GlobalConstants.BASE_URL+ GlobalConstants.CHECKOUT_WITH_CONFIRMATION_CODE_URL+"?"+
                    GlobalConstants.CONFIRMATION_CODE_PARAM+"="+confirmationCode+"&"+
                    GlobalConstants.EVENT_ID_PARAM+"="+eventId+"&"+ GlobalConstants.ATTENDEE_ID_PARAM+"="+attendeeId;
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
            Log.d("Check out response : ","The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readResponse(is);
            Log.e("Service response : ", ":   " + contentAsString);

            if(contentAsString.length() > 0){
                JSONObject job = new JSONObject(contentAsString);
                boolean result = job.getBoolean(GlobalConstants.SUCCESS);
                if(result){
                    returnModel.responseStatus = GlobalConstants.ResponseStatus.OK;
                }else{
                    returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
                    returnModel.message = ""+job.getBoolean(GlobalConstants.MESSAGE);
                }
            }else{
                returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
                returnModel.message = "Error in Checkout!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            returnModel.responseStatus = GlobalConstants.ResponseStatus.Fail;
            returnModel.message = "Error in Checkout!";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return returnModel;
    }

}