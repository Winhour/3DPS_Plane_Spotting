package io.github.winhour.database;

import io.github.winhour.model.AircraftDatabaseModel;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResultsDatabaseConnection {

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + File.separator + "results.db";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            //System.out.println("Connection to SQLite has been established.\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;

    }

    /************************************************************************************************************************************************/

    public void deleteFromPointData() {
        String sql = "DELETE FROM point_data";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void deleteFromPlaneData() {
        String sql = "DELETE FROM plane_data";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public void insertToPointData(int packId, String icao24, String icao, int unix_time, int unix_last, double latitude, double longitude,
                                  Double baro_altitude, Double altitude, boolean on_ground, Double velocity, Double true_track, Double vertical_rate,
                                  String source, String model, String callsign, String registration, String flightnumber, String opicaocode) {

        String sql = "INSERT INTO point_data(packId, icao24, icao, unix_time, unix_last, latitude, longitude, baro_altitude, altitude, on_ground, " +
                "velocity, true_track, vertical_rate, Source, model, callsign, registration, flightnumber, opicaocode) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, packId);
            pstmt.setString(2, icao24);
            pstmt.setString(3, icao);
            pstmt.setInt(4, unix_time);
            pstmt.setInt(5, unix_last);
            pstmt.setDouble(6, latitude);
            pstmt.setDouble(7, longitude);

            if(baro_altitude == null){

            } else {
                pstmt.setDouble(8, baro_altitude);
            }

            if(altitude == null){

            } else {
                pstmt.setDouble(9, altitude);
            }

            pstmt.setBoolean(10, on_ground);

            if(velocity == null){

            } else {
                pstmt.setDouble(11, velocity);
            }

            if(true_track == null){

            } else {
                pstmt.setDouble(12, true_track);
            }

            if(vertical_rate == null){

            } else {
                pstmt.setDouble(13, vertical_rate);
            }

            pstmt.setString(14, source);

            pstmt.setString(15, model);
            pstmt.setString(16, callsign);
            pstmt.setString(17, registration);
            pstmt.setString(18, flightnumber);
            pstmt.setString(19, opicaocode);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public void insertToPlaneData(AircraftDatabaseModel adm){

        String sql = "INSERT INTO plane_data(icao24, registration, manufacturericao, manufacturername, model, typecode, serialnumber, " +
                "linenumber, icaoaircrafttype, operator, operatorcallsign, operatoricao, operatoriata, owner, testreg, registered, reguntil, status, " +
                "built, firstflightdate, seatconfiguration, engines, modes, adsb, acars, notes, categoryDescription) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, adm.getIcao24());
            pstmt.setString(2, adm.getRegistration());
            pstmt.setString(3, adm.getManufacturericao());
            pstmt.setString(4, adm.getManufacturername());
            pstmt.setString(5, adm.getModel());
            pstmt.setString(6, adm.getTypecode());
            pstmt.setString(7, adm.getSerialnumber());
            pstmt.setString(8, adm.getLinenumber());
            pstmt.setString(9, adm.getIcaoaircrafttype());
            pstmt.setString(10, adm.getOperator());
            pstmt.setString(11, adm.getOperatorcallsign());
            pstmt.setString(12, adm.getOperatoricao());
            pstmt.setString(13, adm.getOperatoriata());
            pstmt.setString(14, adm.getOwner());
            pstmt.setString(15, adm.getTestreg());
            pstmt.setString(16, adm.getRegistered());
            pstmt.setString(17, adm.getReguntil());
            pstmt.setString(18, adm.getStatus());
            pstmt.setString(19, adm.getBuilt());
            pstmt.setString(20, adm.getFirstflightdate());
            pstmt.setString(21, adm.getSeatconfiguration());
            pstmt.setString(22, adm.getEngines());
            pstmt.setString(23, adm.getModes());
            pstmt.setString(24, adm.getAdsb());
            pstmt.setString(25, adm.getAcars());
            pstmt.setString(26, adm.getNotes());
            pstmt.setString(27, adm.getCategoryDescription());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

}
