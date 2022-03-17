package io.github.winhour;

import io.github.winhour.misc.ConsoleColors;
import io.github.winhour.misc.Nominatim;
import io.github.winhour.model.FPModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.ToDoubleBiFunction;

public class FlightPlanPointCreator {

    //Simulating flight points

    //Average flight speed for commercial plane = 450 kts

    Date dt = new Date();

    LocalDate today = LocalDate.now();

    Date dt2 = new Date();

    double AVERAGE_PLANE_SPEED = 400.00;

    /************************************************************************************************************************************************/

    public void initialize() {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {

            dt2 = Date.from(today.atStartOfDay(defaultZoneId).toInstant());

        } else {

            LocalDate dt2_date = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));

            dt2 = Date.from(dt2_date.atStartOfDay(defaultZoneId).toInstant());

        }
    }

    /************************************************************************************************************************************************/

    public void calculateCurrentCoordinates(int dep_time, int arr_time, double lat1, double lon1, double lat2, double lon2, int ctime, String id, FPModel fpm, FPModel tmpfp){

        double latC = 0, lonC = 0;

        int current_time = 0;

        System.out.println();

        dep_time = dep_time * 60;
        arr_time = arr_time * 60;
        int TOC_time = fpm.getTOC_time();
        int TOD_time = fpm.getTOD_time();

        int alt = fpm.getAlt();

        System.out.println(id + "   dep(s):" + dep_time + "   arr(s):" + arr_time + "   TOC_time(s):" + TOC_time + "   TOD_time(s):" + TOD_time + "   ctime(m):" + ctime);

        if (ctime != 0){

            current_time = ctime*60;            //make it so it changes after setting

        } else {

            Date dt = new Date();

            long diff = dt.getTime() - dt2.getTime();//as given

            //long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

            current_time = (int)seconds;

            System.out.println(dt + "   " + dt2 + "   " + seconds/60);

        }

        if (arr_time > current_time) {


            /*
            int total_time = arr_time - dep_time;

            double latDif = lat2 - lat1;

            double lonDif = lon2 - lon1;

            int timeDif = current_time - dep_time;

            double timePercentage = (double) timeDif / (double) total_time;
            */

            int total_time = 0;

            double latDif;

            double lonDif;

            int timeDif = 0;

            double timePercentage = 0;


            if (current_time<TOC_time) {

                //Aircraft is climbing

                total_time = TOC_time - dep_time;

                latDif = fpm.getTOClat() - lat1;

                lonDif = fpm.getTOClon() - lon1;

                timeDif = current_time - dep_time;

                timePercentage = (double) timeDif / (double) total_time;

                if (dep_time <= current_time && current_time <= arr_time) {

                    System.out.println("Aircraft is climbing");

                    latC = lat1 + (timePercentage * latDif);
                    lonC = lon1 + (timePercentage * lonDif);
                    alt = (int)(1500*((double)timeDif/60.0));

                }

            }

            if (current_time>TOC_time && current_time<TOD_time) {

                //Aircraft is flying on stable altitude

                total_time = (TOD_time) - (TOC_time);

                latDif = fpm.getTODlat() - fpm.getTOClat();

                lonDif = fpm.getTODlon() - fpm.getTOClon();

                timeDif = current_time - TOC_time;

                timePercentage = (double) timeDif / (double) total_time;

                if (dep_time <= current_time && current_time <= arr_time) {

                    System.out.println("Aircraft is flying");

                    latC = fpm.getTOClat() + (timePercentage * latDif);
                    lonC = fpm.getTOClon() + (timePercentage * lonDif);
                }
            }

            if (current_time>TOD_time){

                //Aircraft is descending

                total_time = arr_time - TOD_time;

                latDif = lat2 - fpm.getTODlat();

                lonDif = lon2 - fpm.getTODlon();

                timeDif = current_time - TOD_time;

                timePercentage = (double) timeDif / (double) total_time;

                if (dep_time <= current_time && current_time <= arr_time) {

                    System.out.println("Aircraft is descending");

                    latC = fpm.getTODlat() + (timePercentage * latDif);
                    lonC = fpm.getTODlon() + (timePercentage * lonDif);
                    int x = alt-(int)(3000*((double)timeDif/60.0));
                    if (x>=11000) {
                        alt = x;
                    } else {
                        System.out.println("arr_time: " + arr_time + " current_time: " + current_time);
                        alt = (int)((((double)arr_time-(double)current_time)/60.0)*1500);
                        if (alt < 0) alt = 0;
                    }
                    if (alt>tmpfp.getAlt()) alt = tmpfp.getAlt();
                }

            }

            System.out.println("timeDif: " + timeDif + "    total_time: " + total_time + "    timePercentage: " + timePercentage + "    current_time: " + current_time);
            System.out.println("lat1: " + lat1 + " lon1: " + lon1 + " lat2: " + lat2 + " lon2: " + lon2);
            System.out.println("TOClat: " + fpm.getTOClat() + " TOClon: " + fpm.getTOClon() + " TODlat: " + fpm.getTODlat() + " TODlon: " + fpm.getTODlon());

            System.out.println("latitude: " + latC + "   longitude: " + lonC + "   altitude: " + alt);

            fpm.setLon_c(lonC);
            fpm.setLat_c(latC);
            fpm.setAlt(alt);

            Nominatim nmn = new Nominatim();
            nmn.initialize(latC, lonC);
            System.out.println(nmn.getDisplayName());

            if (tmpfp != null){
                double speed = calculateSpeed(tmpfp.getLon_c(),tmpfp.getLat_c(),lonC,latC,10);
                fpm.setSpd(speed);
                double track = getAngle(latC, lonC, tmpfp.getLat_c(), tmpfp.getLon_c()) - 180 ;
                fpm.setTrue_track(track);
            } else {
                fpm.setSpd(0);
                fpm.setTrue_track(null);
            }

            fpm.setIteration(fpm.getIteration()+1);

        } else {
            System.out.println("current_time: " + current_time);
            System.out.println("Flight has already landed");
        }


    }

    /************************************************************************************************************************************************/

    public void calculateCoordinatesForFlightBasedOnSpeed(FPModel fpm, double ctime){

        // Velocity = distance/time; distance = Velocity * time; Time = distance/velocity
        //1 kilometer per hour =
        //0.277777778 meters / second

        double latC = 0, lonC = 0;

        double current_time;

        if (ctime != 0){

            current_time = ctime*60;            //make it so it changes after setting

        } else {

            Date dt = new Date();

            long diff = dt.getTime() - dt2.getTime();//as given

            //long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

            current_time = (int)seconds;

            System.out.println(dt + "   " + dt2 + "   " + seconds/60);

        }

        double bearing =  getAngle(fpm.getLat1(), fpm.getLon1(), fpm.getLat2(), fpm.getLon2());

        double latTOC = fpm.getTOClat();
        double lonTOC = fpm.getTOClon();
        double latTOD = fpm.getTODlat();
        double lonTOD = fpm.getTODlon();

        double travelled_distance;

        double TOC_distance;
        double TOD_distance;

        travelled_distance = distance(fpm.getLat1(), fpm.getLat_c(), fpm.getLon1(), fpm.getLon_c(), 0, 0);
        TOC_distance = distance(fpm.getLat1(), latTOC, fpm.getLon1(), lonTOC, 0, 0);
        TOD_distance = distance(fpm.getLat1(), latTOD, fpm.getLon1(), lonTOD, 0, 0);

        System.out.println();

        System.out.println(fpm.getIcao24() + "   dep(m):" + fpm.getDep_time() + "   arr(m):" + fpm.getArr_time() + "   TOC_time(s):" + fpm.getTOC_time() + "   TOD_time(s):" + fpm.getTOD_time() + "   ctime(m):" + ctime);
        System.out.println("lat1: " + fpm.getLat1() + " lon1: " + fpm.getLon1() + " lat2: " + fpm.getLat2() + " lon2: " + fpm.getLon2());
        System.out.println("TOClat: " + fpm.getTOClat() + " TOClon: " + fpm.getTOClon() + " TODlat: " + fpm.getTODlat() + " TODlon: " + fpm.getTODlon());

        System.out.println("travelled_distance: " + travelled_distance + "    total_distance: " + fpm.getLength()/1.852 + "    TOC_distance: " + TOC_distance + "    TOD_distance: " + TOD_distance);


        if (travelled_distance < TOC_distance) {

            calculatePointCaseClimb(fpm, current_time, latTOC, lonTOC, TOC_distance);

        }
        else if (travelled_distance > TOC_distance && travelled_distance < TOD_distance) {

            calculatePointCaseFlight(fpm, current_time, bearing, latTOC, lonTOC, latTOD, lonTOD);

        }

        else if (travelled_distance>TOD_distance && travelled_distance< (fpm.getLength()/1.852)) {

            calculatePointCaseDescent(fpm, current_time, latTOD, lonTOD, TOD_distance);

        } else {

            fpm.setAlt(0);
            fpm.setSpd(0);
            fpm.setLat_c(fpm.getLat2());
            fpm.setLon_c(fpm.getLon2());

            System.out.println(ConsoleColors.RED + "Flight has already landed" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.GREEN + "latC: " + fpm.getLat_c() + " lonC: " + fpm.getLon_c() + " altitude: " + fpm.getAlt() + ConsoleColors.RESET);

            Nominatim nmn = new Nominatim();
            nmn.initialize(fpm.getLat_c(), fpm.getLon_c());
            System.out.println(nmn.getDisplayName());

            fpm.setLanded(true);

        }


        //fpm.setLon_c(lonC);
        //fpm.setLat_c(latC);


        System.out.println("Speed: " + fpm.getSpd() + "   Track: " + fpm.getTrue_track());
        System.out.println();

        fpm.setTrue_track(bearing);


        fpm.setIteration(fpm.getIteration()+1);


    }

    /************************************************************************************************************************************************/

    private void calculatePointCaseClimb(FPModel fpm, double current_time, double latTOC, double lonTOC, double TOC_distance) {

        double lonC;
        double latC;

        // 0 -> speed_kts
        // a = delta V / delta t
        // v=u+a*t where u is initial velocity
        // distance = Velocity * time

        System.out.println(ConsoleColors.BLUE + "Climbing case here" + ConsoleColors.RESET);

        int time_elapsed = (int)current_time - (fpm.getDep_time()*60);

        double climb_distance = TOC_distance;

        double starting_speed_kts = 130.00;
        double starting_speed_m = starting_speed_kts*1.852*0.277777778;

        int delta_t = fpm.getTOC_time() - (fpm.getDep_time()*60);
        double delta_V = (AVERAGE_PLANE_SPEED*1.852*0.277777778) - starting_speed_m;
        double acceleration = delta_V / delta_t;

        double current_distance = 0.5*(acceleration*time_elapsed*time_elapsed);

        double percentage = current_distance / climb_distance;

        System.out.println(ConsoleColors.BLUE + "Delta_t = " + delta_t + "   acceleration = " + acceleration + " m/s^2   current_distance = " + current_distance +
                "   climb_distance = " + climb_distance + "   time_elapsed = " + time_elapsed + "   percentage: " + percentage + ConsoleColors.RESET);


        lonC = fpm.getLon1() + ((lonTOC - fpm.getLon1()) * percentage);
        latC = fpm.getLat1() + ((latTOC - fpm.getLat1()) * percentage);

        double altitude = fpm.getStarting_airport_altitude() + ((fpm.getRoute_alt() - fpm.getStarting_airport_altitude()) * percentage);

        System.out.println(ConsoleColors.GREEN + "latC: " + latC + " lonC: " + lonC + " altitude: " + altitude + ConsoleColors.RESET);

        //System.out.println(ConsoleColors.BLUE + "Climbing case to be done here" + ConsoleColors.RESET);

        Nominatim nmn = new Nominatim();
        nmn.initialize(latC, lonC);
        System.out.println(nmn.getDisplayName());

        System.out.println("Point set in Climbing");

        fpm.setLat_c(latC);
        fpm.setLon_c(lonC);
        fpm.setAlt((int)altitude);

        double speed = starting_speed_kts + (((acceleration*time_elapsed)/1.852)/0.277777778);

        if (speed > 400) speed = 400.00;

        fpm.setSpd(speed);
    }

    /************************************************************************************************************************************************/

    private void calculatePointCaseFlight(FPModel fpm, double current_time, double bearing, double latTOC, double lonTOC, double latTOD, double lonTOD) {

        double lonC;
        double latC;
        System.out.println(ConsoleColors.PURPLE + "Flight case here" + ConsoleColors.RESET);

        //speed kts between 300 - 500

        double speed_kts = AVERAGE_PLANE_SPEED;
        double speed_km = speed_kts*1.852;
        double speed_m = speed_km*0.277777778;

        double flight_distance = distance(latTOC, latTOD, lonTOC, lonTOD, 0, 0);

        int flight_time = (int) (flight_distance / speed_m);            // in seconds

        System.out.println(ConsoleColors.YELLOW + "bearing: " + bearing + " flight_distance: " + flight_distance + " flight_time: " + flight_time + ConsoleColors.RESET);

        int time_elapsed = (int)current_time - fpm.getTOC_time();

        double current_distance = speed_m * time_elapsed;

        double percentage = current_distance / flight_distance;

        System.out.println(ConsoleColors.YELLOW + "current_time: " + current_time + " TOC_Time: " + fpm.getTOC_time() + " Speed(kts): " + speed_kts + ConsoleColors.RESET);

        System.out.println(ConsoleColors.GREEN + "current_distance: " + current_distance + " time_elapsed: " + time_elapsed + " percentage: " + percentage + ConsoleColors.RESET);

        lonC = fpm.getTOClon() + ((fpm.getTODlon() - fpm.getTOClon()) * percentage);
        latC = fpm.getTOClat() + ((fpm.getTODlat() - fpm.getTOClat()) * percentage);

        System.out.println(ConsoleColors.GREEN + "latC: " + latC + " lonC: " + lonC + ConsoleColors.RESET);

        Nominatim nmn = new Nominatim();
        nmn.initialize(latC, lonC);
        System.out.println(nmn.getDisplayName());

        System.out.println("Point set in Flight");

        fpm.setLat_c(latC);
        fpm.setLon_c(lonC);
        fpm.setSpd(speed_kts);
    }

    /************************************************************************************************************************************************/

    private void calculatePointCaseDescent(FPModel fpm, double current_time, double latTOD, double lonTOD, double TOD_distance) {

        double lonC;
        double latC;

        // 70 kts for landing
        // speed_kts -> speed_landing
        // a = delta V / delta t
        // v=u+a*t where u is initial velocity

        double speed_kts = AVERAGE_PLANE_SPEED;

        double route_alt = (double)fpm.getRoute_alt();

        double landing_alt = fpm.getLanding_airport_altitude() + 3000 ; //elevation + 3000

        //System.out.println(ConsoleColors.RED + "Descending case to be done here" + ConsoleColors.RESET);

        //System.out.println(ConsoleColors.RED + "TOD_time(original): " + fpm.getTOD_time() + " TOD_time(generated): " + fpm.getTOD_timeGenerated() + " flight time difference: "
        //        + (fpm.getTOD_timeGenerated()-fpm.getTOC_time()) + ConsoleColors.RESET);

        int time_elapsed = (int)current_time - (fpm.getTOD_timeGenerated());

        double descent_distance_total = (fpm.getLength()/1.852) - TOD_distance;

        double descent_distance = descent_distance_total - 10000;

        double deltaV = (AVERAGE_PLANE_SPEED-140.00)*1.852*0.277777778;
        double deltaT = (fpm.getArr_time()*60) - fpm.getTOD_time();

        double acceleration = (deltaV / deltaT);

        //double current_distance = 0.5*(acceleration*time_elapsed*time_elapsed);

        double current_distance = (AVERAGE_PLANE_SPEED*1.852*0.277777778)*time_elapsed - 0.5*(acceleration*time_elapsed*time_elapsed);

        System.out.println("current distance: " + current_distance + "   descent_distance: " + descent_distance  + "   time_elapsed: " + time_elapsed);

        if (current_distance < descent_distance) {

            System.out.println(ConsoleColors.RED + "Descending case here" + ConsoleColors.RESET);

            System.out.println(ConsoleColors.YELLOW + "current_time: " + current_time + " time_elapsed: " + time_elapsed + " descent_distance: " + descent_distance_total + ConsoleColors.RESET);

            double percentage = current_distance / descent_distance_total;  //descent_distance or descent_distance_total ?

            double percentage2 = current_distance / descent_distance;

            System.out.println(ConsoleColors.GREEN + "current_distance: " + current_distance + " descent_distance: " + descent_distance + " acceleration: " + acceleration + " percentage (whole descent): " + percentage + " percentage2 (real): " + percentage2 + ConsoleColors.RESET);

            lonC = lonTOD + ((fpm.getLon2() - lonTOD) * percentage);
            latC = latTOD + ((fpm.getLat2() - latTOD) * percentage);

            double altitude = 3000 + ((fpm.getRoute_alt()-3000) * (1.0 - percentage2));

            System.out.println(ConsoleColors.GREEN + "latC: " + latC + " lonC: " + lonC + " altitude: " + altitude + ConsoleColors.RESET);

            Nominatim nmn = new Nominatim();
            nmn.initialize(latC, lonC);
            System.out.println(nmn.getDisplayName());

            System.out.println("Point set in Descending");

            fpm.setLat_c(latC);
            fpm.setLon_c(lonC);
            if(altitude > fpm.getRoute_alt()) altitude = fpm.getRoute_alt();
            if (altitude < 3000.00) altitude = 3000;
            fpm.setAlt((int)altitude);
            //fpm.setSpd(speed_kts-(((acceleration*time_elapsed)/1.852)/0.277777778));
            fpm.setSpd((140 + ((1.00-percentage2)*(speed_kts-140))));//*(0.81+(0.19*(1-percentage2))));
            if (fpm.getSpd() < 140.00) fpm.setSpd(140.00);

        } else {

            System.out.println(ConsoleColors.RED + "Landing case here" + ConsoleColors.RESET);

            int landing_distance = 10000;

            double speed_landing_kts = 140.00;
            double speed_landing_m = 140.00 * 1.852 * 0.277777778;   //~36

            double landingLat;
            double landingLon;

            landingLat = fpm.getTODlat() + ((fpm.getLat2()-fpm.getTODlat())*(descent_distance/descent_distance_total));
            landingLon = fpm.getTODlon() + ((fpm.getLon2()-fpm.getTODlon())*(descent_distance/descent_distance_total));

            double landingtime = landing_distance/speed_landing_m;

            System.out.println(ConsoleColors.GREEN + "landingLat: " + landingLat + " landingLon: " + landingLon + " landingtime: " + landingtime + ConsoleColors.RESET);

            //time_elapsed = sqrt(2*current_distance/acceleration)

            //double time_until_landing = Math.sqrt((2*descent_distance)/acceleration);

            double time_until_landing = deltaV/acceleration;

            //double landing_point_time = fpm.getTOD_timeGenerated() + time_until_landing;

            if(fpm.getLanding_point_time() == 0){                                               //might be potential issue when first point generated is in landing mode
                fpm.setLanding_point_time((int)current_time);
            }

            double landing_point_time = fpm.getLanding_point_time();

            System.out.println(ConsoleColors.GREEN + "time_until_landing: " + time_until_landing + " landing_point_time: " + landing_point_time + ConsoleColors.RESET);

            int time_elapsed2 = (int)current_time - (int)landing_point_time;

            System.out.println(ConsoleColors.YELLOW + "current_time: " + current_time + " time_elapsed: " + time_elapsed2 + " landing_distance: " + landing_distance + ConsoleColors.RESET);

            double percentage = time_elapsed2/landingtime;

            double latDif = fpm.getLat2() - landingLat;

            double lonDif = fpm.getLon2() - landingLon;

            System.out.println("latDif = " + latDif + "   lonDif = " + lonDif);

            latC = landingLat + (percentage * latDif);
            lonC = landingLon + (percentage * lonDif);

            double altitude = 3000.00 - (percentage*3000.00);

            if (altitude < 0) altitude = 0;

            System.out.println(ConsoleColors.GREEN + "latC: " + latC + " lonC: " + lonC + " altitude: " + altitude + ConsoleColors.RESET);

            Nominatim nmn = new Nominatim();
            nmn.initialize(latC, lonC);
            System.out.println(nmn.getDisplayName());


            System.out.println("Point set in Landing");

            fpm.setLat_c(latC);
            fpm.setLon_c(lonC);
            fpm.setAlt((int)altitude);
            fpm.setSpd(speed_landing_kts);

        }
    }

    /************************************************************************************************************************************************/

    //https://gis.stackexchange.com/questions/5821/calculating-latitude-longitude-x-miles-from-point

    public void calculateNewPoint(double lat, double lon, int distance, double bearing){

        ///to be decided it necessary

    }


    /************************************************************************************************************************************************/

    public void calculateBasedOnPreviousPoint(){



    }



    /************************************************************************************************************************************************/


    public double calculateSpeed(double latx, double lonx, double laty, double lony, int time_interval){

        double speed = 0;

        double dist = distance(latx,laty,lonx,lony,0,0);

        //System.out.println(ConsoleColors.RED + "Distance (meters) = " + dist + ConsoleColors.RESET);

        //speed = dist/time_interval; // m/s
        //speed = (dist/1000)/(time_interval*(1.0/3600)); //km/h
        speed = ((dist/1000)/(time_interval*(1.0/3600)))*0.539956803; // kts

        //System.out.println(ConsoleColors.RED + "Latx = " + latx + " Lonx = " + lonx + " Laty = " + laty + " Lony = " + lony + ConsoleColors.RESET);

        //System.out.println(ConsoleColors.RED + "Distance = " + dist/1000 + ConsoleColors.RESET);

        //System.out.println(ConsoleColors.RED + "Time = " + (time_interval*(1.0/3600)) + ConsoleColors.RESET);

        //System.out.println(ConsoleColors.RED + "Speed = " + speed + ConsoleColors.RESET);

        return speed;

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

    protected static double getAngle(double x1, double y1, double x2, double y2)
    {

        /* Angle of the plane */

        double w = x2 - x1;
        double h = y2 - y1;

        //System.out.println("x1: " + x1 + "    y1: " + y1 + "   w: " + w + "   x2: " + x2 + "   y2: " + y2+ "   h:" + h);

        double atan = (Math.atan(h/w) / Math.PI * 180);
        if (w < 0 || h < 0)
            atan += 180;
        if (w > 0 && h < 0)
            atan -= 180;
        if (atan < 0)
            atan += 360;

        return atan % 360;
    }

    /************************************************************************************************************************************************/


}
