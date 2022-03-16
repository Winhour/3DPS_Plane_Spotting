package io.github.winhour.model;

public class Waypoint {

    String id;
    Double lat;
    Double lon;
    int alt;
    int kts;
    int option;
    int route_id;
    int waypoint_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public int getKts() {
        return kts;
    }

    public void setKts(int kts) {
        this.kts = kts;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getWaypoint_id() {
        return waypoint_id;
    }

    public void setWaypoint_id(int waypoint_id) {
        this.waypoint_id = waypoint_id;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "id='" + id + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", alt=" + alt +
                ", kts=" + kts +
                ", option=" + option +
                ", route_id=" + route_id +
                ", waypoint_id=" + waypoint_id +
                '}';
    }
}
