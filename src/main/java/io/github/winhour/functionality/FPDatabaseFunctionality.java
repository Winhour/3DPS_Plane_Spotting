package io.github.winhour.functionality;

import io.github.winhour.AIGFPExtractor;
import io.github.winhour.XMLInteraction;
import io.github.winhour.database.FPDatabaseConnection;
import io.github.winhour.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FPDatabaseFunctionality {

    // Doing things on data from Flight Plan database

    private static String outString = "";

    /*********************************************************************************************************************************************/

    public void FPDatabaseDoTheThing() {

        /* Populating the FPDatabase from AIGFP */

        AIGFPExtractor aext = new AIGFPExtractor();
        FPDatabaseConnection fpd = new FPDatabaseConnection();

        List<String> fileNameList = new ArrayList<>();
        List<FlightPlan> flightPlanList = new ArrayList<>();

        fpd.getFileNames(fileNameList);

        fpd.deleteFromAircraft();
        fpd.deleteFromRegistration();
        fpd.deleteFromFlightPlan();
        fpd.deleteFromLeg();
        fpd.deleteFromRoutes();
        fpd.deleteFromWaypoints();


        int j = 0;
        int i = 1;
        WrapInt fpidcounter = new WrapInt(0);
        WrapInt airidcounter = new WrapInt(0);
        WrapInt regidcounter = new WrapInt(0);
        List<Waypoint> allwaypoints = new ArrayList<>();

        for (String f: fileNameList) {

            System.out.println("/*****************************************************************************************/");
            System.out.println(f);
            System.out.println("/*****************************************************************************************/");

            List<String> route_strings = new ArrayList<>();
            String fplanString = aext.extract(f, route_strings);

            /*System.out.println(fplanString);
            System.out.println();
            System.out.println();
            System.out.println();
            for (String str : route_strings){
                System.out.println(str);
            }
            System.out.println();
            System.out.println();
            System.out.println();
             */

            List<Route> routeList = new ArrayList<>();
            XMLInteraction xmlInteraction = new XMLInteraction();
            for (String str : route_strings) {
                xmlInteraction.parseXMLStringRoutes(str, routeList, i);
                i++;
            }


            try {
                fpd.insertIntoRoutes(routeList);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            for (Route r : routeList){
                List<Waypoint> tmpway = r.getWaypoints();
                for (Waypoint w : tmpway){
                    allwaypoints.add(w);
                }
            }


            //printOutRouteData(routeList);

            FlightPlan fPlan = xmlInteraction.parseXMLStringFlightPlan(fplanString, fpidcounter, airidcounter,regidcounter);

            fPlan.setFile_name(f);

            flightPlanList.add(fPlan);

            //printOutFlightPlan(fPlan);


            if (j<7)
                saveOutFlightPlan(fPlan, f, routeList);

            if (j == 7){
                try {
                    writetoFile(outString, "FPlansOUT.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            j++;


            List<Aircraft> tmpair = fPlan.getAircraftList();


            try {
                fpd.insertIntoAircraftAndRegistration(tmpair);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            for (Aircraft a: tmpair){
                List<Registration> regs = a.getRegistrations();
                for (Registration r: regs){
                    if(r.getReg_id() != 99999999 && r.getLegList()!= null) {
                        List<Leg> tmpleg = r.getLegList();
                        try {
                            fpd.insertIntoLegs(tmpleg);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            }



        }


        try {
            writetoFile(outString, "FPlansOUT.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("\n[Storing the Flight Plans]\n");

        try {
            fpd.insertIntoFlightPlans(flightPlanList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("\n[Storing the Waypoints]\n");

        System.out.println("Number of Waypoints: " + allwaypoints.size());

        try {
            fpd.insertIntoWaypoints(allwaypoints);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /*********************************************************************************************************************************************/

    public void saveOutFlightPlan(FlightPlan fPlan, String f, List<Route> routeList) {

        /* Test output string creation */


        outString += ("/*****************************************************************************************/\n");
        outString += (f + "\n");
        outString += ("/*****************************************************************************************/\n");


        outString += "\nROUTES: \n";

        for (Route r : routeList){

            outString +=("Name: " + r.getName() + "  Min: " + r.getMin() + "   Max: " + r.getMax() + "   Alt: " + r.getAlt() + "   Rule: " + r.getRule()+ "   Id: " + r.getRoute_id() +"\n\n");

            for (Waypoint w : r.getWaypoints()){

                outString +=(w.toString()+"\n");

            }

            outString += "\n\n";

        }

        outString += "\nFLIGHTPLAN:\n\nAirport_fs: " + fPlan.getAirport_fs() + "   Fp_Id: " + fPlan.getFp_id() + "   File_name: " + fPlan.getFile_name() + "\n\n\n\n";

        for (String s : fPlan.getAigRouteList()){
            outString += "AIGRoute -> " + s + "\n";
        }

        outString += "\n\n\n\n";

        for (Aircraft a : fPlan.getAircraftList()){
            outString += "\n\nAircraft -> Id: " + a.getId() + "   Type: " + a.getType() + "   Title: " + a.getTitle() + "   Air_Id: " + a.getAir_id() + "   Fp_Id: " + a.getFp_id() + "\n\n";
            List<Registration> regs = a.getRegistrations();
            for (Registration r: regs) {
                //System.out.println("Registration -> Id: " + r.getId() + "   Rotation: " + r.getRotation() + "   Reg_Id: " + r.getReg_id() + "   Air_Id: " + r.getAir_id() + "\n");
                outString += "\n\nRegistration -> Id: " + r.getId() + "   Rotation: " + r.getRotation() + "   Reg_Id: " + r.getReg_id() + "   Air_Id: " + r.getAir_id() + "\n\n";
                if (r.getLegList() != null) {
                    for (Leg l : r.getLegList()) {
                        outString += (l.toString() + "\n");
                    }
                }
            }

            outString += "\n";

        }

        outString += "\n\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n\n";

    }

    /************************************************************************************************************************************************/

    public static void printOutRouteData(List<Route> routeList){

        /* Testing route data */

        for (Route r : routeList){

            System.out.println("Name: " + r.getName() + "  Min: " + r.getMin() + "   Max: " + r.getMax() + "   Alt: " + r.getAlt() + "   Rule: " + r.getRule());

            for (Waypoint w : r.getWaypoints()){

                System.out.println(w.toString());

            }

            System.out.println();

        }

    }

    /************************************************************************************************************************************************/

    private static void printOutFlightPlan(FlightPlan fPlan) {

        /* Testing flight plan */

        System.out.println("Airport_fs: " + fPlan.getAirport_fs() + "\n\n\n");

        for (String s : fPlan.getAigRouteList()){
            System.out.println("AIGRoute -> " + s);
        }

        System.out.println("\n\n\n");

        for (Aircraft a : fPlan.getAircraftList()){
            System.out.println("Aircraft -> Id: " + a.getId() + "   Type: " + a.getType() + "   Title: " + a.getTitle() + "\n");
            List<Registration> regs = a.getRegistrations();
            for (Registration r: regs) {
                System.out.println("Registration -> Id: " + r.getId() + "   Rotation: " + r.getRotation() + "\n");
                if (r.getLegList() != null) {
                    for (Leg l : r.getLegList()) {
                        System.out.println(l.toString());
                    }
                }
            }

            System.out.println();

        }

    }


    /*********************************************************************************************************************************************/

    public void writetoFile(String str, String fileName)
            throws IOException {

        /* Simple write to file */

        Path path = Paths.get(fileName);
        byte[] strToBytes = str.getBytes();

        Files.write(path, strToBytes);

    }

    /*********************************************************************************************************************************************/

}
