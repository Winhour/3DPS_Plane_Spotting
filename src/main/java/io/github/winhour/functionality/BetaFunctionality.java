package io.github.winhour.functionality;

import com.martiansoftware.jsap.JSAPResult;
import io.github.winhour.controller.BetaController;
import io.github.winhour.database.ResultsDatabaseConnection;
import io.github.winhour.model.PlaneData;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BetaFunctionality extends BaseFunctionality{

    /*********************************************************************************************************************************************/

    public void doTheThingForBeta(double lamin, double lamax, double lomin, double lomax, List<List<PlaneData>> listOfLists, int howmany,
                                         Set<String> icao24set, int nrofseeks, int time_in_between, JSAPResult config){

        /* BETA */

        List<PlaneData> dataList = new ArrayList<>();

        BetaController bc = new BetaController();

        if (config.getString("OUTPUT").equals("DB")) {

            if (nrofseeks == 1337) {

                ResultsDatabaseConnection rdc = new ResultsDatabaseConnection();
                if (config.getBoolean("clear")) {
                    rdc.deleteFromPointData();
                }

                while (true) {

                    waitXSec(time_in_between);

                    dataList = new ArrayList<>();

                    String prettyJsonString = bc.getFromOS(lamin, lamax, lomin, lomax);

                    if (prettyJsonString != null) {

                        bc.createListFromJson(prettyJsonString, dataList);

                        listOfLists.add(dataList);

                        insertSingleListtoRDC(dataList, icao24set, rdc);

                        System.out.println("Added another package to database");
                    } else {
                        System.out.println("No connection to the server");
                    }
                }

            } else {

                betaControllerInteraction(lamin, lamax, lomin, lomax, bc, dataList, "test.txt");

                printOutAllData(dataList);

                listOfLists.add(dataList);

                for (int i = 0; i < (nrofseeks - 1); i++) {

                    waitXSec(time_in_between);

                    dataList = new ArrayList<>();

                    betaControllerInteraction(lamin, lamax, lomin, lomax, bc, dataList, "test" + (i + 1) + ".txt");

                    printOutAllData(dataList);

                    listOfLists.add(dataList);

                }

                saveToResultsAndCreateInBetweenPoints(listOfLists, howmany, icao24set);
            }
        } else if (config.getString("OUTPUT").equals("JSON")){

            do {

                betaToJSON(lamin, lamax, lomin, lomax, time_in_between, dataList, bc);

                waitXSec(time_in_between);

                dataList = new ArrayList<>();

            } while (true);

        } else {

            System.out.println("Wrong output type, choose DB or JSON");

        }


    }

    /*********************************************************************************************************************************************/

    private void betaToJSON(double lamin, double lamax, double lomin, double lomax, int time_in_between, List<PlaneData> dataList, BetaController bc) {

        String prettyJsonString = bc.getFromOS(lamin, lamax, lomin, lomax);

        bc.createListFromJson(prettyJsonString, dataList);

        printOutToJSONWithinRectangle(dataList, lomin, lomax, lamin, lamax, time_in_between);
    }

    /************************************************************************************************************************************************/

    private void betaControllerInteraction(double lamin, double lamax, double lomin, double lomax, BetaController betaController, List<PlaneData> dataList, String filename){
        String prettyJsonString = betaController.getFromOS(lamin, lamax, lomin, lomax);

        /* Using the BETA controller */

        try (
                PrintWriter out = new PrintWriter(filename)) {
            out.println(prettyJsonString);
            System.out.println("Wrote to file " + filename +"\n");
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        betaController.createListFromJson(prettyJsonString, dataList);
    }

    /*********************************************************************************************************************************************/





}
