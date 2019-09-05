package com.example.galleryapp.view;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.galleryapp.PreviewFragment;
import java.util.Map;
import java.util.SortedMap;

public class PreviewPagerAdapter extends FragmentStatePagerAdapter {

    private SortedMap<Integer,String> imageMap;
    private PreviewFragment myFragmentContainer[];

    public PreviewPagerAdapter(FragmentManager fm, SortedMap<Integer,String> imageMap) {
        super(fm);
        this.imageMap = imageMap;
        myFragmentContainer = new PreviewFragment[imageMap.size()];
        int i = 0;
        for (Map.Entry item: imageMap.entrySet()) {
            myFragmentContainer[i] = new PreviewFragment((String)item.getValue(),(Integer)item.getKey());
            i++;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return myFragmentContainer[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return imageMap.size();
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = (Bundle) super.saveState();
        bundle.putParcelableArray("states", null); // Never maintain any states from the base class, just null it out
        return bundle;
    }
}
