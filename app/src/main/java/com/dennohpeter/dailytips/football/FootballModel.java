package com.dennohpeter.dailytips.football;

public class FootballModel {
    private String teams,  start_time,  match_date,  pick,  result, odds;
    private int icon;

     public FootballModel(String teams, String start_time, String match_date, String pick, String result, String odds, int icon) {
        this.teams = teams;
        this.start_time = start_time;
        this.match_date = match_date;
        this.pick = pick;
        this.result = result;
        this.odds = odds;
        this.icon = icon;
    }
    public FootballModel(String teams){
         this.teams = teams;
    }

    public String getTeams() {
        return teams;
    }

    public String getStartTime() {
        return start_time;
    }

    public String getMatchDate() {
        return match_date;
    }

    public String getPick() {
        return pick;
    }

    public String getResult() {
        return result;
    }

    public String getOdds() {
        return odds;
    }

    public int getIcon() {
        return icon;
    }
}
