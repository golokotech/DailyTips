package com.dennohpeter.dailytips.football;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dennohpeter.dailytips.football.live_tab.LiveFragment;
import com.dennohpeter.dailytips.football.match_tab.MatchFragment;

import java.util.ArrayList;

public class FootballPageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    FootballPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);

        this.numOfTabs =  numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new LiveFragment();
        }
        return new MatchFragment();
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
