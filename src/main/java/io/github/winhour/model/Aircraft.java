package io.github.winhour.model;

import java.util.List;

public class Aircraft {

    int id;
    String title;
    String type;
    List<Registration> registrations;
    int air_id;
    int fp_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    public int getAir_id() {
        return air_id;
    }

    public void setAir_id(int air_id) {
        this.air_id = air_id;
    }

    public int getFp_id() {
        return fp_id;
    }

    public void setFp_id(int fp_id) {
        this.fp_id = fp_id;
    }
}
