package io.github.winhour.model;

import java.util.ArrayList;
import java.util.List;

public class Route {

    String name;
    int min;
    int max;
    int alt;
    String rule;
    List<Waypoint> waypoints = new ArrayList<>();
    int route_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    @Override
    public String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", alt=" + alt +
                ", rule='" + rule + '\'' +
                ", waypoints=" + waypoints +
                ", route_id=" + route_id +
                '}';
    }
}
