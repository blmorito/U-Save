package com.newera.pnwy.uhackbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Path;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.newera.pnwy.uhackbank.util.RadAPI;

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

public class OpenAccount extends AppCompatActivity {

    EditText edtAccountName, edtUsername, edtPassword, edtEmail;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_account);
        setTitle("Open a New Account");

        edtAccountName = (EditText) findViewById(R.id.edtAccountName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnSignUp = (Button) findViewById(R.id.btnOpenAccount);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtAccountName.getText().toString().isEmpty() ||
                        edtUsername.getText().toString().isEmpty() ||
                        edtPassword.getText().toString().isEmpty() ||
                        edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(OpenAccount.this,"Please fill up all the details",Toast.LENGTH_SHORT).show();
                }else{
                    openAccount(edtAccountName.getText().toString(),edtUsername.getText().toString(),
                            edtPassword.getText().toString(),edtEmail.getText().toString());
                }

            }
        });




    }

    private void openAccount(String accountName, String username, String password, String email) {
        final ProgressDialog pd = new ProgressDialog(OpenAccount.this);
        pd.setMessage("Creating account..");
        pd.show();

        OkHttpClient client = new OkHttpClient();
        String postBody = "{\n" +
                "    \"accountName\": \""+accountName+"\",\n" +
                "    \"userName\": \""+username+"\",\n" +
                "    \"password\": \""+password+"\",\n" +
                "    \"email\": \""+email+"\",\n" +
                "    \"role\": \"user\"\n" +
                "}";
        Log.d("WEEEEEEW", postBody);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, postBody);

        Request req = new Request.Builder()
                .url(RadAPI.BASE_URL + RadAPI.CREATE_ACCOUNT)
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
                OpenAccount.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(OpenAccount.this,resp,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject json = new JSONObject(resp);
                            if(json.getString("result")!= null && json.getString("result").equals("success")){
                                Intent intent = new Intent(OpenAccount.this, Home.class);
                                intent.putExtra("username", json.getString("userName"));
                                intent.putExtra("accountNo", json.getString("accountNo"));
                                startActivity(intent);
                            }else{
                                Toast.makeText(OpenAccount.this,"Server Error",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
