package com.newera.pnwy.uhackbank;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.newera.pnwy.uhackbank.model.Account;
import com.newera.pnwy.uhackbank.model.AccountMapper;
import com.newera.pnwy.uhackbank.util.RadAPI;
import com.newera.pnwy.uhackbank.util.UhackAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnSignin, btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignin = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtUsername.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()){
                    try {
                        checkCredentials(edtUsername.getText().toString(),edtPassword.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    //pakyu
                }
            }
        });


//        textView = (TextView) findViewById(R.id.textView);
//        String accountNo = "102019316862";

//        try {
//            getAccountInfo(accountNo);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void checkCredentials(String username, String password) throws IOException{
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Checking credentials..");
        pd.show();

        OkHttpClient client = new OkHttpClient();
        String postBody = "{\n" +
                "    \"username\": \""+username+"\",\n" +
                "    \"password\": \""+password+"\"\n" +
                "}";
        Log.d("WEEEEEEW", postBody);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, postBody);

        Request req = new Request.Builder()
                .url(RadAPI.BASE_URL + RadAPI.LOG_IN)
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
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,resp,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

//    private void getAccountInfo(String accountInformation) throws IOException {
//
//        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
//        pd.setMessage("Getting account information");
//        pd.show();
//
//
//        OkHttpClient client = new OkHttpClient();
//        Request req = new Request.Builder()
//                .url(UhackAPI.UHACK_BASE_URL + UhackAPI.ACCOUNT_INFO + accountInformation)
//                .get()
//                .addHeader(UhackAPI.HEADER_CLIENTID, UhackAPI.CLIENT_ID)
//                .addHeader(UhackAPI.HEADER_CLIENTSECRET, UhackAPI.CLIENT_SECRET)
//                .addHeader("accept", "application/json")
//                .build();
//
//        client.newCall(req).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                call.cancel();
//                pd.cancel();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String myResponse = response.body().string();
//                pd.cancel();
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            JSONArray jsonArr = new JSONArray(myResponse);
//                            JSONObject json = jsonArr.getJSONObject(0);
//                            Account account = AccountMapper.map(json);
//                            textView.setText(account.toString());
//                        }catch(JSONException exception){
//                            exception.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//    }


}
