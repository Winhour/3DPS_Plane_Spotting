package io.github.winhour.model;

public class DistanceContainer {

    private int distance;
    private String icao;

    public DistanceContainer() {
    }

    public DistanceContainer(int distance, String icao) {
        this.distance = distance;
        this.icao = icao;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }
}
