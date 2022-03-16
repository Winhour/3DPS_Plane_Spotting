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
}
