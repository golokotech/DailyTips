package com.dennohpeter.dailytips.football;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;

import java.util.ArrayList;
import java.util.List;

public class FootballAdapter extends RecyclerView.Adapter<FootballViewHolder> implements Filterable {

    private List<FootballModel> matches;
    private List<FootballModel> matchesFiltered;

    public FootballAdapter(List<FootballModel> matches) {
        this.matches = matches;
        this.matchesFiltered = matches;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String pattern = constraint.toString().toLowerCase();
                if (pattern.isEmpty()){
                    matchesFiltered = matches;
                }
                else {
                    List<FootballModel> filteredList = new ArrayList<>();
                    for (FootballModel model: matches){
                        if (model.getHomeTeam().toLowerCase().contains(pattern) || model.getAwayTeam().toLowerCase().contains(pattern)){
                            filteredList.add(model);
                        }
                    }
                    matchesFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = matchesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                matchesFiltered = (ArrayList<FootballModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public FootballViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_card, parent, false);
        return new FootballViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FootballViewHolder holder, int position) {
        FootballModel model = matchesFiltered.get(position);
        holder.setHomeTeam(model.getHomeTeam());
        holder.setAwayTeam(model.getAwayTeam());
        holder.setOdds(model.getOdds());
        holder.setStartTime(model.getStartTime());
        holder.setResult(model.getResult());
        holder.setPickField(model.getPick());
        holder.setMatchStatus(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return matchesFiltered.size();
    }
}
