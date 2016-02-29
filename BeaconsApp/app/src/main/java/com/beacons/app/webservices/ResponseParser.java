package com.beacons.app.webservices;

import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.constants.AppConstants;

import org.json.JSONObject;

/**
 * Created by aman on 2/29/16.
 */
public class ResponseParser {


    public static EventDetailMainModel parseResponseOfEventDetails(String response){

        EventDetailMainModel returnModel = new EventDetailMainModel();

        try{

            JSONObject mainObj = new JSONObject(response);

            mainObj.getJSONArray(AppConstants.EVENT_DETAILS);



        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }

        return returnModel;
    }

}
