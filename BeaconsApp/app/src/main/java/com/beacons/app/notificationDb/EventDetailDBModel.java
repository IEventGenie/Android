package com.beacons.app.notificationDb;

/**
 * Created by aman on 4/9/16.
 */

public class EventDetailDBModel {

    //private variables
    String _id = "";
    String _confirm_code = "";
    String _last_name = "";
    String _ev_id = "";
    String _ev_addr_txt = "";
    String _ev_city_txt = "";
    String _ev_img_url = "";
    String _ev_loc_txt = "";
    String _ev_name = "";
    String _enable_prechk_in = "";
    String _attendee_id = "";
    String _start_date = "";

    // Empty constructor
    public EventDetailDBModel(){

    }

    // constructor
    public EventDetailDBModel(String _id,String _confirm_code,String _last_name,String _ev_id,String _ev_addr_txt,String _ev_city_txt,
                              String _ev_img_url,String _ev_loc_txt,String _ev_name,String _enable_prechk_in,String _attendee_id,String _start_date){
        this._id = _id;
        this._confirm_code = _confirm_code;
        this._last_name = _last_name;
        this._ev_id = _ev_id;
        this._ev_addr_txt = _ev_addr_txt;
        this._ev_city_txt = _ev_city_txt;
        this._ev_img_url = _ev_img_url;
        this._ev_loc_txt = _ev_loc_txt;
        this._ev_name = _ev_name;
        this._enable_prechk_in = _enable_prechk_in;
        this._attendee_id = _attendee_id;
        this._start_date = _start_date;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }

    public String getConfirmCode() {
        return _confirm_code;
    }

    public void setConfirmCode(String _confirm_code) {
        this._confirm_code = _confirm_code;
    }

    public String getLastName() {
        return _last_name;
    }

    public void setLastName(String _last_name) {
        this._last_name = _last_name;
    }

    public String getEvId() {
        return _ev_id;
    }

    public void setEvId(String _ev_id) {
        this._ev_id = _ev_id;
    }

    public String getEvAddrTxt() {
        return _ev_addr_txt;
    }

    public void setEvAddrTxt(String _ev_addr_txt) {
        this._ev_addr_txt = _ev_addr_txt;
    }

    public String getEvCityTxt() {
        return _ev_city_txt;
    }

    public void setEvCityTxt(String _ev_city_txt) {
        this._ev_city_txt = _ev_city_txt;
    }

    public String getEvImgUrl() {
        return _ev_img_url;
    }

    public void setEvImgUrl(String _ev_img_url) {
        this._ev_img_url = _ev_img_url;
    }

    public String getEvLocTxt() {
        return _ev_loc_txt;
    }

    public void setEvLocTxt(String _ev_loc_txt) {
        this._ev_loc_txt = _ev_loc_txt;
    }

    public String getEvName() {
        return _ev_name;
    }

    public void setEvName(String _ev_name) {
        this._ev_name = _ev_name;
    }

    public String getEnablePrechkIn() {
        return _enable_prechk_in;
    }

    public void setEnablePrechkIn(String _enable_prechk_in) {
        this._enable_prechk_in = _enable_prechk_in;
    }

    public String getAttendeeId() {
        return _attendee_id;
    }

    public void setAttendeeId(String _attendee_id) {
        this._attendee_id = _attendee_id;
    }

    public String getStartDate() {
        return _start_date;
    }

    public void setStartDate(String _start_date) {
        this._start_date = _start_date;
    }

}