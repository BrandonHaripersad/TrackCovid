package com.example.trackcovid.common;

public class CaseTuple {
    int cases;
    int deaths;
    int recoveries;

    public CaseTuple(int cases, int deaths, int recoveries){
        this.cases = cases;
        this.deaths = deaths;
        this.recoveries = recoveries;
    }

    public void setCases(int numCases){
        cases = numCases;
    }

    public int getCases(){
        return cases;
    }

    public void setDeaths(int numDeaths){
        deaths = numDeaths;
    }

    public int getDeaths(){
        return deaths;
    }

    public void setRecoveries(int numRecoveries){
        recoveries = numRecoveries;
    }

    public int getRecoveries(){
        return recoveries;
    }
}
