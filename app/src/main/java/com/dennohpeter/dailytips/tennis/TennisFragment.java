package com.dennohpeter.dailytips.tennis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dennohpeter.dailytips.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TennisFragment extends Fragment {
    private ViewPager viewPager;
    private String date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            date = getArguments().getString("date");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_tennis, container, false);
        TabLayout tabLayout = root.findViewById(R.id.tennis_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Match"));
        tabLayout.addTab(tabLayout.newTab().setText("Live"));

        viewPager = root.findViewById(R.id.tennis_viewPager);
        ArrayList<TennisModel> games = new ArrayList<>();
        viewPager.setAdapter(new TennisPageAdapter(getChildFragmentManager(), tabLayout.getTabCount(), games));
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