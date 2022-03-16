package io.github.winhour.model;

import java.util.List;

public class FlightPlan {

    String airport_fs;
    List<String> aigRouteList;
    List<Aircraft> aircraftList;
    int fp_id;
    String file_name;

    public String getAirport_fs() {
        return airport_fs;
    }

    public void setAirport_fs(String airport_fs) {
        this.airport_fs = airport_fs;
    }

    public List<String> getAigRouteList() {
        return aigRouteList;
    }

    public void setAigRouteList(List<String> aigRouteList) {
        this.aigRouteList = aigRouteList;
    }

    public List<Aircraft> getAircraftList() {
        return aircraftList;
    }

    public void setAircraftList(List<Aircraft> aircraftList) {
        this.aircraftList = aircraftList;
    }

    public int getFp_id() {
        return fp_id;
    }

    public void setFp_id(int fp_id) {
        this.fp_id = fp_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
