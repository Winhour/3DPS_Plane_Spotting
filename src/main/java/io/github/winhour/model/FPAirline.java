package io.github.winhour.model;

public class FPAirline {

    String file_name;
    String icao;
    String iata;
    String callsign;
    String name;
    String type;
    String season;
    String author;
    String country;
    String logo;
    String sourceurl;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }

    @Override
    public String toString() {
        return "FPAirline{" +
                "file_name='" + file_name + '\'' +
                ", icao='" + icao + '\'' +
                ", iata='" + iata + '\'' +
                ", callsign='" + callsign + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", season='" + season + '\'' +
                ", author='" + author + '\'' +
                ", country='" + country + '\'' +
                ", logo='" + logo + '\'' +
                ", sourceurl='" + sourceurl + '\'' +
                '}';
    }
}
