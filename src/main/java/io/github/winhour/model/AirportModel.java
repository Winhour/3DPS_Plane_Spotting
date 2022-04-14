package io.github.winhour.model;

import java.util.ArrayList;
import java.util.List;

public class AirportModel {


    /* Airport object for use within the program */

    private double lat;
    private double lon;
    private String icao;
    private int elevation;

    private boolean isSmall;

    List<FPModel> fpmlist_departures = new ArrayList<>();

    List<Runway_end> runway_ends = new ArrayList<>();

    List<Integer> runway_lengths = new ArrayList<>();

    private double ctr_circle_distance;


    /************************************************************************************************************************************************/

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

    public boolean isSmall() {
        return isSmall;
    }

    public void setSmall(boolean small) {
        isSmall = small;
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

    public List<Runway_end> getRunway_ends() {
        return runway_ends;
    }

    public void setRunway_ends(List<Runway_end> runway_ends) {
        this.runway_ends = runway_ends;
    }

    public void add_to_runway_ends(Runway_end r){
        runway_ends.add(r);
    }

    public List<Integer> getRunway_lengths() {
        return runway_lengths;
    }

    public void setRunway_lengths(List<Integer> runway_lengths) {
        this.runway_lengths = runway_lengths;
    }

    public void add_to_runway_lengths(int l){
        runway_lengths.add(l);
    }

    public double getCtr_circle_distance() {
        return ctr_circle_distance;
    }

    public void setCtr_circle_distance(double ctr_circle_distance) {
        this.ctr_circle_distance = ctr_circle_distance;
    }

    /************************************************************************************************************************************************/

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

    /************************************************************************************************************************************************/

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /************************************************************************************************************************************************/


    @Override
    public String toString() {
        return "AirportModel{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", icao='" + icao + '\'' +
                ", elevation=" + elevation +
                '}';
    }

    /************************************************************************************************************************************************/




}
