package com.riseintech.spulla;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.riseintech.spulla.connection.AndroidMultiPartEntity;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.inputcontrols.Config;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;




/**
 * Created by adm on 8/12/2016.
 */
public class UpgradeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    TextView txtimgpath;
    Button btnimgUpload,btnUpgrade;
    ImageView profileimgView;
    EditText editSchoolView,editcollegeView,editworkView,editarea1View,editarea2View,editarea3View;

    private ProgressDialog pDialog;
    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;
    long totalSize = 0;
    private String imagepath=null;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    boolean isImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_upgrade);

        initUpgrade();


        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(isImageUrl){

                  /* new UploadFileToServer().execute();*/
               new UpgradePage().execute();
                }
                else
                    Toast.makeText(UpgradeActivity.this,"Please Upload Profile picture",Toast.LENGTH_SHORT).show();

            }
        });


        btnimgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(UpgradeActivity.this);
                showFileChooser();

            }
        });

    }

    private void initUpgrade(){

        txtimgpath= (TextView) findViewById(R.id.s_txtpicpath);
        profileimgView= (ImageView) findViewById(R.id.s_pro_pic);
        btnimgUpload= (Button) findViewById(R.id.s_btnupload);
        btnUpgrade= (Button) findViewById(R.id.s_btnupgrade);
        editSchoolView= (EditText) findViewById(R.id.u_school);
        editcollegeView= (EditText) findViewById(R.id.u_colz);
        editworkView= (EditText) findViewById(R.id.u_wplace);
        editarea1View= (EditText) findViewById(R.id.u_area1);
        editarea2View= (EditText) findViewById(R.id.u_area2);
        editarea3View= (EditText) findViewById(R.id.u_area3);


    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    class UpgradePage extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

       /* String schoolName=editSchoolView.getText().toString();
        String collegeName=editcollegeView.getText().toString();
        String workplace=editworkView.getText().toString();
        String playingArea1=editarea1View.getText().toString();
        String playingArea2=editarea2View.getText().toString();
        String playingArea3=editarea3View.getText().toString();*/

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(UpgradeActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
           /* params.put("image_name", " ");
            params.put("school", schoolName);
            params.put("college",collegeName);
            params.put("workplace", workplace);
            params.put("playing_area1", playingArea1);
            params.put("playing_area2",playingArea2);
            params.put("playing_area3", playingArea3);*/
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest("http://cricapi.com/api/cricket/", "GET", params);
                //Log.d("upload ", "------------" + json);
                if (json != null) {
                   // Log.d("upload result", json.toString());

                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(JSONObject json) {
            String success = "";
            String message = "";
            String data = "";
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (json != null) {
                try {

                    JSONArray ja = json.getJSONArray("data");

                    for(int i=0; i < ja.length(); i++) {

                        JSONObject jsonObject = ja.getJSONObject(i);

                        // int id = Integer.parseInt(jsonObject.optString("id").toString());
                        String title = jsonObject.getString("title");
                        String url = jsonObject.getString("description");

                        data += "Blog Number " + (i + 1) + " \n Blog Name= " + title + " \n URL= " + url + " \n\n\n\n ";
                    }
                    //Toast.makeText(UpgradeActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                    //Log.d("message login>>>>",data);

                   /* success = json.getString("status");
                    message = json.getString("msg");
                    Toast.makeText(UpgradeActivity.this, "" + success, Toast.LENGTH_SHORT).show();
                    Log.d("message login>>>>",message);*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (success.equals("1")) {
                Intent intent;




                intent = new Intent(UpgradeActivity.this, UpgradeActivity.class);
                startActivity(intent);



            } else {
                Toast.makeText(UpgradeActivity.this, "" + message, Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
         Uri filePath = data.getData();
            imagepath = getPath(filePath);
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profileimgView.setImageBitmap(bitmap);
                txtimgpath.setText("Uploading file path:" +imagepath);
                isImageUrl=true;


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /**
     * Uploading the file to server
     * */
   /* private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Bitmap.Config.FILE_UPLOAD_URL);

            try {
               *//* AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });*//*

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }*/




    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




    private class UploadFileToServer1 extends AsyncTask<Void, Integer, String> {

        String schoolName=editSchoolView.getText().toString();
        String collegeName=editcollegeView.getText().toString();
        String workplace=editworkView.getText().toString();
        String playingArea1=editarea1View.getText().toString();
        String playingArea2=editarea2View.getText().toString();
        String playingArea3=editarea3View.getText().toString();



        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
           // progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
           // progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
           // progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(imagepath);

                // Adding file data to http body
                entity.addPart("image_name", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("school", new StringBody("dd"));
                entity.addPart("college", new StringBody("ss"));
                entity.addPart("workplace", new StringBody("kk"));
                entity.addPart("playing_area1", new StringBody("nn"));
                entity.addPart("playing_area2", new StringBody("hh"));
                entity.addPart("playing_area3", new StringBody("kl"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e("Upgrade>>>", "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }



    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        long totalSize = 0;
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            pDialog = new ProgressDialog(UpgradeActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible

            // updating percentage value

            if (progress[0] == 99){
                Toast.makeText(UpgradeActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
            }
            //Log.i("Percentage %%%%%%%%%% ", "-------- " + String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                File sourceFile = new File(imagepath);

                // Adding file data to http body
                entity.addPart("image_name", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("school", new StringBody("adc"));
                entity.addPart("college",new StringBody("high"));
                entity.addPart("workplace",new StringBody("high"));
                entity.addPart("playing_area1",new StringBody("high"));
                entity.addPart("playing_area2",new StringBody("high"));
                entity.addPart("playing_area3",new StringBody("high"));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
           // Log.d("camera >>>Activity","" +responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Log.e("RESPONSE", "Response from server: " + result);

            super.onPostExecute(result);
        }
    }




}
