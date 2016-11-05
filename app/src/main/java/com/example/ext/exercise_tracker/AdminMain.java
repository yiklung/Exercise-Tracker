package com.example.ext.exercise_tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AdminMain extends AppCompatActivity {

    EditText un;
    EditText psw;
    EditText fn;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Insomnia.ttf");
        TextView tv_un = (TextView) findViewById(R.id.tv_cUsername);
        TextView tv_psw = (TextView) findViewById(R.id.tv_cPassword);
        TextView tv_fn = (TextView) findViewById(R.id.tv_fn);
        tv_fn.setTypeface(custom_font);
        tv_un.setTypeface(custom_font);
        tv_psw.setTypeface(custom_font);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0c4de")));
            //actionBar.setLogo(R.drawable.logo1);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void doBack(View view) {
        Intent startBack = new Intent (this, LogActivity.class);
        startActivity(startBack);
        AdminMain.this.finish();
    }

    public void doCreate(View view){
        un = (EditText) findViewById(R.id.et_cUsername);
        psw = (EditText) findViewById(R.id.et_cPassword);
        fn = (EditText) findViewById(R.id.et_cFullname);

        final String username = un.getText().toString();
        final String password = psw.getText().toString();
        final String fullname = fn.getText().toString();

        new AsyncLogin().execute(username, password, fullname);

    }

    //::REQUEST TO PHP -- SERVER SIDE
    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(AdminMain.this);
        HttpURLConnection conn;
        URL url = null;
        private static final String TAG = "Testing: ";
        String ip_addr = DataHolder.getInstance().getIP();
        final String dbUrl = "http://"+ip_addr+"/fyp/add.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides


                Log.d(TAG, dbUrl);
                url = new URL(dbUrl);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1])
                        .appendQueryParameter("fullname", params[2]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            if(result.equalsIgnoreCase("success"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast toast = Toast.makeText(getApplicationContext(),"User has been added successfully!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //Toast.makeText(getApplicationContext(), "User has been added successfully!", Toast.LENGTH_SHORT).show();
            }else if (result.equalsIgnoreCase("fail")){

                // If username and password does not match display a error message
                //Toast.makeText(AdminMain.this, "User cannot be created...", Toast.LENGTH_LONG).show();
                Toast toast2 = Toast.makeText(getApplicationContext(),"User cannot be created...",Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.CENTER, 0, 0);
                toast2.show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(AdminMain.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }else {Toast.makeText(AdminMain.this, "From PHP:"+result, Toast.LENGTH_LONG).show();}
        }

    }
}
