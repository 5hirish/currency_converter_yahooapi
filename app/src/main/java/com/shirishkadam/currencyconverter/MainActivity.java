package com.shirishkadam.currencyconverter;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // The Yahoo Finance API

    final static String yahoo_finance_api = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%3D%22USDINR%2C%20EURINR%2CCADINR%2CJPYINR%2CGBPINR%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni != null && ni.isConnected()){

            Toast.makeText(getApplicationContext(),"Exchange Rates Updating...!",Toast.LENGTH_SHORT).show();

            new GetExchageRates(MainActivity.this).execute(yahoo_finance_api);                                  // AsyncTask

        }else {
            Toast.makeText(getApplicationContext(),"Network Connection failed. Currency rates not updated!",Toast.LENGTH_LONG).show();
        }

        // Shared Preferences, the keys are standard currency symbols and same as the keys of JSON returned data.

        SharedPreferences sf = getSharedPreferences("Exchange_Rates", MODE_PRIVATE);
        final float gbp_rate = sf.getFloat("GBP/INR",0);
        final float usd_rate = sf.getFloat("USD/INR",0);
        final float eur_rate = sf.getFloat("EUR/INR",0);
        final float cad_rate = sf.getFloat("CAD/INR",0);
        final float jpn_rate = sf.getFloat("JPY/INR",0);

        if(gbp_rate == 0 || usd_rate == 0 || eur_rate == 0 || cad_rate == 0 || jpn_rate == 0){
            Toast.makeText(getApplicationContext(),"Obsolete Currency Rates...!",Toast.LENGTH_LONG).show();
        }

        final TextView in = (TextView)findViewById(R.id.in_text);
        final EditText us = (EditText)findViewById(R.id.us_text);
        final EditText ca = (EditText)findViewById(R.id.ca_text);
        final EditText gb = (EditText)findViewById(R.id.gb_text);
        final EditText ge = (EditText)findViewById(R.id.ge_text);
        final EditText jp = (EditText)findViewById(R.id.jp_text);

        final Currency currency_inr = Currency.getInstance("INR");
        in.setText(currency_inr.getSymbol(Locale.ENGLISH) +" 00.00");

        us.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()!=0){                                                           // To eliminate crashes when EditText being erased to zero
                in.setText(currency_inr.getSymbol(Locale.ENGLISH) +" "+Float.parseFloat(us.getText().toString()) * usd_rate);
                }
            }
        });

        ca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    in.setText(currency_inr.getSymbol(Locale.ENGLISH) +" " + Float.parseFloat(ca.getText().toString()) * cad_rate);
                }
            }
        });

        gb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    in.setText(currency_inr.getSymbol(Locale.ENGLISH) +" " + Float.parseFloat(gb.getText().toString()) * gbp_rate);
                }
            }
        });

        ge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    in.setText(currency_inr.getSymbol(Locale.ENGLISH) +" " + Float.parseFloat(ge.getText().toString()) * eur_rate);
                }
            }
        });

        jp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    in.setText(currency_inr.getSymbol(Locale.ENGLISH) +" " + Float.parseFloat(jp.getText().toString()) * jpn_rate);
                }
            }
        });


    }
}
