package com.riseintech.spulla.utils;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.riseintech.spulla.GPSTracker;
import com.riseintech.spulla.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by user on 10/27/2016.
 */

public class Util {
    public static String PUSH_NOTIFICATION = "df";
    public static boolean isSportBgChn;
    public static int NOTIFICATION_ID = 121;
    public static String API_KEY = "AIzaSyAN1vRVTVvXPAeyUn80ZGarj2oIZzna33g";
    public static int comment_api_call;
    public static String Sender_ID = "0";
    public static String Sender_Name = "not available";
    public static String Sender_Img = "sd";
    public static String Noti_Msg = "sd";
    public static int Noti_Bg;
    public static int ChatOn;
    public static int LoadPro_Image;
    public static int ChatDtl_On;
    public static String Get_Rquest = "0";
    public static int Get_Rquest_InBg;
    public static int Show_Coach_Dtls;
    public static int Get_Comments = 0;
    public static int User_Like = 5;
    public static int Total_Like = 0;
    public static int Set_Total_Like = 0;
    public static int PageRefreshDismis=0;
    public static boolean isAddressSave;
    public static boolean Add_Qty;

    public static  int FindPlayerAsynccountingVaue=0;

    public  static  String FindPlayerAsynccounting;

    public static String getFindPlayerAsynccounting() {
        return FindPlayerAsynccounting;
    }

    public static void setFindPlayerAsynccounting(String findPlayerAsynccounting) {
        FindPlayerAsynccounting = findPlayerAsynccounting;
    }

    public static String getPathFromUri(final Context context, final Uri uri) {
        boolean isAfterKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        ///storage/emulated/0/Download/Amit-1.pdf
        Log.e("Uri Authority ", "uri:" + uri.getAuthority());
        if (isAfterKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(
                    uri.getAuthority())) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    return "/stroage/" + type + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(
                    uri.getAuthority())) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(
                    uri.getAuthority())) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                contentUri = MediaStore.Files.getContentUri("external");
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {//MediaStore
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {
                MediaStore.Files.FileColumns.DATA
        };
        try {
            cursor = context.getContentResolver().query(
                    uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int cindex = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getString(cindex);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String imagename(Context context, Uri currImageURI) {
        String displayName = "";
        File file = new File(currImageURI.toString());
        String uriString = currImageURI.toString();
        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(currImageURI, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.e("display name content", ": " + displayName);
                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            displayName = file.getName();
            Log.e("display name file", ": " + displayName);
        }
        Log.e("display name ", ": " + displayName);
        return displayName;
    }

    // show image via image url
    public static void showImage(Context context, String url, ImageView imageView) {
        if (!url.isEmpty() && url != null) {
            Picasso.with(context).load(url).placeholder(R.color.colorPrimary).error(R.color.gr).into(imageView);
        }
    }
//////////////////////get latlng from jsonObject

    public static LatLng getLatLong(JSONObject jsonObject) {

        Double lon = new Double(0);
        Double lat = new Double(0);

        try {

            lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (Exception e) {
            e.printStackTrace();

        }

        return new LatLng(lat, lon);
    }

    ///////get Lat long from address
    public static LatLng getLocationFromAddress(Context context, String address) {
       /* StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ", "%20");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
*/

        //   below code get latlng from Geocoder
        Log.e("Address", ": " + address);
        Geocoder coder = new Geocoder(context);
        List<Address> address1;
        LatLng p1 = null;
        try {
            address1 = coder.getFromLocationName(address, 5);
            if (address == null) {
                p1 = new LatLng(0, 0);
                return p1;
            }
            Address location = address1.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {
            ex.printStackTrace();
            p1 = new LatLng(0, 0);

        }
        //////////////////////////
        return p1;
    }

    /////get Address from lat n long
    public static String getAddress(Context context, String lat, String lon) {
        if (lat.isEmpty() || lon.isEmpty()) {
            lat = "0";
            lon = "0";
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String ret = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    Double.parseDouble(lat), Double.parseDouble(lon), 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            " ");
                }
                ret = strReturnedAddress.toString();
            } else {
                ret = context.getString(R.string.notavail);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ret = context.getString(R.string.notavail);
        }
        Log.e("get Address", " " + ret);
        return ret;
    }

    public static void showInfoAlertDialog(final Context context, String title, String details) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.info);
        dialog.setCancelable(true);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        LinearLayout fp_lin = (LinearLayout) dialog.findViewById(R.id.fp_lin);
        fp_lin.setVisibility(View.GONE);
        TextView info = (TextView) dialog.findViewById(R.id.info);
        info.setVisibility(View.GONE);
        TextView titles = (TextView) dialog.findViewById(R.id.title);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ok.getLayoutParams();
        if (title.equalsIgnoreCase("FindPlayer Info")) {
            fp_lin.setVisibility(View.VISIBLE);
            params.addRule(RelativeLayout.BELOW, R.id.fp_lin);

        } else {
            info.setVisibility(View.VISIBLE);
            params.addRule(RelativeLayout.BELOW, R.id.info);

        }
        titles.setText(title);
        info.setText(details);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    public static void showFullImageDialog(Context context, String imageurl, String titlename) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.showfullimage);
        ImageView back_img = (ImageView) dialog.findViewById(R.id.back_img);
        ImageView fact_image = (ImageView) dialog.findViewById(R.id.fact_image);
        Util.showImage(context, imageurl, fact_image);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(fact_image);
        //photoViewAttacher.onDrag(2,2);
        photoViewAttacher.update();
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(titlename);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static String currentDateTime() {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return formattedDate;
    }

    //This method will return current timestamp
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public static void getCurrentLatLong(Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);
        if (gpsTracker.canGetLocation()) {
            PlayerDetails.current_lat = gpsTracker.getLatitude();
            PlayerDetails.current_long = gpsTracker.getLongitude();
        }
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }



    // get hash key for facebook
    public static String printKeyHash(AppCompatActivity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());
            Log.e("Package Name=", "true");

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


}
