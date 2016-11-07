package com.example.ext.exercise_tracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ext.exercise_tracker.DataHolder;

import java.util.List;

public class LoginSettings extends AppCompatActivity {

    EditText ip_addr;
    EditText folder;
    EditText filename;
    private static final String SELECT_SQL = "SELECT * FROM connection WHERE id = 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_settings);
        ip_addr = (EditText) findViewById(R.id.et_ip_setting);
        folder = (EditText) findViewById(R.id.et_folder);
        filename = (EditText) findViewById(R.id.et_filename);

        ip_addr.setFilters(new InputFilter[]{Validation.getFilter("N.")});
        folder.setFilters(new InputFilter[]{Validation.getFilter("A")});
        filename.setFilters(new InputFilter[]{Validation.getFilter("A.")});

        DBHandler db = new DBHandler(this);

        // Reading all dataSettings
        Log.d("Reading: ", "Reading all dataSettings..");
        List<dataSettings> dataSettingsList = db.getAlldataSettings();

        for (dataSettings dataSetting : dataSettingsList) {
            //set Default Values from Local DB from CONNECTION table to editFields::
            ip_addr.setText(dataSetting.getIP());
            folder.setText(dataSetting.getFolder());
            filename.setText(dataSetting.getFile());

            //::Logs::
            String log = "Id: " + dataSetting.getId() + " ,IP: " + dataSetting.getIP() + " ,Folder: " + dataSetting.getFolder() + " ,File: " + dataSetting.getFile();
            // Writing dataSettings to log
            Log.d("dataSettings: : ", log);
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Insomnia.ttf");
        TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        TextView tv_desc2 = (TextView) findViewById(R.id.tvDesc2);
        TextView tv_desc3 = (TextView) findViewById(R.id.tvDesc3);
        tv_desc.setTypeface(custom_font);
        tv_desc2.setTypeface(custom_font);
        tv_desc3.setTypeface(custom_font);






        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0c4de")));
            //actionBar.setLogo(R.drawable.logo1);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
    public void doLogin(View view){
        //Toast.makeText(LoginSettings.this, "IP address:" + ip_addr.getText().toString(), Toast.LENGTH_LONG).show();

        Intent startLogin = new Intent (this, LogActivity.class);

        String msg = ip_addr.getText().toString();
        String msg2 = folder.getText().toString();
        String msg3 = filename.getText().toString();

        DataHolder.getInstance().setIP(msg);
        DataHolder.getInstance().setFolder(msg2);
        DataHolder.getInstance().setFilename(msg3);

        //::Save current Default values to Local DB::
        DBHandler db = new DBHandler(this);
        int countSettings = db.getdataSettingsCount();
        if (countSettings > 0) {
            // Logs
            Log.d("Update: ", "Updating ..");
            db.updateSettings(new dataSettings(1, msg, msg2, msg3));
        }else {
            // Logs
            Log.d("Insert: ", "Inserting ..");
            db.add2Settings(new dataSettings(1, msg, msg2, msg3));
        }

        //::Close current activity and go back to Login Activity::
        startActivity(startLogin);
        LoginSettings.this.finish();
    }
    public void doHome(View view)
    {
        //::Go back to Login Activity without saving current configuration
        Intent goBack = new Intent (this, LogActivity.class);
        startActivity(goBack);
        LoginSettings.this.finish();
    }

}
