package com.dennohpeter.dailytips.football;

public class FootballModel {
    Boolean outcome, isLive;
    private String homeTeam, awayTeam, startTime, matchDate, pick, result, odds;


    public FootballModel() {
    }

    public FootballModel(String homeTeam, String awayTeam, String startTime, String matchDate, String pick, String result, String odds, Boolean outcome, Boolean isLive) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
        this.matchDate = matchDate;
        this.pick = pick;
        this.result = result;
        this.odds = odds;
        this.outcome = outcome;
        this.isLive = isLive;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public Boolean getOutcome() {
        return outcome;
    }

    public void setOutcome(Boolean outcome) {
        this.outcome = outcome;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean live) {
        isLive = live;
    }
}
