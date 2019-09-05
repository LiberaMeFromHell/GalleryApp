package com.example.galleryapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.galleryapp.PreviewFragment;
import com.example.galleryapp.R;
import com.example.galleryapp.model.ImageContainer;

import java.util.SortedMap;

public class PreviewActivity extends AppCompatActivity {

    private int currentPageIndex, previousPageIndex;
    ImageButton deleteButton;
    SortedMap<Integer,String> imageMap;
    ViewPager viewPager;
    PreviewPagerAdapter pagerAdapter;
    ImageContainer imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        getSupportActionBar().hide();

        deleteButton = findViewById(R.id.deleteButton);

        imageContainer = ImageContainer.getInstance(this.getApplication());

        Bundle extras = getIntent().getExtras();
        int imageID = (Integer) extras.get("_ID");
        imageMap = imageContainer.getImageMap();

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PreviewPagerAdapter(getSupportFragmentManager(), imageMap);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(imageContainer.getIndexByKey(imageID));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                If there is an existed tab, the state variable value is 1.
                It switches to 0 if you try to navigate beyond first tab or last tab.
                */
                int currentPage = viewPager.getCurrentItem();
                if (currentPage == imageMap.size() - 1 || currentPage == 0) {
                    previousPageIndex = currentPageIndex;
                    currentPageIndex = state;
                    if (previousPageIndex == 1 && currentPageIndex == 0) {
                        viewPager.setCurrentItem(currentPage == 0 ? imageMap.size() - 1 : 0);
                    }
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition  = viewPager.getCurrentItem();
                int key = ((PreviewFragment)pagerAdapter.getItem(currentPosition)).getImageID();
                imageContainer.excludeImage(key);
                pagerAdapter  = new PreviewPagerAdapter(getSupportFragmentManager(), imageMap);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(currentPosition);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        imageContainer.saveExcludedList(this);
    }
}
