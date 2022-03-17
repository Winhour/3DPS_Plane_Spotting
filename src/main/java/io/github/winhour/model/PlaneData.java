package io.github.winhour.model;

import java.text.DecimalFormat;

public class PlaneData {

    //Primary container for plane data

    private int packId;
    private String icao24;
    private String icao;
    private String country;
    private int unix_time;
    private int unix_last;
    private double latitude;
    private double longitude;
    private Double baro_altitude;
    private boolean on_ground;
    private Double velocity;
    private Double true_track;
    private Double vertical_rate;
    private String squawk;
    private boolean spi;
    private int position_source;
    private Double altitude;
    private String source="d0";
    private String callsign;
    private String departure;
    private String arrival;
    private String type;
    private String flightnumber;
    private String model;
    private String registration;
    private String opicaocode;


    public PlaneData() {
    }

    public PlaneData(int packId, String icao24, String icao, String country, int unix_time, int unix_last, double latitude, double longitude, Double baro_altitude,
                     boolean on_ground, Double velocity, Double true_track, Double vertical_rate, String squawk, boolean spi, int position_source, Double altitude,
                     String source, String callsign, String departure, String arrival, String type, String flightnumber, String model, String registration, String opicaocode) {

        this.packId = packId;
        this.icao24 = icao24;
        this.icao = icao;
        this.country = country;
        this.unix_time = unix_time;
        this.unix_last = unix_last;
        this.latitude = latitude;
        this.longitude = longitude;
        this.baro_altitude = baro_altitude;
        this.on_ground = on_ground;
        this.velocity = velocity;
        this.true_track = true_track;
        this.vertical_rate = vertical_rate;
        this.squawk = squawk;
        this.spi = spi;
        this.position_source = position_source;
        this.altitude = altitude;
        this.source = source;
        this.callsign = callsign;
        this.departure = departure;
        this.arrival = arrival;
        this.type = type;
        this.flightnumber = flightnumber;
        this.model = model;
        this.registration = registration;
        this.opicaocode = opicaocode;

    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    public String getIcao24() {
        return icao24;
    }

    public void setIcao24(String icao24) {
        this.icao24 = icao24;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(int unix_time) {
        this.unix_time = unix_time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        double roundLat = Math.round(latitude*10000.0)/10000.0;
        this.latitude = roundLat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        double roundLon = Math.round(longitude*10000.0)/10000.0;
        this.longitude = roundLon;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public int getUnix_last() {
        return unix_last;
    }

    public void setUnix_last(int unix_last) {
        this.unix_last = unix_last;
    }

    public Double getBaro_altitude() {
        return baro_altitude;
    }

    public void setBaro_altitude(Double baro_altitude) {
        this.baro_altitude = baro_altitude;
    }

    public boolean isOn_ground() {
        return on_ground;
    }

    public void setOn_ground(boolean on_ground) {
        this.on_ground = on_ground;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public Double getTrue_track() {
        return true_track;
    }

    public void setTrue_track(Double true_track) {
        this.true_track = true_track;
    }

    public Double getVertical_rate() {
        return vertical_rate;
    }

    public void setVertical_rate(Double vertical_rate) {
        this.vertical_rate = vertical_rate;
    }

    public String getSquawk() {
        return squawk;
    }

    public void setSquawk(String squawk) {
        this.squawk = squawk;
    }

    public boolean isSpi() {
        return spi;
    }

    public void setSpi(boolean spi) {
        this.spi = spi;
    }

    public int getPosition_source() {
        return position_source;
    }

    public void setPosition_source(int position_source) {
        this.position_source = position_source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlightnumber() {
        return flightnumber;
    }

    public void setFlightnumber(String flightnumber) {
        this.flightnumber = flightnumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getOpicaocode() {
        return opicaocode;
    }

    public void setOpicaocode(String opicaocode) {
        this.opicaocode = opicaocode;
    }


    @Override
    public String toString() {
        return "PlaneData{" +
                "packId = '" + packId + '\'' +
                ", icao24 = '" + icao24 + '\'' +
                ", icao = '" + icao + '\'' +
                ", country = '" + country + '\'' +
                ", unix_time = " + unix_time +
                ", unix_last = " + unix_last +
                ", latitude = " + latitude +
                ", longitude = " + longitude +
                ", baro_altitude = " + baro_altitude +
                ", altitude = " + altitude +
                ", on_ground = " + on_ground +
                ", velocity = " + velocity +
                ", true_track = " + true_track +
                ", vertical_rate = " + vertical_rate +
                ", squawk = '" + squawk + '\'' +
                ", spi = " + spi +
                ", position_source = " + position_source +
                ", Source = " + source +
                '}';
    }

}
