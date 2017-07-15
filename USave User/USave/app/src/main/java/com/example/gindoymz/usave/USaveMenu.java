package com.example.gindoymz.usave;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gindoymz.usave.util.RadAPI;

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

/**
 * Created by gindoymz on 15/07/2017.
 */

public class USaveMenu extends AppCompatActivity{

    private Button btnUsave, btnProcess;
    private Button btnBalance;
    private Button btnTransaction;
    private ImageButton imgButton;
    private TextView accountName, txtAccountNo, txtBalance;
    private EditText edtChange;
    private String accName;
    private String accNo;
    private String accId;
    private String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usavemenu_layout);

        //btnUsave = (Button)findViewById(R.id.btnUsave);
        //btnBalance = (Button)findViewById(R.id.btnBalance);
        btnTransaction = (Button)findViewById(R.id.btnViewSavings);
        accountName = (TextView)findViewById(R.id.txtAccountNameCheckout);
        txtAccountNo = (TextView) findViewById(R.id.txtAccountNo);
        txtBalance = (TextView)findViewById(R.id.txtCurrentBalance);
        //imgButton = (ImageButton) findViewById(R.id.imageButton) ;
        Intent intent = getIntent();
        accNo = intent.getStringExtra("accountno");

        edtChange = (EditText) findViewById(R.id.edtChange);
        btnProcess = (Button) findViewById(R.id.btnProcess);

        try {
            getAccount(accNo,"vendor");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(USaveMenu.this,"asdsd",Toast.LENGTH_LONG).show();
        }

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtChange.getText().toString().isEmpty()){
                    Intent intent = new Intent(USaveMenu.this,ScannerAct.class);
                    intent.putExtra("accountNo", accNo);
                    intent.putExtra("accountId", accId);
                    intent.putExtra("change",Double.parseDouble(edtChange.getText().toString()));
                    startActivity(intent);
                }
            }
        });


//        btnUsave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(USaveMenu.this,ScannerAct.class);
//                intent.putExtra("accountNo", accNo);
//                intent.putExtra("accountId", accId);
//                startActivity(intent);
//
//            }
//        });

//        imgButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(USaveMenu.this,ScannerAct.class);
//                intent.putExtra("accountNo", accNo);
//                intent.putExtra("accountId", accId);
//                startActivity(intent);
//            }
//        });

//        btnBalance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(USaveMenu.this);
//                dialogBuilder.setMessage(formatMoney(balance));
//                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
//
//                final AlertDialog alertDialog = dialogBuilder.create();
//                alertDialog.show();
//            }
//        });
    }

    private void getAccount(String accountNo, String role) throws IOException {
        final ProgressDialog pd = new ProgressDialog(USaveMenu.this);
        pd.setMessage("Loading information..");
        pd.show();

        OkHttpClient client = new OkHttpClient();
        String postBody = "{\n" +
                "    \"account\": \""+accountNo+"\",\n" +
                "    \"role\": \""+role+"\"\n" +
                "}";
        Log.d("WEEEEEEW", postBody);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, postBody);

        Request req = new Request.Builder()
                .url(RadAPI.BASE_URL + RadAPI.ACCOUNT_INFO)
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
                USaveMenu.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jArr = new JSONArray(resp);
                            JSONObject jObject = jArr.getJSONObject(0);
                            accName = jObject.getString("account_name");
                            accId = jObject.getString("userId");
                            balance = jObject.getString("avaiable_balance");
                            accountName.setText(accName);
                            txtBalance.setText(formatMoney(balance));
                            txtAccountNo.setText(jObject.getString("accountNo"));

                            accountName.setVisibility(View.VISIBLE);
                            btnUsave.setVisibility(View.VISIBLE);
                            btnBalance.setVisibility(View.VISIBLE);
                            btnTransaction.setVisibility(View.VISIBLE);
                        } catch (JSONException e){
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
}
