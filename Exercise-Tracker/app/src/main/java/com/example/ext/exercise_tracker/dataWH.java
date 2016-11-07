package com.example.ext.exercise_tracker;

/**
 * Created by 4325966 on 21/9/2016.
 */
public class dataWH {

    private int id;
    private int steps;
    private int calories;
    private String date;

    public dataWH()
    {
    }

    public dataWH(int id,int steps,int calories, String date)
    {
        this.id=id;
        this.steps=steps;
        this.calories=calories;
        this.date=date;
    }

    public void setId(int id) { this.id = id; }
    public void setSteps(int steps) { this.steps = steps; }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getId() { return id; }
    public int getSteps() {
        return steps;
    }
    public int getCalories() { return calories; }
    public String getDate() {
        return date;
    }
}

