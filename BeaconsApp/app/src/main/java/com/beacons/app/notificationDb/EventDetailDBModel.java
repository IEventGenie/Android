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
    String _chk_in_start_date = "";
    String _chk_in_end_date = "";
    String _pre_chk_in_strt_date = "";
    String _pre_chk_in_end_date = "";
    String _enabl_checkin = "";
    String _ev_status = "";
    String _ev_pre_chkin_status = "";
    String _ev_start_date = "";
    String _ev_end_date = "";
    String _checkin_status = "";

    // Empty constructor
    public EventDetailDBModel(){

    }

    // constructor
    /*public EventDetailDBModel(String _id,String _confirm_code,String _last_name,String _ev_id,String _ev_addr_txt,String _ev_city_txt,
                              String _ev_img_url,String _ev_loc_txt,String _ev_name,String _enable_prechk_in,String _attendee_id,String _start_date,
                              String _chk_in_end_date,String _pre_chk_in_strt_date,String _pre_chk_in_end_date,String _enabl_Checkin,String _ev_status){
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
        this._chk_in_start_date = _start_date;
        this._chk_in_end_date = _chk_in_end_date;
        this._pre_chk_in_strt_date = _pre_chk_in_strt_date;
        this._pre_chk_in_end_date = _pre_chk_in_end_date;
        this._enabl_checkin = _enabl_Checkin;
        this._ev_status = _ev_status;
    }*/

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

    public String getChkinStartDate() {
        return _chk_in_start_date;
    }

    public void setChkinStartDate(String _start_date) {
        this._chk_in_start_date = _start_date;
    }

    public String getChkInEndDate() {
        return _chk_in_end_date;
    }

    public void setChkInEndDate(String _chk_in_end_date) {
        this._chk_in_end_date = _chk_in_end_date;
    }

    public String getPreChkInStrtDate() {
        return _pre_chk_in_strt_date;
    }

    public void setPreChkInStrtDate(String _pre_chk_in_strt_date) {
        this._pre_chk_in_strt_date = _pre_chk_in_strt_date;
    }

    public String getPreChkInEndDate() {
        return _pre_chk_in_end_date;
    }

    public void setPreChkInEndDate(String _pre_chk_in_end_date) {
        this._pre_chk_in_end_date = _pre_chk_in_end_date;
    }

    public String getEnablCheckin() {
        return _enabl_checkin;
    }

    public void setEnablCheckin(String _enabl_Checkin) {
        this._enabl_checkin = _enabl_Checkin;
    }

    public String getEventStatus() {
        return _ev_status;
    }

    public void setEventStatus(String _ev_status) {
        this._ev_status = _ev_status;
    }

    public String getEvPreChkinStatus() {
        return _ev_pre_chkin_status;
    }

    public void setEvPreChkinStatus(String _ev_pre_chkin_status) {
        this._ev_pre_chkin_status = _ev_pre_chkin_status;
    }

    public String getEvStartDate() {
        return _ev_start_date;
    }

    public void setEvStartDate(String _ev_start_date) {
        this._ev_start_date = _ev_start_date;
    }

    public String getEvEndDate() {
        return _ev_end_date;
    }

    public void setEvEndDate(String _ev_end_date) {
        this._ev_end_date = _ev_end_date;
    }

    public String getPreCheckinStatus() {
        return _checkin_status;
    }

    public void setPreCheckinStatus(String _checkin_status) {
        this._checkin_status = _checkin_status;
    }

}