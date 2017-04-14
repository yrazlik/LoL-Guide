package com.yrazlik.loltr.utils;

import android.content.Context;

import com.yrazlik.loltr.data.RecentSearchItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by yrazlik on 14/04/17.
 */

public class SharedPrefsUtils {

    public static void saveRecentSearchesArray(ArrayList<RecentSearchItem> obj, Context context) {

        String fileName = "com.yrazlik.loltr.recentsearches";
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            ObjectOutputStream out = new ObjectOutputStream(fos);

            out.writeObject(obj);

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<RecentSearchItem> loadRecentSearchesArrayList(Context context) {
        try {
            String fileName = "com.yrazlik.loltr.recentsearches";
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(fis);

            ArrayList<RecentSearchItem> obj = (ArrayList<RecentSearchItem>) in.readObject();

            in.close();
            return obj;

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
