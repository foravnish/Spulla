package com.riseintech.spulla;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.riseintech.spulla.AsyncTaskService.Customer_supportAsync;
import com.riseintech.spulla.connection.InternetStatus;

public class CustomerSupportActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button comp_btn, sugg_btn, ptn_btn, joint_tm_btn, emapnel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customer Support");
        comp_btn = (Button) findViewById(R.id.comp_btn);
        sugg_btn = (Button) findViewById(R.id.sugg_btn);
        ptn_btn = (Button) findViewById(R.id.ptn_btn);
        joint_tm_btn = (Button) findViewById(R.id.joint_tm_btn);
        emapnel_btn = (Button) findViewById(R.id.emapnel_btn);

        comp_btn.setOnClickListener(this);
        sugg_btn.setOnClickListener(this);
        ptn_btn.setOnClickListener(this);
        joint_tm_btn.setOnClickListener(this);
        emapnel_btn.setOnClickListener(this);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.joint_tm_btn:
                showSupportDialog(CustomerSupportActivity.this, getString(R.string.join_team),
                        "Join our team and let’s together build a stronger brand.");
                break;

            case R.id.sugg_btn:
                showSupportDialog(CustomerSupportActivity.this, getString(R.string.suggestion),
                        "Join our team and let’s together build a stronger brand.");
                break;
            case R.id.ptn_btn:
                showSupportDialog(CustomerSupportActivity.this, getString(R.string.ptn_with),
                        "Join our team and let’s together build a stronger brand.");
                break;
            case R.id.emapnel_btn:
                showSupportDialog(CustomerSupportActivity.this, getString(R.string.empanel_with),
                        "Join our team and let’s together build a stronger brand.");
                break;
            case R.id.comp_btn:
                showSupportDialog(CustomerSupportActivity.this, getString(R.string.complaint),
                        "Join our team and let’s together build a stronger brand.");
                break;
        }
    }

    public void showSupportDialog(final Context context, final String title, String details) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.customersupportdialog);
        dialog.setCancelable(true);
        Button ok = (Button) dialog.findViewById(R.id.submit_cmplnt);
        final EditText cmplnt_details = (EditText) dialog.findViewById(R.id.cmpt_box);
        Button cancel_cmplt = (Button) dialog.findViewById(R.id.cancel_cmplt);
        TextView info = (TextView) dialog.findViewById(R.id.info);
        TextView titles = (TextView) dialog.findViewById(R.id.title);
        titles.setText(title);
        info.setText(details);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cmplnt_details.getText().toString().isEmpty()) {
                    cmplnt_details.setError(context.getString(R.string.empty_msg));
                } else if (!InternetStatus.isConnectingToInternet(context)) {
                } else {
                    new Customer_supportAsync(context, title, cmplnt_details.getText().toString()).execute();
                    dialog.cancel();
                }
            }
        });
        cancel_cmplt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
}
