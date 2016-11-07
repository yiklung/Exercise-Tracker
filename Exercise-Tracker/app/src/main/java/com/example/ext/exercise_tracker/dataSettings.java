package com.example.ext.exercise_tracker;

/**
 * Created by 4325966 on 17/10/2016.
 */
public class dataSettings {
    private int id;
    private String ip;
    private String file;
    private String folder;

    public dataSettings()
    {
    }

    public dataSettings(int id,String ip,String file, String folder)
    {
        this.id=id;
        this.ip=ip;
        this.file=file;
        this.folder=folder;
    }

    public void setId(int id) { this.id = id; }
    public void setSteps(String ip) { this.ip = ip; }
    public void setCalories(String file) {
        this.file = file;
    }
    public void setDate(String folder) {
        this.folder = folder;
    }

    public int getId() { return id; }
    public String getIP() {
        return ip;
    }
    public String getFile() { return file; }
    public String getFolder() {
        return folder;
    }
}
