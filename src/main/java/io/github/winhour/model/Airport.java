package io.github.winhour.model;

public class Airport {

    /* Airport object model */

    private String icao;
    private String elevation;
    private boolean atc_tower;
    private String name;
    private boolean closed;

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public boolean isAtc_tower() {
        return atc_tower;
    }

    public void setAtc_tower(boolean atc_tower) {
        this.atc_tower = atc_tower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "icao='" + icao + '\'' +
                ", elevation='" + elevation + '\'' +
                ", atc_tower=" + atc_tower +
                ", name='" + name + '\'' +
                ", closed=" + closed +
                '}';
    }
}
