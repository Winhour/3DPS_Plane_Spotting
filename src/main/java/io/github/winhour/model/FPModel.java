package io.github.winhour.model;

public class FPModel {

    //Primary container for plane data (flight plan version)

    private int dep_time;
    private int arr_time;
    private double lon1;
    private double lon2;
    private double lat1;
    private double lat2;
    private String icao24;
    private double lat_c;
    private double lon_c;
    private int unix_time;
    private Double true_track;
    private String dep_apt;
    private String arr_apt;
    private String reg;
    private int alt;
    private double spd;             // 1km/h = 0.539956803 kts (knots)
    private double length;
    private double TOC;             //In nautical miles     1 nm = 1.852 km
    private double TOD;             //In nautical miles

    //An Average jet passenger plane has about 1,000 - 2,000 feet per minute climb rate.
    //
    //So, it will take 15 to 30 minutes to get to 30,000 - assuming no air traffic delay.
    //Idle descent in many jets is around 3,000 feet per minute until reaching 10,000 feet.
    // There is a speed restriction of 250 knots below 10,000 feet, therefore the flight management computer will slow the aircraft to 250 knots and continue
    // the descent at approximately 1,500 feet per minute

    private int alt_current;
    private int timetoclimb;
    private int timetodescend;

    private int TOC_time;
    private int TOD_time;

    private double TOClon;
    private double TOClat;
    private double TODlon;
    private double TODlat;

    private int landing_airport_altitude;
    private int starting_airport_altitude;
    private int route_alt;

    private int TOD_timeGenerated;

    private int iteration = 0;

    private boolean isLanded = false;

    private String type;
    private String model;
    private String man;
    private String callsign;
    private String op;

    private int landing_point_time = 0;

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

    public double getLon1() {
        return lon1;
    }

    public void setLon1(double lon1) {
        this.lon1 = lon1;
    }

    public double getLon2() {
        return lon2;
    }

    public void setLon2(double lon2) {
        this.lon2 = lon2;
    }

    public double getLat1() {
        return lat1;
    }

    public void setLat1(double lat1) {
        this.lat1 = lat1;
    }

    public double getLat2() {
        return lat2;
    }

    public void setLat2(double lat2) {
        this.lat2 = lat2;
    }

    public String getIcao24() {
        return icao24;
    }

    public void setIcao24(String icao24) {
        this.icao24 = icao24;
    }

    public double getLat_c() {
        return lat_c;
    }

    public void setLat_c(double lat_c) {
        this.lat_c = lat_c;
    }

    public double getLon_c() {
        return lon_c;
    }

    public void setLon_c(double lon_c) {
        this.lon_c = lon_c;
    }

    public int getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(int unix_time) {
        this.unix_time = unix_time;
    }

    public Double getTrue_track() {
        return true_track;
    }

    public void setTrue_track(Double true_track) {
        this.true_track = true_track;
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

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public double getSpd() {
        return spd;
    }

    public void setSpd(double spd) {
        this.spd = spd;
    }

    public double getLength() {
        return length;
    }

    public void setLength() {
        //this.length = 1.852*(double)calculateLength(lat1, lat2, lon1, lon2);        //In nautical miles
        this.length = 1.852*distance(lat1, lat2, lon1, lon2,0,0);
    }

    public double getTOC() {
        return TOC;
    }

    public void setTOC() {
        this.TOC = alt/120;
    }

    public double getTOD() {
        return TOD;
    }

    public void setTOD() {
        this.TOD = alt/333.33;
    }

    public int getAlt_current() {
        return alt_current;
    }

    public void setAlt_current(int alt_current) {
        this.alt_current = alt_current;
    }

    public int getTimetoclimb() {
        return timetoclimb;
    }

    public void setTimetoclimb() {
        this.timetoclimb = (alt/1500)*60;                    //the divisivion number is average feet per minute
    }

    public int getTimetodescend() {
        return timetodescend;
    }

    public void setTimetodescend() {
        this.timetodescend = 400+(((alt-10000)/3000)*60);
    }

    public int getTOC_time() {
        return TOC_time;
    }

    public void setTOC_time() {
        this.TOC_time = (dep_time*60)+timetoclimb;
    }

    public int getTOD_time() {
        return TOD_time;
    }

    public void setTOD_time() {
        this.TOD_time = (arr_time*60)-timetodescend;
    }

    public double getTOClon() {
        return TOClon;
    }

    public void setTOClon() {
        double Tdist = 1.852 * TOC;
        /*
        System.out.println("Tdist: " + Tdist);
        System.out.println("Length: " + length);
        System.out.println("Lon2: " + lon2 + " Lon1:" + lon1);
        System.out.println("Lat2: " + lat2 + " Lat1:" + lat1);
        System.out.println("Difference: " + ((Tdist/length)*(lon2-lon1)));
         */
        this.TOClon = lon1 + ((Tdist/(length/1000))*(lon2-lon1));
    }

    public double getTOClat() {
        return TOClat;
    }

    public void setTOClat() {
        double Tdist = Tdist = 1.852 * TOC;
        this.TOClat = lat1 + ((Tdist/(length/1000))*(lat2-lat1));
    }

    public double getTODlon() {
        return TODlon;
    }

    public void setTODlon() {
        double Tdist = 1.852 * TOD;
        this.TODlon = lon2 - ((Tdist/(length/1000))*(lon2-lon1));
    }

    public double getTODlat() {
        return TODlat;
    }

    public void setTODlat() {
        double Tdist = 1.852 * TOD;
        this.TODlat = lat2 - ((Tdist/(length/1000))*(lat2-lat1));
    }


    public int getLanding_airport_altitude() {
        return landing_airport_altitude;
    }

    public void setLanding_airport_altitude(int landing_airport_altitude) {
        this.landing_airport_altitude = landing_airport_altitude;
    }

    public int getStarting_airport_altitude() {
        return starting_airport_altitude;
    }

    public void setStarting_airport_altitude(int starting_airport_altitude) {
        this.starting_airport_altitude = starting_airport_altitude;
    }

    public int getRoute_alt() {
        return route_alt;
    }

    public void setRoute_alt(int route_alt) {
        this.route_alt = route_alt;
    }

    public int getTOD_timeGenerated() {
        return TOD_timeGenerated;
    }

    public void setTOD_timeGenerated() {

        double speed_kts = 400.00;
        double speed_km = speed_kts*1.852;
        double speed_m = speed_km*0.277777778;

        double flight_distance = distance(TOClat, TODlat, TOClon, TODlon, 0, 0);

        this.TOD_timeGenerated = TOC_time + (int)(flight_distance/speed_m);

        //System.out.println("Flight_distance: " + flight_distance + "    speed_m: " + speed_m + "   TOC_TOD_time_difference: " + (int)(flight_distance/speed_m));
        //System.out.println("Speed_m in previous case: " + (flight_distance/(TOD_time - TOC_time)));

    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public boolean isLanded() {
        return isLanded;
    }

    public void setLanded(boolean landed) {
        isLanded = landed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public int getLanding_point_time() {
        return landing_point_time;
    }

    public void setLanding_point_time(int landing_point_time) {
        this.landing_point_time = landing_point_time;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    /************************************************************************************************************************************************/

    @Override
    public String toString() {
        return "FPModel{" +
                "dep_time=" + dep_time +
                ", arr_time=" + arr_time +
                ", lon1=" + lon1 +
                ", lon2=" + lon2 +
                ", lat1=" + lat1 +
                ", lat2=" + lat2 +
                ", icao24='" + icao24 + '\'' +
                ", lat_c=" + lat_c +
                ", lon_c=" + lon_c +
                ", unix_time=" + unix_time +
                ", true_track=" + true_track +
                ", dep_apt='" + dep_apt + '\'' +
                ", arr_apt='" + arr_apt + '\'' +
                '}';
    }

    /************************************************************************************************************************************************/

    public int calculateLength(double lat1, double lat2, double lon1, double lon2){

        double result;

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        result = (c * r)*3280.8399;

        //System.out.println(result);

        return (int) result;

    }

    /************************************************************************************************************************************************/

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /************************************************************************************************************************************************/

}
