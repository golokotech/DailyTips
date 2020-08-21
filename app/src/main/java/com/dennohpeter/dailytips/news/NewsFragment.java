package com.dennohpeter.dailytips.news;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NewsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "NewsFragment";
    private FirestoreRecyclerAdapter<NewsModel, NewsViewHolder> recyclerAdapter;
    private RecyclerView recyclerView;
    private Query query;
    private ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;
    private LinearLayout nothing_to_show;
    private TextView no_events_description;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        query = FirebaseFirestore.getInstance().collection("news").orderBy("publishedDate");
        this.context = getContext();
        loadAd();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        this.context = getContext();
        recyclerView = root.findViewById(R.id.news_container);
        nothing_to_show = root.findViewById(R.id.nothing_to_show);
        no_events_description = root.findViewById(R.id.no_events_description);

        progressBar = root.findViewById(R.id.progress_circular);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        populateRecyclerViewer();
        return root;
    }

    private void populateRecyclerViewer() {
        progressBar.setVisibility(View.VISIBLE);
        FirestoreRecyclerOptions<NewsModel> recyclerOptions = new FirestoreRecyclerOptions
                .Builder<NewsModel>()
                .setQuery(query, NewsModel.class)
                .build();

        recyclerAdapter = new FirestoreRecyclerAdapter<NewsModel, NewsViewHolder>(
                recyclerOptions
        ) {
            @Override
            protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull NewsModel model) {
                holder.setHeading(model.getHeading());
                holder.setCoverImage(model.getCoverImage());
                holder.setContent(model.getContent());
                holder.setPublishedDate(model.getPublishedDate());
            }

            @NonNull
            @Override
            public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_card, parent, false);
                return new NewsViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    showNothingToShowWidget();
                } else {
                    hideNothingToShowWidget();
                }
                progressBar.setVisibility(View.GONE);
                showAd();
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void showNothingToShowWidget() {
        no_events_description.setText(R.string.no_news_text);
        nothing_to_show.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideNothingToShowWidget() {
        nothing_to_show.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeGroup(R.id.action_orderBy);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();

    }

    private void loadAd() {
        MobileAds.initialize(getContext(), initializationStatus -> {
        });
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    private void showAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "The interstitial wasn't loaded yet.");
        }

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