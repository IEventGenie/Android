package com.beacons.app.constants;

/**
 * Created by aman on 2/27/16.
 */
public class AppConstants {

    public enum ResponseStatus
    {
        OK,
        Fail,
        AuthorisationRequired
    }

    //URLS====
    public static final String BASE_URL = "https://dev.icheckingenie.com/";
    public static final String GET_ALL_EVENT_DETAILS_CONFIRMATION_CODE_URL = "DesktopModules/CheckinService/API/iCheckinEvents/GetAllEventAttendeeDetailByConfirmationCode?confirmationCode=";
    //========

    public static final String LAST_NAME = "lastName";

}
