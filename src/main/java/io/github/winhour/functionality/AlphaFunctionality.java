package io.github.winhour.functionality;

import com.martiansoftware.jsap.JSAPResult;
import io.github.winhour.OutJSONConverter;
import io.github.winhour.PlaneDataCreator;
import io.github.winhour.controller.AlphaController;
import io.github.winhour.database.AlphaDatabaseConnection;
import io.github.winhour.database.ResultsDatabaseConnection;
import io.github.winhour.model.PlaneData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AlphaFunctionality extends BaseFunctionality{

    /*********************************************************************************************************************************************/

    public void doTheThingForAlpha(double lamin, double lamax, double lomin, double lomax, List<List<PlaneData>> listOfLists, int howmany,
                                          Set<String> icao24set, int nrofseeks, int time_in_between, JSAPResult config) {

        /* ALPHA */

        List<PlaneData> dataList = new ArrayList<>();

        AlphaController osc = new AlphaController();

        if (config.getString("OUTPUT").equals("DB")){

            if (nrofseeks == 1337) {

                ResultsDatabaseConnection rdc = new ResultsDatabaseConnection();
                if (config.getBoolean("clear")) {
                    rdc.deleteFromPointData();
                }

                while (true) {

                    waitXSec(time_in_between);

                    dataList = new ArrayList<>();

                    String prettyJsonString = osc.getFromOS(lamin, lamax, lomin, lomax);

                    osc.createListFromJson(prettyJsonString, dataList);

                    listOfLists.add(dataList);

                    insertSingleListtoRDC(dataList, icao24set, rdc);

                    System.out.println("Added another package to database");
                }

            } else {

                alphaControllerInteraction(lamin, lamax, lomin, lomax, osc, dataList, "test.txt");

                printOutAllData(dataList);

                listOfLists.add(dataList);

                for (int i = 0; i < (nrofseeks - 1); i++) {

                    wait10Sec();

                    dataList = new ArrayList<>();

                    alphaControllerInteraction(lamin, lamax, lomin, lomax, osc, dataList, "test" + (i + 1) + ".txt");

                    printOutAllData(dataList);

                    listOfLists.add(dataList);

                    //json somewhere here?

                }

                saveToResultsAndCreateInBetweenPoints(listOfLists, howmany, icao24set);
            }

        } else if (config.getString("OUTPUT").equals("JSON")){

            do {

                alphaToJSON(lamin, lamax, lomin, lomax, time_in_between, dataList, osc);

                waitXSec(time_in_between);

                dataList = new ArrayList<>();

            } while (true);

        } else {
            System.out.println("Wrong output type, choose DB or JSON");
        }

    }

    /*********************************************************************************************************************************************/

    private void alphaToJSON(double lamin, double lamax, double lomin, double lomax, int time_in_between, List<PlaneData> dataList, AlphaController osc) {
        String prettyJsonString = osc.getFromOS(lamin, lamax, lomin, lomax);

        osc.createListFromJson(prettyJsonString, dataList);

        printOutToJSONWithinRectangle(dataList, lomin, lomax, lamin, lamax, time_in_between);
    }

    /************************************************************************************************************************************************/

    private void alphaControllerInteraction(double lamin, double lamax, double lomin, double lomax, AlphaController osc, List<PlaneData> dataList, String filename){
        String prettyJsonString = osc.getFromOS(lamin, lamax, lomin, lomax);

        /* Using the ALPHA controller */

        try (
                PrintWriter out = new PrintWriter(filename)) {
            out.println(prettyJsonString);
            System.out.println("Wrote to file " + filename +"\n");
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        osc.createListFromJson(prettyJsonString, dataList);
    }

    /************************************************************************************************************************************************/

}
