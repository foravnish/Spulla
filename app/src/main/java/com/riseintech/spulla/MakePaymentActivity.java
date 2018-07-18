package com.riseintech.spulla;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MakePaymentActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar_payment;
    TextView cancel, contine,price_dtls,amtpay_dtls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        inIt();
    }

    private void inIt() {
        toolbar_payment = (Toolbar) findViewById(R.id.toolbar_payment);
        toolbar_payment.setTitle("Payment Details");
        //*add toolbar to actionbar*//
        setSupportActionBar(toolbar_payment);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contine = (TextView) findViewById(R.id.contine);
        price_dtls = (TextView) findViewById(R.id.price_dtls);
        amtpay_dtls = (TextView) findViewById(R.id.amtpay_dtls);
        cancel = (TextView) findViewById(R.id.cancel);

        amtpay_dtls.setText(getString(R.string.Rs)+" "+BuyNow.payable_amt);
        price_dtls.setText(getString(R.string.Rs)+" "+BuyNow.payable_amt);

        cancel.setOnClickListener(this);
        contine.setOnClickListener(this);

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
            case R.id.cancel:
                finish();
                break;
            case R.id.contine:

                break;
        }

    }
}
