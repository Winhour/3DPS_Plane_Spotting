package io.github.winhour.functionality;

import io.github.winhour.FlightPlanPointCreator;
import io.github.winhour.OutJSONConverter;
import io.github.winhour.database.FPDatabaseConnection;
import io.github.winhour.model.AirportModel;
import io.github.winhour.model.FPModel;
import io.github.winhour.model.Waypoint;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OmegaFunctionality extends BaseFunctionality{

    // Functionality with the usage of FPModels from fpdatabase

    /*********************************************************************************************************************************************/

    private void printOutToJSONWithinRectangleFP(List<FPModel> fpList, int time_interval, double ctime, FlightPlanPointCreator fppc, List<FPModel> tmpFPList, int do_while_number) {

        /* Print out the PlaneData to JSON */

        OutJSONConverter ojc = new OutJSONConverter();

        String jsonOut = ojc.convertFPM(fpList, tmpFPList);

        //System.out.println(jsonOut);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        int i = 0;

        for (FPModel f: fpList){
            /*
            if (i != 0) {
                FPModel test = new FPModel();
                test.setLon_c(f.getLon_c());
                test.setLat_c(f.getLat_c());
                test.setIcao24(f.getIcao24());
                test.setAlt(f.getAlt());
                tmpFPList.set(i, test);
                i++;
            }
             */

            double lat_start = f.getLat_c();
            double lon_start = f.getLon_c();
            double alt_start = f.getAlt();
            double spd_start = f.getSpd();

            if (f.getIteration() == 1) {
                fppc.calculateCurrentCoordinates(f.getDep_time(), f.getArr_time(), f.getLat1(), f.getLon1(), f.getLat2(), f.getLon2(), (int)ctime, f.getIcao24(), f, tmpFPList.get(i));
                fppc.calculateCoordinatesForFlightBasedOnSpeed(f, ctime);
            }
            else if (f.getIteration() > 1) {
                //fppc.calculateCurrentCoordinates(f.getDep_time(), f.getArr_time(), f.getLat1(), f.getLon1(), f.getLat2(), f.getLon2(), ctime, f.getIcao24(), f, tmpFPList.get(i));
                fppc.calculateCoordinatesForFlightBasedOnSpeed(f, ctime);
                f.setUnix_time(f.getUnix_time()+time_interval);
            } else {
                System.out.println("Something went wrong with iteration in FPModel");
            }
            //System.out.println(f.getDep_time()*60 + "   " + f.getTimetoclimb() + "   " + f.getTOC_time() + "   " + f.getTimetodescend() + "   " + f.getTOD_time() + "   " +  f.getArr_time()*60);
            //if (ctime != 0){
                //ctime += (time_interval/60);
            //}
            FPModel test = new FPModel();
            test.setLon_c(f.getLon_c());
            test.setLat_c(f.getLat_c());
            test.setIcao24(f.getIcao24());
            test.setAlt(f.getAlt());
            tmpFPList.set(i, test);
            i++;

            double lat_delta = f.getLat_c() - lat_start;
            double lon_delta = f.getLon_c() - lon_start;
            double alt_delta = f.getAlt() - alt_start;
            double spd_delta = f.getSpd() - spd_start;

            DecimalFormat df = new DecimalFormat("###.##########");

            System.out.println("lon_delta = " + new BigDecimal(lon_delta) + " lat_delta = " +  new BigDecimal(lat_delta) + " alt_delta = " + alt_delta + " spd_delta = " + spd_delta);

            double distance_from_last_point = distance(f.getLat_c(), lat_start, f.getLon_c(), lon_start, 0, 0);

            double pointSpeed = distance_from_last_point/time_interval;
            double pointSpeedKts = ((pointSpeed/0.277777778)/1.852);

            System.out.println("Distance between points: " + distance_from_last_point + " m   Point speed (kts): " + pointSpeedKts + "\n");



        }

        System.out.println("\nNumber of aircraft: " + fpList.size() + "   " + formatter.format(date) +"\n");

        if (do_while_number > 1) {

            try {
                writetoFile(jsonOut, "AircraftList.json");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    /************************************************************************************************************************************************/

    private void doTheThingForOmega(double ctime, int time_interval, List<FPModel> fpList, FlightPlanPointCreator fppc, List<FPModel> tmpFPList, int do_while_number) {

        printOutToJSONWithinRectangleFP(fpList, time_interval, ctime, fppc, tmpFPList, do_while_number);
    }

    /*********************************************************************************************************************************************/

    private void doTheThingForOmega(double lamin, double lamax, double lomin, double lomax, int ctime, int time_interval, List<Waypoint> waypointList) {

        List<FPModel> fpList = new ArrayList<>();

        Date dt = new Date();

        LocalDate today = LocalDate.now();

        Date dt2 = new Date();

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {

            ZoneId defaultZoneId = ZoneId.systemDefault();

            dt2 = Date.from(today.atStartOfDay(defaultZoneId).toInstant());

        } else {

            LocalDate dt2_date = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));

            ZoneId defaultZoneId = ZoneId.systemDefault();

            dt2 = Date.from(dt2_date.atStartOfDay(defaultZoneId).toInstant());

        }

        int minutes = 0;

        if (ctime != 0) {

            minutes = ctime;

        } else {

            minutes = (int) TimeUnit.MILLISECONDS.toMinutes(dt.getTime() - dt2.getTime());

        }

        System.out.println("Current time from MON 0:00 in minutes: " + minutes);

        FPDatabaseConnection fpdc = new FPDatabaseConnection();
        fpdc.getNecessaryAttributesFromTables(minutes, fpList, lamin, lamax, lomin, lomax, waypointList);

        System.out.println();
        for (FPModel f : fpList) {
            System.out.println(f.toString());
        }


        FlightPlanPointCreator fppc = new FlightPlanPointCreator();
        fppc.initialize();

        /*
        for (FPModel f: fpList){
            fppc.calculateCurrentCoordinates(f.getDep_time(),f.getArr_time(),f.getLat1(),f.getLon1(),f.getLat2(),f.getLon2(),ctime, f.getIcao24(), f);
        }
        wait10Sec();
        for (FPModel f: fpList){
            fppc.calculateCurrentCoordinates(f.getDep_time(),f.getArr_time(),f.getLat1(),f.getLon1(),f.getLat2(),f.getLon2(),ctime, f.getIcao24(), f);
        }
        wait10Sec();
        for (FPModel f: fpList){
            fppc.calculateCurrentCoordinates(f.getDep_time(),f.getArr_time(),f.getLat1(),f.getLon1(),f.getLat2(),f.getLon2(),ctime, f.getIcao24(), f);
        }
         */

        printOutToJSONWithinRectangleFP(fpList, time_interval, ctime, fppc, new ArrayList<>(), 2);
    }

    /*********************************************************************************************************************************************/

    private void doTheThingForOmega(double lamin, double lamax, double lomin, double lomax, int ctime, int time_interval) {

        List<FPModel> fpList = new ArrayList<>();

        Date dt = new Date();

        LocalDate today = LocalDate.now();

        Date dt2 = new Date();

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {

            ZoneId defaultZoneId = ZoneId.systemDefault();

            dt2 = Date.from(today.atStartOfDay(defaultZoneId).toInstant());

        } else {

            LocalDate dt2_date = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));

            ZoneId defaultZoneId = ZoneId.systemDefault();

            dt2 = Date.from(dt2_date.atStartOfDay(defaultZoneId).toInstant());

        }

        int minutes = 0;

        if (ctime != 0) {

            minutes = ctime;

        } else {

            minutes = (int) TimeUnit.MILLISECONDS.toMinutes(dt.getTime() - dt2.getTime());

        }

        System.out.println("Current time from MON 0:00 in minutes: " + minutes);

        FPDatabaseConnection fpdc = new FPDatabaseConnection();
        //fpdc.getNecessaryAttributesFromTablesOLD(minutes, fpList, lamin, lamax, lomin, lomax);
        fpdc.getNecessaryAttributesFromTablesV4(minutes, fpList, lamin, lamax, lomin, lomax);

        System.out.println();
        for (FPModel f : fpList) {
            System.out.println(f.toString());
        }


        FlightPlanPointCreator fppc = new FlightPlanPointCreator();
        fppc.initialize();

        /*
        for (FPModel f: fpList){
            fppc.calculateCurrentCoordinates(f.getDep_time(),f.getArr_time(),f.getLat1(),f.getLon1(),f.getLat2(),f.getLon2(),ctime, f.getIcao24(), f);
        }
        wait10Sec();
        for (FPModel f: fpList){
            fppc.calculateCurrentCoordinates(f.getDep_time(),f.getArr_time(),f.getLat1(),f.getLon1(),f.getLat2(),f.getLon2(),ctime, f.getIcao24(), f);
        }
        wait10Sec();
        for (FPModel f: fpList){
            fppc.calculateCurrentCoordinates(f.getDep_time(),f.getArr_time(),f.getLat1(),f.getLon1(),f.getLat2(),f.getLon2(),ctime, f.getIcao24(), f);
        }
         */

        printOutToJSONWithinRectangleFP(fpList, time_interval, ctime, fppc, new ArrayList<>(),2);
    }

    /*********************************************************************************************************************************************/

    public void doTheThingForOmegaRep(double lamin, double lamax, double lomin, double lomax, double ctime, int time_interval, List<Waypoint> waypointList) {

        /* OMEGA repeatedly for testing */

        Date dt = new Date();

        LocalDate today = LocalDate.now();

        Date dt2 = new Date();

        List<FPModel> tmpFPList = new ArrayList<>();

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {

            ZoneId defaultZoneId = ZoneId.systemDefault();

            dt2 = Date.from(today.atStartOfDay(defaultZoneId).toInstant());

        } else {

            LocalDate dt2_date = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));

            ZoneId defaultZoneId = ZoneId.systemDefault();

            dt2 = Date.from(dt2_date.atStartOfDay(defaultZoneId).toInstant());

        }

        double minutes = 0;

        if (ctime != 0) {

            minutes = ctime;

        } else{

            minutes = (int) TimeUnit.MILLISECONDS.toMinutes(dt.getTime() - dt2.getTime());

        }

        System.out.println("Current time from MON 0:00 in minutes: " + minutes);

        List<FPModel> fpList = new ArrayList<>();

        FPDatabaseConnection fpdc = new FPDatabaseConnection();
        //fpdc.getNecessaryAttributesFromTables(minutes, fpList, lamin, lamax, lomin, lomax, waypointList);
        fpdc.getNecessaryAttributesFromTablesV4(minutes, fpList, lamin, lamax, lomin, lomax);

        /*
        for(int i=0;i<fpList.size();i++){
            for (int j=0;j<fpList.size();j++){
                if(i!=j){
                    if(fpList.get(i).getTOC_time() == (fpList.get(j).getTOC_time())){
                        fpList.remove(j);
                    }
                }
            }
        }
         */

        Set<String> aircraftIcaos = new HashSet<>();
        List<AirportModel> amList = new ArrayList<>();

        System.out.println();
        for (FPModel f: fpList){
            System.out.println(f.toString());

            //Temporary setting here

            f.setType("B738");
            f.setModel("Boeing 737-800");
            f.setMan("Boeing");

            //setting up aircrafts

            aircraftIcaos.add(f.getDep_apt());
            aircraftIcaos.add(f.getArr_apt());


        }

        //System.out.println(aircraftIcaos);

        for (String ai : aircraftIcaos){

            AirportModel tmp = fpdc.getFromAirports(ai);
            amList.add(tmp);

        }

        for (AirportModel am: amList){


            System.out.println(am.toString());

            for (FPModel f: fpList){
                if (f.getDep_apt().equals(am.getIcao())){
                    am.add_to_fpmlist_departures(f);
                }
            }

        }

        for (FPModel f: fpList){
            FlightPlanPointCreator fppc = new FlightPlanPointCreator();
            fppc.initialize();
            if (f.getIteration() == 1) {
                fppc.calculateCurrentCoordinates(f.getDep_time(), f.getArr_time(), f.getLat1(), f.getLon1(), f.getLat2(), f.getLon2(), ctime, f.getIcao24(), f, f);     //TODO: check the last f
                fppc.calculateCoordinatesForFlightBasedOnSpeed(f, ctime);
            }
            else if (f.getIteration() > 1) {
                //fppc.calculateCurrentCoordinates(f.getDep_time(), f.getArr_time(), f.getLat1(), f.getLon1(), f.getLat2(), f.getLon2(), ctime, f.getIcao24(), f, f);
                fppc.calculateCoordinatesForFlightBasedOnSpeed(f, ctime);
            } else {
                System.out.println("Something went wrong with iteration in FPModel");
            }
            FPModel test = new FPModel();
            test.setLon_c(f.getLon_c());
            test.setLat_c(f.getLat_c());
            test.setIcao24(f.getIcao24());
            test.setAlt(f.getAlt());
            tmpFPList.add(test);
        }

        FlightPlanPointCreator fppc = new FlightPlanPointCreator();
        fppc.initialize();

        int do_while_number = 0;

        do {

            //Save current time

            Date date = new Date();

            doTheThingForOmega(ctime,time_interval,fpList, fppc, tmpFPList, do_while_number);

            if(do_while_number > 1) {

                //Modify in order to consider real time

                Date date2 = new Date();

                int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(date2.getTime() - date.getTime());

                System.out.println("Real time seconds passed: " + seconds);

                if(time_interval>=seconds) {

                    waitXSec(time_interval - seconds);

                } else {

                    time_interval = seconds;

                }

                System.out.println("Time_interval: " + time_interval + "\n");

            }

            do_while_number++;
            ctime += (double)time_interval/60;

        } while (true);


    }

    /************************************************************************************************************************************************/

    public void testOmega(double lat1, double lon1, double lat2, double lon2, int dep_time, int arr_time, int alt, int time_interval, double ctime){

        //new BigDecimal(myvalue).toPlainString()

        int ctime2 = (int)ctime;

        FPModel fpm = new FPModel();
        FPModel tmp = new FPModel();

        boolean isFirst = true;

        fpm.setIcao24("TEST_AIRPLANE");
        fpm.setLat1(lat1);
        fpm.setLon1(lon1);
        fpm.setLat2(lat2);
        fpm.setLon2(lon2);
        fpm.setAlt(alt);
        fpm.setRoute_alt(alt);
        fpm.setDep_time(dep_time);
        fpm.setArr_time(arr_time);
        fpm.setTimetoclimb();
        fpm.setTimetodescend();
        fpm.setTOC();
        fpm.setTOD();
        fpm.setTOD_time();
        fpm.setTOC_time();
        fpm.setLength();
        fpm.setTOClat();
        fpm.setTOClon();
        fpm.setTODlat();
        fpm.setTODlon();
        fpm.setStarting_airport_altitude(120);
        fpm.setLanding_airport_altitude(150);
        fpm.setTOD_timeGenerated();
        fpm.setType("B738");
        fpm.setModel("Boeing 737-800");
        fpm.setMan("Boeing");
        fpm.setReg("SP-TST");
        fpm.setCallsign("LOT999");
        fpm.setOp("LOT Polish");

        fpm.setIteration(5);

        FlightPlanPointCreator fppc = new FlightPlanPointCreator();
        fppc.initialize();

        Date dt = new Date();

        long time = dt.getTime();

        long injection_time = TimeUnit.MILLISECONDS.toSeconds(time);


        List<FPModel> fpList = new ArrayList<>();
        List<FPModel> tmpFPList = new ArrayList<>();

        double percentage = (double)((ctime) - dep_time)/(arr_time - dep_time);

        fpList.add(fpm);

        fpList.get(0).setLat_c(lat1+(percentage*(lat2-lat1)));
        fpList.get(0).setLon_c(lon1+(percentage*(lon2-lon1)));

        tmpFPList.add(fpm);

        do {

            System.out.println("Percentage: " + percentage);

            System.out.println("Lon_c: " + fpm.getLon_c());
            System.out.println("Lat_c: " + fpm.getLat_c());

            //fppc.calculateCoordinatesForFlightBasedOnSpeed(fpm, ctime);

            if (!fpList.get(0).isLanded()) {
                printOutToJSONWithinRectangleFP(fpList, time_interval, ctime, fppc, tmpFPList, 2);
            } else {

                try {
                    writetoFile("", "AircraftList.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //fpList.remove(0);
                //break;
            }

            if(!isFirst){

                //dLat, dLon, dAlt, dSpd, dTrk

                double dLat = fpList.get(0).getLat_c() - tmp.getLat_c();
                double dLon = fpList.get(0).getLon_c() - tmp.getLon_c();
                double dAlt = fpList.get(0).getAlt() - tmp.getAlt();
                double dSpd = fpList.get(0).getSpd() - tmp.getSpd();
                double dTrk = fpList.get(0).getTrue_track() - tmp.getTrue_track();

                DecimalFormat df = new DecimalFormat("###.##########");

                //new BigDecimal(dLat).toPlainString()

                System.out.println("dLat: " + df.format(new BigDecimal(dLat)) + "   dLon: " + df.format(new BigDecimal(dLon)) + "   dAlt: " + dAlt + "   dSpd: " + dSpd + "   dTrk: " + dTrk);

                double distance_from_last_point = distance(fpList.get(0).getLat_c(), tmp.getLat_c(), fpList.get(0).getLon_c(), tmp.getLon_c(), 0, 0);

                double pointSpeed = distance_from_last_point/time_interval;
                double pointSpeedKts = ((pointSpeed/0.277777778)/1.852);

                System.out.println("Point Speed: " + pointSpeedKts + " kts    tmp_latC = " + tmp.getLat_c());

            } else {

                isFirst = false;

            }

            waitXSec(time_interval);

            ctime += (double)time_interval/60;

            Date dt2 = new Date();

            long time2 = dt2.getTime();

            //long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

            long current_time = TimeUnit.MILLISECONDS.toSeconds(time2);

            long real_time_difference = current_time - injection_time;

            int real_time = (ctime2*60 + (int)real_time_difference)/60;

            System.out.println("Real time difference: " + real_time_difference + "s   Real time: " + real_time + " min   ctime = " + ctime  + " min");
            System.out.println();

            for (FPModel f: fpList) {
                System.out.println(fpm.toString());
            }

            System.out.println();

            tmp.setLon_c(fpList.get(0).getLon_c());
            tmp.setLat_c(fpList.get(0).getLat_c());
            tmp.setAlt(fpList.get(0).getAlt());
            tmp.setSpd(fpList.get(0).getSpd());
            tmp.setTrue_track(fpList.get(0).getTrue_track());

        } while (true);



    }


    /************************************************************************************************************************************************/

    public void testOmega2(double lat1, double lon1, double lat2, double lon2, int dep_time, int arr_time, int alt){

        dep_time = dep_time;  //changing minutes to seconds
        arr_time = arr_time;

        FPModel fpm = new FPModel();

        fpm.setIcao24("TEST_AIRPLANE");
        fpm.setLat1(lat1);
        fpm.setLon1(lon1);
        fpm.setLat2(lat2);
        fpm.setLon2(lon2);
        fpm.setAlt(alt);
        fpm.setRoute_alt(alt);
        fpm.setDep_time(dep_time);
        fpm.setArr_time(arr_time);
        fpm.setTimetoclimb();
        fpm.setTimetodescend();
        fpm.setTOC();
        fpm.setTOD();
        fpm.setTOD_time();
        fpm.setTOC_time();
        fpm.setLength();
        fpm.setTOClat();
        fpm.setTOClon();
        fpm.setTODlat();
        fpm.setTODlon();
        fpm.setStarting_airport_altitude(120);
        fpm.setLanding_airport_altitude(150);
        fpm.setTOD_timeGenerated();
        fpm.setIteration(1);

        FlightPlanPointCreator fppc = new FlightPlanPointCreator();
        fppc.initialize();

        int time1 = (dep_time + 10);
        fpm.setLat_c(lat1+(0.01*(lat2-lat1)));
        fpm.setLon_c(lon1+(0.01*(lon2-lon1)));

        System.out.println(fpm.toString());
        System.out.println(time1 + "   " + fpm.getTOD_timeGenerated());

        fppc.calculateCoordinatesForFlightBasedOnSpeed(fpm, time1);

        int time5 = ((fpm.getTOD_timeGenerated()/60) + 1);
        fpm.setLat_c(lat2-1.0);
        fpm.setLon_c(lon2-1.0);

        fppc.calculateCoordinatesForFlightBasedOnSpeed(fpm, time5);

        int time6 = ((fpm.getTOD_timeGenerated()/60) + 3);

        fppc.calculateCoordinatesForFlightBasedOnSpeed(fpm, time6);

        int time2 = ((fpm.getTOD_timeGenerated()/60) + 10);
        fpm.setLat_c(lat2-0.1);
        fpm.setLon_c(lon2-0.1);

        fppc.calculateCoordinatesForFlightBasedOnSpeed(fpm, time2);

        int time3 = ((fpm.getTOD_timeGenerated()/60) + 13);
        fpm.setLat_c(lat2-0.1);
        fpm.setLon_c(lon2-0.1);

        fppc.calculateCoordinatesForFlightBasedOnSpeed(fpm, time3);

        int time4 = ((fpm.getTOD_timeGenerated()/60) + 16);
        fpm.setLat_c(lat2-0.1);
        fpm.setLon_c(lon2-0.1);

        fppc.calculateCoordinatesForFlightBasedOnSpeed(fpm, time4);


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
