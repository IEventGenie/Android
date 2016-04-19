package com.beacons.app.webservices;

import com.beacons.app.WebserviceDataModels.AttendeeDetailCommonModel;
import com.beacons.app.WebserviceDataModels.AttendeeDetailModel;
import com.beacons.app.WebserviceDataModels.CategoryCommonModel;
import com.beacons.app.WebserviceDataModels.CategoryTypeModel;
import com.beacons.app.WebserviceDataModels.EventDetailCommonModel;
import com.beacons.app.WebserviceDataModels.EventDetailMainModel;
import com.beacons.app.WebserviceDataModels.EventDetailMobileCheckinSettingsModel;
import com.beacons.app.WebserviceDataModels.EventDetailModel;
import com.beacons.app.constants.GlobalConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aman on 2/29/16.
 */
public class ResponseParser {


    public static EventDetailMainModel parseResponseOfEventDetails(String response){

        EventDetailMainModel returnModel = new EventDetailMainModel();

        try{

            JSONObject mainObj = new JSONObject(response);

            //====Event Detail Parsing Start MAIN ===
            //Event Detail Parsing Start===
            JSONArray evDetArr = mainObj.getJSONArray(GlobalConstants.EVENT_DETAILS);

            JSONObject evDetObj = evDetArr.getJSONObject(0);

            EventDetailModel evDetModel = new EventDetailModel();

            if(evDetObj.has(GlobalConstants.ACCT_ID))
            {
                evDetModel.Acct_Id = evDetObj.getString(GlobalConstants.ACCT_ID);
            }
            if(evDetObj.has(GlobalConstants.CNFRM_CD_LGTH_NB))
            {
                evDetModel.Cnfrm_Cd_Lgth_Nb = evDetObj.getString(GlobalConstants.CNFRM_CD_LGTH_NB);
            }
            if(evDetObj.has(GlobalConstants.COMT_TXT))
            {
                evDetModel.Comt_Txt = evDetObj.getString(GlobalConstants.COMT_TXT);
            }
            if(evDetObj.has(GlobalConstants.CRT_BY_USRID))
            {
                evDetModel.Crt_By_Usrid = evDetObj.getString(GlobalConstants.CRT_BY_USRID);
            }
            if(evDetObj.has(GlobalConstants.CRT_DTTM))
            {
                evDetModel.Crt_Dttm = evDetObj.getString(GlobalConstants.CRT_DTTM);
            }
            if(evDetObj.has(GlobalConstants.DEL_IND))
            {
                evDetModel.Del_Ind = evDetObj.getString(GlobalConstants.DEL_IND);
            }
            if(evDetObj.has(GlobalConstants.ENABLE_DB_SYNC_IN))
            {
                evDetModel.Enabl_Db_Sync_In = evDetObj.getString(GlobalConstants.ENABLE_DB_SYNC_IN);
            }
            if(evDetObj.has(GlobalConstants.ENABLE_IEVENTGENIE_DATASRC_IN))
            {
                evDetModel.Enabl_Ieventgenie_Data_Src_In = evDetObj.getString(GlobalConstants.ENABLE_IEVENTGENIE_DATASRC_IN);
            }
            if(evDetObj.has(GlobalConstants.ENABLE_CHECK_IN))
            {
                evDetModel.Enabl_Checkin = evDetObj.getString(GlobalConstants.ENABLE_CHECK_IN);
            }
            if(evDetObj.has(GlobalConstants.ENABLE_PRECHECKIN))
            {
                evDetModel.Enabl_PreCheckin = evDetObj.getString(GlobalConstants.ENABLE_PRECHECKIN);
            }
            if(evDetObj.has(GlobalConstants.EV_ADDR_1_TXT))
            {
                evDetModel.Ev_Addr_1_Txt = evDetObj.getString(GlobalConstants.EV_ADDR_1_TXT);
            }
            if(evDetObj.has(GlobalConstants.EV_ADDR_2_TXT))
            {
                evDetModel.Ev_Addr_2_Txt = evDetObj.getString(GlobalConstants.EV_ADDR_2_TXT);
            }
            if(evDetObj.has(GlobalConstants.EV_BARCD_TYPE_CD))
            {
                evDetModel.Ev_Barcd_Type_Cd = evDetObj.getString(GlobalConstants.EV_BARCD_TYPE_CD);
            }
            if(evDetObj.has(GlobalConstants.EV_CD_TXT))
            {
                evDetModel.Ev_Cd_Txt = evDetObj.getString(GlobalConstants.EV_CD_TXT);
            }
            if(evDetObj.has(GlobalConstants.EV_CHK_IN_END_DTTM))
            {
                evDetModel.Ev_Chk_In_End_Dttm = evDetObj.getString(GlobalConstants.EV_CHK_IN_END_DTTM);
            }
            if(evDetObj.has(GlobalConstants.EV_CHK_IN_STRT_DTTM))
            {
                evDetModel.Ev_Chk_In_Strt_Dttm = evDetObj.getString(GlobalConstants.EV_CHK_IN_STRT_DTTM);
            }
            if(evDetObj.has(GlobalConstants.EV_CITY_TXT))
            {
                evDetModel.Ev_City_Txt = evDetObj.getString(GlobalConstants.EV_CITY_TXT);
            }
            if(evDetObj.has(GlobalConstants.EV_CNTCT_ID))
            {
                evDetModel.Ev_Cntct_Id = evDetObj.getString(GlobalConstants.EV_CNTCT_ID);
            }
            if(evDetObj.has(GlobalConstants.Ev_Cont_Email))
            {
                evDetModel.Ev_Cont_Email = evDetObj.getString(GlobalConstants.Ev_Cont_Email);
            }
            if(evDetObj.has(GlobalConstants.EV_DESC))
            {
                evDetModel.Ev_Desc = evDetObj.getString(GlobalConstants.EV_DESC);
            }
            if(evDetObj.has(GlobalConstants.EV_EARLY_CHK_IN_END_DTTM))
            {
                evDetModel.Ev_Early_Chk_In_End_Dttm = evDetObj.getString(GlobalConstants.EV_EARLY_CHK_IN_END_DTTM);
            }
            if(evDetObj.has(GlobalConstants.EV_EARLY_CHK_IN_START_DTTM))
            {
                evDetModel.Ev_Early_Chk_In_Strt_Dttm = evDetObj.getString(GlobalConstants.EV_EARLY_CHK_IN_START_DTTM);
            }
            if(evDetObj.has(GlobalConstants.EV_END_DT))
            {
                evDetModel.Ev_End_Dt = evDetObj.getString(GlobalConstants.EV_END_DT);
            }
            if(evDetObj.has(GlobalConstants.EV_ID))
            {
                evDetModel.Ev_Id = evDetObj.getString(GlobalConstants.EV_ID);
            }
            if(evDetObj.has(GlobalConstants.EV_IMG_THUMB_URL))
            {
                evDetModel.Ev_Img_Thumb_Url = evDetObj.getString(GlobalConstants.EV_IMG_THUMB_URL);
            }
            if(evDetObj.has(GlobalConstants.EV_IMG_URL))
            {
                evDetModel.Ev_Img_Url = evDetObj.getString(GlobalConstants.EV_IMG_URL);
            }
            if(evDetObj.has(GlobalConstants.EV_LOC_TXT))
            {
                evDetModel.Ev_Loc_Txt = evDetObj.getString(GlobalConstants.EV_LOC_TXT);
            }
            if(evDetObj.has(GlobalConstants.EV_NM))
            {
                evDetModel.Ev_Nm = evDetObj.getString(GlobalConstants.EV_NM);
            }
            if(evDetObj.has(GlobalConstants.EV_ST_CD))
            {
                evDetModel.Ev_St_Cd = evDetObj.getString(GlobalConstants.EV_ST_CD);
            }
            if(evDetObj.has(GlobalConstants.EV_STRT_DT))
            {
                evDetModel.Ev_Strt_Dt = evDetObj.getString(GlobalConstants.EV_STRT_DT);
            }
            if(evDetObj.has(GlobalConstants.EV_STS_CD))
            {
                evDetModel.Ev_Sts_Cd = evDetObj.getString(GlobalConstants.EV_STS_CD);
            }
            if(evDetObj.has(GlobalConstants.EV_TYPE_ID))
            {
                evDetModel.Ev_Type_Id = evDetObj.getString(GlobalConstants.EV_TYPE_ID);
            }
            if(evDetObj.has(GlobalConstants.EV_ZIP_CD))
            {
                evDetModel.Ev_Zip_Cd = evDetObj.getString(GlobalConstants.EV_ZIP_CD);
            }
            if(evDetObj.has(GlobalConstants.HOSTED_BY_TXT))
            {
                evDetModel.Hosted_By_Txt = evDetObj.getString(GlobalConstants.HOSTED_BY_TXT);
            }
            if(evDetObj.has(GlobalConstants.MOD_BY_USRID))
            {
                evDetModel.Mod_By_Usrid = evDetObj.getString(GlobalConstants.MOD_BY_USRID);
            }
            if(evDetObj.has(GlobalConstants.MOD_DTTM))
            {
                evDetModel.Mod_Dttm = evDetObj.getString(GlobalConstants.MOD_DTTM);
            }
            if(evDetObj.has(GlobalConstants.MODULE_ID))
            {
                evDetModel.ModuleId = evDetObj.getString(GlobalConstants.MODULE_ID);
            }
            if(evDetObj.has(GlobalConstants.PARNT_EV_ID))
            {
                evDetModel.Parnt_Ev_Id = evDetObj.getString(GlobalConstants.PARNT_EV_ID);
            }
            if(evDetObj.has(GlobalConstants.PENDING_CHECKINS))
            {
                evDetModel.PendingCheckIns = evDetObj.getString(GlobalConstants.PENDING_CHECKINS);
            }
            if(evDetObj.has(GlobalConstants.REGS_STRT_DT))
            {
                evDetModel.Regs_Strt_Dt = evDetObj.getString(GlobalConstants.REGS_STRT_DT);
            }
            if(evDetObj.has(GlobalConstants.SYNC_EV_ID))
            {
                evDetModel.Sync_Ev_Id = evDetObj.getString(GlobalConstants.SYNC_EV_ID);
            }
            if(evDetObj.has(GlobalConstants.TOTALATTENDEES))
            {
                evDetModel.TotalAttendees = evDetObj.getString(GlobalConstants.TOTALATTENDEES);
            }
            if(evDetObj.has(GlobalConstants.TOTAL_CHECKINS))
            {
                evDetModel.TotalCheckIns = evDetObj.getString(GlobalConstants.TOTAL_CHECKINS);
            }
            if(evDetObj.has(GlobalConstants.EV_TIME_ZONE))
            {
                evDetModel.Ev_Time_Zone = evDetObj.getString(GlobalConstants.EV_TIME_ZONE);
            }
            if(evDetObj.has(GlobalConstants.EV_CONTACT_PHONE))
            {
                evDetModel.Ev_Contact_Phone = evDetObj.getString(GlobalConstants.EV_CONTACT_PHONE);
            }
            if(evDetObj.has(GlobalConstants.EV_WEB_URL))
            {
                evDetModel.Ev_Web_Url = evDetObj.getString(GlobalConstants.EV_WEB_URL);
            }

            returnModel.detailModel = evDetModel;
            //Event Detail Parsing End===


            //Mobile settings Parsing====
            if(evDetObj.has(GlobalConstants.MOBILE_CHECKINGSETTING)){
                JSONObject checkinSettingsObj = evDetObj.getJSONObject(GlobalConstants.MOBILE_CHECKINGSETTING);
                EventDetailMobileCheckinSettingsModel mobsettingsModel = new EventDetailMobileCheckinSettingsModel();
                try{
                    mobsettingsModel.Custm_Fld_1_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_1_SEL_IN);
                    mobsettingsModel.Custm_Fld_2_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_2_SEL_IN);
                    mobsettingsModel.Custm_Fld_3_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_3_SEL_IN);
                    mobsettingsModel.Custm_Fld_4_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_4_SEL_IN);
                    mobsettingsModel.Custm_Fld_5_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_5_SEL_IN);
                    mobsettingsModel.Custm_Fld_6_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_6_SEL_IN);
                    mobsettingsModel.Custm_Fld_7_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_7_SEL_IN);
                    mobsettingsModel.Custm_Fld_8_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_8_SEL_IN);
                    mobsettingsModel.Custm_Fld_9_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_9_SEL_IN);
                    mobsettingsModel.Custm_Fld_10_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_10_SEL_IN);
                    mobsettingsModel.Custm_Fld_11_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_11_SEL_IN);
                    mobsettingsModel.Custm_Fld_12_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_12_SEL_IN);
                    mobsettingsModel.Custm_Fld_13_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_13_SEL_IN);
                    mobsettingsModel.Custm_Fld_14_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_14_SEL_IN);
                    mobsettingsModel.Custm_Fld_15_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_15_SEL_IN);
                    mobsettingsModel.Custm_Fld_16_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_16_SEL_IN);
                    mobsettingsModel.Custm_Fld_17_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_17_SEL_IN);
                    mobsettingsModel.Custm_Fld_18_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_18_SEL_IN);
                    mobsettingsModel.Custm_Fld_19_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_19_SEL_IN);
                    mobsettingsModel.Custm_Fld_20_Sel_In = checkinSettingsObj.getBoolean(GlobalConstants.CUSTM_FLD_20_SEL_IN);
                }catch (Exception e1){
                    System.out.println(e1.getStackTrace());
                }

                returnModel.mobileCheckinSettings = mobsettingsModel;
            }
            //Mobile settings parsing end===


            //Attendee Custom Field Parsing=======
            if(evDetObj.has(GlobalConstants.ATTENDEE_CUSTOME_FIELD_SETTINGS)){
                JSONArray attendeeSettingsArr = evDetObj.getJSONArray(GlobalConstants.ATTENDEE_CUSTOME_FIELD_SETTINGS);
                ArrayList<EventDetailCommonModel> listAttendeeSettings = new ArrayList<EventDetailCommonModel>();
                for (int i = 0; i < attendeeSettingsArr.length(); i++) {
                    JSONObject attendeeSettingsObj = attendeeSettingsArr.getJSONObject(i);

                    EventDetailCommonModel attComModel = new EventDetailCommonModel();

                    if(attendeeSettingsObj.has(GlobalConstants.FIELD_NAME)){
                        attComModel.FieldName = attendeeSettingsObj.getString(GlobalConstants.FIELD_NAME);
                    }

                    if(attendeeSettingsObj.has(GlobalConstants.IS_ON)){
                        attComModel.isOn = attendeeSettingsObj.getBoolean(GlobalConstants.IS_ON);
                    }

                    if(attendeeSettingsObj.has(GlobalConstants.CATEGORY)){
                        attComModel.Category = attendeeSettingsObj.getString(GlobalConstants.CATEGORY);
                    }
                    listAttendeeSettings.add(attComModel);
                }
                returnModel.attendeeCustomFieldSettings = listAttendeeSettings;
            }
            //Attendee Custom Field Parsing End===

            //Housing Custom Field Parsing=======
            if(evDetObj.has(GlobalConstants.HOUSING_CUSTOME_FIELD_SETTINGS)){
                JSONArray hSettingsArr = evDetObj.getJSONArray(GlobalConstants.HOUSING_CUSTOME_FIELD_SETTINGS);
                ArrayList<EventDetailCommonModel> listHousingSettings = new ArrayList<EventDetailCommonModel>();
                for (int i = 0; i < hSettingsArr.length(); i++) {
                    JSONObject hSettingsObj = hSettingsArr.getJSONObject(i);

                    EventDetailCommonModel attComModel = new EventDetailCommonModel();

                    if(hSettingsObj.has(GlobalConstants.FIELD_NAME)){
                        attComModel.FieldName = hSettingsObj.getString(GlobalConstants.FIELD_NAME);
                    }

                    if(hSettingsObj.has(GlobalConstants.IS_ON)){
                        attComModel.isOn = hSettingsObj.getBoolean(GlobalConstants.IS_ON);
                    }

                    if(hSettingsObj.has(GlobalConstants.CATEGORY)){
                        attComModel.Category = hSettingsObj.getString(GlobalConstants.CATEGORY);
                    }
                    listHousingSettings.add(attComModel);
                }
                returnModel.housingCustomFieldSettings = listHousingSettings;
            }
            //Housing Custom Field Parsing End===

            //Volunteer Custom Field Parsing=======
            if(evDetObj.has(GlobalConstants.VOLUNTEER_CUSTOME_FIELD_SETTINGS)){
                JSONArray vSettingsArr = evDetObj.getJSONArray(GlobalConstants.VOLUNTEER_CUSTOME_FIELD_SETTINGS);
                ArrayList<EventDetailCommonModel> listvolunteerSettings = new ArrayList<EventDetailCommonModel>();
                for (int i = 0; i < vSettingsArr.length(); i++) {
                    JSONObject vSettingsObj = vSettingsArr.getJSONObject(i);

                    EventDetailCommonModel attComModel = new EventDetailCommonModel();

                    if(vSettingsObj.has(GlobalConstants.FIELD_NAME)){
                        attComModel.FieldName = vSettingsObj.getString(GlobalConstants.FIELD_NAME);
                    }

                    if(vSettingsObj.has(GlobalConstants.IS_ON)){
                        attComModel.isOn = vSettingsObj.getBoolean(GlobalConstants.IS_ON);
                    }

                    if(vSettingsObj.has(GlobalConstants.CATEGORY)){
                        attComModel.Category = vSettingsObj.getString(GlobalConstants.CATEGORY);
                    }
                    listvolunteerSettings.add(attComModel);
                }
                returnModel.volunteerCustomFieldSettings = listvolunteerSettings;
            }
            //Housing Custom Field Parsing End===


            //=====Event Detail Parsing End=====

            //=====Attendee Detail Parsing Start====
            JSONArray attDetArr = mainObj.getJSONArray(GlobalConstants.ATTENDEE_DETAILS);

            JSONObject attDetObj = attDetArr.getJSONObject(0);

            AttendeeDetailModel attDetModel = new AttendeeDetailModel();

            if(attDetObj.has(GlobalConstants.ID))
            {
                attDetModel.Id = attDetObj.getString(GlobalConstants.ID);
            }
            if(attDetObj.has(GlobalConstants.FIRST_NAME))
            {
                attDetModel.FirstName = attDetObj.getString(GlobalConstants.FIRST_NAME);
            }
            if(attDetObj.has(GlobalConstants.LAST_NAME))
            {
                attDetModel.LastName = attDetObj.getString(GlobalConstants.LAST_NAME);
            }
            if(attDetObj.has(GlobalConstants.CONFIRMATION_CODE))
            {
                attDetModel.ConfirmationCode = attDetObj.getString(GlobalConstants.CONFIRMATION_CODE);
            }
            if(attDetObj.has(GlobalConstants.EMAIL))
            {
                attDetModel.Email = attDetObj.getString(GlobalConstants.EMAIL);
            }
            if(attDetObj.has(GlobalConstants.GUESTS))
            {
                attDetModel.Guests = attDetObj.getString(GlobalConstants.GUESTS);
            }
            if(attDetObj.has(GlobalConstants.PHONE_NUMBER))
            {
                attDetModel.PhoneNumber = attDetObj.getString(GlobalConstants.PHONE_NUMBER);
            }
            if(attDetObj.has(GlobalConstants.CITY))
            {
                attDetModel.City = attDetObj.getString(GlobalConstants.CITY);
            }
            if(attDetObj.has(GlobalConstants.STATE))
            {
                attDetModel.State = attDetObj.getString(GlobalConstants.STATE);
            }
            if(attDetObj.has(GlobalConstants.VALIDITY))
            {
                attDetModel.Validity = attDetObj.getString(GlobalConstants.VALIDITY);
            }
            if(attDetObj.has(GlobalConstants.NOTES))
            {
                attDetModel.Notes = attDetObj.getString(GlobalConstants.NOTES);
            }
            if(attDetObj.has(GlobalConstants.STATUS))
            {
                attDetModel.Status = attDetObj.getString(GlobalConstants.STATUS);
            }
            if(attDetObj.has(GlobalConstants.EVENT_ID))
            {
                attDetModel.EventId = attDetObj.getString(GlobalConstants.EVENT_ID);
            }
            if(attDetObj.has(GlobalConstants.ATTENDEE_TYPE_ID))
            {
                attDetModel.AttendeeTypeId = attDetObj.getString(GlobalConstants.ATTENDEE_TYPE_ID);
            }

            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_1))
            {
                attDetModel.CustomField1 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_1));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_2))
            {
                attDetModel.CustomField2 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_2));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_3))
            {
                attDetModel.CustomField3 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_3));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_4))
            {
                attDetModel.CustomField4 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_4));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_5))
            {
                attDetModel.CustomField5 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_5));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_6))
            {
                attDetModel.CustomField6 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_6));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_7))
            {
                attDetModel.CustomField7 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_7));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_8))
            {
                attDetModel.CustomField8 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_8));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_9))
            {
                attDetModel.CustomField9 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_9));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_10))
            {
                attDetModel.CustomField10 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_10));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_11))
            {
                attDetModel.CustomField11 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_11));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_12))
            {
                attDetModel.CustomField12 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_12));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_13))
            {
                attDetModel.CustomField13 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_13));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_14))
            {
                attDetModel.CustomField14 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_14));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_15))
            {
                attDetModel.CustomField15 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_15));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_16))
            {
                attDetModel.CustomField16 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_16));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_17))
            {
                attDetModel.CustomField17 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_17));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_18))
            {
                attDetModel.CustomField18 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_18));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_19))
            {
                attDetModel.CustomField19 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_19));
            }
            if(attDetObj.has(GlobalConstants.CUSTOME_FIELD_20))
            {
                attDetModel.CustomField20 = getCustomAttendeeFields(attDetObj.getJSONObject(GlobalConstants.CUSTOME_FIELD_20));
            }

            returnModel.attendeeDetail = attDetModel;
            //=====Attendee Detail Parsing End====

            //=====Category Type Parsing Start====
            if(mainObj.has(GlobalConstants.CATEGORY_TYPES)){
                JSONArray catTypeArr = mainObj.getJSONArray(GlobalConstants.CATEGORY_TYPES);

                ArrayList<CategoryTypeModel> catModels = new ArrayList<CategoryTypeModel>();

                for (int i = 0; i < catTypeArr.length(); i++) {
                    CategoryTypeModel model = new CategoryTypeModel();

                    JSONObject catDetObj = catTypeArr.getJSONObject(0);
                    ArrayList<CategoryCommonModel> childModels = new ArrayList<CategoryCommonModel>();

                    if(catDetObj.has(GlobalConstants.CHILDREN)){

                        JSONArray childArr = catDetObj.getJSONArray(GlobalConstants.CHILDREN);
                        for (int j = 0; j < childArr.length(); j++) {
                            JSONObject childJob = childArr.getJSONObject(j);
                            CategoryCommonModel inmodel = new CategoryCommonModel();
                            if(childJob.has(GlobalConstants.ID)){
                                inmodel.Id = childJob.getString(GlobalConstants.ID);
                            }
                            if(childJob.has(GlobalConstants.TEXT)){
                                inmodel.Text = childJob.getString(GlobalConstants.TEXT);
                            }
                            childModels.add(inmodel);
                        }
                    }

                    model.childrenModels = childModels;

                    CategoryCommonModel outmodel = new CategoryCommonModel();

                    if(catDetObj.has(GlobalConstants.ID)){
                        outmodel.Id = catDetObj.getString(GlobalConstants.ID);
                    }
                    if(catDetObj.has(GlobalConstants.TEXT)){
                        outmodel.Text = catDetObj.getString(GlobalConstants.TEXT);
                    }

                    model.categoryDetail = outmodel;

                    catModels.add(model);
                }

                returnModel.categoryDetail = catModels;
            }
            //=====Category Type Parsing Start====

        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }

        return returnModel;
    }

    public static AttendeeDetailCommonModel getCustomAttendeeFields(JSONObject job) {

        AttendeeDetailCommonModel model = new AttendeeDetailCommonModel();
        try {
            if (job.has(GlobalConstants.LABEL)) {
                model.Label = job.getString(GlobalConstants.LABEL);
            }
            if (job.has(GlobalConstants.VALUE)) {
                model.Value = job.getString(GlobalConstants.VALUE);
            }
            if (job.has(GlobalConstants.IS_ENABLED)) {
                model.IsEnabled = job.getBoolean(GlobalConstants.IS_ENABLED);
            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return model;
    }

}