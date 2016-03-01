package com.beacons.app.WebserviceDataModels;

import java.util.ArrayList;

/**
 * Created by aman on 2/29/16.
 */
public class EventDetailMainModel {

    //event details==
    public EventDetailModel detailModel = new EventDetailModel();
    public EventDetailMobileCheckinSettingsModel mobileCheckinSettings = new EventDetailMobileCheckinSettingsModel();
    public ArrayList<EventDetailCommonModel> attendeeCustomFieldSettings = new ArrayList<EventDetailCommonModel>();
    public ArrayList<EventDetailCommonModel> housingCustomFieldSettings = new ArrayList<EventDetailCommonModel>();
    public ArrayList<EventDetailCommonModel> volunteerCustomFieldSettings = new ArrayList<EventDetailCommonModel>();
    //================

    //attendee details==
    public AttendeeDetailModel attendeeDetail = new AttendeeDetailModel();
    //==================

    //Category details==
    public CategoryTypeModel categoryDetail = new CategoryTypeModel();
    //==================

}
