package io.github.winhour.model;

public class AircraftDatabaseModel {

    private String icao24;
    private String registration;
    private String manufacturericao;
    private String manufacturername;
    private String model;
    private String typecode;
    private String serialnumber;
    private String linenumber;
    private String icaoaircrafttype;
    private String operator;
    private String operatorcallsign;
    private String operatoricao;
    private String operatoriata;
    private String owner;
    private String testreg;
    private String registered;
    private String reguntil;
    private String status;
    private String built;
    private String firstflightdate;
    private String seatconfiguration;
    private String engines;
    private String modes;
    private String adsb;
    private String acars;
    private String notes;
    private String categoryDescription;

    public String getIcao24() {
        return icao24;
    }

    public void setIcao24(String icao24) {
        this.icao24 = icao24;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getManufacturericao() {
        return manufacturericao;
    }

    public void setManufacturericao(String manufacturericao) {
        this.manufacturericao = manufacturericao;
    }

    public String getManufacturername() {
        return manufacturername;
    }

    public void setManufacturername(String manufacturername) {
        this.manufacturername = manufacturername;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(String linenumber) {
        this.linenumber = linenumber;
    }

    public String getIcaoaircrafttype() {
        return icaoaircrafttype;
    }

    public void setIcaoaircrafttype(String icaoaircrafttype) {
        this.icaoaircrafttype = icaoaircrafttype;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorcallsign() {
        return operatorcallsign;
    }

    public void setOperatorcallsign(String operatorcallsign) {
        this.operatorcallsign = operatorcallsign;
    }

    public String getOperatoricao() {
        return operatoricao;
    }

    public void setOperatoricao(String operatoricao) {
        this.operatoricao = operatoricao;
    }

    public String getOperatoriata() {
        return operatoriata;
    }

    public void setOperatoriata(String operatoriata) {
        this.operatoriata = operatoriata;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTestreg() {
        return testreg;
    }

    public void setTestreg(String testreg) {
        this.testreg = testreg;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getReguntil() {
        return reguntil;
    }

    public void setReguntil(String reguntil) {
        this.reguntil = reguntil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuilt() {
        return built;
    }

    public void setBuilt(String built) {
        this.built = built;
    }

    public String getFirstflightdate() {
        return firstflightdate;
    }

    public void setFirstflightdate(String firstflightdate) {
        this.firstflightdate = firstflightdate;
    }

    public String getSeatconfiguration() {
        return seatconfiguration;
    }

    public void setSeatconfiguration(String seatconfiguration) {
        this.seatconfiguration = seatconfiguration;
    }

    public String getEngines() {
        return engines;
    }

    public void setEngines(String engines) {
        this.engines = engines;
    }

    public String getModes() {
        return modes;
    }

    public void setModes(String modes) {
        this.modes = modes;
    }

    public String getAdsb() {
        return adsb;
    }

    public void setAdsb(String adsb) {
        this.adsb = adsb;
    }

    public String getAcars() {
        return acars;
    }

    public void setAcars(String acars) {
        this.acars = acars;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    @Override
    public String toString() {
        return "AircraftDatabaseModel{" +
                "icao24='" + icao24 + '\'' +
                ", registration='" + registration + '\'' +
                ", manufacturericao='" + manufacturericao + '\'' +
                ", manufacturername='" + manufacturername + '\'' +
                ", model='" + model + '\'' +
                ", typecode='" + typecode + '\'' +
                ", serialnumber='" + serialnumber + '\'' +
                ", linenumber='" + linenumber + '\'' +
                ", icaoaircrafttype='" + icaoaircrafttype + '\'' +
                ", operator='" + operator + '\'' +
                ", operatorcallsign='" + operatorcallsign + '\'' +
                ", operatoricao='" + operatoricao + '\'' +
                ", operatoriata='" + operatoriata + '\'' +
                ", owner='" + owner + '\'' +
                ", testreg='" + testreg + '\'' +
                ", registered='" + registered + '\'' +
                ", reguntil='" + reguntil + '\'' +
                ", status='" + status + '\'' +
                ", built='" + built + '\'' +
                ", firstflightdate='" + firstflightdate + '\'' +
                ", seatconfiguration='" + seatconfiguration + '\'' +
                ", engines='" + engines + '\'' +
                ", modes='" + modes + '\'' +
                ", adsb='" + adsb + '\'' +
                ", acars='" + acars + '\'' +
                ", notes='" + notes + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                '}';
    }
}
