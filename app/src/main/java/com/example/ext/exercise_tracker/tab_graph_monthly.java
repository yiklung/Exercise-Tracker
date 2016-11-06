package com.example.ext.exercise_tracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 7435916 on 31/10/2016.
 */
public class tab_graph_monthly extends Fragment {

    private DatePicker datePicker;
    private Calendar date;
    private TextView dateViewS;
    private TextView dateViewE;
    private int year, month, day;
    private Button mstartDateM;
    private Button mendDateM;
    private Button mgenerate;
    private Button mback;
    static String dateEM;
    static String dateSM;
    private GraphView graph;
    private GraphView graph1;

    private static final String TAG="TESTING::::::::::::::";
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_UserID = "user_id";
    private static final String TAG_NAME = "steps";
    private static final String TAG_ADD ="calories";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        String stringFromPHP,str;
        View rootView = inflater.inflate(R.layout.tab_graph_monthly, container, false);
        dateViewS = (TextView) rootView.findViewById(R.id.startDateViewM);
        dateViewE = (TextView) rootView.findViewById(R.id.endDateViewM);
        mstartDateM  = (Button) rootView.findViewById(R.id.startDateM);
        mstartDateM.setOnClickListener(btnClick);
        mendDateM  = (Button) rootView.findViewById(R.id.endDateM);
        mendDateM.setOnClickListener(btnClick2);
        mgenerate = (Button) rootView.findViewById(R.id.generateM);
        mgenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mgenerate) {
                    Log.d("ONCLICKLISTENER::", "ENTER FUNCTION");
                    createGraph();
                }
            }
        });

        mback = (Button) rootView.findViewById(R.id.back);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
            }
        });

        graph = (GraphView) rootView.findViewById(R.id.graph2);

        graph1 = (GraphView) rootView.findViewById(R.id.graph2C);

        return rootView;
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mstartDateM) {
                Log.d("ONCLICKLISTENER::", "ENTER FUNCTION");
                showDatePicker();
            }
        }
    };

    private View.OnClickListener btnClick2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mendDateM) {
                Log.d("ONCLICKLISTENER::", "ENTER FUNCTION");
                showDatePicker2();
            }
        }
    };

    /*@Override
    public void onBackPressed() {

    }*/


    private void createGraph() {

        String stringFromPHP, str;
        //::Initialize DATE on which will be made PHP request, as an example DATE is 'TODAY'
        SimpleDateFormat today = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = today.format(new Date());
        String currentPHP = getData();
        Log.d(TAG, "CurrentStringFromPHP=" + currentPHP);
        //::take DATA FROM PHP Session and its Steps Amount in Array
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stringFromPHP = DataHolder.getInstance().getStrPHP();

        Log.d(TAG, "stringFromPHP=" + stringFromPHP);
        //showList(stringFromPHP);
        //String[] str = new String[10];
        JSONArray peoples = null;
        //DataPoint[] dataPool = null;
        //DataPoint[] dataPool1 = null;
        try {
            JSONObject jsonObj = new JSONObject(stringFromPHP);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            //dataPool = new DataPoint[peoples.length()+1];
            DataPoint[] dataPool = new DataPoint[peoples.length()];
            //dataPool[0] = new DataPoint(0,0);
            //dataPool1 = new DataPoint[peoples.length()+1];
            DataPoint[] dataPool1 = new DataPoint[peoples.length()];
            //dataPool1[0] = new DataPoint(0,0);
            int i;
            for (i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                //String user_id = c.getString(TAG_UserID);
                String steps = c.getString(TAG_NAME);
                String calories = c.getString(TAG_ADD);

                int id_int = Integer.parseInt(id);
                int steps_int = Integer.parseInt(steps);
                int calories_int = Integer.parseInt(calories);

                //dataPool[i+1] = new DataPoint(id_int,steps_int);
                //dataPool1[i+1] = new DataPoint(id_int,calories_int);
                dataPool[i] = new DataPoint(id_int, steps_int);
                dataPool1[i] = new DataPoint(id_int, calories_int);


                Log.d(TAG, "ID:" + id);
                //Log.d(TAG, "user_ID:"+user_id);
                Log.d(TAG, "steps:" + steps);
                Log.d(TAG, "calories:" + calories);
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPool);
            series.setTitle("Steps");
            series.setDrawBackground(true);
            series.setColor(Color.argb(255, 255, 60, 60));
            series.setBackgroundColor(Color.argb(100, 204, 119, 119));
            series.setDrawDataPoints(true);
            graph.removeAllSeries();
            graph.addSeries(series);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Steps");
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Session");
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
            graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(50);
            graph.getViewport().setScrollable(true);

            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dataPool1);
            series1.setTitle("Calories");
            series1.setDrawBackground(true);
            series1.setColor(Color.argb(255, 255, 60, 60));
            series1.setBackgroundColor(Color.argb(100, 204, 119, 119));
            series1.setDrawDataPoints(true);
            graph1.removeAllSeries();
            graph1.addSeries(series1);
            graph1.getGridLabelRenderer().setVerticalAxisTitle("Calories");
            graph1.getGridLabelRenderer().setHorizontalAxisTitle("Session");
            graph1.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
            graph1.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
            graph1.getViewport().setScalable(true);
            graph1.getViewport().setScrollable(true);
            graph1.getViewport().setXAxisBoundsManual(true);
            graph1.getViewport().setMinX(0);
            graph1.getViewport().setMaxX(50);
            graph1.getViewport().setScrollable(true);

            graph1.getLegendRenderer().setVisible(true);
            graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        } catch (JSONException e) {
            Log.d(TAG, "JSON EXCEPTION");
            e.printStackTrace();
        }
    }

    public void showDatePicker() {
        DatePickerFragmentSM date = new DatePickerFragmentSM();
        /**
         * Set Up Current Date Into dialog
         */
        Log.d("showDatePicker::","ENTER FUNCTION");
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Log.d("onDateSet::", "ENTER FUNCTION");
            String strDayOfMonth = String.valueOf(dayOfMonth);
            String strMonthOfYear = String.valueOf(monthOfYear + 1);
            String temp;
            String temp2;
            if (dayOfMonth<10){
                temp = "0" + strDayOfMonth;
                strDayOfMonth = temp;
            }
            if (monthOfYear<9){
                temp2 = "0" + strMonthOfYear;
                strMonthOfYear = temp2;
            }
            dateViewS.setText(String.valueOf(year) + "/" + strMonthOfYear
                    + "/" + strDayOfMonth);
            dateSM = dateViewS.getText().toString();
        }
    };

    public void showDatePicker2() {
        DatePickerFragmentEM date = new DatePickerFragmentEM();
        /**
         * Set Up Current Date Into dialog
         */
        Log.d("showDatePicker::","ENTER FUNCTION");
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate2);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Log.d("onDateSet::", "ENTER FUNCTION");
            String strDayOfMonth = String.valueOf(dayOfMonth);
            String strMonthOfYear = String.valueOf(monthOfYear + 1);
            String temp;
            String temp2;
            if (dayOfMonth<10){
                temp = "0" + strDayOfMonth;
                strDayOfMonth = temp;
            }
            if (monthOfYear<9){
                temp2 = "0" + strMonthOfYear;
                strMonthOfYear = temp2;
            }
            dateViewE.setText(String.valueOf(year) + "/" + strMonthOfYear
                    + "/" + strDayOfMonth);
            dateEM = dateViewE.getText().toString();
        }
    };

    public static String getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            String myJSON;
            @Override
            protected String doInBackground(String... params) {
                String IP = DataHolder.getInstance().getIP();
                String folder = DataHolder.getInstance().getFolder();
                String user_id = DataHolder.getInstance().getUserID();
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://"+IP+"/"+folder+"/graph_monthly.php?dateSM=" + dateSM + "&dateEM=" + dateEM + "&user_id=" + user_id);

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
                        Log.d(TAG, "LINE=" + line);
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    DataHolder.getInstance().setStrPHP(result);
                    /////////////////////::::::::::::::::::::::::::
                    Log.d(TAG, "send2DataHolder:::"+ DataHolder.getInstance().getStrPHP());
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
                showList(myJSON);
            }
            protected String getDataString(){
                Log.d(TAG, "getDataRETURN_VALUE:::"+myJSON);
                return myJSON;
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
        String DataString = g.getDataString();
        Log.d(TAG, "DataString:" + DataString);
        return DataString;
    }

    protected static void showList(String myJSON){
        JSONArray peoples = null;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String user_id = c.getString(TAG_UserID);
                String steps = c.getString(TAG_NAME);
                String calories = c.getString(TAG_ADD);

                Log.d(TAG, "ID:"+id);
                Log.d(TAG, "steps:"+steps);
                Log.d(TAG, "calories:"+calories);

            }
        } catch (JSONException e) {
            Log.d(TAG, "JSON EXCEPTION");
            e.printStackTrace();
        }

    }

}




