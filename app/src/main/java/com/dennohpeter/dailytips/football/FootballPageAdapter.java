package com.dennohpeter.dailytips.football;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dennohpeter.dailytips.R;

public class FootballPageAdapter extends FragmentPagerAdapter {
    private Context context;

    FootballPageAdapter(FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MatchFragment();
        Bundle bundle = new Bundle();
        if (position == 1) {
            bundle.putBoolean("isLive", true);
        }
        else {
            bundle.putBoolean("isLive", false);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.tab_titles)[position];
    }
}
