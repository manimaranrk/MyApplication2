package com.purpleknot1.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class StatusActivity1 extends Activity {

    private ProgressDialog pDialog;
    NetworkConnection networkConnection;

    JSONParser jsonParser = new JSONParser();

    byte[] imageBytes;
    String imageFilePath;
    ToggleButton toggleButton;

    private int mYear, mMonth, mDay, mHour, mMinute;

    Button dateButton,timeButton;

    String dat,tim;

    ToggleButton interestedToggle,awareToggle,warmToggle,demoToggle,interventionToggle;

    Button captureButton,submitButton;

    int imageCount;

    String venueName;

    String venueNme,visitId,venueStatus,postVisitStatus,venueCmd,stageNumber,date;

    EditText commentEdittext;

    String currentDateandTime;

    int spcialStatus1 = 0,spcialStatus2 = 0,spcialStatus3 = 0,spcialStatus4 = 0,spcialStatus5 = 0;

    String scheduleDate;

    LinearLayout interestedLinear,awareLinear,warmLinear,demoLinear,interventionLinear;

    ImageView interestedOn,interestedNetrol,interestedOff,awareOn,awareNetrol,awareOff,warmOn,warmNetrol,
            warmOff,demoOn,demoNetrol,demoOff,interventionOn,interventionNetrol,interventionOff;

    Button agreementButton;


    ArrayList<HashMap<String, String>> venueStatusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.status2);

        //  toggleButton = (ToggleButton)findViewById(R.id.status_toggle);




        dateButton = (Button)findViewById(R.id.date_button1);
        timeButton = (Button)findViewById(R.id.time_Button1);


        interventionToggle = (ToggleButton)findViewById(R.id.intervention_toggle1);

        agreementButton = (Button)findViewById(R.id.agreement_button1);

        commentEdittext = (EditText)findViewById(R.id.comment_edittext1);

       // captureButton = (Button)findViewById(R.id.capture_image_button1);

        submitButton = (Button)findViewById(R.id.submit_button1);

        networkConnection = new NetworkConnection(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentDateandTime = sdf.format(new Date());

        Log.e("currentDateandTime",currentDateandTime);

        agreementButton.setVisibility(View.INVISIBLE);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            venueNme = extras.getString("venueName");
            //venueStatus = extras.getString("venueStatus");
            venueStatusList= (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("venueStatusList");

            visitId = extras.getString("visitId");

            postVisitStatus = extras.getString("postVisitStatus");
            date = extras.getString("date");
            stageNumber = extras.getString("stageNumber") ;


            if(stageNumber.equals("2")){

                agreementButton.setVisibility(View.VISIBLE);

            }

            // Log.e("post visit",venueStatus);
            //   Log.e("post venueDate",venueDate);





//            if(venueStatus!="null") {
//
//                scheduleVenueStatusSpinner.setSelection(getIndex(scheduleVenueStatusSpinner, venueStatus));
//            }
        }


        agreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StatusActivity1.this, AgreementDetailsActivity.class);
                intent.putExtra("venueName", venueNme);
                intent.putExtra("venueStatus", venueStatus);
                intent.putExtra("postVisitStatus", postVisitStatus);
                startActivity(intent);
                finish();

            }
        });




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                if(interestedToggle.isChecked()){
//
//                    spcialStatus1 = 1;
//
//                }
//                if(awareToggle.isChecked()){
//
//                    spcialStatus2 = 1;
//
//                }
//
//                if(warmToggle.isChecked()){
//
//                    spcialStatus3 = 1;
//
//                }
//
//                if(demoToggle.isChecked()){
//
//                    spcialStatus4 = 1;
//
//                }
                if(interventionToggle.isChecked()){

                    spcialStatus5 = 1;

                }

                scheduleDate = dat+" "+tim;


//                if (venueName == null) {
//                    Log.e("venueName((((", venueNme);
//
//                }  if (postVisitStatus == null) {
//                    Log.e("scheduleVenueStatus((((", postVisitStatus);
//                } else {
                String venueImagePath;
                Log.e("venueName))))", venueNme);
                Log.e("postVisitStatus))))", postVisitStatus);








//                if (Image.getInstance().getImagePath().size() != 0) {
//
//                    for (String imagePath : Image.getInstance().getImagePath()) {
//
//                        Log.e("imagePathhhhh --", imagePath);
//
//                        if (imagePath != null) {
//
//                            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
//                            venueCmd = commentEdittext.getText().toString();
//                            String imageString = encodeTobase64(myBitmap);
//                            new UpdateVenueStatus().execute(imageString);
//                        }
//                    }
//                } else {
                    venueCmd = commentEdittext.getText().toString();
                    new UpdateVenueStatus1().execute();
             //   }
                //  }
            }
        });


//        captureButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 0);
//
//
//            }
//        });


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(StatusActivity1.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

//                                    Toast.makeText(getApplicationContext(),
//                                            "Date : " + dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year,
//                                            Toast.LENGTH_SHORT).show();
                                //    dateButton.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                dateButton.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dat = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                // Display Selected date in textbox
                                //     txtDate.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(StatusActivity1.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                //  txtTime.setText(hourOfDay + ":" + minute);
//                                    Toast.makeText(getApplicationContext(),
//                                            "Time : " + hourOfDay + ":" + minute,
//                                            Toast.LENGTH_SHORT).show();
                                tim = hourOfDay + ":" + minute + ":00";

                                timeButton.setText(hourOfDay + ":" + minute + ":00");
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }
        });
    }

    class UpdateVenueStatus1 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StatusActivity1.this);
            pDialog.setMessage("Upload the Image...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            JSONObject json = null;
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String visitStatus1=null,visitStatus2=null,visitStatus3=null,visitStatus4=null;

            if (networkConnection.isNetworkAvailable()) {



                if(!venueStatusList.isEmpty()) {

                    for (int i = 0; i < venueStatusList.size(); i++) {

                        if(venueStatusList.size() ==1){
                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);


                            params.add(new BasicNameValuePair("venue_status", visitStatus1));

                            params.add(new BasicNameValuePair("visit_status3", ""));


                            params.add(new BasicNameValuePair("visit_status2", ""));

                            params.add(new BasicNameValuePair("visit_status1", ""));

                        }
                        else if(venueStatusList.size() ==2){

                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
                            visitStatus2=venueStatusList.get(1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);

                            params.add(new BasicNameValuePair("venue_status", visitStatus2));

                            params.add(new BasicNameValuePair("visit_status3", visitStatus1));


                            params.add(new BasicNameValuePair("visit_status2", ""));

                            params.add(new BasicNameValuePair("visit_status1", ""));


                        }
                        else if(venueStatusList.size() ==3){

                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
                            visitStatus2=venueStatusList.get(1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
                            visitStatus3=venueStatusList.get(2).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);

                            params.add(new BasicNameValuePair("venue_status", visitStatus3));

                            params.add(new BasicNameValuePair("visit_status3", visitStatus1));


                            params.add(new BasicNameValuePair("visit_status2", visitStatus2));

                            params.add(new BasicNameValuePair("visit_status1", ""));

                        }
                        else if(venueStatusList.size() ==4){

                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
                            visitStatus2=venueStatusList.get(1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
                            visitStatus3=venueStatusList.get(2).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
                            visitStatus4=venueStatusList.get(3).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);


                            params.add(new BasicNameValuePair("venue_status", visitStatus4));

                            params.add(new BasicNameValuePair("visit_status3", visitStatus3));


                            params.add(new BasicNameValuePair("visit_status2", visitStatus2));

                            params.add(new BasicNameValuePair("visit_status1", visitStatus1));



                        }

                    }


                }


                params.add(new BasicNameValuePair("name", venueNme));
                params.add(new BasicNameValuePair("visit_id", visitId));
                params.add(new BasicNameValuePair("stage_number", stageNumber));

                params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));
                params.add(new BasicNameValuePair("last_visit", currentDateandTime));
                params.add(new BasicNameValuePair("comment", venueCmd));
                params.add(new BasicNameValuePair("date", date));


//                params.add(new BasicNameValuePair("spcialStatus1", spcialStatus1+""));
//                params.add(new BasicNameValuePair("spcialStatus2", spcialStatus2+""));
//                params.add(new BasicNameValuePair("spcialStatus3", spcialStatus3+""));
//                params.add(new BasicNameValuePair("spcialStatus4", spcialStatus4+""));
                params.add(new BasicNameValuePair("spcialStatus5", spcialStatus5+""));


                params.add(new BasicNameValuePair("scheduleDate", scheduleDate));


                json = jsonParser.makeHttpRequest(ApplicationConstants.url_create_agreement_details_without_image,
                        "POST", params);

                try {
                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                    if (success == 1) {

                        startActivity(new Intent(StatusActivity1.this, ScheduledVenueActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(StatusActivity1.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {

            } catch (final Exception e) {

            } finally {
                pDialog = null;
            }
        }
    }

//    class UpdateVenueStatus extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(StatusActivity1.this);
//            pDialog.setMessage("Upload the Image...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        /**
//         * Creating product
//         * */
//        protected String doInBackground(String... args) {
//
//            JSONObject json = null;
//            if (networkConnection.isNetworkAvailable()) {
//                Log.e("network1===", networkConnection.isNetworkAvailable() + "");
//
//                String imageString=args[0];
//
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("name", venueNme));
//                params.add(new BasicNameValuePair("venue_status", venueStatus));
//                params.add(new BasicNameValuePair("user_image", imageString));
//                params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));
//                params.add(new BasicNameValuePair("last_visit", currentDateandTime));
//                params.add(new BasicNameValuePair("comment", venueCmd));
//
////                params.add(new BasicNameValuePair("spcialStatus1", spcialStatus1+""));
////                params.add(new BasicNameValuePair("spcialStatus2", spcialStatus2+""));
////                params.add(new BasicNameValuePair("spcialStatus3", spcialStatus3+""));
////                params.add(new BasicNameValuePair("spcialStatus4", spcialStatus4+""));
//                params.add(new BasicNameValuePair("spcialStatus5", spcialStatus5+""));
//
//                params.add(new BasicNameValuePair("scheduleDate", scheduleDate));
//
//
//                Log.e("parms post--",venueStatus);
//
//                // getting JSON Object
//                // Note that create product url accepts POST method
//                json = jsonParser.makeHttpRequest(ApplicationConstants.url_create_agreement_details_with_image,
//                        "POST", params);
//
//                // check log cat fro response
//                Log.d("Create Response", json.toString());
//
//                // check for success tag
//                try {
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                    if (success == 1) {
//
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatusServerSync(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatusServerSync(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
//
//                        startActivity(new Intent(StatusActivity1.this, ScheduledVenueActivity.class));
//                        finish();
//
//                    }
//
////                    else{
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatus(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatus(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
////
////                        startActivity(new Intent(UpdateVenueStatusActivity.this, ScheduledVenueActivity.class));
////                        finish();
////
////                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else {
//                pDialog.dismiss();
//                Looper.prepare();
//                Toast.makeText(StatusActivity1.this,
//                        "Network is not Available. Please Check Your Internet Connection ",
//                        Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog once done
//            try {
//                if ((pDialog != null) && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//
//            } catch (final IllegalArgumentException e) {
//                // Handle or log or ignore
//            } catch (final Exception e) {
//                // Handle or log or ignore
//            } finally {
//                pDialog = null;
//            }
//        }
//    }

//    class UpdateVenueStatus extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(Stage1StatusActivity.this);
//            pDialog.setMessage("Upload the Image...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        /**
//         * Creating product
//         * */
//        protected String doInBackground(String... args) {
//
//            JSONObject json = null;
//            if (networkConnection.isNetworkAvailable()) {
//                Log.e("network1===", networkConnection.isNetworkAvailable() + "");
//
//                String imageString=args[0];
//
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("name", venueNme));
//                params.add(new BasicNameValuePair("venue_status", venueStts));
//                params.add(new BasicNameValuePair("user_image", imageString));
//                params.add(new BasicNameValuePair("postvisit_Status", venueStatus));
//                params.add(new BasicNameValuePair("last_visit", currentDateandTime));
//                params.add(new BasicNameValuePair("comment", venueCommend));
//
//                Log.e("parms post--",venueStatus);
//
//                // getting JSON Object
//                // Note that create product url accepts POST method
//                json = jsonParser.makeHttpRequest(ApplicationConstants.url_upload_image,
//                        "POST", params);
//
//                // check log cat fro response
//                Log.d("Create Response", json.toString());
//
//                // check for success tag
//                try {
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                    if (success == 1) {
//
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatusServerSync(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatusServerSync(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
//
//                        startActivity(new Intent(Stage1StatusActivity.this, ScheduledVenueActivity.class));
//                        finish();
//
//                    }
//
////                    else{
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatus(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatus(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
////
////                        startActivity(new Intent(UpdateVenueStatusActivity.this, ScheduledVenueActivity.class));
////                        finish();
////
////                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else {
//                pDialog.dismiss();
//                Looper.prepare();
//                Toast.makeText(Stage1StatusActivity.this,
//                        "Network is not Available. Please Check Your Internet Connection ",
//                        Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog once done
//            try {
//                if ((pDialog != null) && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//
//            } catch (final IllegalArgumentException e) {
//                // Handle or log or ignore
//            } catch (final Exception e) {
//                // Handle or log or ignore
//            } finally {
//                pDialog = null;
//            }
//        }
//    }


//    public static String encodeTobase64(Bitmap image)
//    {
//        Bitmap immagex=image;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
//
//        Log.e("LOOK", imageEncoded);
//        return imageEncoded;
//    }




//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if(data.getExtras().get("data")!=null) {
//
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//            String partFilename = currentDateFormat();
//            File outputFile = storeCameraPhotoInSDCard(bitmap, partFilename);
//
//            imageCount = Image.getInstance().getImageCount();
//            Log.e("imagecount", imageCount + "");
//
//            Log.e("outputFile====", outputFile.toString());
//
//            imageFilePath = outputFile.toString();
//
//            try {
//                int imagecount = Image.getInstance().getImageCount();
//                Log.e("array--", imagecount + "");
//
//                for (int i = imageCount; i <= imagecount; i++) {
//
//                    Image.getInstance().getImagePath().add(i, outputFile.toString());
////----------------------------
//
////                for(String imagePath : Image.getInstance().getImagePath()){
////
////                    Log.e("imagePath--",imagePath);
////
////                }
//
//
////-----------------------------
//
//                }
//                imageCount++;
//                Image.getInstance().setImageCount(imageCount);
//                Log.e("imageCount", imageCount + "");
//
//
//            } catch (Exception e) {
//                Log.e("eeee", e.toString());
//            }
//
//
//            //  ImageAttachment.getInstance().setImageBaseBytes(imageFilePath);
//
//            // display the image from SD Card to ImageView Control
////        imageFilename = "Site_" + partFilename + ".jpg";
////        //     Bitmap mBitmap = getImageFileFromSDCard(imageFilename);
////
////
//            imageBytes = getBytes(outputFile);
////
////
////
////        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).storeImage(venueNameEdittext.getText().toString(),
////                imageFilePath);
//
//
//            ImageView image = new ImageView(this);
//
//            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
//            image.setLayoutParams(layoutParams);
//
//            image.setImageBitmap(bmp);
//
//            //  image.setImageResource(R.drawable.YOUR_IMAGE_ID);
//
//            AlertDialog.Builder builder =
//                    new AlertDialog.Builder(this).
//                            setMessage("Cuptured image").
//                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).
//                            setView(image);
//            builder.create().show();
//        }
//    }

//    private byte[] getBytes(File filePath) {
//
//        Bitmap bm = BitmapFactory.decodeFile(filePath.toString());
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//        byte[] imageByte = baos.toByteArray();
//
//        return imageByte;
//    }


//    private File storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
//
//        File filepath= Environment.getExternalStorageDirectory();
//
//
//        File dir = new File(filepath.getAbsolutePath()+"/Purpleknot/Purpleknot Images/");
//
//        dir.mkdirs();
//
//        File outputFile = new File(dir, venueName + currentDate + ".jpg");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return outputFile;
//    }


//    private String currentDateFormat(){
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//        String  currentTimeStamp = dateFormat.format(new Date());
//        return currentTimeStamp;
//    }





}
