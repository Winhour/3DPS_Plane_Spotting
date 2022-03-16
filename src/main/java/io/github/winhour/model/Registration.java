package io.github.winhour.model;

import java.util.List;

public class Registration {

    String id;
    int rotation;
    List<Leg> legList;
    int air_id;
    int reg_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public List<Leg> getLegList() {
        return legList;
    }

    public void setLegList(List<Leg> legList) {
        this.legList = legList;
    }

    public int getAir_id() {
        return air_id;
    }

    public void setAir_id(int air_id) {
        this.air_id = air_id;
    }

    public int getReg_id() {
        return reg_id;
    }

    public void setReg_id(int reg_id) {
        this.reg_id = reg_id;
    }
}
