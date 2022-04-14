package io.github.winhour.model;

public class Runway_end {

    /* Runway end object model */

    private String icao;
    private String designator;
    private int id;
    private double latitude;
    private double longitude;
    private double displaced_threshold;
    private double overrun;
    private int runway_markings;
    private int approach_lighting;
    private boolean tdz_lighting;
    private int reil;

    private double lat_side_one;
    private double lon_side_one;
    private double lat_side_two;
    private double lon_side_two;

    private double lat_star_point;
    private double lon_star_point;;
    private double altitude_star_point;

    private double lat_sid_point;
    private double lon_sid_point;
    private double altitude_sid_point;

    private int zone_length;
    private int zone_width;

    private double S1_lat;
    private double S1_lon;

    private double S2_lat;
    private double S2_lon;

    private double S3_lat;
    private double S3_lon;

    private double S4_lat;
    private double S4_lon;




    /************************************************************************************************************************************************/


    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDisplaced_threshold() {
        return displaced_threshold;
    }

    public void setDisplaced_threshold(double displaced_threshold) {
        this.displaced_threshold = displaced_threshold;
    }

    public double getOverrun() {
        return overrun;
    }

    public void setOverrun(double overrun) {
        this.overrun = overrun;
    }

    public int getRunway_markings() {
        return runway_markings;
    }

    public void setRunway_markings(int runway_markings) {
        this.runway_markings = runway_markings;
    }

    public int getApproach_lighting() {
        return approach_lighting;
    }

    public void setApproach_lighting(int approach_lighting) {
        this.approach_lighting = approach_lighting;
    }

    public boolean isTdz_lighting() {
        return tdz_lighting;
    }

    public void setTdz_lighting(boolean tdz_lighting) {
        this.tdz_lighting = tdz_lighting;
    }

    public int getReil() {
        return reil;
    }

    public void setReil(int reil) {
        this.reil = reil;
    }

    /************************************************************************************************************************************************/

    public double getLat_side_one() {
        return lat_side_one;
    }

    public void setLat_side_one(double lat_side_one) {
        this.lat_side_one = lat_side_one;
    }

    public double getLon_side_one() {
        return lon_side_one;
    }

    public void setLon_side_one(double lon_side_one) {
        this.lon_side_one = lon_side_one;
    }

    public double getLat_side_two() {
        return lat_side_two;
    }

    public void setLat_side_two(double lat_side_two) {
        this.lat_side_two = lat_side_two;
    }

    public double getLon_side_two() {
        return lon_side_two;
    }

    public void setLon_side_two(double lon_side_two) {
        this.lon_side_two = lon_side_two;
    }

    public double getLat_star_point() {
        return lat_star_point;
    }

    public void setLat_star_point(double lat_star_point) {
        this.lat_star_point = lat_star_point;
    }

    public double getLon_star_point() {
        return lon_star_point;
    }

    public void setLon_star_point(double lon_star_point) {
        this.lon_star_point = lon_star_point;
    }

    /************************************************************************************************************************************************/

    @Override
    public String toString() {
        return "Runway_end{" +
                "icao='" + icao + '\'' +
                ", designator='" + designator + '\'' +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", displaced_threshold=" + displaced_threshold +
                ", overrun=" + overrun +
                ", runway_markings=" + runway_markings +
                ", approach_lighting=" + approach_lighting +
                ", tdz_lighting=" + tdz_lighting +
                ", reil=" + reil +
                '}';
    }

    /************************************************************************************************************************************************/

}
