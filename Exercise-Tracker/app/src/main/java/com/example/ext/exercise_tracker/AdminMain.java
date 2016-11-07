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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    EditText w;
    EditText h;
    EditText e;
    EditText g;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        //Initialisation::
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Insomnia.ttf");
        TextView tv_un = (TextView) findViewById(R.id.tv_cUsername);
        TextView tv_psw = (TextView) findViewById(R.id.tv_cPassword);
        TextView tv_fn = (TextView) findViewById(R.id.tv_fn);
        TextView tv_h = (TextView) findViewById(R.id.tv_height);
        TextView tv_w = (TextView) findViewById(R.id.tv_weight);
        TextView tv_e = (TextView) findViewById(R.id.tv_email);
        TextView tv_g = (TextView) findViewById(R.id.tv_goal);
        tv_fn.setTypeface(custom_font);
        tv_un.setTypeface(custom_font);
        tv_psw.setTypeface(custom_font);
        tv_h.setTypeface(custom_font);
        tv_w.setTypeface(custom_font);
        tv_e.setTypeface(custom_font);
        tv_g.setTypeface(custom_font);

        final Button btn_create = (Button) findViewById(R.id.btn_create);
        btn_create.setVisibility(View.INVISIBLE);
        final int valiArray[] = new int[7];

        un = (EditText) findViewById(R.id.et_cUsername);
        psw = (EditText) findViewById(R.id.et_cPassword);
        fn = (EditText) findViewById(R.id.et_cFullname);
        w = (EditText) findViewById(R.id.et_weight);
        h = (EditText) findViewById(R.id.et_height);
        e = (EditText) findViewById(R.id.et_email);
        g = (EditText) findViewById(R.id.et_goal);

        un.setFilters(new InputFilter[]{Validation.getFilter("AN")});
        psw.setFilters(new InputFilter[]{Validation.getFilter("AN")});
        fn.setFilters(new InputFilter[]{Validation.getFilter("A")});
        e.setFilters(new InputFilter[]{Validation.getFilter("ANat")});
        w.setFilters(new InputFilter[]{Validation.getFilter("N")});
        h.setFilters(new InputFilter[]{Validation.getFilter("N")});
        g.setFilters(new InputFilter[]{Validation.getFilter("N")});

        final CheckBox cb_un = (CheckBox) findViewById(R.id.cb_un);
        final CheckBox cb_psw = (CheckBox) findViewById(R.id.cb_psw);
        final CheckBox cb_fn = (CheckBox) findViewById(R.id.cb_fn);
        final CheckBox cb_email = (CheckBox) findViewById(R.id.cb_email);
        final CheckBox cb_height = (CheckBox) findViewById(R.id.cb_height);
        final CheckBox cb_weight = (CheckBox) findViewById(R.id.cb_weight);
        final CheckBox cb_goal = (CheckBox) findViewById(R.id.cb_goal);

        //::UN LISTENER
        un.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick="";
                if (!Validation.isEmpty(un.getText().toString())) {
                    cb_un.setVisibility(View.VISIBLE);
                    cb_un.setChecked(true);
                    valiArray[0] = 1;
                    if (Validation.getValidation(valiArray)){
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    tick="true";
                    Log.d("VALIDATION:::ISEMPTY==",tick);
                } else {
                    cb_un.setVisibility(View.VISIBLE);
                    cb_un.setChecked(false);
                    valiArray[0] = 0;
                    btn_create.setVisibility(View.INVISIBLE);
                    tick = "false";
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        //::PSW LISTENER
        psw.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick = "";
                if (!Validation.isEmpty(psw.getText().toString())) {
                    cb_psw.setVisibility(View.VISIBLE);
                    cb_psw.setChecked(true);
                    tick = "true";
                    valiArray[1] = 1;
                    if (Validation.getValidation(valiArray)){
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                } else {
                    cb_psw.setVisibility(View.VISIBLE);
                    cb_psw.setChecked(false);
                    tick = "false";
                    valiArray[1]=0;
                    btn_create.setVisibility(View.INVISIBLE);
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        //::FN LISTENER
        fn.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick = "";
                if (!Validation.isEmpty(fn.getText().toString())) {
                    cb_fn.setVisibility(View.VISIBLE);
                    cb_fn.setChecked(true);
                    tick = "true";
                    valiArray[2] = 1;
                    if (Validation.getValidation(valiArray)){
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                } else {
                    cb_fn.setVisibility(View.VISIBLE);
                    cb_fn.setChecked(false);
                    tick = "false";
                    valiArray[2]=0;
                    btn_create.setVisibility(View.INVISIBLE);
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        //::EMAIL LISTENER
        e.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick = "";
                if (Validation.isValidEmail(e.getText().toString())) {
                    cb_email.setVisibility(View.VISIBLE);
                    cb_email.setChecked(true);
                    tick = "true";
                    valiArray[3] = 1;
                    if (Validation.getValidation(valiArray)){
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    Log.d("VALI:isValidEmail=", tick);
                } else {
                    cb_email.setVisibility(View.VISIBLE);
                    cb_email.setChecked(false);
                    tick = "false";
                    valiArray[3]=0;
                    btn_create.setVisibility(View.INVISIBLE);
                    Log.d("VALI:isValidEmail=", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        //::Height LISTENER
        h.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick = "";
                if (!Validation.isEmpty(h.getText().toString())) {
                    cb_height.setVisibility(View.VISIBLE);
                    cb_height.setChecked(true);
                    tick = "true";
                    valiArray[4] = 1;
                    if (Validation.getValidation(valiArray)){
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                } else {
                    cb_height.setVisibility(View.VISIBLE);
                    cb_height.setChecked(false);
                    tick = "false";
                    valiArray[4]=0;
                    btn_create.setVisibility(View.INVISIBLE);
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        //::Weight LISTENER
        w.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick = "";
                if (!Validation.isEmpty(w.getText().toString())) {
                    cb_weight.setVisibility(View.VISIBLE);
                    cb_weight.setChecked(true);
                    tick = "true";
                    valiArray[5] = 1;
                    if (Validation.getValidation(valiArray)){
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                } else {
                    cb_weight.setVisibility(View.VISIBLE);
                    cb_weight.setChecked(false);
                    tick = "false";
                    valiArray[5]=0;
                    btn_create.setVisibility(View.INVISIBLE);
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        //::GOAL LISTENER
        g.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick = "";
                if (!Validation.isEmpty(g.getText().toString())) {
                    cb_goal.setVisibility(View.VISIBLE);
                    cb_goal.setChecked(true);
                    tick = "true";
                    valiArray[6] = 1;
                    if (Validation.getValidation(valiArray)){
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                } else {
                    cb_goal.setVisibility(View.VISIBLE);
                    cb_goal.setChecked(false);
                    tick = "false";
                    valiArray[6]=0;
                    btn_create.setVisibility(View.INVISIBLE);
                    Log.d("VALIDATION:::ISEMPTY==", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
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

        final String username = un.getText().toString();
        final String password = psw.getText().toString();
        final String fullname = fn.getText().toString();
        final String weight = w.getText().toString();
        final String height = h.getText().toString();
        final String email = e.getText().toString();
        final String goal = g.getText().toString();

        new AsyncLogin().execute(username, password, fullname, weight, height, email, goal);

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
                Log.d(TAG, dbUrl);
                url = new URL(dbUrl);
            } catch (MalformedURLException e) {
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
                        .appendQueryParameter("fullname", params[2])
                        .appendQueryParameter("weight", params[3])
                        .appendQueryParameter("height", params[4])
                        .appendQueryParameter("email", params[5])
                        .appendQueryParameter("goal", params[6]);
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

            pdLoading.dismiss();

            if(result.equalsIgnoreCase("success"))
            {
                Toast toast = Toast.makeText(getApplicationContext(),"User has been added successfully!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //Toast.makeText(getApplicationContext(), "User has been added successfully!", Toast.LENGTH_SHORT).show();
            }else if (result.equalsIgnoreCase("fail")){

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
