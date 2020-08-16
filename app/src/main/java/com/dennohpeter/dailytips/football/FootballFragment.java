package com.dennohpeter.dailytips.football;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dennohpeter.dailytips.R;
import com.google.android.material.tabs.TabLayout;

public class FootballFragment extends Fragment {
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_football, container, false);

        TabLayout tabLayout = root.findViewById(R.id.football_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Match"));
        tabLayout.addTab(tabLayout.newTab().setText("Live"));

        viewPager = root.findViewById(R.id.football_viewPager);
        viewPager.setAdapter(new FootballPageAdapter(getChildFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;

    }
}