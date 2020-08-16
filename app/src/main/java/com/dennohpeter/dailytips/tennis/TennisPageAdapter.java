package com.dennohpeter.dailytips.tennis;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.dennohpeter.dailytips.tennis.live_tab.LiveFragment;
import com.dennohpeter.dailytips.tennis.match_tab.MatchFragment;

import java.util.ArrayList;

public class TennisPageAdapter extends FragmentPagerAdapter {
    private ArrayList<TennisModel> games;
    private int numOfTabs;
    public TennisPageAdapter(FragmentManager fm, int numOfTabs, ArrayList<TennisModel> games) {
        super(fm, numOfTabs);

        this.numOfTabs = numOfTabs;
        this.games = games;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MatchFragment(games);
            case 1:
                return new LiveFragment(games);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

