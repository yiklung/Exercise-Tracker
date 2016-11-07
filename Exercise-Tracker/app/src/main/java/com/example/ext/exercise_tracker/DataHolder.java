package com.example.ext.exercise_tracker;

/**
 * Created by 4325966 on 30/9/2016.
 */
public class DataHolder {
    private String ipAddr, ipFolder, ipFilename, user_id, username, strPHP, email, weight, height, goal;

    public String getIP() {return ipAddr;}
    public String getFolder() {return ipFolder;}
    public String getFilename() {return ipFilename;}
    public String getUserID() {return user_id;}
    public String getUsername() {return username;}
    public String getStrPHP() {return strPHP;}
    public String getWeight() {return weight;}
    public String getHeight() {return height;}
    public String getEmail() {return email;}
    public String getGoal() {return goal;}

    public void setIP(String ipAddr) {this.ipAddr = ipAddr;}
    public void setFolder(String ipFolder) {this.ipFolder = ipFolder;}
    public void setFilename(String ipFilename) {this.ipFilename = ipFilename;}
    public void setUserID(String user_id) {this.user_id = user_id;}
    public void setUsername(String username) {this.username = username;}
    public void setStrPHP(String strPHP) {this.strPHP = strPHP;}
    public void setHeight(String height) {this.height = height;}
    public void setWeight(String weight) {this.weight = weight;}
    public void setEmail(String email) {this.email = email;}
    public void setGoal(String goal) {this.goal = goal;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
