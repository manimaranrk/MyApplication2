package com.purpleknot1.Util;


public class ApplicationConstants {

//    public static String url_server = "http://128.199.95.176/purpleknot";
    public static String url_server = "http://128.199.168.98/android";

 //   public static String url_server = "http://192.168.1.105/android_test_connect";

//    http://192.168.1.108/android_test_connect/

    public static String url_validate_user = url_server+"/user_login.php";
    public static String url_update_user_status = url_server+"/current_login_status.php";


    public static String url_validate_user_details = url_server+"/validate_user_details.php";
    public static String url_create_password = url_server+"/create_password.php";
    public static String url_change_password = url_server+"/change_password.php";


    public static String url_daily_report = url_server+"/daily_report.php";

    public static String url_monthly_report = url_server+"/monthly_report.php";

    public static String url_weekly_report = url_server+"/weekly_report.php";

    public static String url_select_venue = url_server+"/select_venue.php";
    public static String url_select_region = url_server+"/select_region.php";
   // public static String url_select_user = url_server+"/select_user.php";

    public static String url_create_region = url_server+"/create_region.php";
    public static String url_create_user = url_server+"/create_user.php";
    public static String url_create_venue = url_server+"/create_venue.php";
    public static String url_create_venue1 = url_server+"/create_venue_with_gps.php";

    public static String url_create_pg = url_server+"/create_pg_with_gps.php";
    public static String url_create_pgpv = url_server+"/create_pgvg_with_gps.php";



    public static String url_create_venue2 = url_server+"/create_venue_without_gps.php";


    public static String url_update_venue = url_server+"/update_venue.php";

    public static String url_update_venue_position = url_server+"/update_position.php";
    public static String url_update_pg_position = url_server+"/update_pg_position.php";

    public static String url_update_same_status = url_server+"/same_status_update.php";

    public static String url_no_update = url_server+"/no_update.php";

    public static String url_select_venue_byname = url_server+"/select_venue_by_name.php";


    public static String url_select_scheduledvenue = url_server+"/scheduled_venue.php";

    public static String url_select_scheduledvenue_by_userid = url_server+"/select_schedule_venue_by_userid.php";

    public static String url_select_scheduledvenue_by_userid1 = url_server+"/select_schedule_venue_by_userid1.php";

    public static String url_select_scheduledvenue_userid = url_server+"/select_scheduledvenue_userid.php";
    public static String url_select_scheduledvenue_userid1 = url_server+"/select_scheduledvenue_userid1.php";

    public static String url_select_scheduledpgvg_userid1 = url_server+"/select_schedulepgvg_userid.php";

    public static String url_select_user = url_server+"/select_user.php";


    public static String url_register_user = url_server+"/register_user.php";
    public static String url_select_request = url_server+"/new_user_request.php";
    public static String url_upload_image = url_server+"/venue_image_upload.php";

    public static String url_select_city = url_server+"/select_city.php";

    public static String url_select_state = url_server+"/select_state.php";
    public static String url_select_city_by_state = url_server+"/select_city_by_state.php";
    public static String url_select_zone_by_city = url_server+"/select_zone_by_id.php";


    public static String url_select_location = url_server+"/select_location_by_cityname.php";
    public static String url_select_venue_by_location = url_server+"/select_venue_by_location.php";

    public static String url_upload_withoutimage = url_server+"/venue_without_image.php";

    public static String url_stage1_status_upload = url_server+"/stage1_status_upload.php";



    public static String url_upload_stage2 = url_server+"/stage2_upload.php";

    public static String url_upload1_stage2 = url_server+"/stage2_upload1.php";
    public static String url_upload2_stage2 = url_server+"/stage2_upload2.php";

    public static String url_pgvg_status= url_server+"/update_pgvg_status.php";

    public static String url_upload1_stage3 = url_server+"/stage3_upload1.php";

    public static String url_get_visit_id = url_server+"/get_visit_id_by_venue_name.php";

    public static String url_get_live_demo = url_server+"/get_live_demo_status.php";

    public static String url_create_agreement_detail = url_server+"/create_agreement_detail.php";


    public static String url_create_venue_agreement = url_server+"/create_venue_agreement.php";

    public static String url_create_venue_agreement1 = url_server+"/create_venue_agreement1.php";

    public static String url_create_agreement = url_server+"/agreement_submit.php";

    public static String url_schedule_venue_by_admin = url_server+"/schedule_venue_by_admin.php";

    public static String url_select_stage_venue = url_server+"/select_stage_by_venue.php";

    public static String url_select_stage_venue1 = url_server+"/select_stage_by_venue2.php";

    public static String url_select_stage_venue2 = url_server+"/select_status.php";

    public static String url_select_pg_status = url_server+"/select_pg_status.php";


    public static String url_create_agreement_details_with_image = url_server+"/create_agreement_details_with_image.php";

    public static String url_create_agreement_details_without_image = url_server+"/create_agreement_details_without_image.php";

    public static String url_select_special_status = url_server+"/select_special_status.php";

    //----------------------------------------------------------------------------------------------

    public static final String TAG_SUCCESS = "success";

    //----------------------------------------------------------------------------------------------


    //                        $user = array();
//                        $user["id"] = $row["f_user_id"];
//                        $user["username"] = $row["f_username"];
//                        $user["designation"] = $row["f_designation"];
//                        $user["employee_id"] = $row["employee_id"];
//
//                        $user["fullname"] = $row["f_fullname"];
//                        $user["imei"] = $row["f_imei"];
//                        $user["mobile_no"] = $row["f_mobile_number"];
//                        $user["city_id"] = $row["f_city_id"];
//                        $user["manager_1"] = $row["f_manager1"];
//                        $user["manager_2"] = $row["f_manager2"];
//                        $user["manager_3"] = $row["f_manager3"];
//                        $user["manager_4"] = $row["f_manager4"];
//                        $user["accessgrant"] = $row["f_accessgrant"];
//                        $user["mobile_app_access"] = $row["f_mobile_app_access"];

    public static final String TAG_USER = "User";
    public static final String TAG_USER_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_USER_DSIGNATION = "designation";
    public static final String TAG_USER_CITY_ID = "city_id";
   // public static final String TAG_USER_EMPLOYEEID = "employee_id";

    public static final String TAG_USR = "User";
    public static final String TAG_USR_ID = "f_user_id";
    public static final String TAG_USR_NME = "f_username";
    public static final String TAG_USR_DESIGNATION = "f_designation";
    public static final String TAG_USR_FULLNME = "f_fullname";
    public static final String TAG_USR_IMEI = "f_imei";
    public static final String TAG_USR_MOBILE = "f_mobile_number";
    public static final String TAG_USR_CTY_ID = "f_city_id";
    public static final String TAG_USR_MANAGER1 = "f_manager1";
    public static final String TAG_USR_MANAGER2 = "f_manager2";
    public static final String TAG_USR_MANAGER3 = "f_manager3";
    public static final String TAG_USR_MANAGER4 = "f_manager4";
    public static final String TAG_USR_ACCESSGRANT = "f_accessgrant";
    public static final String TAG_USR_MOBILE_ACCESS = "f_mobile_app_access";


    //-----------------------------------------------------------------

    public static final String TAG_STAGE = "Stage";

    public static final String TAG_STATUS_ID = "status_id";

    public static final String TAG_STAGE_NUMBER = "stage_number";

    public static final String TAG_STATUS_DESCRIPTION = "description";


    //==============================================================================================

    public static final String TAG_PG_STAGE = "Pg_Stage";

    public static final String TAG_PG_STATUS_id = "status_id";

    public static final String TAG_PG_STAGE_NUMBER = "stage_number";
    public static final String TAG_PG_STATUS_DESCRIPTION = "description";


    //----------------------------------------------------------------------------------------------

    public static final String TAG_SPECIAL_STATUS = "Special_Status";
    public static final String TAG_SPECIAL_ID = "status_id";
    public static final String TAG_SPECIAL_STAGENO = "stage_no";
    public static final String TAG_SPECIAL_NAME = "status_name";






    //----------------------------------------------------------------------------------------------

    public static final String TAG_DAILY_REPORT = "Daily_Report";
    public static final String TAG_REPORT_VISIT_ID = "Visit_Id";
    public static final String TAG_REPORT_VENUE_ID = "Venue_Id";
    public static final String TAG_REPORT_VENUE_NAME = "Venue_Name";
    public static final String TAG_REPORT_EMPLOYEE_ID = "Employee_Id";
    public static final String TAG_REPORT_CURRENT_STATUS = "Current_Status";
    public static final String TAG_REPORT_POSTVISIT_STATUS = "PostVisit_Status";
    public static final String TAG_REPORT_SCHEDULE_DATE = "Schedule_Date";
    public static final String TAG_REPORT_LAST_VISIT_DATE = "Last_Visit_Date";
    public static final String TAG_REPORT_VISIT_NUMBER = "Visit_Number";
    public static final String TAG_REPORT_SCHEDULE_STATUS = "Schedule_Status";






    //----------------------------------------------------------------------------------------------

//    public static final String TAG_ID = "id";
//    public static final String TAG_VENUEID = "venue_id";
//    public static final String TAG_VENUEHISTORE = "Venue_history";
//    public static final String TAG_STATUS = "stage1_status";
//    public static final String TAG_COMMENT = "comment";
//    public static final String TAG_FOLLOWUP = "followup";
//    public static final String TAG_EMPLOYEEID = "employee_id";
//    public static final String TAG_HISTORETIME = "history_time";

    public static final String TAG_VISITID = "Visit_Id";
    public static final String TAG_VENUEID = "Venue_Id";
    public static final String TAG_VENUENAME = "Venue_Name";
    public static final String TAG_USERRID = "User_Id";
    public static final String TAG_CURRENTSTATUS = "Current_Status";
    public static final String TAG_POSTVISITSTATUS = "PostVisit_Status";
    public static final String TAG_SCHEDULEDATE = "Schedule_Date";
    public static final String TAG_LASTVISITDATE = "Last_Visit_Date";
    public static final String TAG_VISITNUMBER = "Visit_Number";
    public static final String TAG_SCHEDULESTATUS = "Schedule_Status";
    public static final String TAG_MANAGERMESSAGE = "Manager_Message";

    public static final String TAG_VENUE_SPECIAL_STATUS1 = "Special_Status_1";
    public static final String TAG_VENUE_SPECIAL_STATUS2 = "Special_Status_2";
    public static final String TAG_VENUE_SPECIAL_STATUS3 = "Special_Status_3";
    public static final String TAG_VENUE_SPECIAL_STATUS4 = "Special_Status_4";
    public static final String TAG_VENUE_SPECIAL_STATUS5 = "Special_Status_5";
    public static final String TAG_VENUE_LOCATION_CAPTURED = "Location_Captured";
    public static final String TAG_VENUE_LATITUDE = "Latitude";
    public static final String TAG_VENUE_LONGITUDE = "Longitude";


    //=================================================================================

    public static final String TAG_PGHISTORY = "Pg_History";

    public static final String TAG_PG_NAME = "Pg_Name";
    public static final String TAG_PG_VISIT_ID = "Pg_Visit_Id";
    public static final String TAG_PG_ID= "Pg_Id";
    public static final String TAG_PG_USER_ID = "Pg_User_Id";
    public static final String TAG_PG_CURRENT_STATUS = "Pg_Current_Status";
    public static final String TAG_PG_POST_VISIT_STATUS = "Pg_PostVisit_Status";
    public static final String TAG_PG_LOCATION_CAPTURED = "Pg_Location_Captured";
    public static final String TAG_PG_LATITUDE = "Pg_Latitude";
    public static final String TAG_PG_LONGITUDE = "Pg_Longitude";
    public static final String TAG_PG_COUNT = "Pg_Count";






    //==================================================================================

    public static final String TAG_VENUEHISTORE = "Venue_history";

    public static final String TAG_USERS = "User";
    public static final String TAG_USERSNAME = "username";
    public static final String TAG_USERSROLE = "role";
    public static final String TAG_USERID = "employee_id";

    //====================================================================


    public static final String TAG_GET_VENUE = "Venue";

    public static final String TAG_GET_VENUE_NAME = "Venue_Name";
    public static final String TAG_GET_VENUE_ADDRESS1 = "Venue_Address1";
    public static final String TAG_GET_VENUE_ADDRESS2 = "Venue_Address2";
    public static final String TAG_GET_VENUE_CONTACT_NAME = "Venue_Contact_Name";
    public static final String TAG_GET_VENUE_CONTACT_DESIGNATION = "Venue_Contact_Designation";
    public static final String TAG_GET_VENUE_CONTACT_NUMBER = "Venue_Contact_Number";
    public static final String TAG_GET_VENUE_EMAIL = "Venue_Email";
    public static final String TAG_GET_VENUE_OWNER_NAME = "Venue_Owner_Name";
    public static final String TAG_GET_VENUE_OWNER_NUMBER = "Venue_Owner_Number";
    public static final String TAG_GET_VENUE_CLASS = "Venue_Class";
    public static final String TAG_GET_VENUE_PAST_OCCUMANCY = "Venue_Past_Occupancy";
    public static final String TAG_GET_VENUE_SEATING_CAPACITY = "Venue_Seating_Capacity";
    public static final String TAG_GET_VENUE_WEBSITE = "Venue_Website";




//    $venuehistory1["Venue_Id"] = $venueid;
//    $venuehistory1["Venue_Name"] = $venuename;
//    $venuehistory1["Venue_Address1"] = $row["f_addr_1"];
//    $venuehistory1["Venue_Address2"] = $row["f_addr_2"];
//    $venuehistory1["Venue_Contact_Name"] = $row["f_contact_name"];
//    $venuehistory1["Venue_Contact_Designation"] = $row["f_contact_designation"];
//    $venuehistory1["Venue_Contact_Number"] = $row["f_contact_number"];
//    $venuehistory1["Venue_Email"] = $row["f_email"];
//    $venuehistory1["Venue_Owner_Name"] = $row["f_owner_name"];
//    $venuehistory1["Venue_Owner_Number"] = $row["f_owner_contact_number"];
//    $venuehistory1["Venue_Class"] = $row["f_mandapam_class"];
//    $venuehistory1["Venue_Past_Occupancy"] = $row["f_past_occupany"];
//    $venuehistory1["Venue_Seating_Capacity"] = $row["f_seating_capacity"];
//    $venuehistory1["Venue_Website"] = $row["f_website_url"];



//========================================================================

    public static final String TAG_CTY = "City";
    public static final String TAG_CTY_ID = "city_id";
    public static final String TAG_CTY_STATE = "state";
    public static final String TAG_CTY_NAME = "City_name";
    public static final String TAG_CTY_NME = "city_name";


    //-------------------------------------------------------------------

    public static final String TAG_ZONE = "Zone";

    public static final String TAG_ZONE_NAME = "zone_name";

//=================================================

    public static final String TAG_LIVE_DEMO_STATUS = "Live_demo_status";
    public static final String TAG_LIVE_DEMO = "Live_Demo";


    //------------------------------------------------------------

    public static final String TAG_VENUE_ID = "Venue_visit_id";
    public static final String TAG_VISIT_ID = "Visit_Id";


    //======================================================================

    public static final String TAG_EXISTING_STATE = "State";

    public static final String TAG_EXISTING_STATE_NAME = "state_name";



    //----------------------------------------------------------------

    public static final String TAG_LOCATION = "Location";
    public static final String TAG_LOCATION_NAME = "location_name";


    //--------------------------------------------------------------------

    public static final String TAG_SCHEDUL_VENUE = "Venue";
    public static final String TAG_SCHEDUL_VENUE_NAME = "Venue_Name";
    public static final String TAG_SCHEDUL_VENUE_ID = "Venue_id";
    public static final String TAG_SCHEDUL_VENUE_CURRENT_STATUS = "Current_Status";
    public static final String TAG_SCHEDUL_VENUE_SCHEDULE_STATUS = "schedule_Status";


//    public static final String TAG_STATUS = "stage1_status";
//    public static final String TAG_COMMENT = "comment";
//    public static final String TAG_FOLLOWUP = "followup";
//
//    public static final String TAG_HISTORETIME = "history_time";

    //----------------------------------------------------------------------------------------------

    public static final String TAG_VENUE = "Venues";
    public static final String TAG_NAME = "name";
    public static final String TAG_ADDRESS1 = "address_1";
    public static final String TAG_ADDRESS2 = "address_2";
    public static final String TAG_CITY = "city";
    public static final String TAG_STATE = "state";
    public static final String TAG_PINCODE = "pincode";
    public static final String TAG_REGION_ID = "region_id";
    public static final String TAG_CONTACTNAME = "contact_name";
    public static final String TAG_CONTACTNUMBER = "contact_number";
    public static final String TAG_CONTACTEMAIL = "contact_email";
    public static final String TAG_OWNERNAME = "owner_name";
    public static final String TAG_OWNERNUMBER = "owner_number";
    public static final String TAG_CLASS = "class";
    public static final String TAG_SEATING = "website";
    public static final String TAG_WEBSITE = "seating_capacity";
    public static final String TAG_PASTOCCUPANCY = "past_occupancy";
    public static final String TAG_LAT = "lat";
    public static final String TAG_LNG = "lng";
    public static final String TAG_VENUESTATUS = "venue_status";





}
