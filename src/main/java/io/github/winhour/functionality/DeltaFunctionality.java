package io.github.winhour.functionality;

import io.github.winhour.OutJSONConverter;
import io.github.winhour.controller.DeltaController;
import io.github.winhour.model.PlaneData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeltaFunctionality extends BaseFunctionality{

    /*********************************************************************************************************************************************/

    public void doTheThingForDelta(double lamin, double lamax, double lomin, double lomax, int time_interval) {

        /* DELTA */

        List<PlaneData> dataList = new ArrayList<>();

        DeltaController dc = new DeltaController();

        String prettyJSONString = dc.getFromOS();

        System.out.println();

        dc.createListFromJson(prettyJSONString, dataList);

            /*
            for (PlaneData d: dataList){
                System.out.println(d.toString());
            }
             */



        //printOutToJSON(dataList);
        printOutToJSONWithinRectangle(dataList, lomin, lomax, lamin, lamax, time_interval);


    }

    /*********************************************************************************************************************************************/

    public void doTheThingForDeltaRep(double lamin, double lamax, double lomin, double lomax, int time_interval) {

        /* DELTA repeatedly for testing */

        do {

            doTheThingForDelta(lamin,lamax,lomin,lomax,time_interval);

            waitXSec(time_interval);

        } while (true);


    }



    /*********************************************************************************************************************************************/


}
