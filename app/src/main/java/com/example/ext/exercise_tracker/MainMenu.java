package com.example.ext.exercise_tracker;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Text;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MainMenu extends AppCompatActivity {

    private TextView tv_steps;
    private TextView Calories;
    private TextView tv_start;
    private TextView tv_end;
    private TextView tv_timer;
    private TextView tv_username;
    private TextView tv_date;
    private TextView tv_goal;

    private EditText et_steps;
    private EditText et_startTime;
    private EditText et_endTime;
    private EditText et_calories;

    Button btn_share;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    // Sensor Manager
    private SensorManager sensorManager;

    // Values to Calculate Number of Steps
    private float previousZ;
    private float currentZ;
    private float previousX;
    private float currentX;
    private float previousY;
    private float currentY;

    // Values for steps
    private int numsteps;

    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    // X, Y, Z-Points at which we want to trigger a 'steps'
    private double thresholdy;
    private double thresholdx;
    private double thresholdz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0c4de")));
            actionBar.setLogo(R.drawable.shoe48);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        et_steps = (EditText) findViewById(R.id.et_steps);
        et_startTime = (EditText) findViewById(R.id.et_startTime);
        et_endTime = (EditText) findViewById(R.id.et_endTime);
        et_calories = (EditText) findViewById(R.id.et_calories);

        tv_goal = (TextView) findViewById(R.id.tv_goal);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_username = (TextView) findViewById(R.id.tv_userName);
        tv_date = (TextView) findViewById(R.id.tv_date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime2 = sdf2.format(new Date());
        et_startTime.setText(currentDateandTime);
        tv_date.setText(currentDateandTime2);
        tv_username.setText(DataHolder.getInstance().getUsername());
        //Timer::
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        //Display Corrections::
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels;
        float dpWidth  = outMetrics.widthPixels;

        int intScrHeight = (int)(Math.round(dpHeight));
        int intScrWidth = (int)(Math.round(dpWidth));
        Log.d("SCR WIDTH::::",Integer.toString(intScrWidth));
        int i;
        for (i=1;i<8;i++) {
            int resID1 = getResources().getIdentifier("layout"+Integer.toString(i), "id", getPackageName());
            setLayoutSize(intScrWidth, resID1);
        }

        // Set Accelerometer X,Y,Z-axis threshold (For sensitivity)
        thresholdy = 9;
        thresholdx = 4;
        thresholdz = 6;

        // Initiate Values for x, y, z
        previousY = 0;
        currentY = 0;
        previousX = 0;
        currentX = 0;
        previousZ = 0;
        currentZ = 0;

        // Initiate steps value
        numsteps = 0;

        //Enable the listener - we will write this later in the class
        enableAccelerometerListening();


        double Caloriesvalue = 1000;
        Caloriesvalue = UnitConverter.step2calories(Caloriesvalue);
        et_calories.setText(String.format("%.3f", Caloriesvalue));
        et_steps.setText("1000");

        int steps = Integer.parseInt(et_steps.getText().toString());
        int goal = Integer.parseInt(DataHolder.getInstance().getGoal());
        double progress = steps*100/goal;
        tv_goal.setText(Double.toString(progress)+"%");


        //::STEP LISTENER
        et_steps.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int steps = Integer.parseInt(et_steps.getText().toString());
                int goal = Integer.parseInt(DataHolder.getInstance().getGoal());
                double progress = steps*100/goal;
                tv_goal.setText(Double.toString(progress)+"%");

                double Caloriesvalue;
                Caloriesvalue = UnitConverter.step2calories(steps);
                et_calories.setText(String.format("%.3f", Caloriesvalue));

                Log.d("sListener:::::::::::", "height=" + steps);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        et_steps.setFocusable(false);
        et_calories.setFocusable(false);
    }
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            mins = mins % 60;
            int milliseconds = (int) (updatedTime % 1000);
            String z;
            if (hours<10){
                z="0";
            }else {z="";}
            tv_timer.setText(z + hours + ":"
                    + String.format("%02d", mins) +
                    ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };
    private void setLayoutSize(int width, int resID){

        LinearLayout layout = (LinearLayout)findViewById(resID);
        // Gets the layout params that will allow you to resize the layout
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) layout.getLayoutParams();

        params.width = (int)(Math.round(width));
        Log.d("Width to layout::",Integer.toString(params.width));
        layout.setLayoutParams(params);
    }
    private void enableAccelerometerListening(){
        // Initialize the Sensor Manager
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Gather the values from accelerometer
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Fetch the current X, Y, Z
            currentY = y;
            currentX = x;
            currentZ = z;

            // Measure if a step is taken
            if ((Math.abs(currentY - previousY) > thresholdy) || (Math.abs(currentX - previousX) > thresholdx) ||
                    (Math.abs(currentZ - previousZ) > thresholdz)) {

                numsteps++;
                et_steps.setText(String.valueOf(numsteps));

                double Caloriesvalue = new Double(numsteps);
                Caloriesvalue = UnitConverter.step2calories(Caloriesvalue);
                et_calories.setText(String.format("%.3f", Caloriesvalue));
            } // end if

            // Store the previous X, Y, Z
            previousY = y;
            previousX = x;
            previousZ = z;

        } // end onSensorChanged


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Empty - required by Class
        } // end onAccuracy Changed
    }; // ends private inner class sensorEventListener

        public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
        String date = sdf.format(new Date());
        return date;
    }


    public void doStop(View view){

        final String steps = et_steps.getText().toString();
        final String calories = et_calories.getText().toString();
        final String s_time = et_startTime.getText().toString();
        final String user_id = DataHolder.getInstance().getUserID();

        et_endTime.setText(getDate());
        final String e_time = et_endTime.getText().toString();

        new AsyncLogin().execute(steps, calories, s_time, e_time, user_id);

        et_steps.setText("0");
        et_calories.setText("");
        et_startTime.setText(getDate());
        et_endTime.setText("");

        // Reset steps to 0 after pressed 'Stop'
        numsteps = 0;
        et_steps.setText(String.valueOf(numsteps));
        et_calories.setText(String.valueOf(numsteps));
        tv_timer.setText("00:00:00");
    }
    //:: REQUEST TO PHP
    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainMenu.this);
        HttpURLConnection conn;
        URL url = null;
        private static final String TAG = "Testing: ";
        String ip_addr = DataHolder.getInstance().getIP();
        String ip_folder = DataHolder.getInstance().getFolder();
        String ip_filename = "add_session.php";
        //final String dbUrl = "http://"+ip.getText().toString()+"/fyp/index.php";
        final String dbUrl = "http://"+ip_addr+"/"+ip_folder+"/"+ip_filename;

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
                        .appendQueryParameter("steps", params[0])
                        .appendQueryParameter("calories", params[1])
                        .appendQueryParameter("start_time", params[2])
                        .appendQueryParameter("end_time", params[3])
                        .appendQueryParameter("user_id", params[4]);
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

                Toast.makeText(getApplicationContext(), "Session has been saved successfully!", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(Main.this,MainMenu.class);
               // startActivity(intent);
                //LogActivity.this.finish();

            }else if (result.equalsIgnoreCase("fail")){

                // If username and password does not match display a error message
                Toast.makeText(MainMenu.this, "Cannot save session", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(MainMenu.this, "Connection Problem. Check IP settings.", Toast.LENGTH_LONG).show();

            }else {Toast.makeText(MainMenu.this, "From PHP:"+result, Toast.LENGTH_LONG).show();}
        }

    }

    public void doGraphActivity (View view){
        Intent startGraphActivity = new Intent (this, GraphActivity.class);
        startActivity(startGraphActivity);
        MainMenu.this.finish();
    }
    public void doLogOut (View view){
        Intent goBack = new Intent (this, LogActivity.class);
        startActivity(goBack);
        MainMenu.this.finish();
    }
    public void doProfile (View view){
        Intent goProfile = new Intent (this, Profile.class);
        startActivity(goProfile);
        MainMenu.this.finish();
    }

    public void doShare (View view){
        //Intent startShare = new Intent (this, Share.class);
        //startActivity(startShare);

        String msg;
        msg = getMsg();
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);

        List<Intent> targetShareIntents=new ArrayList<Intent>();
        List<ResolveInfo> resInfos=getPackageManager().queryIntentActivities(shareIntent, 0);
        if(!resInfos.isEmpty()){
            System.out.println("Have package");
            for(ResolveInfo resInfo : resInfos){
                String packageName=resInfo.activityInfo.packageName;

                Log.i("Package Name", packageName);
//                if(packageName.contains("twitter") || packageName.contains("Messaging") || packageName.contains("whatsapp")){
                    Intent intent=new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, msg);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
//                }
            }
            if(!targetShareIntents.isEmpty()){
                System.out.println("Have Intent");
                Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            }else{
                System.out.println("Do not Have Intent");
            }
        }

    }

    public String getMsg() {
        String msg, fullname;
        fullname = DataHolder.getInstance().getUsername();
        msg = fullname + " is willing to share his/her ExerciseTracker progress" + "\n" +
                //"ID: "+ et_sid.getText() + "\n" +
                "For chosen session " + fullname + " did " + et_steps.getText() + " steps," + "\n" +
                "also during that session has been burnt " + et_calories.getText() + " calories, " + "\n" +
                "above information was saved on " + et_startTime.getText();
        return msg;
    }
    public void convert(View v){

       // Calories.setText();

    }
    //::Dates Difference
    public void dateDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }

    //::Action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.share_id:
                //Toast.makeText(getApplicationContext(),"WhatsApp is selected",Toast.LENGTH_SHORT).show();
                doShare(null);
                return true;
            case R.id.logOut_id:
                //Toast.makeText(getApplicationContext(),"WhatsApp is selected",Toast.LENGTH_SHORT).show();
                doLogOut(null);
                return true;
            case R.id.profile_id:
                //Toast.makeText(getApplicationContext(),"WhatsApp is selected",Toast.LENGTH_SHORT).show();
                doProfile(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
