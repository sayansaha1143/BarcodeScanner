package com.example.sayan.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sayan on 11/29/2016.
 */

public class MainActivity extends Activity{

    TextView barcodeResult;
    TextView txt;
    private static String url = "";

    private static final String TAG_ID = "item_name";
    private static final String API_ID = "6c465ba0";
    private static final String API_KEY = "8420ffb98a579cea87963df2e2fc5458";
    private static String UPC = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        barcodeResult = (TextView)findViewById(R.id.scan_barcode_result);
        txt = (TextView) findViewById(R.id.txt1);


    }

    public void scanBarcode(View view){
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if (data != null){
                    final Barcode barcode = data.getParcelableExtra("barcodes");
                    UPC = barcode.displayValue;
                    barcodeResult.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            barcodeResult.setText(    // Update the TextView
                                    barcode.displayValue
                            );
                        }
                    });

                    JSONParser jParser = new JSONParser();
                    url = "https://api.nutritionix.com/v1_1/item?upc="+ UPC +"&appId="+ API_ID +"&appKey=" + API_KEY;
                    // Getting JSON from URL
                    JSONObject json = jParser.getJSONFromUrl(url);

                    try {
                        String id = json.getString(TAG_ID);
                        txt.setText(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    barcodeResult.setText("No Barcode Found");
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}