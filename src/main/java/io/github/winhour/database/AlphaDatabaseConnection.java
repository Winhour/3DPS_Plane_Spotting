package io.github.winhour.database;

import io.github.winhour.model.AircraftDatabaseModel;

import java.io.File;
import java.sql.*;
import java.util.List;

public class AlphaDatabaseConnection {

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + File.separator + "ac.db";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            //System.out.println("Connection to SQLite has been established.\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;

    }

    /************************************************************************************************************************************************/

    public void selectTest(String icao24){
        String sql = "SELECT registration, model, typecode, serialnumber, owner" + " FROM aircraftDatabase WHERE icao24 = ? LIMIT 1";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,icao24);
            //
            ResultSet rs  = pstmt.executeQuery();

            if(rs.next() == false){
                System.out.println("Did not find data in database for the given icao24");
            }
            else {
                // loop through the result set
                do  {
                    System.out.println("Database data: {" +
                            "icao24: " + icao24 + "\t" +
                            "registration: " + rs.getString("registration") + "\t" +
                            "model: " + rs.getString("model") + "\t" +
                            "typecode: " + rs.getString("typecode") + "\t" +
                            "serialnumber: " + rs.getString("serialnumber") + "\t" +
                            "owner: " + rs.getString("owner") + "}"
                    );
                } while (rs.next());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

    public void getFromAircraftDatabase(String icao24, List<AircraftDatabaseModel> admlist){
        String sql = "SELECT * " + " FROM aircraftDatabase WHERE icao24 = ? LIMIT 1";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,icao24);
            //
            ResultSet rs  = pstmt.executeQuery();

            AircraftDatabaseModel adm = new AircraftDatabaseModel();

            if(rs.next() == false){
                System.out.println("Did not find data in database for the given icao24");
            }
            else {
                // loop through the result set
                do  {

                    adm.setIcao24(icao24);
                    adm.setRegistration(rs.getString("registration"));
                    adm.setManufacturericao(rs.getString("manufacturericao"));
                    adm.setManufacturername(rs.getString("manufacturername"));
                    adm.setModel(rs.getString("model"));
                    adm.setTypecode(rs.getString("typecode"));
                    adm.setSerialnumber(rs.getString("serialnumber"));
                    adm.setLinenumber(rs.getString("linenumber"));
                    adm.setIcaoaircrafttype(rs.getString("icaoaircrafttype"));
                    adm.setOperator(rs.getString("operator"));
                    adm.setOperatorcallsign(rs.getString("operatorcallsign"));
                    adm.setOperatoricao(rs.getString("operatoricao"));
                    adm.setOperatoriata(rs.getString("operatoriata"));
                    adm.setOwner(rs.getString("owner"));
                    adm.setTestreg(rs.getString("testreg"));
                    adm.setRegistered(rs.getString("registered"));
                    adm.setReguntil(rs.getString("reguntil"));
                    adm.setStatus(rs.getString("status"));
                    adm.setBuilt(rs.getString("built"));
                    adm.setFirstflightdate(rs.getString("firstflightdate"));
                    adm.setSeatconfiguration(rs.getString("seatconfiguration"));
                    adm.setEngines(rs.getString("engines"));
                    adm.setModes(rs.getString("modes"));
                    adm.setAdsb(rs.getString("adsb"));
                    adm.setAcars(rs.getString("acars"));
                    adm.setNotes(rs.getString("notes"));
                    adm.setCategoryDescription(rs.getString("categoryDescription"));

                    admlist.add(adm);

                } while (rs.next());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /************************************************************************************************************************************************/

}
