package io.github.winhour.model;

public enum AircraftTypes {

    //https://www.w3schools.com/java/java_enums.asp
    //https://stackoverflow.com/questions/9712977/is-it-possible-to-have-an-enum-class-with-enums-of-two-or-more-words

    BOEING("Boeing"),
    AIRBUS("Airbus"),
    EMBRAER("Embraer"),
    MCDONNELL_DOUGLAS("McDonnell Douglas"),
    BOMBARDIER("Bombardier"),
    FALCON("Falcon"),
    CESSNA("Cessna")
    ;

    private final String toString;

    private AircraftTypes(String toString) {
        this.toString = toString;
    }

    public String toString(){
        return toString;
    }

}
