package com.beacons.app.WebserviceDataModels;

import java.util.ArrayList;

/**
 * Created by aman on 2/29/16.
 */
public class EventDetailMainModel {

    //event details==
    EventDetailModel detailModel = new EventDetailModel();
    EventDetailMobileCheckinSettings mobileCheckinSettings = new EventDetailMobileCheckinSettings();
    ArrayList<EventDetailCommonModel> attendeeCustomFieldSettings = new ArrayList<EventDetailCommonModel>();
    ArrayList<EventDetailCommonModel> housingCustomFieldSettings = new ArrayList<EventDetailCommonModel>();
    ArrayList<EventDetailCommonModel> volunteerCustomFieldSettings = new ArrayList<EventDetailCommonModel>();
    //================

    //attendee details==
    AttendeeDetailModel attendeeDetail = new AttendeeDetailModel();
    //==================

    //Category details==
    CategoryTypeModel categoryDetail = new CategoryTypeModel();
    //==================

}
