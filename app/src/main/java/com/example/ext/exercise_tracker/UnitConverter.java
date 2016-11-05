package com.example.ext.exercise_tracker;

import android.util.Log;

/**
 * Created by 4325966 on 28/10/2016.
 */
public class UnitConverter {
    final static double wFactor = 0.57;
    public static double step2calories(double S){
        //INITIAL DATA:: height in cm, weight in kg::
        String str_height = DataHolder.getInstance().getHeight();
        String str_weight = DataHolder.getInstance().getWeight();
        Log.d("FROM DATAHOLDER::","Weight="+str_weight+" Height="+str_height);
        int height = Integer.parseInt(str_height);
        int weight = Integer.parseInt(str_weight);

        double Pace = height*0.415;
        double CaloriesPerMile = wFactor*weight*2.2; //2.2 pounds in 1 kg;
        double StepsPerMile = 160934/Pace; //160934 cm in 1 mile

        double convertionFactor = CaloriesPerMile/StepsPerMile;
        double Calories = S*convertionFactor;
        //DataHolder.getInstance().ge
        return Calories;
    }
}
