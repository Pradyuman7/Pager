package com.pd.cards;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pd.pager.PagerLayout;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        PagerLayout pager = findViewById(R.id.rl_main);

        List<String> titleList = new ArrayList<>();
        titleList.add("Page1");
        titleList.add("Page2");
        titleList.add("Page3");
        titleList.add("Page4");
        titleList.add("Page5");

        if(pager == null)
            Log.i("Pager_Null","Pager is null");


        pager.setTitles(titleList);


        fragments.add(new Page1());
        fragments.add(new Page2());
        fragments.add(new Page3());
        fragments.add(new Page4());
        fragments.add(new Page5());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        pager.setAdapter(adapter);

    }

    @SuppressWarnings("unchecked")
    private <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }
}