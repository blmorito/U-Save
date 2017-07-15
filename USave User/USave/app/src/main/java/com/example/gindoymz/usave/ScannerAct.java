package com.example.gindoymz.usave;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gindoymz.usave.util.RadAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import static com.example.gindoymz.usave.R.id.btnBalance;
//import static com.example.gindoymz.usave.R.id.btnUsave;

public class ScannerAct extends AppCompatActivity {
    private SurfaceView mySurfaceView;
    private QREader qrEader;
    private TextView textView;

    private EditText txtAmount;

    private Button btnProceed;

    private String accountNo;
    private String amount;
    private String pin;
    private String vendorId;

    private int my_request_code = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usavetransfer_layout);
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);
        textView = (TextView) findViewById(R.id.textView2);

        txtAmount = (EditText)findViewById(R.id.txtAmount);

        btnProceed = (Button)findViewById(R.id.btnProceed);

        LayoutInflater inflater = LayoutInflater.from(ScannerAct.this);
        View dialogView = inflater.inflate(R.layout.pin_layout, null);
        final EditText tPin = (EditText) dialogView.findViewById(R.id.txtPin);

        Intent intent = getIntent();
        vendorId = intent.getStringExtra("accountId");


        txtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtAmount.getText().toString().isEmpty());
                 btnProceed.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (tPin.toString().isEmpty())
                    Toast.makeText(ScannerAct.this, "Please input pin", Toast.LENGTH_LONG).show();
                else {
                    pin = tPin.getText().toString();
                    try {
                        fundTransfer(textView.getText().toString(),vendorId,txtAmount.getText().toString(),pin);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    my_request_code);

        }

        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(data);
                        if (!data.isEmpty()) {
                            accountNo = data;
                            txtAmount.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == my_request_code) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"CAMERA OKAY", Toast.LENGTH_SHORT).show();
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrEader.initAndStart(mySurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrEader.releaseAndCleanup();
    }

    private void fundTransfer(String accountNo, String vendorId, String amount, String pin) throws IOException {
        final ProgressDialog pd = new ProgressDialog(ScannerAct.this);
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
                ScannerAct.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jObject = new JSONObject(resp);
                            String res = jObject.getString("result");
                            if (res.equals("success")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ScannerAct.this);
                                dialogBuilder.setMessage("Successfully transferred funds");
                                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                      finish();
                                    }
                                });

                                final AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.show();
                            } else {
                                Toast.makeText(ScannerAct.this, "Invalid PIN", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
