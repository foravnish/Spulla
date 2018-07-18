package com.riseintech.spulla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.riseintech.spulla.AsyncTaskService.AddTournamentsAsync;
import com.riseintech.spulla.connection.InternetStatus;

/**
 * Created by Risein Tech on 10/25/2016.
 */

public class BookCoach extends AppCompatActivity implements View.OnClickListener {
    TextView amount_txv;
    Button pay_now;
    String fee_amount;
    String fee_type;
    String id;
    String full_fees;
    Toolbar toolbar_paynow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookcoach);
        toolbar_paynow = (Toolbar) findViewById(R.id.toolbar_paynow);
        toolbar_paynow.setTitle("Pay Now");
        setSupportActionBar(toolbar_paynow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pay_now = (Button) findViewById(R.id.pay_now);
        amount_txv = (TextView) findViewById(R.id.amount_txv);
        fee_type = getIntent().getStringExtra("fee_type");
        fee_amount = getIntent().getStringExtra("fee_amount");
        id = getIntent().getStringExtra("id");
        full_fees = getIntent().getStringExtra("full_fees");

        amount_txv.setText(fee_amount);
        pay_now.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_now:
                if (fee_type.equalsIgnoreCase("tournament fee")
                        && InternetStatus.isConnectingToInternet(BookCoach.this)) {
                    new AddTournamentsAsync(BookCoach.this, id, full_fees, fee_amount).execute();
                    finish();
                } else {
                    finish();
                }
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
