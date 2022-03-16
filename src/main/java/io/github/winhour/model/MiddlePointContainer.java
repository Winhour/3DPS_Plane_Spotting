package io.github.winhour.model;

public class MiddlePointContainer {

    int flag_state = 0;

    /*
    0 for 0 points added
    1 for 1 points added
    2 for 2 points added
    can't be anything else
     */

    String icao;
    double lat1, lat2, lon1, lon2;
    double latout, lonout;

    public int getFlag_state() {
        return flag_state;
    }

    public void setFlag_state(int flag_state) {
        this.flag_state = flag_state;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public double getLat1() {
        return lat1;
    }

    public void setLat1(double lat1) {
        this.lat1 = lat1;
    }

    public double getLat2() {
        return lat2;
    }

    public void setLat2(double lat2) {
        this.lat2 = lat2;
    }

    public double getLon1() {
        return lon1;
    }

    public void setLon1(double lon1) {
        this.lon1 = lon1;
    }

    public double getLon2() {
        return lon2;
    }

    public void setLon2(double lon2) {
        this.lon2 = lon2;
    }

    public double getLatout() {
        return latout;
    }

    public void setLatout() {
        this.latout = lat1 + ((lat2-lat1)/2);
    }

    public double getLonout() {
        return lonout;
    }

    public void setLonout() {
        this.lonout = lon1 + ((lon2-lon1)/2);
    }
}
