package com.dennohpeter.dailytips.football;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dennohpeter.dailytips.R;
import com.google.android.material.tabs.TabLayout;

public class FootballFragment extends Fragment implements SearchView.OnQueryTextListener {

    private MenuItem orderBy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_football, container, false);

        ViewPager viewPager = root.findViewById(R.id.football_viewPager);
        PagerAdapter pagerAdapter = new FootballPageAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getContext());
        viewPager.setAdapter(pagerAdapter);


        TabLayout tabLayout = root.findViewById(R.id.football_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return root;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        orderBy = menu.add(getContext().getString(R.string.orderBy));
        orderBy.setIcon(R.drawable.ic_order_by_icon);
        orderBy.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item == orderBy) {
            Toast.makeText(getContext(), "OrderBy", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(this.getString(R.string.search_match_hint));
            searchView.setOnQueryTextListener(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}