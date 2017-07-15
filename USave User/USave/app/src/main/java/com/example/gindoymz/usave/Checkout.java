package com.example.gindoymz.usave;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gindoymz.usave.util.RadAPI;
import com.example.gindoymz.usave.util.VendorSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Checkout extends AppCompatActivity {

    private String customer_acc_no,vendorId;
    private EditText edtPin;
    private TextView txtAccountName,txtAccountNo,txtChange,txtConvenienceFee,txtTotalAmt;
    private Button btnProcessUSAVE;
    private Double change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        edtPin = (EditText) findViewById(R.id.edtPin);
        txtAccountName = (TextView) findViewById(R.id.txtAccountNameCheckout);
        txtAccountNo = (TextView) findViewById(R.id.txtAccountNoCheckout);
        txtChange = (TextView) findViewById(R.id.txtChangeCheckout);
        txtConvenienceFee = (TextView) findViewById(R.id.txtConvenienceFee);
        txtTotalAmt = (TextView) findViewById(R.id.txtTotalAmount);


        btnProcessUSAVE = (Button) findViewById(R.id.btnProcessUSave);
        Intent intent = getIntent();
        customer_acc_no = intent.getExtras().getString("customer_acc_no");
        change = intent.getExtras().getDouble("change");
        vendorId = intent.getExtras().getString("vendor_id");

        getAccountInfo(customer_acc_no,true);

        btnProcessUSAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fundTransfer(customer_acc_no,vendorId,""+change,edtPin.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fundTransfer(String accountNo, String vendorId, String amount, String pin) throws IOException {
        final ProgressDialog pd = new ProgressDialog(Checkout.this);
        pd.setMessage("Loading information..");
        pd.show();

        OkHttpClient client = new OkHttpClient();
        String postBody = "{\n" +
                "    \"accountNo\": \""+accountNo+"\",\n" +
                "    \"vendorId\": \""+vendorId+"\",\n" +
                "    \"amount\": \""+amount+"\",\n" +
                "    \"password\": \""+pin+"\"\n" +
                "}";
        Log.d("WEEEEEEW", postBody);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, postBody);

        Request req = new Request.Builder()
                .url(RadAPI.BASE_URL + RadAPI.FUND_TRANSFER)
                .post(body)
                .build();

        Log.d("DEB",body.toString());
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                pd.cancel();
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();
                pd.cancel();
                Checkout.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jObject = new JSONObject(resp);
                            String res = jObject.getString("result");
                            if (res.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Checkout.this);
                                dialogBuilder.setMessage("Successfully transferred funds");
                                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        Intent i = new Intent(Checkout.this,USaveMenu.class);
                                        i.putExtra("accountno", VendorSession.getInstance().getAccountNo());
                                        startActivity(i);
                                        //startActivity(new Intent(Checkout.this,USaveMenu.class));
                                    }
                                });

                                final AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.show();
                            } else {
                                Toast.makeText(Checkout.this, "Invalid PIN", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getAccountInfo(String accountNo, boolean showProgress) {
        final ProgressDialog pd = new ProgressDialog(Checkout.this);
        pd.setMessage("Loading information..");
        if(showProgress){
            pd.show();
        }

        OkHttpClient client = new OkHttpClient();
        String postBody = "{\n" +
                "    \"account\": \""+accountNo+"\",\n" +
                "    \"role\": \"user\"\n" +
                "}";
        Log.d("WEEEEEEW", postBody);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, postBody);

        Request req = new Request.Builder()
                .url(RadAPI.BASE_URL + RadAPI.ACCOUNT_INFO)
                .post(body)
                .build();

        Log.d("DEB",req.toString());
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                pd.cancel();
                //mySwipeRefreshLayout.setRefreshing(false);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();
                Log.d("DEB",resp);
                pd.cancel();

                Checkout.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray jsonArr = new JSONArray(resp);
                            JSONObject json = jsonArr.getJSONObject(0);

                            txtAccountName.setText(json.getString("account_name"));
                            txtAccountNo.setText(json.getString("account_no"));
                            txtChange.setText(formatMoney(""+change));
                            txtConvenienceFee.setText(getConvenienceFee(change));
                            txtTotalAmt.setText(getTotalAmount(change));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private String formatMoney(String current_balance) {
        double money = Double.parseDouble(current_balance);
        DecimalFormat dFormat = new DecimalFormat("####,###,###.00");
        return "PHP "+dFormat.format(money);

    }

    private String getConvenienceFee(Double change){
        Double percent = 0.03;
        Double convFee = change * percent;
        return formatMoney(""+convFee);
    }

    private String getTotalAmount(Double change){
        Double convFee = change * 0.03;
        return formatMoney(""+(change - convFee));
    }
}
