package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.purpleknot1.DTO.ImageAttachmentDto;
import com.purpleknot1.DTO.ImageDto;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Image;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class UpdateVenueStatusActivity1 extends Activity {

    String imageFilePath;
    String imageFilename;
    byte[] imageBytes;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    NetworkConnection networkConnection;

    EditText venueNameEdittext,venuecommendEdittext;

    String venueNme;

    File storageDirectory;

    Spinner scheduleVenueStatusSpinner;

    CheckBox notInterestCheckbox,interventionCheckbox,introductoryCheckbox,friendlyCheckbon;

    String venueName,scheduleVenueStatus;

    String imageBase64Bytes;

    HashSet<String> hashSet = new HashSet();

    ArrayList<String> ar = new ArrayList<String>();



    ImageDto imageDto=new ImageDto();

    ArrayList<String> alist = new ArrayList<>();

    ImageAttachmentDto imageAttachmentDto=new ImageAttachmentDto();
    String venueStatus,venueDate;

    boolean isChecked;

    String currentDateandTime;

    String venueCommend;

    TextView venueStatusTextview;

    ImageView backImageview;

    Spinner stateSpinner,citySpinner;



    int imageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_venue_status);

        scheduleVenueStatusSpinner=(Spinner)findViewById(R.id.schedulevenuestatus_spinner);

        venueNameEdittext=(EditText)findViewById(R.id.schedulevenue_name_edittext);

        venuecommendEdittext=(EditText)findViewById(R.id.venuecommend_edittext);

        notInterestCheckbox=(CheckBox)findViewById(R.id.notinterest_checkbox);

        interventionCheckbox = (CheckBox)findViewById(R.id.intervention_checkbox);

        introductoryCheckbox = (CheckBox)findViewById(R.id.introductory_checkbox);

        friendlyCheckbon = (CheckBox)findViewById(R.id.friendly_checkbox);

        venueStatusTextview=(TextView)findViewById(R.id.venuestatus_textview);

        backImageview=(ImageView)findViewById(R.id.updatestatus_back_imageview);

      //  stateSpinner = (Spinner)findViewById(R.id.update_venue_state_spinner);

      //  citySpinner =  (Spinner)findViewById(R.id.update_venue_city_spinner);

        networkConnection = new NetworkConnection(this);

        storageDirectory = getExternalFilesDir("");

        storageDirectory.delete();

        loadSpinner();

        imageAttachmentDto=new ImageAttachmentDto();


        backImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UpdateVenueStatusActivity1.this, ScheduledVenueActivity.class);
                startActivity(intent);
                finish();

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String venueName = extras.getString("venueName");
            venueStatus = extras.getString("venueStatus");
            venueDate = extras.getString("venueDate");

            Log.e("post visit",venueStatus);
            Log.e("post venueDate",venueDate);

            venueNameEdittext.setText(venueName);

            if(venueStatus!="null") {

                scheduleVenueStatusSpinner.setSelection(getIndex(scheduleVenueStatusSpinner, venueStatus));
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentDateandTime = sdf.format(new Date());

        Log.e("currentDateandTime",currentDateandTime);

        notInterestCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isChecked = notInterestCheckbox.isChecked();

                if (isChecked) {

                    venueStatusTextview.setVisibility(View.GONE);
                    scheduleVenueStatusSpinner.setVisibility(View.GONE);
                } else {

                    venueStatusTextview.setVisibility(View.VISIBLE);
                    scheduleVenueStatusSpinner.setVisibility(View.VISIBLE);
                }

            }
        });

        notInterestCheckbox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        interventionCheckbox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        introductoryCheckbox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        friendlyCheckbon.setOnCheckedChangeListener(new myCheckBoxChnageClicker());

    }


    class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub

            // Toast.makeText(CheckBoxCheckedDemo.this, &quot;Checked =&gt; &quot;+isChecked, Toast.LENGTH_SHORT).show();

            if(isChecked) {
                if(buttonView==notInterestCheckbox) {
                    scheduleVenueStatus = notInterestCheckbox.getText().toString();
                }

                if(buttonView==interventionCheckbox) {
                    scheduleVenueStatus = interventionCheckbox.getText().toString();
                }

                if(buttonView==introductoryCheckbox) {
                    scheduleVenueStatus = introductoryCheckbox.getText().toString();
                }

                if(buttonView==friendlyCheckbon) {
                    scheduleVenueStatus = friendlyCheckbon.getText().toString();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void createVenueButtonClick(View view){

        if (networkConnection.isNetworkAvailable()) {

            venueName=venueNameEdittext.getText().toString();

            if(notInterestCheckbox.isChecked()){
                scheduleVenueStatus = notInterestCheckbox.getText().toString();
            }
            else if(interventionCheckbox.isChecked()) {
                scheduleVenueStatus = interventionCheckbox.getText().toString();
            }
            else if(introductoryCheckbox.isChecked()) {
                scheduleVenueStatus = introductoryCheckbox.getText().toString();
            }

            else if(friendlyCheckbon.isChecked()) {
                scheduleVenueStatus = friendlyCheckbon.getText().toString();
            }

            else
            {
                scheduleVenueStatus = scheduleVenueStatusSpinner.getSelectedItem().toString();
            }

            if(venueName==null)
            {
                Log.e("venueName((((",venueName);

            }
            else if(scheduleVenueStatus==null)
            {
                Log.e("scheduleVenueStatus((((",scheduleVenueStatus);
            }

            else{
                String venueImagePath;
                Log.e("venueName))))",venueName);
                Log.e("scheduleVenueStatus))))", scheduleVenueStatus);

                if(Image.getInstance().getImagePath().size()!=0) {

                    for (String imagePath : Image.getInstance().getImagePath()) {

                        Log.e("imagePathhhhh --", imagePath);

                        if (imagePath!=null) {

                            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                            venueCommend=venuecommendEdittext.getText().toString();
                            String imageString = encodeTobase64(myBitmap);
                            new UpdateVenueStatus().execute(imageString);
                        }
                    }
                }
                else{
                    venueCommend=venuecommendEdittext.getText().toString();
                    new UpdateVenueStatus1().execute();
                }
            }
        }
        else{

            Toast.makeText(UpdateVenueStatusActivity1.this,
                    "Network is not Available. Please Check Your Internet Connection ",
                    Toast.LENGTH_SHORT).show();
        }
    }

    class UpdateVenueStatus1 extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateVenueStatusActivity1.this);
            pDialog.setMessage("Upload the Image...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            JSONObject json = null;

            if (networkConnection.isNetworkAvailable()) {
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", venueName));
                params.add(new BasicNameValuePair("venue_status", scheduleVenueStatus));
                params.add(new BasicNameValuePair("postvisit_Status", venueStatus));
                params.add(new BasicNameValuePair("last_visit", currentDateandTime));
                params.add(new BasicNameValuePair("comment", venueCommend));

                Log.e("parms post--", venueStatus);

                json = jsonParser.makeHttpRequest(ApplicationConstants.url_upload_withoutimage,
                        "POST", params);

                Log.d("Create Response", json.toString());

                try {
                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                    if (success == 1) {

                        startActivity(new Intent(UpdateVenueStatusActivity1.this, ScheduledVenueActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdateVenueStatusActivity1.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                pDialog = null;
            }
        }
    }

    class UpdateVenueStatus extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateVenueStatusActivity1.this);
            pDialog.setMessage("Upload the Image...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            JSONObject json = null;
            if (networkConnection.isNetworkAvailable()) {
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");

                String imageString=args[0];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", venueName));
                params.add(new BasicNameValuePair("venue_status", scheduleVenueStatus));
                params.add(new BasicNameValuePair("user_image", imageString));
                params.add(new BasicNameValuePair("postvisit_Status", venueStatus));
                params.add(new BasicNameValuePair("last_visit", currentDateandTime));
                params.add(new BasicNameValuePair("comment", venueCommend));

                Log.e("parms post--",venueStatus);

                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_upload_image,
                        "POST", params);

                // check log cat fro response
			Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                    if (success == 1) {

//                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
//                                updateVenueStatusServerSync(venueNme, scheduleVenueStatus);
//
//                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
//                                getVenueIdbyName(venueNme);
//
//                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
//                                updateVenueScheduleStatusServerSync(venueId);
//
//                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);

                        startActivity(new Intent(UpdateVenueStatusActivity1.this, ScheduledVenueActivity.class));
                        finish();

                    }

//                    else{
//
//                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
//                                updateVenueStatus(venueNme, scheduleVenueStatus);
//
//                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
//                                getVenueIdbyName(venueNme);
//
//                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
//                                updateVenueScheduleStatus(venueId);
//
//                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
//
//                        startActivity(new Intent(UpdateVenueStatusActivity.this, ScheduledVenueActivity.class));
//                        finish();
//
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdateVenueStatusActivity1.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            try {
                if ((pDialog != null) && pDialog.isShowing()) {
            pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                pDialog = null;
            }
        }
    }

    public void captureImageviewClick(View view){




        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);




    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


        if(data.getExtras().get("data")!=null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            String partFilename = currentDateFormat();
            File outputFile = storeCameraPhotoInSDCard(bitmap, partFilename);

            imageCount = Image.getInstance().getImageCount();
            Log.e("imagecount", imageCount + "");

            Log.e("outputFile====", outputFile.toString());

            imageFilePath = outputFile.toString();

            try {
                int imagecount = Image.getInstance().getImageCount();
                Log.e("array--", imagecount + "");

                for (int i = imageCount; i <= imagecount; i++) {

                    Image.getInstance().getImagePath().add(i, outputFile.toString());
//----------------------------

//                for(String imagePath : Image.getInstance().getImagePath()){
//
//                    Log.e("imagePath--",imagePath);
//
//                }


//-----------------------------

                }
                imageCount++;
                Image.getInstance().setImageCount(imageCount);
                Log.e("imageCount", imageCount + "");


            } catch (Exception e) {
                Log.e("eeee", e.toString());
            }


            //  ImageAttachment.getInstance().setImageBaseBytes(imageFilePath);

            // display the image from SD Card to ImageView Control
//        imageFilename = "Site_" + partFilename + ".jpg";
//        //     Bitmap mBitmap = getImageFileFromSDCard(imageFilename);
//
//
            imageBytes = getBytes(outputFile);
//
//
//
//        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).storeImage(venueNameEdittext.getText().toString(),
//                imageFilePath);


            ImageView image = new ImageView(this);

            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
            image.setLayoutParams(layoutParams);

            image.setImageBitmap(bmp);

            //  image.setImageResource(R.drawable.YOUR_IMAGE_ID);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this).
                            setMessage("Cuptured image").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).
                            setView(image);
            builder.create().show();
        }
    }

    private byte[] getBytes(File filePath) {

        Bitmap bm = BitmapFactory.decodeFile(filePath.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] imageByte = baos.toByteArray();

        return imageByte;
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    private File storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){

        File filepath= Environment.getExternalStorageDirectory();


        File dir = new File(filepath.getAbsolutePath()+"/Purpleknot/Purpleknot Images/");

        dir.mkdirs();

        File outputFile = new File(dir, venueName + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }

    private String currentDateFormat(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void loadSpinner(){


        List<String> list = new ArrayList<String>();
        list.add("Select Status");

        list.add("To be initiated");
        list.add("Initiated");
        list.add("Manager Met");
        list.add("Demo Required");
        list.add("Demo Provided to manager");
        list.add("Owner Met");
        list.add("Commercial Negotiation in Progress");
        list.add("Details Collected for Agreement");
        list.add("Template of Agreement Provided");
        list.add("Agreement Accepted");

        list.add("Agreement Sent By HQ");
        list.add("Agreement Submitted to Venue Partner");
        list.add("Signed Agreement Collected from Venue Partner");
        list.add("Agreement Sent to HQ");
//        list.add("Agreement Submitted to Venue Partner");
//        list.add("Agreement Dispatched to HQ");
//
//        list.add("Agreement Received at HQ");
//        list.add("Wedding Data Collection Initiated");
//        list.add("Wedding Data Collected for Conversion");
//        list.add("Agreement Signed and Received from Venue Partner");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scheduleVenueStatusSpinner.setAdapter(dataAdapter);
    }
}
