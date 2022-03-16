package io.github.winhour.model;

public class Leg {

    String aigroute;
    int alt;
    String dep_apt;
    String arr_apt;
    int dep_time;
    int arr_time;
    String callsign;
    int flightnumber;
    String flightrule;
    String tng;
    int reg_id;

    public String getAigroute() {
        return aigroute;
    }

    public void setAigroute(String aigroute) {
        this.aigroute = aigroute;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public String getDep_apt() {
        return dep_apt;
    }

    public void setDep_apt(String dep_apt) {
        this.dep_apt = dep_apt;
    }

    public String getArr_apt() {
        return arr_apt;
    }

    public void setArr_apt(String arr_apt) {
        this.arr_apt = arr_apt;
    }

    public int getDep_time() {
        return dep_time;
    }

    public void setDep_time(int dep_time) {
        this.dep_time = dep_time;
    }

    public int getArr_time() {
        return arr_time;
    }

    public void setArr_time(int arr_time) {
        this.arr_time = arr_time;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public int getFlightnumber() {
        return flightnumber;
    }

    public void setFlightnumber(int flightnumber) {
        this.flightnumber = flightnumber;
    }

    public String getFlightrule() {
        return flightrule;
    }

    public void setFlightrule(String flightrule) {
        this.flightrule = flightrule;
    }

    public String getTng() {
        return tng;
    }

    public void setTng(String tng) {
        this.tng = tng;
    }

    public int getReg_id() {
        return reg_id;
    }

    public void setReg_id(int reg_id) {
        this.reg_id = reg_id;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "aigroute='" + aigroute + '\'' +
                ", alt=" + alt +
                ", dep_apt='" + dep_apt + '\'' +
                ", arr_apt='" + arr_apt + '\'' +
                ", dep_time=" + dep_time +
                ", arr_time=" + arr_time +
                ", callsign='" + callsign + '\'' +
                ", flightnumber=" + flightnumber +
                ", flightrule='" + flightrule + '\'' +
                ", tng='" + tng + '\'' +
                ", reg_id=" + reg_id +
                '}';
    }

}
