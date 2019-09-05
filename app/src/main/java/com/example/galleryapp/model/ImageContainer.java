package com.example.galleryapp.model;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ImageContainer {

    private static ImageContainer instance;
    private SortedMap<Integer, String> imageMap;
    private Set<Integer> excludedSet;
    private static Application application;
    private SharedPreferences preferences;

    private ImageContainer(Application application) {

        ImageContainer.application = application;
        initImageMap(application);
        preferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }

    public static synchronized ImageContainer getInstance(Application application) {

        if (instance == null) {
            instance = new ImageContainer(application);
        }
        return instance;
    }

    private void initImageMap(Application application) {

        if (imageMap == null)
            imageMap = new TreeMap<>(Collections.<Integer>reverseOrder());
        initExclusionSet();
        String[] items = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media._ID, MediaStore.MediaColumns.DATE_MODIFIED,};

        Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        //Gathering external and internal images' URIs
        Cursor cursorExternal = application.getContentResolver().query(uriExternal, items, null, null, null);
        Cursor cursorInternal = application.getContentResolver().query(uriInternal, items, null, null, null);

        Cursor cursorMediator = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});

        int column_index_data = cursorMediator.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int column_index_id = cursorMediator.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

        while (cursorMediator.moveToNext()) {
            String fullImagePath = cursorMediator.getString(column_index_data);
            int imageID = Integer.parseInt(cursorMediator.getString(column_index_id));
            imageMap.put(imageID, fullImagePath);
        }

        filterImages();
    }

    private void initExclusionSet() {

        if (excludedSet == null)
            excludedSet = new HashSet<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        String str = preferences.getString("EXCLUDED", null);
        if (str != null) {
            Log.d("Share load: " + application.toString(), str.toString());
            StringTokenizer sT = new StringTokenizer(str, ",");

            for (int i = 0; i < sT.countTokens(); i++) {
                excludedSet.add(Integer.parseInt(sT.nextToken()));
            }
        } else {
            Log.d("Share load: " + application.toString(), "its null in your eyes");
        }

    }

    private void filterImages() {

        if (excludedSet != null && excludedSet.size() > 0)
            for (Integer i: excludedSet) {
                Log.d("Filter", "filterImages: " + i);
                imageMap.remove(i);
            }
    }

    public void saveExcludedList(Activity activity) {

        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        StringBuilder str = new StringBuilder();
        for (Integer i : excludedSet) {
            str.append(i).append(",");
        }

        editor.putString("EXCLUDED", str.toString());
        Log.d("Share save: " + activity.toString(), str.toString());
        editor.commit();
    }

    public void excludeImage(int _id) {

        excludedSet.add(_id);
        for (Integer id : excludedSet) {
            Log.d(TAG, "excludeImage: " + id);
        }
        initImageMap(application);
    }

    public SortedMap<Integer, String> getImageMap() {
        return imageMap;
    }

    public int getIndexByKey(int key) {

        ArrayList<Integer> list = new ArrayList<Integer>(imageMap.keySet());
        return list.indexOf(key);
    }
}