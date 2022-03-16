package io.github.winhour.functionality;

import io.github.winhour.OutJSONConverter;
import io.github.winhour.PlaneDataCreator;
import io.github.winhour.database.AlphaDatabaseConnection;
import io.github.winhour.database.ResultsDatabaseConnection;
import io.github.winhour.model.PlaneData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class BaseFunctionality {

    static List <PlaneData> tmpList = null;
    static boolean isFirst = true;

    /*********************************************************************************************************************************************/

    public void saveToResultsAndCreateInBetweenPoints(List<List<PlaneData>> listOfLists, int howmany, Set<String> icao24set) {

        /* Save data and create intermediary points */

        PlaneDataCreator pdc = new PlaneDataCreator();
        ResultsDatabaseConnection rdc = new ResultsDatabaseConnection();
        rdc.deleteFromPointData();

        pdc.setHowmany(howmany);

        int k = listOfLists.size()-1;

        for(int l=0;l<k;l++){
            List<PlaneData> tmp1 = listOfLists.get(l);
            List<PlaneData> tmp2 = listOfLists.get(l+1);

            for(int x=1;x<howmany;x++) {

                List<PlaneData> testList = pdc.createIntermediaryPlaneData(x, tmp1, tmp2);
                listOfLists.add(testList);

            }

        }

        int m = listOfLists.size();

        for (int n=0; n<m; n++){
            insertSingleListtoRDC(listOfLists.get(n),icao24set,rdc);
        }

    }

    /*********************************************************************************************************************************************/

    public void resultsDatabaseSave(List<PlaneData> dataList, List<PlaneData> dataList2, List<PlaneData> dataList3, List<PlaneData> testList,
                                            List<PlaneData> testList2, List<PlaneData> testList3, List<PlaneData> testList4, Set<String> icao24set){

        ResultsDatabaseConnection rdc = new ResultsDatabaseConnection();

        rdc.deleteFromPointData();

        insertPD(dataList, dataList2, dataList3, rdc, icao24set);

        insertPD(testList, testList2, testList3, rdc, icao24set);

        insertSingleListtoRDC(testList4, icao24set, rdc);

    }

    /*********************************************************************************************************************************************/

    public void printTestLists(List<PlaneData> testList, List<PlaneData> testList2, List<PlaneData> testList3, List<PlaneData> testList4){

        /* Print out information about the lists */


        System.out.println("\n\nTEST LIST NR 1 (BETWEEN 1 and 2, STEP 1)\n\n");

        printOutAllData(testList);

        System.out.println("\n\nTEST LIST NR 2 (BETWEEN 1 and 2, STEP 2)\n\n");

        printOutAllData(testList2);

        System.out.println("\n\nTEST LIST NR 3 (BETWEEN 2 and 3, STEP 1)\n\n");

        printOutAllData(testList3);

        System.out.println("\n\nTEST LIST NR 4 (BETWEEN 2 and 3, STEP 2)\n\n");

        printOutAllData(testList4);


    }

    /*********************************************************************************************************************************************/

    public void printOutAllData(List<PlaneData> dataList){

        /* Function for testing received data */

        AlphaDatabaseConnection dbc = new AlphaDatabaseConnection();

        int i = 1;

        for (PlaneData p :dataList) {
            System.out.print(i + ") ");
            System.out.println(p.toString());
            dbc.selectTest(p.getIcao24());
            System.out.println("\n*********************************************************\n");
            i++;
        }

    }

    /*********************************************************************************************************************************************/

    public void insertSingleListtoRDC(List<PlaneData> testList4, Set<String> icao24set, ResultsDatabaseConnection rdc) {

        /* Insert single list to point data */

        for (PlaneData p :testList4) {
            if (!p.getIcao24().equals("") && p.getIcao24() != null) {
                rdc.insertToPointData(p.getPackId(), p.getIcao24(), p.getIcao(), p.getUnix_time(), p.getUnix_last(), p.getLatitude(), p.getLongitude(), p.getBaro_altitude(),
                        p.getAltitude(), p.isOn_ground(), p.getVelocity(), p.getTrue_track(), p.getVertical_rate(), p.getSource(), p.getModel(), p.getCallsign(),
                        p.getRegistration(), p.getFlightnumber(), p.getOpicaocode());

                icao24set.add(p.getIcao24());
            }
        }
    }

    /************************************************************************************************************************************************/

    public void insertPD(List<PlaneData> dataList, List<PlaneData> dataList2, List<PlaneData> dataList3, ResultsDatabaseConnection rdc, Set<String> icao24set) {

        /* Insert multiple lists to point data */

        insertSingleListtoRDC(dataList, icao24set, rdc);

        insertSingleListtoRDC(dataList2, icao24set, rdc);

        insertSingleListtoRDC(dataList3, icao24set, rdc);
    }

    /************************************************************************************************************************************************/

    public void wait10Sec(){

        /* Wait for 10 seconds */

        System.out.println("\nWaiting for next data pack... \n");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /************************************************************************************************************************************************/

    public void waitXSec(int x){

        /* Wait for X seconds */

        System.out.println("\nWaiting for next data pack... \n");

        try {
            Thread.sleep(x*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /************************************************************************************************************************************************/

    public void writetoFile(String str, String fileName)
            throws IOException {

        /* Simple write to file */

        Path path = Paths.get(fileName);
        byte[] strToBytes = str.getBytes();

        Files.write(path, strToBytes);

    }

    /************************************************************************************************************************************************/

    public void printOutToJSONWithinRectangle(List<PlaneData> dataList, double lonmin, double lonmax, double latmin, double latmax, int time_interval) {

        /* Print out the PlaneData to JSON */

        OutJSONConverter ojc = new OutJSONConverter();

        List<PlaneData> newList = new ArrayList<>();

        newList = ojc.limitRectangle(dataList,latmin,latmax,lonmin,lonmax);

        String jsonOut = ojc.convert(newList, tmpList);

        //System.out.println(jsonOut);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        System.out.println("\nNumber of aircraft: " + newList.size() + "   " + formatter.format(date) +"\n");

        try {
            writetoFile(jsonOut, "AircraftList.json");
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (isFirst){

            tmpList = newList;
            isFirst = false;

        } else {

            /*
            if (tmpListCounter >= 20) {
                tmpList = newList;
                tmpListCounter = 0;
            }

            tmpListCounter += time_interval;
             */

            //System.out.println("Counter: " + tmpListCounter);

        }

    }


}
