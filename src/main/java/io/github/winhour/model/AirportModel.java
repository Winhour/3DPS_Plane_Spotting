package io.github.winhour.model;

import java.util.ArrayList;
import java.util.List;

public class AirportModel {

    /* Airport object for use within the program */

    private double lat;
    private double lon;
    private String icao;
    private int elevation;

    List<FPModel> fpmlist_departures = new ArrayList<>();

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public List<FPModel> getFpmlist_departures() {
        return fpmlist_departures;
    }

    public void setFpmlist_departures(List<FPModel> fpmlist) {
        this.fpmlist_departures = fpmlist;
    }

    public void add_to_fpmlist_departures(FPModel f){
        fpmlist_departures.add(f);
    }

    private void modifyTimes(int interval){

        //change deptime and arrtime so they're modified by the interval

        for (FPModel f : fpmlist_departures){

            for (FPModel f2 : fpmlist_departures){

                if (f.getDep_time() == f2.getDep_time() && f.getIcao24() != f2.getIcao24() && f.getDep_apt() == f2.getDep_apt()){

                    f2.setDep_time(f2.getDep_time()+interval);
                    f2.setArr_time(f2.getArr_time()+interval);

                }

            }

        }

    }


    @Override
    public String toString() {
        return "AirportModel{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", icao='" + icao + '\'' +
                ", elevation=" + elevation +
                '}';
    }




}
