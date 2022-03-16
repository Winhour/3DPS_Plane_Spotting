package io.github.winhour.functionality;

import io.github.winhour.XMLInteraction;
import io.github.winhour.database.AlphaDatabaseConnection;
import io.github.winhour.database.FPDatabaseConnection;
import io.github.winhour.database.ResultsDatabaseConnection;
import io.github.winhour.model.AircraftDatabaseModel;
import io.github.winhour.model.FPAirline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AircraftDatabaseFunctionality {

    /*********************************************************************************************************************************************/

    public void populateAircraftDatabase(Set<String> icao24Set, List<AircraftDatabaseModel> admlist) {

        AlphaDatabaseConnection dbc = new AlphaDatabaseConnection();
        ResultsDatabaseConnection rdc = new ResultsDatabaseConnection();

        saveAircraftdatabaseModels(icao24Set, admlist, dbc, rdc);

        XMLInteraction xmlInteraction = new XMLInteraction();
        FPDatabaseConnection fpDatabaseConnection = new FPDatabaseConnection();

        List<FPAirline> airList = new ArrayList<>();

        xmlInteraction.parseXMLStringAirlines(airList);


        for (FPAirline f : airList){
            //fpDatabaseConnection.insertToAirlines(f);
            System.out.println(f.toString());
        }

    }

    /*********************************************************************************************************************************************/

    private void saveAircraftdatabaseModels(Set<String> icao24Set, List<AircraftDatabaseModel> admlist, AlphaDatabaseConnection dbc, ResultsDatabaseConnection rdc) {

        for (String s : icao24Set) {

            dbc.getFromAircraftDatabase(s, admlist);

        }

        rdc.deleteFromPlaneData();

        for (AircraftDatabaseModel a : admlist) {

            if (!a.getIcao24().equals("Infinity") && !a.getIcao24().equals("") && a.getIcao24() != null) {

                rdc.insertToPlaneData(a);

            }

        }
    }

    /*********************************************************************************************************************************************/

}
