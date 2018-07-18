package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 11/16/2016.
 */

public class AddTournamentsAsync extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    public static JSONArray coachArray;
    String id, full_fee, book_amount;
    HashMap<String, String> params = new HashMap<>();
    Context context;

    public AddTournamentsAsync(Context context, String id, String full_fee, String book_amount) {
        this.id = id;
        this.full_fee = full_fee;
        this.book_amount = book_amount;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        // pDialog.setCancelable(false);
       // pDialog.show();
        params.put("t_id", id);
        params.put("full_fee", full_fee);
        params.put("p_id", MyPreference.loadUserid(context));
        params.put("plays_as", PlayerDetails.getPlay_As());
        params.put("book_amount", book_amount);
        //Log.e("params0", ": " + params);
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {
            JSONObject json = jsonParser.makeHttpRequest(
                    Api.Add_Tournament, "GET", params);
            //Log.d("JSON ", "------------" + json);
            if (json != null) {
                //Log.d("JSON result", json.toString());

                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(JSONObject json) {
       /* if (pDialog.isShowing())
            pDialog.dismiss();*/

        if (json != null) {
            try {
                if (json.getString("status").equalsIgnoreCase("1")) {
                    Toast.makeText(context, "Successfully added to tournament" , Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, TournamentDetailsActivity.class));
                } else {
                    Toast.makeText(context, "" + json.getString("msg"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context, "Some Problem in Add Tournament", Toast.LENGTH_SHORT).show();
        }

    }
}
