package com.example.gindoymz.usave;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gindoymz.usave.util.RadAPI;

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

public class USaveMain extends AppCompatActivity {

    private EditText username;
    private EditText password;

    private Button login;

    private String uname;
    private String pword;
    private String vendor;

    private JSONObject jObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usavemain_layout);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        vendor = "vendor";

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                pword = password.getText().toString();

                if (uname.isEmpty() || pword.isEmpty()) {
                    Toast.makeText(USaveMain.this, "Please enter username/password", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        checkCredentials(uname,pword,vendor);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void checkCredentials(String username, String password, String vendor) throws IOException {
        final ProgressDialog pd = new ProgressDialog(USaveMain.this);
        pd.setMessage("Checking credentials..");
        pd.show();

        OkHttpClient client = new OkHttpClient();
        String postBody = "{\n" +
                "    \"username\": \""+username+"\",\n" +
                "    \"password\": \""+password+"\",\n" +
                "    \"role\": \""+vendor+"\"\n" +
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
                USaveMain.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(USaveMain.this, USaveMenu.class);
                        try {
                            JSONObject jObject = new JSONObject(resp);
                            if (jObject.getString("result").toString().equals("success")) {
                                intent.putExtra("accountno", jObject.getString("accountNo"));
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(USaveMain.this, "Invalid username/password", Toast.LENGTH_LONG).show();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
