package io.github.winhour.database;

import io.github.winhour.model.Airport;
import io.github.winhour.model.DistanceContainer;
import io.github.winhour.model.Runway;
import io.github.winhour.model.Runway_end;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AirrunDatabaseConnection {

    /* Interaction with the processed airport runway database */

    int lowestdt = 0;
    String resultTmp = "Icao not found";
    Set<DistanceContainer> dCont = new HashSet<>();
    double modifier = 0.5;

    public String getResultTmp() {
        return resultTmp;
    }

    public void setResultTmp(String resultTmp) {
        this.resultTmp = resultTmp;
    }

    public int getLowestdt() {
        return lowestdt;
    }

    public void setLowestdt(int lowestdt) {
        this.lowestdt = lowestdt;
    }

    public Set<DistanceContainer> getdCont() {
        return dCont;
    }

    public void setdCont(Set<DistanceContainer> dCont) {
        this.dCont = dCont;
    }

    public double getModifier() {
        return modifier;
    }

    public void setModifier(double modifier) {
        this.modifier = modifier;
    }

    /************************************************************************************************************************************************/

    private Connection connect() {

        /* Establish database connection */

        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "\\airrun.db";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            //System.out.println("Connection to SQLite has been established.\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;

    }

    /************************************************************************************************************************************************/

    public void deleteFromAirports() {
        String sql = "DELETE FROM airports";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromRunways() {
        String sql = "DELETE FROM runways";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromRunwayEnds() {
        String sql = "DELETE FROM runway_ends";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void insertToAirports(Airport airport){

        String sql = "INSERT INTO airports(icao, elevation, atc_tower, name, closed) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, airport.getIcao());
            pstmt.setString(2, airport.getElevation());
            pstmt.setBoolean(3, airport.isAtc_tower());
            pstmt.setString(4, airport.getName());
            pstmt.setBoolean(5, airport.isClosed());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            //System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void insertToRunways(Runway runway){

        String sql = "INSERT INTO runways(icao, width, surface_type, runway_shoulder_surface, runway_smoothness, centre_line_lights, edge_lights, auto_signs, runway_start_id, runway_end_id, length) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, runway.getIcao());
            pstmt.setDouble(2, runway.getWidth());
            pstmt.setInt(3, runway.getSurface_type());
            pstmt.setInt(4, runway.getRunway_shoulder_surface());
            pstmt.setDouble(5, runway.getRunway_smoothness());
            pstmt.setBoolean(6, runway.isCentre_line_lights());
            pstmt.setInt(7, runway.getEdge_lights());
            pstmt.setBoolean(8, runway.isAuto_signs());
            pstmt.setInt(9, runway.getRunway_start_id());
            pstmt.setInt(10, runway.getRunway_end_id());
            pstmt.setInt(11, runway.getLength());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void insertToRunwayEnds(Runway_end runwayEnd){

        String sql = "INSERT INTO runway_ends(icao, designator, id, latitude, longitude, displaced_threshold, overrun, runway_markings, approach_lighting, tdz_lighting, reil) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, runwayEnd.getIcao());
            pstmt.setString(2, runwayEnd.getDesignator());
            pstmt.setInt(3, runwayEnd.getId());
            pstmt.setDouble(4, runwayEnd.getLatitude());
            pstmt.setDouble(5, runwayEnd.getLongitude());
            pstmt.setDouble(6, runwayEnd.getDisplaced_threshold());
            pstmt.setDouble(7, runwayEnd.getOverrun());
            pstmt.setInt(8, runwayEnd.getRunway_markings());
            pstmt.setInt(9, runwayEnd.getApproach_lighting());
            pstmt.setBoolean(10, runwayEnd.isTdz_lighting());
            pstmt.setInt(11, runwayEnd.getReil());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void getRunwayEnds(List<Runway_end> relist){

        String sql = "SELECT * FROM runway_ends";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){

                Runway_end tmp = new Runway_end();

                tmp.setId(rs.getInt("id"));
                tmp.setIcao(rs.getString("icao"));
                tmp.setDesignator(rs.getString("designator"));
                tmp.setLatitude(rs.getDouble("latitude"));
                tmp.setLongitude(rs.getDouble("longitude"));
                tmp.setDisplaced_threshold(rs.getDouble("displaced_threshold"));
                tmp.setOverrun(rs.getDouble("overrun"));
                tmp.setRunway_markings(rs.getInt("runway_markings"));
                tmp.setApproach_lighting(rs.getInt("approach_lighting"));
                tmp.setTdz_lighting(rs.getBoolean("tdz_lighting"));
                tmp.setReil(rs.getInt("reil"));

                relist.add(tmp);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /************************************************************************************************************************************************/

    public void getRunways(List<Runway> rlist){

        String sql = "SELECT * FROM runways";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){

                Runway tmp = new Runway();

                tmp.setIcao(rs.getString("icao"));
                tmp.setRunway_start_id(rs.getInt("runway_start_id"));
                tmp.setRunway_end_id(rs.getInt("runway_end_id"));
                tmp.setWidth(rs.getDouble("width"));
                tmp.setSurface_type(rs.getInt("surface_type"));
                tmp.setRunway_smoothness(rs.getDouble("runway_smoothness"));
                tmp.setRunway_shoulder_surface(rs.getInt("runway_shoulder_surface"));
                tmp.setCentre_line_lights(rs.getBoolean("centre_line_lights"));
                tmp.setEdge_lights(rs.getInt("edge_lights"));
                tmp.setAuto_signs(rs.getBoolean("auto_signs"));
                tmp.setLength(rs.getInt("length"));

                rlist.add(tmp);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /************************************************************************************************************************************************/

    public void getAirports(List<Airport> alist){

        String sql = "SELECT * FROM airports";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){

                Airport tmp = new Airport();

                tmp.setIcao(rs.getString("icao"));
                tmp.setElevation(rs.getString("elevation"));
                tmp.setAtc_tower(rs.getBoolean("atc_tower"));
                tmp.setName(rs.getString("name"));
                tmp.setClosed(rs.getBoolean("closed"));

                alist.add(tmp);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



    /************************************************************************************************************************************************/

    public void checkIfLatitudeLongitudeInSquare(double lat, double lon, double modifierIn, int lmin){

        String sql = "SELECT id, latitude, longitude FROM runway_ends WHERE id%2 == 0 AND latitude < ? AND latitude > ? AND longitude > ? AND longitude < ?";

        String resultIcao="Icao not found";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setDouble(1,lat+modifierIn);
            pstmt.setDouble(2,lat-modifierIn);
            pstmt.setDouble(3,lon-modifierIn);
            pstmt.setDouble(4,lon+modifierIn);


            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()){

                resultIcao = findRunwayForGivenId(rs.getInt("id"),lmin,rs.getDouble("latitude"), rs.getDouble("longitude"),lat,lon);


            }

            while(getResultTmp().equals("Icao not found")){
                modifierIn = modifierIn + 0.5;
                checkIfLatitudeLongitudeInSquare(lat,lon,modifierIn,lmin);
            }

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
        }

        //System.out.println(getResultTmp());

        modifier = modifierIn;

    }

    /************************************************************************************************************************************************/

    public String findRunwayForGivenId(int id, int lmin, double latr, double lonr, double lat, double lon){

        String sql = "SELECT runway_end_id, icao, length, width FROM runways WHERE runway_end_id = ?";


        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setInt(1,id);
            int tmpdistance = 0;

            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()){

                //System.out.println("\n" + lowestdt + "\n");


                if(lmin<=rs.getInt("length")){
                    tmpdistance = calculateDistanceBetweenTwoPoints(lat,latr,lon,lonr);

                    DistanceContainer dc = new DistanceContainer(tmpdistance,rs.getString("icao"));

                    if(lowestdt == 0){
                        //lowestd = tmpdistance;
                        this.setLowestdt(tmpdistance);
                        //System.out.println(tmpdistance + "   " + lowestdt);
                    }



                    if (lowestdt>=tmpdistance) {

                        setResultTmp(rs.getString("icao"));
                        //System.out.println(getResultTmp());

                        //double resultName = rs.getDouble("width");

                        //System.out.println(resultName);

                        this.setLowestdt(tmpdistance);

                    }

                    dCont.add(dc);

                    //System.out.println("\n" + lowestdt + "   " + getResultTmp() + "\n");
                }

            }

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
        }

        return getResultTmp();

    }


    /************************************************************************************************************************************************/

    public int calculateDistanceBetweenTwoPoints(double lat1, double lat2, double lon1, double lon2){

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

        return (int) result;
    }

    /************************************************************************************************************************************************/

    public void checkIfLatitudeLongitudeInSquareV2(double lat, double lon, int lmin){

        String sql = "SELECT id, latitude, longitude FROM runway_ends WHERE id%2 == 0 AND latitude < ? AND latitude > ? AND longitude > ? AND longitude < ?";

        String resultIcao="Icao not found";

        modifier = modifier + 1;

        setdCont(new HashSet<>());

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setDouble(1,lat+modifier);
            pstmt.setDouble(2,lat-modifier);
            pstmt.setDouble(3,lon-modifier);
            pstmt.setDouble(4,lon+modifier);


            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()){

                resultIcao = findRunwayForGivenId(rs.getInt("id"),lmin,rs.getDouble("latitude"), rs.getDouble("longitude"),lat,lon);


            }

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
        }

        //System.out.println(getResultTmp());

    }

    /************************************************************************************************************************************************/

    public void checkIfLatitudeLongitudeInSquareV3(double lat, double lon, int lmin){

        String sql = "SELECT id, latitude, longitude FROM runway_ends WHERE id%2 == 0 AND latitude < ? AND latitude > ? AND longitude > ? AND longitude < ?";

        String resultIcao="Icao not found";

        modifier = modifier + 1.5;

        setdCont(new HashSet<>());

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setDouble(1,lat+modifier);
            pstmt.setDouble(2,lat-modifier);
            pstmt.setDouble(3,lon-modifier);
            pstmt.setDouble(4,lon+modifier);


            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()){

                resultIcao = findRunwayForGivenId(rs.getInt("id"),lmin,rs.getDouble("latitude"), rs.getDouble("longitude"),lat,lon);


            }

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
        }

        //System.out.println(getResultTmp());

    }

}
