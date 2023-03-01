package com.lnct.bhopal.ac.in.idealab;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.lnct.ac.in.idealab.Models.User;

import java.io.File;
import java.io.IOException;

public class Utils {

    private static Utils util = new Utils();
    private static Gson gson = new Gson();

    public Utils() {

    }

    public static Utils getInstance() {
        return util;
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager manager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean hasStoragePermission(Context c) {
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static void requestStoragePermission(Context c) {
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)c, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
        if(ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)c, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }

    public static File getDataDir(Context c) {
        return c.getDataDir();
    }

    public static SharedPreferences getPrefs(Context c) {
        return c.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getPrefsEditor(Context c) {
        return c.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
    }

    public static void createDataFile(Context c) {
        File root = getDataDir(c);
    }

    public static void createImageCacheDir(Context c) throws IOException {
        File f = new File(c.getCacheDir(), File.separator + "event_image");
        Log.i("image cache file----", f.getAbsolutePath());
        if(!f.exists()) {
            f.mkdir();
        }
    }

    public static File getImageCacheDir(Context c) {
        return new File(c.getCacheDir()+File.separator+"event_image");
    }

    public static void saveUser(Context context , User u){
        SharedPreferences.Editor editor = getPrefsEditor(context);
        String userObj = gson.toJson(u);
        editor.putString("USER",userObj);
        editor.apply();
    }

    public static User getUser(Context context){
        if(getPrefs(context).contains("USER")){
            String user = getPrefs(context).getString("USER","{}");
            return gson.fromJson(user,User.class);
        }

        return null;

    }

    public static boolean isUserPresent(Context c){
        if(getPrefs(c).contains("USER")) return true;
        else return false;
    }

    public static void deleteUser(Context c){
        SharedPreferences.Editor editor = getPrefsEditor(c);
        editor.remove("USER");
        editor.commit();
    }

}
