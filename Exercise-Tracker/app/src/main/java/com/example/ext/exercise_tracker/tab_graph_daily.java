package com.example.ext.exercise_tracker;

/**
 * Created by 7435916 on 31/10/2016.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Date;

public class tab_graph_daily extends Fragment{

    private static final String TAG="TESTING::::::::::::::";
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_UserID = "user_id";
    private static final String TAG_NAME = "steps";
    private static final String TAG_ADD ="calories";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        String stringFromPHP,str;
        View rootView = inflater.inflate(R.layout.tab_graph_daily, container, false);
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        GraphView graph1 = (GraphView) rootView.findViewById(R.id.graphC);
        //by Art::::::::::::::::::::::::

        //::Initialize DATE on which will be made PHP request, as an example DATE is 'TODAY'
        SimpleDateFormat today = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = today.format(new Date());
        String currentPHP = getData();
        Log.d(TAG, "CurrentStringFromPHP="+currentPHP);
        //::take DATA FROM PHP Session and its Steps Amount in Array






                /*AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("DB Data Extraction");
                dialog.setMessage("Keep Patience...");
                dialog.setNeutralButton("OK", null);
                dialog.create().show();*/
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stringFromPHP = DataHolder.getInstance().getStrPHP();

        Log.d(TAG, "stringFromPHP="+stringFromPHP);
        //showList(stringFromPHP);

        //::::::::::::::::::::::::::::::
                /*BarGraphSeries<DataPoint> series2 = new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 0),
                        new DataPoint(3, 10),
                        new DataPoint(4, 20),
                        //new DataPoint(3, 3),
                        //new DataPoint(4, 1),
                        //new DataPoint(5, 3),
                        //new DataPoint(6, 7)
                });*/
        //String[] str = new String[10];
        JSONArray peoples = null;
        DataPoint[] dataPool = null;
        DataPoint[] dataPool1 = null;
        try {
            JSONObject jsonObj = new JSONObject(stringFromPHP);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            //dataPool = new DataPoint[peoples.length()+1];
            dataPool = new DataPoint[peoples.length()];
            //dataPool[0] = new DataPoint(0,0);
            //dataPool1 = new DataPoint[peoples.length()+1];
            dataPool1 = new DataPoint[peoples.length()];
            //dataPool1[0] = new DataPoint(0,0);
            int i;
            for (i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = (Integer.toString(i + 1));
                Log.d("SESSION N::",id);
                //String id = c.getString(TAG_ID);
                //String user_id = c.getString(TAG_UserID);
                String steps = c.getString(TAG_NAME);
                String calories = c.getString(TAG_ADD);

                int id_int = Integer.parseInt(id);
                int steps_int = Integer.parseInt(steps);
                int calories_int = Integer.parseInt(calories);

                //dataPool[i+1] = new DataPoint(id_int,steps_int);
                //dataPool1[i+1] = new DataPoint(id_int,calories_int);
                dataPool[i] = new DataPoint(id_int,steps_int);
                dataPool1[i] = new DataPoint(id_int,calories_int);

                Log.d(TAG, "ID:"+id);
                //Log.d(TAG, "user_ID:"+user_id);
                Log.d(TAG, "steps:"+steps);
                Log.d(TAG, "calories:"+calories);
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSON EXCEPTION");
            e.printStackTrace();
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPool);
        series.setTitle("Steps");
        series.setDrawBackground(true);
        series.setColor(Color.argb(255, 255, 60, 60));
        series.setBackgroundColor(Color.argb(100, 204, 119, 119));
        series.setDrawDataPoints(true);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Steps");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Session");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(20);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dataPool1);
        series1.setTitle("Calories");
        series1.setDrawBackground(true);
        series1.setColor(Color.argb(255, 255, 60, 60));
        series1.setBackgroundColor(Color.argb(100, 204, 119, 119));
        series1.setDrawDataPoints(true);
        graph1.addSeries(series1);
        graph1.getGridLabelRenderer().setVerticalAxisTitle("Calories");
        graph1.getGridLabelRenderer().setHorizontalAxisTitle("Session");
        graph1.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        graph1.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
        graph1.getViewport().setScalable(true);
        graph1.getViewport().setScrollable(true);
        graph1.getViewport().setScrollableY(true);
        graph1.getViewport().setScalableY(true);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(20);

        graph1.getLegendRenderer().setVisible(true);
        graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        return rootView;
    }

    public static String getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            String myJSON;
            @Override
            protected String doInBackground(String... params) {
                String IP = DataHolder.getInstance().getIP();
                String folder = DataHolder.getInstance().getFolder();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String date = sdf.format(new Date());
                String user_id = DataHolder.getInstance().getUserID();
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                String dailyUrl = "http://"+IP+"/"+folder+"/graph_daily.php?date="+date+"&user_id="+user_id;
                HttpPost httppost = new HttpPost(dailyUrl);
                Log.d("CHECKING DAILY URL::::","ULR="+dailyUrl);
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
