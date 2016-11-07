package com.example.ext.exercise_tracker;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 4325966 on 7/11/2016.
 */
public class Validation {

    public static InputFilter getFilter(final String s){
        //::SETUP input FILTER
        InputFilter filterAN= new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    String checkMe = String.valueOf(source.charAt(i));
                    Pattern pattern;
                    switch (s){
                        case "AN": {
                            pattern = Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*");
                        }
                        break;
                        case "A":{
                            pattern = Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]*");
                        }
                        break;
                        case "N":{
                            pattern = Pattern.compile("[1234567890]*");
                        }
                        break;
                        case "ANat":{
                            pattern = Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@._]*");
                        }
                        break;
                        case "N.":{
                            pattern = Pattern.compile("[1234567890.]*");
                        }
                        break;
                        case "A.": {
                            pattern = Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.]*");
                        }
                        break;
                        default:
                            pattern = Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*");
                    }


                    Matcher matcher = pattern.matcher(checkMe);
                    boolean valid = matcher.matches();
                    if(!valid){
                        Log.d("", "invalid");
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter returnFilter;

        return filterAN;
    }
    public static boolean isEmpty(String s){
        if (s.equals("") || s.equals(null)) {
            return true;
        }else return false;
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public final static boolean getValidation(int valiArray[]){
        int i,sum = 0;
        for (i=0; i<valiArray.length; i++){
            sum += valiArray[i];
        }
        if (sum == valiArray.length) {
            return true;
        }else {return false;}
    }
}
