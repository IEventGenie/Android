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

    public static final String LAST_NAME_KEY = "lastName";

    public static final String EVENT_DETAILS = "eventDetails";

    public static final String ACCT_ID = "Acct_Id";
    public static final String CNFRM_CD_LGTH_NB = "Cnfrm_Cd_Lgth_Nb";
    public static final String COMT_TXT = "Comt_Txt";
    public static final String CRT_BY_USRID = "Crt_By_Usrid";
    public static final String CRT_DTTM = "Crt_Dttm";
    public static final String DEL_IND = "Del_Ind";
    public static final String ENABLE_DB_SYNC_IN = "Enabl_Db_Sync_In";
    public static final String ENABLE_IEVENTGENIE_DATASRC_IN = "Enabl_Ieventgenie_Data_Src_In";
    public static final String ENABLE_CHECK_IN = "Enabl_Checkin";
    public static final String ENABLE_PRECHECKIN = "Enabl_PreCheckin";
    public static final String EV_ADDR_1_TXT = "Ev_Addr_1_Txt";
    public static final String EV_ADDR_2_TXT  = "Ev_Addr_2_Txt";
    public static final String EV_BARCD_TYPE_CD = "Ev_Barcd_Type_Cd";
    public static final String EV_CD_TXT = "Ev_Cd_Txt";
    public static final String EV_CHK_IN_END_DTTM = "Ev_Chk_In_End_Dttm";
    public static final String EV_CHK_IN_STRT_DTTM = "Ev_Chk_In_Strt_Dttm";
    public static final String EV_CITY_TXT = "Ev_City_Txt";
    public static final String EV_CNTCT_ID = "Ev_Cntct_Id";
    public static final String Ev_Cont_Email = "Ev_Cont_Email";
    public static final String EV_DESC = "Ev_Desc";
    public static final String EV_EARLY_CHK_IN_END_DTTM = "Ev_Early_Chk_In_End_Dttm";
    public static final String EV_EARLY_CHK_IN_START_DTTM = "Ev_Early_Chk_In_Strt_Dttm";
    public static final String EV_END_DT = "Ev_End_Dt";
    public static final String EV_ID = "Ev_Id";
    public static final String EV_IMG_THUMB_URL = "Ev_Img_Thumb_Url";
    public static final String EV_IMG_URL = "Ev_Img_Url";
    public static final String EV_LOC_TXT = "Ev_Loc_Txt";
    public static final String EV_NM = "Ev_Nm";
    public static final String EV_ST_CD = "Ev_St_Cd";
    public static final String EV_STRT_DT = "Ev_Strt_Dt";
    public static final String EV_STS_CD = "Ev_Sts_Cd";
    public static final String EV_TYPE_ID = "Ev_Type_Id";
    public static final String EV_ZIP_CD = "Ev_Zip_Cd";
    public static final String HOSTED_BY_TXT = "Hosted_By_Txt";
    public static final String MOD_BY_USRID ="Mod_By_Usrid";
    public static final String MOD_DTTM = "Mod_Dttm";
    public static final String MODULE_ID = "ModuleId";
    public static final String PARNT_EV_ID = "Parnt_Ev_Id";
    public static final String PENDING_CHECKINS = "PendingCheckIns";
    public static final String REGS_STRT_DT = "Regs_Strt_Dt";
    public static final String SYNC_EV_ID = "Sync_Ev_Id";
    public static final String TOTALATTENDEES = "TotalAttendees";
    public static final String TOTAL_CHECKINS = "TotalCheckIns";
    public static final String EV_TIME_ZONE = "Ev_Time_Zone";
    public static final String EV_CONTACT_PHONE = "Ev_Contact_Phone";
    public static final String EV_WEB_URL = "Ev_Web_Url";
    public static final String SUBEVENTDETAILS = "subEventDetails";
    public static final String MOBILE_CHECKINGSETTING = "MobileCheckingSettings";
    public static final String ATTENDEE_CUSTOME_FIELD_SETTINGS = "AttendeeCustomFieldSettings";
    public static final String HOUSING_CUSTOME_FIELD_SETTINGS = "HousingCustomFieldSettings";
    public static final String VOLUNTEER_CUSTOME_FIELD_SETTINGS = "VolunteerCustomFieldSettings";
    public static final String ATTENDEE_DETAILS = "attendeeDetails";
    public static final String CATEGORY_TYPES = "categoryTypes";


    public static final String CUSTM_FLD_1_SEL_IN= "Custm_Fld_1_Sel_In";
    public static final String CUSTM_FLD_2_SEL_IN= "Custm_Fld_2_Sel_In";
    public static final String CUSTM_FLD_3_SEL_IN= "Custm_Fld_3_Sel_In";
    public static final String CUSTM_FLD_4_SEL_IN= "Custm_Fld_4_Sel_In";
    public static final String CUSTM_FLD_5_SEL_IN= "Custm_Fld_5_Sel_In";
    public static final String CUSTM_FLD_6_SEL_IN= "Custm_Fld_6_Sel_In";
    public static final String CUSTM_FLD_7_SEL_IN= "Custm_Fld_7_Sel_In";
    public static final String CUSTM_FLD_8_SEL_IN= "Custm_Fld_8_Sel_In";
    public static final String CUSTM_FLD_9_SEL_IN= "Custm_Fld_9_Sel_In";
    public static final String CUSTM_FLD_10_SEL_IN= "Custm_Fld_10_Sel_In";
    public static final String CUSTM_FLD_11_SEL_IN= "Custm_Fld_11_Sel_In";
    public static final String CUSTM_FLD_12_SEL_IN= "Custm_Fld_12_Sel_In";
    public static final String CUSTM_FLD_13_SEL_IN= "Custm_Fld_13_Sel_In";
    public static final String CUSTM_FLD_14_SEL_IN= "Custm_Fld_14_Sel_In";
    public static final String CUSTM_FLD_15_SEL_IN= "Custm_Fld_15_Sel_In";
    public static final String CUSTM_FLD_16_SEL_IN= "Custm_Fld_16_Sel_In";
    public static final String CUSTM_FLD_17_SEL_IN= "Custm_Fld_17_Sel_In";
    public static final String CUSTM_FLD_18_SEL_IN= "Custm_Fld_18_Sel_In";
    public static final String CUSTM_FLD_19_SEL_IN= "Custm_Fld_19_Sel_In";
    public static final String CUSTM_FLD_20_SEL_IN= "Custm_Fld_20_Sel_In";


    public static final String FIELD_NAME = "fieldName";
    public static final String IS_ON = "isOn";
    public static final String CATEGORY = "Category";

    public static final String ID = "Id";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String CONFIRMATION_CODE = "ConfirmationCode";
    public static final String EMAIL = "Email";
    public static final String GUESTS = "Guests";
    public static final String PHONE_NUMBER = "PhoneNumber";
    public static final String CITY = "City";
    public static final String STATE = "State";
    public static final String VALIDITY = "Validity";
    public static final String NOTES = "Notes";
    public static final String STATUS = "Status";
    public static final String EVENT_ID = "EventId";
    public static final String ATTENDEE_TYPE_ID = "AttendeeTypeId";
    public static final String CUSTOME_FIELD_1 = "CustomField1";
    public static final String CUSTOME_FIELD_2 = "CustomField2";
    public static final String CUSTOME_FIELD_3 = "CustomField3";
    public static final String CUSTOME_FIELD_4 = "CustomField4";
    public static final String CUSTOME_FIELD_5 = "CustomField5";
    public static final String CUSTOME_FIELD_6 = "CustomField6";
    public static final String CUSTOME_FIELD_7 = "CustomField7";
    public static final String CUSTOME_FIELD_8 = "CustomField8";
    public static final String CUSTOME_FIELD_9 = "CustomField9";
    public static final String CUSTOME_FIELD_10 = "CustomField10";
    public static final String CUSTOME_FIELD_11 = "CustomField11";
    public static final String CUSTOME_FIELD_12 = "CustomField12";
    public static final String CUSTOME_FIELD_13 = "CustomField13";
    public static final String CUSTOME_FIELD_14 = "CustomField14";
    public static final String CUSTOME_FIELD_15 = "CustomField15";
    public static final String CUSTOME_FIELD_16 = "CustomField16";
    public static final String CUSTOME_FIELD_17 = "CustomField17";
    public static final String CUSTOME_FIELD_18 = "CustomField18";
    public static final String CUSTOME_FIELD_19 = "CustomField19";
    public static final String CUSTOME_FIELD_20 = "CustomField20";


    public static final String LABEL= "Label";
    public static final String VALUE= "Value";
    public static final String IS_ENABLED= "IsEnabled";


    public static final String CHILDREN = "Children";
    public static final String TEXT = "Text";

}

    //public const string BASE_URL = "https://dev.icheckingenie.com/DesktopModules/CheckinService/API/iCheckinEvents/";
    //public const string GET_ALL_EVENT_ATTENDEE_DETAIL_BY_CONFIRMATION_CODE = "GetAllEventAttendeeDetailByConfirmationCode";







