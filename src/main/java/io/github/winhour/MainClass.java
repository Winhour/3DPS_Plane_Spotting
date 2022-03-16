package io.github.winhour;
import java.util.*;

import com.martiansoftware.jsap.*;
import io.github.winhour.database.AirrunDatabaseConnection;
import io.github.winhour.database.FPDatabaseConnection;
import io.github.winhour.functionality.*;
import io.github.winhour.misc.Nominatim;
import io.github.winhour.misc.RegexComparer;
import io.github.winhour.model.*;


public class MainClass {

    //private static int HOW_MANY = 2;                /* How many points in between */
    //private static int NUMBEROFSEEKS = 3;           /* How many seeks */
    //private static String DATATYPE = "DELTA";              /* Which data to use */
    //private static int TIME_IN_BETWEEN = 10;


    private static String outString = "";


    public static void main(String[] args) {

        JSAP jsap = new JSAP();             /*Used for command line inputs */

        try {
            jsap = initializeJSAP(jsap);        /*Function initializing input flags */
        } catch (JSAPException e) {
            e.printStackTrace();
        }

        JSAPResult config = jsap.parse(args);  /* Encapsulates the results of JSAP's parse() methods. */

        RegexComparer rc = new RegexComparer();                         /* Testing regex */





        System.out.println(rc.compare("AIGAIM_RAF-Avia Antonov An-26 - YL-RAD (NO MODEL)"));

        if (config.getBoolean("help")){
            help();
        } else {

            if (config.getDoubleArray("RECTANGLE").length < 4) {

                System.out.println("Rectangle needs 4 parameters, in order: lamin, lamax, lomin, lomax");
                help();

            } else {

                double lamin = config.getDoubleArray("RECTANGLE")[0];   //51.907001886741014;
                double lamax = config.getDoubleArray("RECTANGLE")[1];   //52.68304276227743;
                double lomin = config.getDoubleArray("RECTANGLE")[2];   //19.931945800781254;
                double lomax = config.getDoubleArray("RECTANGLE")[3];   //21.568908691406254;

                //System.out.println(lamin + "  " + lamax + "   " + lomin + "   " + lomax);

                int HOW_MANY = config.getInt("HMPOINTS");                /* How many points in between */
                int NUMBEROFSEEKS = config.getInt("NROFSEEKS");           /* How many seeks */
                String DATATYPE = config.getString("SOURCE");              /* Which data to use */
                int TIME_IN_BETWEEN = config.getInt("INTERVAL");


                List<List<PlaneData>> listOfLists = new ArrayList<List<PlaneData>>();

                Set<String> icao24Set = new HashSet<>();

                List<AircraftDatabaseModel> admlist = new ArrayList<>();

                FPDatabaseConnection fpdc = new FPDatabaseConnection();
                /*
                fpdc.showMaxFromWaypointsForRouteTest(225216);
                fpdc.showMaxFromWaypointsForRoute(157);
                fpdc.showMaxFromWaypointsForRoute(28);
                fpdc.showMaxFromWaypointsForRoute(203807);
                fpdc.showMaxFromWaypointsForRoute(172);
                fpdc.showMaxFromWaypointsForRoute(233);
                fpdc.showMaxFromWaypointsForRoute(83);
                fpdc.showMaxFromWaypointsForRoute(179);
                 */

                //fpdc.showMaxFromWaypointsForRouteTest(225216);

                System.out.println();
                System.out.println();

                List<Route> listRoutes = new ArrayList<>();
                List<Waypoint> listWaypoints = new ArrayList<>();

                /*
                OmegaFunctionality of = new OmegaFunctionality();
                of.testOmega2(52.16622913, 20.965229094999998 , 55.971382000000006, 37.4149735, 9515 , 9645 , 30000);
                 */

                chooseProgramRoute(config, lamin, lamax, lomin, lomax, HOW_MANY, NUMBEROFSEEKS, DATATYPE, TIME_IN_BETWEEN, listOfLists, icao24Set, listWaypoints);


                //Populating Aircraft Database

                //AircraftDatabaseFunctionality adf = new AircraftDatabaseFunctionality();
                //adf.populateAircraftDatabase(icao24Set, admlist);




                //Populating the FPDatabase

                /*
                FPDatabaseFunctionality fpdf = new FPDatabaseFunctionality();
                fpdf.FPDatabaseDoTheThing();
                 */



                //Populating the FPDatabase with Aircraft Data

                /*
                AirrunDatabaseConnection adc = new AirrunDatabaseConnection();

                List <Airport> alist = new ArrayList<>();
                List<Runway> rlist = new ArrayList<>();
                List<Runway_end> relist = new ArrayList<>();

                adc.getAirports(alist);
                adc.getRunways(rlist);
                adc.getRunwayEnds(relist);

                fpdc.insertIntoAirports(alist);
                fpdc.insertIntoRunways(rlist);
                fpdc.insertIntoRunwayEnds(relist);
                 */



                //Creating Middle Points in airports


                //createMiddlePointsForAirports(fpdc);


            }

        }



    }


    /************************************************************************************************************************************************/

    private static void chooseProgramRoute(JSAPResult config, double lamin, double lamax, double lomin, double lomax, int HOW_MANY, int NUMBEROFSEEKS, String DATATYPE, int TIME_IN_BETWEEN, List<List<PlaneData>> listOfLists, Set<String> icao24Set, List<Waypoint> waypointList) {

        //--rectangle:51.90,52.69,19.93,21.56


        if (DATATYPE.equals("BETA")) {

            BetaFunctionality bf = new BetaFunctionality();

            bf.doTheThingForBeta(lamin, lamax, lomin, lomax, listOfLists, HOW_MANY + 1, icao24Set, NUMBEROFSEEKS, TIME_IN_BETWEEN, config);

        } else if (DATATYPE.equals("ALPHA")) {

            AlphaFunctionality af = new AlphaFunctionality();

            af.doTheThingForAlpha(lamin, lamax, lomin, lomax, listOfLists, HOW_MANY + 1, icao24Set, NUMBEROFSEEKS, TIME_IN_BETWEEN, config);

        } else if (DATATYPE.equals("DELTA")) {

            /*
            lamin = 50;
            lamax = 55;
            lomin = 16;
            lomax = 23;
             */

            DeltaFunctionality df = new DeltaFunctionality();

            df.doTheThingForDeltaRep(lamin, lamax, lomin, lomax, TIME_IN_BETWEEN);

        } else if (DATATYPE.equals("OMEGA")) {

            OmegaFunctionality omf = new OmegaFunctionality();

            omf.doTheThingForOmegaRep(lamin, lamax, lomin, lomax, config.getInt("CTIME"), TIME_IN_BETWEEN, waypointList);

        } else if (DATATYPE.equals("TEST_OMEGA")) {

            OmegaFunctionality omf = new OmegaFunctionality();

            //omf.testOmega(52.16622913, 20.965229094999998, 55.971382000000006, 37.4149735, 9515 , 9645 , 30000, 10, 9600);
            omf.testOmega(lamin, lomin, lamax, lomax, config.getInt("DTIME") , config.getInt("ATIME") , 30000, TIME_IN_BETWEEN, config.getInt("CTIME"));


        } else if (DATATYPE.equals("TEST_OMEGA2")) {

            OmegaFunctionality omf = new OmegaFunctionality();

            omf.doTheThingForOmegaRep(lamin, lamax, lomin, lomax, config.getInt("CTIME"), TIME_IN_BETWEEN, waypointList);

        }
        else {
            System.out.println("Please select a viable Data Type");
        }
    }

    /************************************************************************************************************************************************/

    private static JSAP initializeJSAP(JSAP jsap) throws JSAPException {

        /*Function initializes all the necessary flags for command line interaction*/

        FlaggedOption opt1 = new FlaggedOption("SOURCE")
                .setStringParser(JSAP.STRING_PARSER)
                .setDefault("BETA")
                .setRequired(true)
                .setShortFlag('s')
                .setLongFlag("source");

        jsap.registerParameter(opt1);

        QualifiedSwitch qs1 = (QualifiedSwitch)
                (new QualifiedSwitch("RECTANGLE")
                        .setStringParser(JSAP.DOUBLE_PARSER)
                        .setShortFlag('r')
                        .setLongFlag("rectangle")
                        .setList(true)
                        .setListSeparator(','));

        jsap.registerParameter(qs1);

        FlaggedOption opt2 = new FlaggedOption("INTERVAL")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("10")
                .setRequired(true)
                .setShortFlag('i')
                .setLongFlag("interval");

        jsap.registerParameter(opt2);

        Switch sw1 = new Switch("oj")
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("oj");

        jsap.registerParameter(sw1);

        Switch sw2 = new Switch("od")
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("od");

        jsap.registerParameter(sw2);

        FlaggedOption opt3 = new FlaggedOption("NROFSEEKS")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("1337")
                .setRequired(true)
                .setShortFlag('n')
                .setLongFlag("nrofseeksl");

        jsap.registerParameter(opt3);

        FlaggedOption opt4 = new FlaggedOption("HMPOINTS")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("2")
                .setRequired(true)
                .setShortFlag('p')
                .setLongFlag("hmpoints");

        jsap.registerParameter(opt4);

        FlaggedOption opt5 = new FlaggedOption("CTIME")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("0")
                .setRequired(true)
                .setShortFlag('t')
                .setLongFlag("ctime");

        jsap.registerParameter(opt5);

        FlaggedOption opt6 = new FlaggedOption("OUTPUT")
                .setStringParser(JSAP.STRING_PARSER)
                .setDefault("JSON")
                .setRequired(true)
                .setShortFlag('o')
                .setLongFlag("output");

        jsap.registerParameter(opt6);

        FlaggedOption opt7 = new FlaggedOption("DTIME")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("0")
                .setRequired(true)
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("dtime");

        jsap.registerParameter(opt7);

        FlaggedOption opt8 = new FlaggedOption("ATIME")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("0")
                .setRequired(true)
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("atime");

        jsap.registerParameter(opt8);

        Switch sw3 = new Switch("clear")
                .setShortFlag('c')
                .setLongFlag("clear");

        jsap.registerParameter(sw3);

        Switch sw4 = new Switch("help")                     /* help information flag */
                .setShortFlag('h')
                .setLongFlag("help");

        jsap.registerParameter(sw4);

        return jsap;

    }

    /************************************************************************************************************************************************/

    private static void createMiddlePointsForAirports(FPDatabaseConnection fpdc) {
        List<MiddlePointContainer> mpclist = new ArrayList<>();

        fpdc.getIcaosFromAirports(mpclist);
        fpdc.getMiddleFromRunwayEnds(mpclist);

        for(MiddlePointContainer mpc: mpclist){
            System.out.println(mpc.getIcao() + "   " + mpc.getLatout() + "   " + mpc.getLonout());
            fpdc.insertMiddleIntoAirports(mpc);
        }
    }

    /************************************************************************************************************************************************/

    private static void help(){

        System.out.println();
        System.out.println("SOURCE:     --source BETA      -s ALPHA");
        System.out.println("INTERVAL:     --interval 10      -i 5000");
        System.out.println("RECTANGLE: lamin,lamax,lomin,lomax   --rectangle:51.90,52.68,19.93,21.56     -r:51.90,52.68,19.93,21.56");
        System.out.println("TIME: -t 641 (minutes from mon 0:00)     --ctime 1567");
        System.out.println("OUTPUT: -o JSON (default)     --output DB");
        System.out.println("CLEAR DATABASE:    --clear     -c");
        System.out.println("java -jar 3DPS.jar -s BETA -i 10 -r:51.90,52.68,19.93,21.56 -c");
        System.out.println("FOR OMEGA TEST: --dtime 9000 --atime 9560  departure and arrival in minutes");
        System.out.println();

    }

    /************************************************************************************************************************************************/


}
