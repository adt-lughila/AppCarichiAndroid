package com.appcarichi.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static String getProperty(String key, Context context)  {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();

        try {
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
        } catch (Exception e)  {
            e.printStackTrace();
            return "http://192.168.1.41:8080/resources";
        }

        return properties.getProperty(key);
    }
}
