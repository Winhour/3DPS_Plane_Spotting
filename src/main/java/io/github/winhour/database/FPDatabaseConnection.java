package io.github.winhour.database;

import io.github.winhour.model.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FPDatabaseConnection {

    // Flight Plan database interaction

    /************************************************************************************************************************************************/

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + File.separator + "fpdatabase.db";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            //System.out.println("Connection to SQLite has been established.\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;

    }

    /************************************************************************************************************************************************/

    public void deleteFromAirlines() {

        String sql = "DELETE FROM Airlines";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void insertToAirlines(FPAirline fpa){

        String sql = "INSERT INTO Airlines(file_name, ICAO, IATA, callsign, name, type, season, author, country, logo, sourceurl) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, fpa.getFile_name());
                pstmt.setString(2, fpa.getIcao());
                pstmt.setString(3, fpa.getIata());
                pstmt.setString(4, fpa.getCallsign());
                pstmt.setString(5, fpa.getName());
                pstmt.setString(6, fpa.getType());
                pstmt.setString(7, fpa.getSeason());
                pstmt.setString(8, fpa.getAuthor());
                pstmt.setString(9, fpa.getCountry());
                pstmt.setString(10, fpa.getLogo());
                pstmt.setString(11, fpa.getSourceurl());

                pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/


    public void getFileNames(List<String> fileNameList){

        String sql = "SELECT file_name" + " FROM Airlines";

        try (Connection conn = this.connect();

            PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                fileNameList.add(rs.getString("file_name"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /************************************************************************************************************************************************/

    /*
    int total_time = arr_time - dep_time;

    double latDif = lat2 - lat1;

    double lonDif = lon2 - lon1;

    int timeDif = current_time - dep_time;

    double timePercentage = (double) timeDif / (double) total_time;

    latC = lat1 + (timePercentage * latDif);
    lonC = lon1 + (timePercentage * lonDif);

    " INNER JOIN " + "(SELECT DISTINCT air_id AS a_id FROM Aircraft) z ON b.air_id = z.a_id" +

    String sql_help = "SELECT route_id, MAX(waypoint_id) AS w_max FROM Waypoints WHERE r_id = route_id";

     */

    /************************************************************************************************************************************************/

    public void getNecessaryAttributesFromTablesOLD(int current_time, List<FPModel> fpList, double lat_in1, double lat_in2, double lon_in1, double lon_in2){

        //ORDER BY RANDOM()

        String sql = "SELECT * FROM" + "(SELECT fp_id FROM Flightplan) a " + "INNER JOIN " + "(SELECT air_id, fp_id FROM Aircraft) b " +
                "ON a.fp_id = b.fp_id" + " INNER JOIN " + "(SELECT reg_id, air_id, id FROM Registration) c ON b.air_id = c.air_id " + " INNER JOIN " +
                "(SELECT reg_id, aigroute, dep_time, arr_time, dep_apt, arr_apt FROM Leg WHERE dep_time<" + current_time +  " AND dep_time >(" + current_time + "-(5*60)) AND " +
                "arr_time>" + current_time + ") d ON c.reg_id = d.reg_id" + " INNER JOIN " +
                "(SELECT name, route_id, alt FROM Routes) e ON d.aigroute = e.name" + " INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints WHERE r1_id == route_id) f ON e.route_id = f.r1_id AND waypoint_id == 1" + " INNER JOIN " +                       // AND waypoint_id == 1
                "(SELECT route_id AS r_id, waypoint_id AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints WHERE r_id == route_id ORDER BY w_id DESC) g ON e.route_id = g.r_id AND w_id == 2 "              // AND w_id == 2
                //+ "INNER JOIN " + "(SELECT MAX(waypoint_id) AS w_max, route_id AS r2 FROM Waypoints WHERE route_id = r2) h ON w_id = w_max "
                 //  + " INNER JOIN (SELECT route_id AS r2_id, MAX(waypoint_id) AS w_max FROM Waypoints WHERE r2_id == route_id) h ON r2_id = r_id AND w_id = w_max "
                + "WHERE (((lat + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lat2-lat))) > "+lat_in1+") AND " +
                "((lat + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lat2-lat))) < "+lat_in2+") AND " +
                "((lon + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lon2-lon))) > "+lon_in1+") AND " +
                "((lon + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lon2-lon))) < "+lon_in2+"))" +
                ""
                + " LIMIT 100";

        try (Connection conn = this.connect();

            PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            long unixTime = System.currentTimeMillis() / 1000L;

            double distance;
            double time_dif;

            while (rs.next()){

                printOutDataFromTables(rs);

                FPModel tmp = new FPModel();

                tmp.setIcao24(rs.getString("reg_id") + "_" + rs.getString("air_id") + "_" + rs.getString("id"));
                tmp.setArr_time(rs.getInt("arr_time"));
                tmp.setDep_time(rs.getInt("dep_time"));
                tmp.setLat1(rs.getDouble("lat"));
                tmp.setLon1(rs.getDouble("lon"));
                tmp.setLat2(rs.getDouble("lat2"));
                tmp.setLon2(rs.getDouble("lon2"));
                tmp.setDep_apt(rs.getString("dep_apt"));
                tmp.setArr_apt(rs.getString("arr_apt"));
                tmp.setReg(rs.getString("id"));
                tmp.setUnix_time((int)unixTime);
                tmp.setAlt(rs.getInt("alt"));
                tmp.setTimetoclimb();
                tmp.setTimetodescend();
                tmp.setTOC();
                tmp.setTOD();
                tmp.setTOD_time();
                tmp.setTOC_time();

                distance = (double)(tmp.calculateLength(tmp.getLat1(), tmp.getLat2(), tmp.getLon1(), tmp.getLon2()));
                time_dif = (double)(tmp.getArr_time() - tmp.getDep_time());
                tmp.setSpd(((distance/1000)/(time_dif/60))*0.539956803);                          //kts

                fpList.add(tmp);


            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /************************************************************************************************************************************************/

    public void getNecessaryAttributesFromTablesV2(int current_time, List<FPModel> fpList, double lat_in1, double lat_in2, double lon_in1, double lon_in2){

        //ORDER BY RANDOM()

        String sql = "SELECT * FROM" + "(SELECT fp_id FROM Flightplan) a " + "INNER JOIN " + "(SELECT air_id, fp_id FROM Aircraft) b " +
                "ON a.fp_id = b.fp_id" + " INNER JOIN " + "(SELECT reg_id, air_id, id FROM Registration) c ON b.air_id = c.air_id " + " INNER JOIN " +
                "(SELECT reg_id, aigroute, dep_time, arr_time, dep_apt, arr_apt FROM Leg WHERE dep_time<" + current_time +  " AND dep_time >(" + current_time + "-(5*60)) AND " +
                "arr_time>" + current_time + ") d ON c.reg_id = d.reg_id" + " INNER JOIN " +
                "(SELECT name, route_id, alt FROM Routes) e ON d.aigroute = e.name" + " INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints WHERE r1_id == route_id) f ON e.route_id = f.r1_id AND waypoint_id == 1";


        try (Connection conn = this.connect();

            PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            long unixTime = System.currentTimeMillis() / 1000L;

            double distance=0;
            double time_dif=0;

            while (rs.next()) {

                FPModel tmp = new FPModel();

                tmp.setIcao24(rs.getString("reg_id") + "_" + rs.getString("air_id") + "_" + rs.getString("id"));
                tmp.setArr_time(rs.getInt("arr_time"));
                tmp.setDep_time(rs.getInt("dep_time"));
                tmp.setLat1(rs.getDouble("lat"));
                tmp.setLon1(rs.getDouble("lon"));
                tmp.setDep_apt(rs.getString("dep_apt"));
                tmp.setArr_apt(rs.getString("arr_apt"));
                tmp.setReg(rs.getString("id"));
                tmp.setUnix_time((int) unixTime);
                tmp.setAlt(rs.getInt("alt"));

                int dt = tmp.getDep_time();
                int at = tmp.getArr_time();
                double lat = tmp.getLat1();
                double lon = tmp.getLon1();
                int routein_id = rs.getInt("route_id");

                showMaxFromWaypointsForRoute(routein_id, dt, at, lat, lon, fpList, tmp, lat_in1, lat_in2, lon_in1, lon_in2, current_time, distance, time_dif);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /************************************************************************************************************************************************/

    public void getNecessaryAttributesFromTables(int current_time, List<FPModel> fpList, double lat_in1, double lat_in2, double lon_in1, double lon_in2, List<Waypoint> waypointList){

        //ORDER BY RANDOM()

        String sql = "SELECT * FROM" + "(SELECT fp_id FROM Flightplan) a " + "INNER JOIN " + "(SELECT air_id, fp_id FROM Aircraft) b " +
                "ON a.fp_id = b.fp_id" + " INNER JOIN " + "(SELECT reg_id, air_id, id FROM Registration) c ON b.air_id = c.air_id " + " INNER JOIN " +
                "(SELECT reg_id, aigroute, dep_time, arr_time, dep_apt, arr_apt FROM Leg WHERE dep_time<" + current_time +  " AND dep_time >(" + current_time + "-(5*60)) AND " +
                "arr_time>" + current_time + ") d ON c.reg_id = d.reg_id" + " INNER JOIN " +
                "(SELECT name, route_id, alt FROM Routes) e ON d.aigroute = e.name" + " INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints WHERE r1_id == route_id) f ON e.route_id = f.r1_id AND waypoint_id == 1";


        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            long unixTime = System.currentTimeMillis() / 1000L;

            double distance=0;
            double time_dif=0;

            while (rs.next()) {

                FPModel tmp = new FPModel();

                tmp.setIcao24(rs.getString("reg_id") + "_" + rs.getString("air_id") + "_" + rs.getString("id"));
                tmp.setArr_time(rs.getInt("arr_time"));
                tmp.setDep_time(rs.getInt("dep_time"));
                tmp.setLat1(rs.getDouble("lat"));
                tmp.setLon1(rs.getDouble("lon"));
                tmp.setDep_apt(rs.getString("dep_apt"));
                tmp.setArr_apt(rs.getString("arr_apt"));
                tmp.setReg(rs.getString("id"));
                tmp.setUnix_time((int) unixTime);
                tmp.setAlt(rs.getInt("alt"));
                tmp.setTimetoclimb();
                tmp.setTimetodescend();
                tmp.setTOC();
                tmp.setTOD();
                tmp.setTOD_time();
                tmp.setTOC_time();

                int dt = tmp.getDep_time();
                int at = tmp.getArr_time();
                double lat = tmp.getLat1();
                double lon = tmp.getLon1();
                int routein_id = rs.getInt("route_id");

                int tmpmax = 0;

                double lat2 = 0;
                double lon2 = 0;

                for (Waypoint w : waypointList){
                    if(w.getRoute_id() == routein_id){
                        if (tmpmax < w.getWaypoint_id()){
                            tmpmax = w.getWaypoint_id();
                            lat2 = w.getLat();
                            lon2 = w.getLon();
                        }
                    }
                }

                System.out.println(routein_id + "   " + tmpmax + "   " + lat2 + "   " + lon2);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /************************************************************************************************************************************************/

    public void getNecessaryAttributesFromTablesV4(double current_time, List<FPModel> fpList, double lat_in1, double lat_in2, double lon_in1, double lon_in2){

        //ORDER BY RANDOM()

        String sql = "SELECT * FROM" + "(SELECT fp_id FROM Flightplan) a " + "INNER JOIN " + "(SELECT air_id, fp_id FROM Aircraft) b " +
                "ON a.fp_id = b.fp_id" + " INNER JOIN " + "(SELECT reg_id, air_id, id FROM Registration) c ON b.air_id = c.air_id " + " INNER JOIN " +
                "(SELECT reg_id, aigroute, dep_time, arr_time, dep_apt, arr_apt FROM Leg WHERE dep_time<" + current_time +  " AND dep_time >(" + current_time + "-(5*60)) AND " +
                "arr_time>" + current_time + ") d ON c.reg_id = d.reg_id" + " INNER JOIN " +
                "(SELECT name, route_id, alt FROM Routes) g ON d.aigroute = g.name" + " INNER JOIN " +
                "(SELECT icao, latitude_mid AS lat, longitude_mid AS lon, elevation AS ele1 FROM main.airports) e ON e.icao = d.dep_apt INNER JOIN " +
                "(SELECT icao, latitude_mid AS lat2, longitude_mid AS lon2, elevation AS ele2 FROM main.airports) f ON f.icao = d.arr_apt "
                + "WHERE (((lat + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lat2-lat))) > "+lat_in1+") AND " +
                "((lat + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lat2-lat))) < "+lat_in2+") AND " +
                "((lon + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lon2-lon))) > "+lon_in1+") AND " +
                "((lon + ((("+current_time*1.00+"-d.dep_time*1.00)/(d.arr_time-d.dep_time))*(lon2-lon))) < "+lon_in2+"))" +
                ""
                + " LIMIT 100";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            long unixTime = System.currentTimeMillis() / 1000L;

            double distance;
            double time_dif;

            while (rs.next()){

                //printOutDataFromTables(rs);

                FPModel tmp = new FPModel();

                tmp.setIcao24(rs.getString("reg_id") + "_" + rs.getString("air_id") + "_" + rs.getString("id"));
                tmp.setArr_time(rs.getInt("arr_time"));
                tmp.setDep_time(rs.getInt("dep_time"));
                tmp.setLat1(rs.getDouble("lat"));
                tmp.setLon1(rs.getDouble("lon"));
                tmp.setLat2(rs.getDouble("lat2"));
                tmp.setLon2(rs.getDouble("lon2"));
                tmp.setDep_apt(rs.getString("dep_apt"));
                tmp.setArr_apt(rs.getString("arr_apt"));
                tmp.setReg(rs.getString("id"));
                tmp.setUnix_time((int)unixTime);
                tmp.setAlt(rs.getInt("alt"));
                tmp.setTimetoclimb();
                tmp.setTimetodescend();
                tmp.setTOC();
                tmp.setTOD();
                tmp.setTOD_time();
                tmp.setTOC_time();
                tmp.setLength();
                tmp.setTOClat();
                tmp.setTOClon();
                tmp.setTODlat();
                tmp.setTODlon();
                tmp.setStarting_airport_altitude(rs.getInt("ele1"));
                tmp.setLanding_airport_altitude(rs.getInt("ele2"));
                tmp.setRoute_alt(rs.getInt("alt"));
                tmp.setTOD_timeGenerated();
                tmp.setIteration(1);

                distance = (double)(tmp.calculateLength(tmp.getLat1(), tmp.getLat2(), tmp.getLon1(), tmp.getLon2()));
                time_dif = (double)(tmp.getArr_time() - tmp.getDep_time());
                tmp.setSpd((((distance/1000)/(time_dif/60))*0.539956803)*0.666);                          //kts 0.666 to lower speed, for the version that didn't have set speed

                fpList.add(tmp);


            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /************************************************************************************************************************************************/

    public void getSingleFPModel(int current_time, List<FPModel> fpList, double lat_in1, double lat_in2, double lon_in1, double lon_in2, int air_id){

        //ORDER BY RANDOM()

        String sql = "SELECT * FROM" + "(SELECT fp_id FROM Flightplan) a " + "INNER JOIN " + "(SELECT air_id, fp_id FROM Aircraft) b " +
                "ON a.fp_id = b.fp_id" + " INNER JOIN " + "(SELECT reg_id, air_id, id FROM Registration) c ON b.air_id = c.air_id " + " INNER JOIN " +
                "(SELECT reg_id, aigroute, dep_time, arr_time, dep_apt, arr_apt FROM Leg WHERE dep_time<" + current_time +  " AND dep_time >(" + current_time + "-(5*60)) AND " +
                "arr_time>" + current_time + ") d ON c.reg_id = d.reg_id" + " INNER JOIN " +
                "(SELECT name, route_id, alt FROM Routes) g ON d.aigroute = g.name" + " INNER JOIN " +
                "(SELECT icao, latitude_mid AS lat, longitude_mid AS lon, elevation AS ele1 FROM main.airports) e ON e.icao = d.dep_apt INNER JOIN " +
                "(SELECT icao, latitude_mid AS lat2, longitude_mid AS lon2, elevation AS ele2 FROM main.airports) f ON f.icao = d.arr_apt "
                + "WHERE air_id = ?"
                + " LIMIT 100";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setInt(1,air_id);

            ResultSet rs = pstmt.executeQuery();

            long unixTime = System.currentTimeMillis() / 1000L;

            double distance;
            double time_dif;

            while (rs.next()){

                //printOutDataFromTables(rs);

                FPModel tmp = new FPModel();

                tmp.setIcao24(rs.getString("reg_id") + "_" + rs.getString("air_id") + "_" + rs.getString("id"));
                tmp.setArr_time(rs.getInt("arr_time"));
                tmp.setDep_time(rs.getInt("dep_time"));
                tmp.setLat1(rs.getDouble("lat"));
                tmp.setLon1(rs.getDouble("lon"));
                tmp.setLat2(rs.getDouble("lat2"));
                tmp.setLon2(rs.getDouble("lon2"));
                tmp.setDep_apt(rs.getString("dep_apt"));
                tmp.setArr_apt(rs.getString("arr_apt"));
                tmp.setReg(rs.getString("id"));
                tmp.setUnix_time((int)unixTime);
                tmp.setAlt(rs.getInt("alt"));
                tmp.setTimetoclimb();
                tmp.setTimetodescend();
                tmp.setTOC();
                tmp.setTOD();
                tmp.setTOD_time();
                tmp.setTOC_time();
                tmp.setLength();
                tmp.setTOClat();
                tmp.setTOClon();
                tmp.setTODlat();
                tmp.setTODlon();
                tmp.setStarting_airport_altitude(rs.getInt("ele1"));
                tmp.setLanding_airport_altitude(rs.getInt("ele2"));
                tmp.setRoute_alt(rs.getInt("alt"));
                tmp.setTOD_timeGenerated();
                tmp.setIteration(1);

                distance = (double)(tmp.calculateLength(tmp.getLat1(), tmp.getLat2(), tmp.getLon1(), tmp.getLon2()));
                time_dif = (double)(tmp.getArr_time() - tmp.getDep_time());
                tmp.setSpd((((distance/1000)/(time_dif/60))*0.539956803)*0.666);                          //kts 0.666 to lower speed, for the version that didn't have set speed

                fpList.add(tmp);


            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    /************************************************************************************************************************************************/
    public void showMaxFromWaypointsForRoute(int rid, int dt, int at, double lat, double lon, List<FPModel> fpList, FPModel tmp, double lat_in1, double lat_in2, double lon_in1, double lon_in2, int current_time,
                                             double distance, double time_dif){

        //String given_name = "AIG_EPWA-OMDB_IFR_FL390";


        String sql = "SELECT route_id AS r_id, waypoint_id AS w_id, lat, lon FROM Waypoints WHERE r_id == " + rid + " ORDER BY w_id";

        /*
        String sql = "SELECT * FROM (SELECT name, route_id, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT route_id AS r_id, MAX(waypoint_id) AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints ORDER BY w_id DESC) ON route_id = r_id INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints) ON r_id = r1_id AND waypoint_id == 1";
         */



        /*
        String sql = "SELECT * FROM (SELECT name, route_id, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT route_id AS r_id, waypoint_id AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints ORDER BY w_id DESC) ON route_id = r_id AND w_id == 2 INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints) ON r_id = r1_id AND waypoint_id == 1";
         */



        /*
        String sql = "SELECT * FROM (SELECT name, route_id, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT route_id AS r_id, MAX(waypoint_id) AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints WHERE r_id == 225216 ORDER BY w_id DESC) ON route_id = r_id INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints WHERE r1_id == 225216) ON r_id = r1_id AND waypoint_id == 1";
         */

        //SELECT *
        //   FROM t1 WHERE(id, rev) IN(SELECT id, MAX(rev) FROM t1 GROUP BY id)

        /*
        String sql = "SELECT * FROM (SELECT name, route_id AS r_route, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT * FROM Waypoints WHERE (route_id, waypoint_id, lat, lon) IN (SELECT route_id, MAX(waypoint_id), lat, lon FROM Waypoints ORDER BY waypoint_id DESC)) ON route_id = r_route INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints) ON route_id = r1_id AND w_id == 1";
         */



        try (Connection conn = this.connect();

            PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){

                //System.out.println(rs.getString("name") + "   " + rs.getInt("route_id") + "   " + rs.getDouble("alt"));
                System.out.println(rs.getInt("r_id") + "   " + rs.getInt("w_id") + "   " + rs.getDouble("lat") + "   " + rs.getDouble("lon"));
                //System.out.println(rs.getInt("r1_id") + "   " + rs.getInt("waypoint_id") + "   " + rs.getDouble("lat") + "   " + rs.getDouble("lon"));

                /*
                String sql2 = "SELECT route_id AS r_id, waypoint_id AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints WHERE r_id == " + rid + " AND w_id == 1";

                try (Connection conn2 = this.connect();

                     PreparedStatement pstmt2  = conn2.prepareStatement(sql2)) {

                    ResultSet rs2 = pstmt2.executeQuery();

                    while (rs2.next()){

                        System.out.println("INSIDE LOOP: " + rs.getInt("r_id") + "   " + rs.getInt("w_id") + "   " + rs.getDouble("lat2") + "   " + rs.getDouble("lon2"));

                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                 */

                double lat_e = (lat+(((current_time*1.00-dt*1.00)/(at-dt))*(rs.getDouble("lat")-lat)));
                double lon_e = (lon+(((current_time*1.00-dt*1.00)/(at-dt))*(rs.getDouble("lon")-lon)));

                if(lat_e>lat_in1 && lat_e<lat_in2 && lon_e>lon_in1 && lon_e<lon_in2){

                    tmp.setLat2(rs.getDouble("lat"));
                    tmp.setLon2(rs.getDouble("lon"));

                    distance = (double)(tmp.calculateLength(tmp.getLat1(), tmp.getLat2(), tmp.getLon1(), tmp.getLon2()));
                    time_dif = (double)(tmp.getArr_time() - tmp.getDep_time());
                    tmp.setSpd(((distance/1000)/(time_dif/60))*0.539956803);

                    fpList.add(tmp);
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    /************************************************************************************************************************************************/

    public void showMaxFromWaypointsForRouteTest(int rid){



        String sql = "SELECT * FROM (SELECT name, route_id, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT route_id AS r_id, MAX(waypoint_id) AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints ORDER BY w_id DESC) ON route_id = r_id INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints) ON r_id = r1_id AND waypoint_id == 1";



        /*
        String sql = "SELECT * FROM (SELECT name, route_id, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT r1_id AS r_id, MAX(waypoint_id) AS w_id, lat AS lat2, lon AS lon2 FROM (SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints WHERE r1_id = r.route_id) ORDER BY w_id DESC) ON route_id = r_id";
        */

        /*
        String sql = "SELECT * FROM (SELECT name, route_id, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT route_id AS r_id, waypoint_id AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints ORDER BY w_id DESC) ON route_id = r_id AND w_id == 2 INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints) ON r_id = r1_id AND waypoint_id == 1";
         */



        /*
        String sql = "SELECT * FROM (SELECT name, route_id, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT route_id AS r_id, MAX(waypoint_id) AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints WHERE r_id == 225216 ORDER BY w_id DESC) ON route_id = r_id INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id, lat, lon FROM Waypoints WHERE r1_id == 225216) ON r_id = r1_id AND waypoint_id == 1";
         */

        //SELECT *
        //   FROM t1 WHERE(id, rev) IN(SELECT id, MAX(rev) FROM t1 GROUP BY id)

        /*
        String sql = "SELECT * FROM (SELECT name, route_id AS r_route, alt FROM Routes WHERE route_id == 225216) r INNER JOIN " +
                "(SELECT * FROM Waypoints WHERE (route_id, waypoint_id, lat, lon) IN (SELECT route_id, MAX(waypoint_id), lat, lon FROM Waypoints ORDER BY waypoint_id DESC)) ON route_id = r_route INNER JOIN " +
                "(SELECT route_id AS r1_id, waypoint_id AS w_id, lat AS lat2, lon AS lon2 FROM Waypoints) ON route_id = r1_id AND w_id == 1";
         */



        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            if(rs.getFetchSize() == 0) System.out.println("No results");

            while (rs.next()){

                //System.out.println(rs.getString("name") + "   " + rs.getInt("route_id") + "   " + rs.getDouble("alt"));
                System.out.println(rs.getInt("r_id") + "   " + rs.getInt("w_id") + "   " + rs.getDouble("lat") + "   " + rs.getDouble("lon"));
                //System.out.println(rs.getInt("r1_id") + "   " + rs.getInt("waypoint_id") + "   " + rs.getDouble("lat") + "   " + rs.getDouble("lon"));

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    /************************************************************************************************************************************************/

    public void idiotInsertToLists(List<Route> listRoutes, List<Waypoint> listWaypoints){


        String sql = "SELECT * FROM Routes";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){

                Route r = new Route();

                r.setRoute_id(rs.getInt("route_id"));
                r.setName(rs.getString("name"));
                r.setAlt(rs.getInt("alt"));

                listRoutes.add(r);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("Routes done");

        String sql2 = "SELECT * FROM Waypoints";

        try (Connection conn2 = this.connect();

             PreparedStatement pstmt2  = conn2.prepareStatement(sql2)) {

            ResultSet rs2 = pstmt2.executeQuery();

            while (rs2.next()){

                Waypoint w = new Waypoint();

                w.setRoute_id(rs2.getInt("route_id"));
                w.setLat(rs2.getDouble("lat"));
                w.setLon(rs2.getDouble("lon"));
                w.setWaypoint_id(rs2.getInt("waypoint_id"));

                listWaypoints.add(w);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("Waypoints done");

        /*
        for (Route i : listRoutes){
            System.out.println(i.toString());
        }

        for (Waypoint d : listWaypoints){
            System.out.println(d.toString());
        }
         */


    }


    /************************************************************************************************************************************************/

    private void printOutDataFromTables(ResultSet rs) throws SQLException {
        System.out.println("fp_id: " + rs.getString("fp_id") + "   air_id: " + rs.getString("air_id") + "   reg_id: " + rs.getString("reg_id")
                + "   id(reg): " + rs.getString("id") + "   aigroute: " + rs.getString("aigroute") + "   dep_time: " + rs.getString("dep_time")
                + "   arr_time: " + rs.getString("arr_time") + "   name: " + rs.getString("name") + "   route_id: " + rs.getString("route_id")
                + "   waypoint_id: " + rs.getString("waypoint_id") + "   lat: " + rs.getString("lat") + "   lon: " + rs.getString("lon")
                + "   w_id: " + rs.getString("w_id") + "   lat2: " + rs.getString("lat2") + "   lon2: " + rs.getString("lon2")
        );
    }

    /************************************************************************************************************************************************/


    public void insertIntoFlightPlans(List<FlightPlan> fplist) throws SQLException {

        String SQL_INSERT="INSERT INTO FlightPlan(fp_id, Airport_fs, file_name) VALUES(?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);)

        {
            int i = 0;

            for (FlightPlan fp : fplist) {

                statement.setInt(1, fp.getFp_id());
                statement.setString(2, fp.getAirport_fs());
                statement.setString(3, fp.getFile_name());

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == fplist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public void insertIntoAircraftAndRegistration(List<Aircraft> airlist) throws SQLException {

        String SQL_INSERT="INSERT INTO Aircraft(air_id, title, type, fp_id, id) VALUES(?,?,?,?,?)";
        String SQL_INSERT2 = "INSERT INTO Registration(reg_id, air_id, id, rotation) VALUES(?,?,?,?)";


        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
             PreparedStatement statement2 = connection.prepareStatement(SQL_INSERT2);)


        {
            int i = 0;

            for (Aircraft air : airlist) {

                statement.setInt(1, air.getAir_id());
                statement.setString(2, air.getTitle());
                statement.setString(3, air.getType());
                statement.setInt(4, air.getFp_id());
                statement.setInt(5, air.getId());


                List<Registration> regs = air.getRegistrations();

                //check if regs is null or size < 1

                for(Registration reg: regs) {

                    if (reg.getReg_id() != 0) {
                        statement2.setInt(1, reg.getReg_id());
                        statement2.setInt(2, reg.getAir_id());
                        statement2.setString(3, reg.getId());
                        statement2.setInt(4, reg.getRotation());
                    } else {
                        statement2.setInt(1, 99999999);
                        statement2.setInt(2, air.getAir_id());
                        statement2.setString(3, "XXX");
                        statement2.setInt(4, 0);
                    }
                    statement2.addBatch();
                }

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == airlist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                    statement2.executeBatch();
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    /************************************************************************************************************************************************/

    public void insertIntoLegs(List<Leg> leglist) throws SQLException {

        String SQL_INSERT="INSERT INTO Leg(reg_id, aigroute, alt, dep_apt, arr_apt, dep_time, arr_time, callsign, flightnumber, flightrule, tng) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);)

        {
            int i = 0;

            for (Leg l : leglist) {

                statement.setInt(1, l.getReg_id());
                statement.setString(2, l.getAigroute());
                statement.setInt(3, l.getAlt());
                statement.setString(4, l.getDep_apt());
                statement.setString(5, l.getArr_apt());
                statement.setInt(6, l.getDep_time());
                statement.setInt(7, l.getArr_time());
                statement.setString(8, l.getCallsign());
                statement.setInt(9, l.getFlightnumber());
                statement.setString(10, l.getFlightrule());
                statement.setString(11, l.getTng());


                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == leglist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    /************************************************************************************************************************************************/

    public void insertIntoRoutes(List<Route> routelist) throws SQLException {

        String SQL_INSERT="INSERT INTO Routes(route_id, name, max, min, alt, rule) VALUES(?,?,?,?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);)

        {
            int i = 0;

            for (Route r : routelist) {

                statement.setInt(1, r.getRoute_id());
                statement.setString(2, r.getName());
                statement.setInt(3, r.getMax());
                statement.setInt(4, r.getMin());
                statement.setInt(5, r.getAlt());
                statement.setString(6, r.getRule());


                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == routelist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public void insertIntoWaypoints(List<Waypoint> waylist) throws SQLException {

        String SQL_INSERT="INSERT INTO Waypoints(route_id, id, lat, lon, alt, kts, option, waypoint_id) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);)

        {
            int i = 0;

            for (Waypoint w : waylist) {
                //statement.setString(1, entity.getSomeProperty());
                // ...

                statement.setInt(1, w.getRoute_id());
                statement.setString(2, w.getId());
                statement.setDouble(3, w.getLat());
                statement.setDouble(4, w.getLon());
                statement.setInt(5, w.getAlt());
                statement.setInt(6, w.getKts());
                statement.setInt(7, w.getOption());
                statement.setInt(8, w.getWaypoint_id());


                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == waylist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public void insertIntoAirports(List<Airport> alist){

        String SQL_INSERT="INSERT INTO airports(icao, elevation, atc_tower, name, closed) VALUES(?,?,?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);)

        {
            int i = 0;

            for (Airport a : alist) {

                statement.setString(1, a.getIcao());
                statement.setString(2, a.getElevation());
                statement.setBoolean(3, a.isAtc_tower());
                statement.setString(4, a.getName());
                statement.setBoolean(5, a.isClosed());

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == alist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public AirportModel getFromAirports(String icao){

        AirportModel tmp = new AirportModel();

        String sql = "SELECT * FROM airports WHERE icao = ?";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setString(1, icao);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){

                tmp.setLat(rs.getDouble("latitude_mid"));
                tmp.setLon(rs.getDouble("longitude_mid"));
                tmp.setIcao(icao);
                tmp.setElevation(rs.getInt("elevation"));

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return tmp;

    }



    /************************************************************************************************************************************************/

    public void insertIntoRunways(List<Runway> rlist){

        String SQL_INSERT="INSERT INTO runways(icao, runway_start_id, runway_end_id, width, surface_type, runway_smoothness, runway_shoulder_surface, " +
                "centre_line_lights, edge_lights, auto_signs, length) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);)

        {
            int i = 0;

            for (Runway r : rlist) {

                statement.setString(1, r.getIcao());
                statement.setInt(2, r.getRunway_start_id());
                statement.setInt(3, r.getRunway_end_id());
                statement.setDouble(4, r.getWidth());
                statement.setInt(5, r.getSurface_type());
                statement.setInt(6, (int)r.getRunway_smoothness());
                statement.setInt(7, r.getRunway_shoulder_surface());
                statement.setBoolean(8, r.isCentre_line_lights());
                statement.setInt(9, r.getEdge_lights());
                statement.setBoolean(10, r.isAuto_signs());
                statement.setInt(11, r.getLength());

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == rlist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public void insertIntoRunwayEnds(List<Runway_end> relist){

        String SQL_INSERT="INSERT INTO runway_ends(id, icao, designator, latitude, longitude, displaced_threshold, overrun, runway_markings, approach_lighting, tdz_lighting, reil) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = this.connect();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT);)

        {
            int i = 0;

            for (Runway_end r : relist) {

                statement.setInt(1, r.getId());
                statement.setString(2, r.getIcao());
                statement.setString(3, r.getDesignator());
                statement.setDouble(4,r.getLatitude());
                statement.setDouble(5, r.getLongitude());
                statement.setDouble(6, r.getDisplaced_threshold());
                statement.setDouble(7, r.getOverrun());
                statement.setInt(8, r.getRunway_markings());
                statement.setInt(9, r.getApproach_lighting());
                statement.setBoolean(10, r.isTdz_lighting());
                statement.setInt(11, r.getReil());

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == relist.size()) {
                    statement.executeBatch(); // Execute every 1000 items.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public void getIcaosFromAirports(List<MiddlePointContainer> mpclist){

        String sql = "SELECT icao, name FROM airports ORDER BY name";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql);) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                MiddlePointContainer mpc = new MiddlePointContainer();
                mpc.setIcao(rs.getString("icao"));

                mpclist.add(mpc);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }




    /************************************************************************************************************************************************/

    public void getMiddleFromRunwayEnds(List<MiddlePointContainer> mpclist){

        String sql = "SELECT icao, latitude, longitude, id FROM runway_ends ORDER BY id";

        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {


                String icao = rs.getString("icao");

                for(MiddlePointContainer mpc : mpclist){
                    if (icao.equals(mpc.getIcao())){

                        if(mpc.getFlag_state()<2){

                            if(mpc.getFlag_state() == 0) {
                                mpc.setLat1(rs.getDouble("latitude"));
                                mpc.setLon1(rs.getDouble("longitude"));
                            }

                            if(mpc.getFlag_state() == 1){
                                mpc.setLat2(rs.getDouble("latitude"));
                                mpc.setLon2(rs.getDouble("longitude"));
                                mpc.setLatout();
                                mpc.setLonout();
                            }

                            mpc.setFlag_state((mpc.getFlag_state()+1));
                        }

                        break;
                    }
                }


            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /************************************************************************************************************************************************/

    public void insertMiddleIntoAirports(MiddlePointContainer mdc){

        String sql = "UPDATE airports SET latitude_mid = ?, longitude_mid = ? WHERE icao = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, mdc.getLatout());
            pstmt.setDouble(2, mdc.getLonout());
            pstmt.setString(3, mdc.getIcao());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    /************************************************************************************************************************************************/

    public void getRunwayEndsForAirportIcao(AirportModel airport){

        String sql = "SELECT id, icao, latitude, longitude FROM runway_ends WHERE icao = ? ORDER BY id";

        try (Connection conn = this.connect();

             PreparedStatement pstmt  = conn.prepareStatement(sql);) {

            pstmt.setString(1, airport.getIcao());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Runway_end rend = new Runway_end();

                rend.setId(rs.getInt("id"));
                rend.setLatitude(rs.getDouble("latitude"));
                rend.setLongitude(rs.getDouble("longitude"));
                rend.setIcao(rs.getString("icao"));

                
                airport.add_to_runway_ends(rend);

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



    /************************************************************************************************************************************************/

    public void deleteFromFlightPlan() {

        String sql = "DELETE FROM FlightPlan";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromAircraft() {

        String sql = "DELETE FROM Aircraft";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromLeg() {

        String sql = "DELETE FROM Leg";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromRegistration() {

        String sql = "DELETE FROM Registration";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromRoutes() {

        String sql = "DELETE FROM Routes";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromWaypoints() {

        String sql = "DELETE FROM Waypoints";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

}
