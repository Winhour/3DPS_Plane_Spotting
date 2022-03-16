package io.github.winhour.model;

public class Runway {

    /* Runway object model */

    private String icao;
    private double width;
    private int surface_type;
    private int runway_shoulder_surface;
    private double runway_smoothness;
    private boolean centre_line_lights;
    private int edge_lights;
    private boolean auto_signs;
    private int runway_start_id;
    private int runway_end_id;
    private int length;

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getSurface_type() {
        return surface_type;
    }

    public void setSurface_type(int surface_type) {
        this.surface_type = surface_type;
    }

    public int getRunway_shoulder_surface() {
        return runway_shoulder_surface;
    }

    public void setRunway_shoulder_surface(int runway_shoulder_surface) {
        this.runway_shoulder_surface = runway_shoulder_surface;
    }

    public double getRunway_smoothness() {
        return runway_smoothness;
    }

    public void setRunway_smoothness(double runway_smoothness) {
        this.runway_smoothness = runway_smoothness;
    }

    public boolean isCentre_line_lights() {
        return centre_line_lights;
    }

    public void setCentre_line_lights(boolean centre_line_lights) {
        this.centre_line_lights = centre_line_lights;
    }

    public int getEdge_lights() {
        return edge_lights;
    }

    public void setEdge_lights(int edge_lights) {
        this.edge_lights = edge_lights;
    }

    public boolean isAuto_signs() {
        return auto_signs;
    }

    public void setAuto_signs(boolean auto_signs) {
        this.auto_signs = auto_signs;
    }

    public int getRunway_start_id() {
        return runway_start_id;
    }

    public void setRunway_start_id(int runway_start_id) {
        this.runway_start_id = runway_start_id;
    }

    public int getRunway_end_id() {
        return runway_end_id;
    }

    public void setRunway_end_id(int runway_end_id) {
        this.runway_end_id = runway_end_id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Runway{" +
                "icao='" + icao + '\'' +
                ", width=" + width +
                ", surface_type=" + surface_type +
                ", runway_shoulder_surface=" + runway_shoulder_surface +
                ", runway_smoothness=" + runway_smoothness +
                ", centre_line_lights=" + centre_line_lights +
                ", edge_lights=" + edge_lights +
                ", auto_signs=" + auto_signs +
                ", runway_start_id=" + runway_start_id +
                ", runway_end_id=" + runway_end_id +
                ", length=" + length +
                '}';
    }

    public int calculateLength(double lat1, double lat2, double lon1, double lon2){

        double result;

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        result = (c * r)*3280.8399;

        return (int) result;
    }




}
