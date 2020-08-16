package com.dennohpeter.dailytips.football.match_tab;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennohpeter.dailytips.R;
import com.dennohpeter.dailytips.football.FootballModel;
import com.dennohpeter.dailytips.football.FootballViewHolder;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<FootballViewHolder> implements Filterable {
    private ArrayList<FootballModel> games, filteredGames;
    private Context context;

    MatchAdapter(Context context, ArrayList<FootballModel> games) {
        this.context = context;
        this.games = games;
        this.filteredGames = games;
    }
    void clear(){
        this.games.clear();
    }
    void add(ArrayList<FootballModel> games){
        this.games.addAll(games);
        notifyDataSetChanged();
    }
    void remove(int position){
        games.remove(position);
        notifyItemRemoved(position);
    }

    int size() {
        return filteredGames.size();
    }


    @NonNull
    @Override
    public FootballViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_card, parent, false);
        return new FootballViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FootballViewHolder holder, int position) {
        String[] teams = filteredGames.get(position).getTeams().split("-");
        holder.home_team.setText(teams[0].trim());
        holder.away_team.setText(teams[1].trim());
        holder.start_time.setText(filteredGames.get(position).getStartTime());
        String pick = context.getString(R.string.pick, filteredGames.get(position).getPick());
        holder.pick.setText(pick);
        String result = context.getString(R.string.result, filteredGames.get(position).getResult());
        holder.result.setText(result);
        holder.won_or_lost.setImageResource(filteredGames.get(position).getIcon());
        String odds = context.getString(R.string.odds, filteredGames.get(position).getOdds());
        holder.odds.setText(odds);


    }

    @Override
    public int getItemCount() {
        return filteredGames.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String queryText = charSequence.toString().toLowerCase();
                if (queryText.isEmpty()){
                    filteredGames = games;
                }
                else {
                    ArrayList<FootballModel> matching_games = new ArrayList<>();
                    for(FootballModel match: games){
                        if (match.getTeams().toLowerCase().contains(queryText) || match.getStartTime().toLowerCase().contains(queryText)){
                            matching_games.add(match);
                        }
                    }
                    filteredGames = matching_games;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredGames;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                filteredGames = (ArrayList<FootballModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
