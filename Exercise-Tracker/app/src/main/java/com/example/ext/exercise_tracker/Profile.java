package com.example.ext.exercise_tracker;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Profile extends AppCompatActivity {

    Button btn_bmi;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String TAG="TESTING::::::::::::::";

    EditText weight;
    EditText height;
    EditText bmi;
    EditText name;
    EditText email;
    EditText goal;

    TextView tv_weight;
    TextView tv_height;
    TextView tv_bmi;
    TextView tv_name;
    TextView tv_email;
    TextView tv_tresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        weight = (EditText) findViewById(R.id.et_weight);
        height = (EditText) findViewById(R.id.et_height);
        bmi = (EditText) findViewById(R.id.et_bmi);
        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_email);
        goal = (EditText) findViewById(R.id.et_tresh);

        name.setFilters(new InputFilter[]{Validation.getFilter("Aspace")});
        email.setFilters(new InputFilter[]{Validation.getFilter("ANat")});
        weight.setFilters(new InputFilter[]{Validation.getFilter("N")});
        height.setFilters(new InputFilter[]{Validation.getFilter("N")});
        goal.setFilters(new InputFilter[]{Validation.getFilter("N")});

        btn_bmi = (Button)findViewById(R.id.bmi_standard);
        btn_bmi.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
        bmi.setEnabled(false);

        tv_bmi = (TextView) findViewById(R.id.tv_bmi);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_tresh = (TextView) findViewById(R.id.tv_tresh);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Insomnia.ttf");
        tv_weight.setTypeface(custom_font);
        tv_height.setTypeface(custom_font);
        tv_bmi.setTypeface(custom_font);
        tv_email.setTypeface(custom_font);
        tv_name.setTypeface(custom_font);
        tv_tresh.setTypeface(custom_font);

        final CheckBox cb_email = (CheckBox) findViewById(R.id.cb_email);
        final Button btn_create = (Button) findViewById(R.id.btn_apply);
        btn_create.setVisibility(View.INVISIBLE);
        final int valiArray[] = new int[1];

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0c4de")));
            //actionBar.setLogo(R.drawable.logo1);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        //::EMAIL LISTENER
        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String tick = "";
                if (Validation.isValidEmail(email.getText().toString())) {
                    cb_email.setVisibility(View.VISIBLE);
                    cb_email.setChecked(true);
                    tick = "true";
                    valiArray[0] = 1;
                    if (Validation.getValidation(valiArray)) {
                        btn_create.setVisibility(View.VISIBLE);
                    }
                    Log.d("VALI:isValidEmail=", tick);
                } else {
                    cb_email.setVisibility(View.VISIBLE);
                    cb_email.setChecked(false);
                    tick = "false";
                    valiArray[0] = 0;
                    btn_create.setVisibility(View.INVISIBLE);
                    Log.d("VALI:isValidEmail=", tick);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        //::Initialisation parameters
        name.setText(DataHolder.getInstance().getUsername());
        weight.setText(DataHolder.getInstance().getWeight());
        height.setText(DataHolder.getInstance().getHeight());
        goal.setText(DataHolder.getInstance().getGoal());
        String email_str = DataHolder.getInstance().getEmail();
        if (email_str.equals(" ")){
            email_str = null;
            Log.d("STRING::","changed to_"+email_str+"!");
            email.setText("");
        }else {email.setText(email_str);}


        btn_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Profile.this);
                dialog.setContentView(R.layout.image_dialog);
                dialog.setTitle("~BMI Standard Refer~");

                Button dismissButton = (Button) dialog.findViewById(R.id.close);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        weight.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                bmi.setText(getBMI());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        height.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int w = getWeight();
                int h = getHeight();
                Log.d("hListener:::::::::::", "height=" + h + " weight=" + w);
                String str_bmi = getBMI();
                Log.d("hListener:::::::::::", "str_BMI" + str_bmi);
                bmi.setText(str_bmi);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        String user_id = DataHolder.getInstance().getUserID();
        //new AsyncProfile().execute("", "", user_id);
        getData("","","","","");
    }
    public String getBMI(){
        int int_weight = getWeight();
        int int_height = getHeight();
        String string_bmi="";
        double double_bmi = 0.00000000000;
        Log.d("get BMI :::::::::::","height="+int_height+" weight="+int_weight);
        double_bmi = (int_weight*10000/(int_height*int_height));
        if (double_bmi>100){
            double_bmi = 0;
        }
        string_bmi = Double.toString(double_bmi);
        return string_bmi;
    }
    public int getWeight(){
        if(!TextUtils.isEmpty(weight.getText().toString())) {
            //etUserName.setError("Your message");
            //return;

        //if (weight.getText().toString()!=null){
            int int_weight = Integer.parseInt(weight.getText().toString());
            if (int_weight > 0){
            return int_weight;
            }else return 1;
        }else return 1;
    }
    public int getHeight(){
        if(!TextUtils.isEmpty(height.getText().toString())) {
        //if (height.getText().toString()!=null) {
            int int_height = Integer.parseInt(height.getText().toString());
            if (int_height > 0) {
                return int_height;
            } else return 1;
        }else return 1;
    }
    public void doApply(View view){
        String user_id = DataHolder.getInstance().getUserID();
        //::Initiate JAVA->PHP
        String w = weight.getText().toString();
        String h = height.getText().toString();
        String e = email.getText().toString();
        String n = name.getText().toString();
        String g = goal.getText().toString();

        DataHolder.getInstance().setWeight(w);
        DataHolder.getInstance().setHeight(h);
        DataHolder.getInstance().setEmail(e);
        DataHolder.getInstance().setUsername(n);
        DataHolder.getInstance().setGoal(g);

        if (w!=null && w!="" && h!=null && h!="") {
            getData(w,h,n,e,g);
        }else Toast.makeText(getApplicationContext(),"Weight and Height cannot be empty!", Toast.LENGTH_LONG).show();
        doHome(null);
    }

    public void getData(final String w,final String h,final String n, final String e, final  String goal){
        class GetDataJSON extends AsyncTask<String, Void, String>{
            String myJSON;
            @Override
            protected String doInBackground(String... params) {
                String IP = DataHolder.getInstance().getIP();
                String folder = DataHolder.getInstance().getFolder();
                String user_id = DataHolder.getInstance().getUserID();
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://"+IP+"/"+folder+"/user_info.php?user_id="+user_id+"&weight="+w+"&height="+h+"&name="+n+"&email="+e+"&goal="+goal);
                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        Log.d(TAG, "LINE="+line);
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    //DataHolder.getInstance().setStrPHP(result);
                    /////////////////////::::::::::::::::::::::::::
                    //Log.d(TAG, "send2DataHolder:::"+ DataHolder.getInstance().getStrPHP());
                    /////////////////////::::::::::::::::::::::::::
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                String[] tokens = myJSON.split(",");
                switch (tokens[0]){
                    case "fail":
                        setWH("","","","");
                        break;
                    case "success":
                        setWH(tokens[2],tokens[1],tokens[3],tokens[4]);
                        break;
                    case "update":
                        Toast.makeText(getApplicationContext(),"Weight and Height has been UPDATED!", Toast.LENGTH_LONG).show();
                        break;
                    default: {
                    }
                }
                Log.d("myJSON=",myJSON);


                //showList(myJSON);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public void setWH(String w, String h, String e, String n){
        weight.setText(w);
        height.setText(h);
        if (e.equals(" ")) {
            email.setText("");
        }else {email.setText(e);}
        name.setText(n);
    }
    public void doHome(View view)
    {
        //::Go back to Login Activity without saving current configuration
        Intent goBack = new Intent (this, MainMenu.class);
        startActivity(goBack);
        Profile.this.finish();
    }
}
