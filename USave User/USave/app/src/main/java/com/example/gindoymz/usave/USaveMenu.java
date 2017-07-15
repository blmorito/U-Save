package com.example.gindoymz.usave;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gindoymz.usave.util.RadAPI;
import com.google.android.gms.vision.text.Text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.vendor;
import static com.example.gindoymz.usave.R.id.login;
import static com.example.gindoymz.usave.R.id.username;

/**
 * Created by gindoymz on 15/07/2017.
 */

public class USaveMenu extends AppCompatActivity{

    private Button btnUsave;
    private Button btnBalance;
    private Button btnTransaction;

    private TextView accountName;

    private String accName;
    private String accNo;
    private String accId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usavemenu_layout);

        btnUsave = (Button)findViewById(R.id.btnUsave);
        btnBalance = (Button)findViewById(R.id.btnBalance);
        btnTransaction = (Button)findViewById(R.id.btnTransactions);
        accountName = (TextView)findViewById(R.id.vendorName);

        Intent intent = getIntent();
        accNo = intent.getStringExtra("accountno");


        try {
            getAccount(accNo,"vendor");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(USaveMenu.this,"asdsd",Toast.LENGTH_LONG).show();
        }

        btnUsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(USaveMenu.this,ScannerAct.class);
                intent.putExtra("accountNo", accNo);
                intent.putExtra("accountId", accId);
                startActivity(intent);

            }
        });
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
                            Toast.makeText(USaveMenu.this,resp,Toast.LENGTH_LONG).show();
                            accName = jObject.getString("account_name");
                            accId = jObject.getString("userId");
                            accountName.setText(accName);


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
}
