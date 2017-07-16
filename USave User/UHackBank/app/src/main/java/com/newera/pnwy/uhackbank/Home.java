package com.newera.pnwy.uhackbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newera.pnwy.uhackbank.util.RadAPI;

import net.glxn.qrgen.android.QRCode;

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

public class Home extends AppCompatActivity {
    ImageView imageView;
    String username, accountNo;
    TextView txtAccountName,txtAccountNo,txtCurrentBalance;
    SwipeRefreshLayout mySwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        accountNo = intent.getExtras().getString("accountNo");

        txtAccountName = (TextView) findViewById(R.id.txtAccountName);
        txtAccountNo = (TextView) findViewById(R.id.txtAccountNo);
        txtAccountNo.setText(accountNo);
        txtCurrentBalance = (TextView) findViewById(R.id.txtCurrentBalance);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAccountInfo(accountNo,false);
            }
        });

        getAccountInfo(accountNo,true);
        //Toast.makeText(Home.this,username+" "+accountNo,Toast.LENGTH_SHORT).show();

        imageView = (ImageView) findViewById(R.id.imgQRCode);
        Bitmap bitmap = QRCode.from(accountNo).bitmap();
        imageView.setImageBitmap(bitmap);
    }

    private void getAccountInfo(String accountNo, boolean showProgress) {
        final ProgressDialog pd = new ProgressDialog(Home.this);
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
                .url(RadAPI.BASE_URL + RadAPI.GET_ACCOUNT)
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

                Home.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mySwipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONArray jsonArr = new JSONArray(resp);
                            JSONObject json = jsonArr.getJSONObject(0);
                            txtAccountName.setText(json.getString("account_name"));
                            txtCurrentBalance.setText(formatMoney(json.getString("current_balance")));
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
}
