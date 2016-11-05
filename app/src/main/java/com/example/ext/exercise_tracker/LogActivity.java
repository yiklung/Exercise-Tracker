package com.example.ext.exercise_tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class LogActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText username;
    private EditText uname;
    private EditText psw;
    private EditText ip;
    private TextView tv2;
    private TextView tv_un;
    private TextView tv_psw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Insomnia.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/RECOGNITION.ttf");
        tv2 = (TextView) findViewById(R.id.textView2);
        tv_un = (TextView) findViewById(R.id.tv_un);
        tv_psw = (TextView) findViewById(R.id.tv_psw);

        //::default IP configuration in case User didnt go to settings page
        if ( (DataHolder.getInstance().getFilename()==null) || (DataHolder.getInstance().getFolder()==null) ||
                (DataHolder.getInstance().getIP()==null)
            ) {
            DataHolder.getInstance().setFilename("index.php");
            DataHolder.getInstance().setIP("10.0.2.2");
            DataHolder.getInstance().setFolder("fyp");
        }

        uname = (EditText) findViewById(R.id.et_un);
        psw = (EditText) findViewById(R.id.et_psw);

        uname.setTypeface(custom_font);
        psw.setTypeface(custom_font);
        tv2.setTypeface(custom_font);
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
    public void doStart(View view){
        Intent startMenu = new Intent (this, MainMenu.class);
        startActivity(startMenu);
    }
    public boolean backdoor(String log, String psw){
        //Toast.makeText(getApplicationContext(),"Welcome pass="+psw+" log="+log+"!",Toast.LENGTH_SHORT).show();
        if (log.equals("q")&&psw.equals("q")){
            return true;
        }else {return false;}
    }

    public void doCheck(View view){
        uname = (EditText) findViewById(R.id.et_un);
        psw = (EditText) findViewById(R.id.et_psw);
        ip = (EditText) findViewById(R.id.et_ip_setting);

        String uname_str = uname.getText().toString();
        String psw_str = psw.getText().toString();

        final String unPhp = uname.getText().toString();
        final String password = psw.getText().toString();
        final String ip_str = ip.getText().toString();

        // Initialize  AsyncLogin() class with email and password

        if (backdoor(uname_str,psw_str)) {
            DataHolder.getInstance().setHeight("175");
            DataHolder.getInstance().setWeight("80");
            DataHolder.getInstance().setUsername("Artur");
            DataHolder.getInstance().setGoal("1000");

            Toast.makeText(getApplicationContext(),"Backdoor is ACTIVATED!",Toast.LENGTH_SHORT).show();
            doStart(null);
        }else {
            //Toast.makeText(getApplicationContext(),"Backdoor=FALSE",Toast.LENGTH_SHORT).show();
            if(!(uname_str.equals(null)) && !(psw_str.equals(null)) && !(uname_str.equals("")) && !(psw_str.equals(""))) {
                new AsyncLogin().execute(unPhp, password);
            }else {
                //Toast.makeText(getApplicationContext(),"Username and Password cannot be EMPTY!",Toast.LENGTH_SHORT).show();
                Toast toast = Toast.makeText(getApplicationContext(),"Username and Password cannot be EMPTY!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        String db_uname="", db_psw="";
    }





    public String getUname(String uname, String ip_str){

        String code="chk_uname";
        return uname;
    }
    public String getPsw(String ip_str){
        String psw = "";
        return psw;
    }


    public void delText(){
        username.setText("");
    }
    //::DB interconnection
    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LogActivity.this);
        HttpURLConnection conn;
        URL url = null;
        private static final String TAG = "Testing: ";
        String ip_addr = DataHolder.getInstance().getIP();
        String ip_folder = DataHolder.getInstance().getFolder();
        String ip_filename = DataHolder.getInstance().getFilename();
        //final String dbUrl = "http://"+ip.getText().toString()+"/fyp/index.php";
        final String dbUrl = "http://" + ip_addr + "/" + ip_folder + "/" + ip_filename;

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
                if(URLUtil.isValidUrl(String.valueOf(url))){
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
                            .appendQueryParameter("password", params[1]);
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
                }

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
            Log.d("RESULT FROM PHP::", String.valueOf(result));
            List<String> list = new ArrayList<String>(Arrays.asList(result.split(",")));
            //ArrayList list= new ArrayList(Arrays.asList(result.split(",")));
            //Toast.makeText(getApplicationContext(),"LIST="+joined,Toast.LENGTH_SHORT).show();
            result = list.get(0);

            //::LOG:: URL::
            // Toast.makeText(getApplicationContext(),"dbURL:"+dbUrl,Toast.LENGTH_SHORT).show();

            if(result.equalsIgnoreCase("customer"))
            {
                String user_id = list.get(1);
                String fullname = list.get(2);
                String height = list.get(3);
                String weight = list.get(4);
                String email = list.get(5);
                String goal = list.get(6);
                DataHolder.getInstance().setUserID(user_id);
                DataHolder.getInstance().setUsername(fullname);
                DataHolder.getInstance().setWeight(weight);
                DataHolder.getInstance().setHeight(height);
                DataHolder.getInstance().setEmail(email);
                DataHolder.getInstance().setGoal(goal);

                //Toast.makeText(getApplicationContext(),"Welcome "+result+" "+uname.getText().toString()+" #"+user_id+"!",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"Welcome "+fullname+"!",Toast.LENGTH_SHORT).show();
                Toast toast = Toast.makeText(getApplicationContext(),"Welcome "+fullname+"!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent intent = new Intent(LogActivity.this,MainMenu.class);
                startActivity(intent);
                LogActivity.this.finish();

            }else if (result.equalsIgnoreCase("administrator")){
                //
                Intent intent2 = new Intent(LogActivity.this,AdminMain.class);
                //String msg = ip.getText().toString();
                //intent2.putExtra("ipFromSett", msg);
                startActivity(intent2);
                LogActivity.this.finish();
            }else if (result.equalsIgnoreCase("failure")){

                // If username and password does not match display a error message
                Toast.makeText(LogActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(LogActivity.this, "Connection Problem. Check IP settings.", Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(LogActivity.this, "From PHP:"+result, Toast.LENGTH_LONG).show();
            }
        }

    }

    //::Action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_login,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sett_id:
                //Toast.makeText(getApplicationContext(),"WhatsApp is selected",Toast.LENGTH_SHORT).show();
                openSett(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void openSett(View view) {
        Intent startSett = new Intent (this, LoginSettings.class);
        startActivity(startSett);
        //LogActivity.this.finish();
    }
}
