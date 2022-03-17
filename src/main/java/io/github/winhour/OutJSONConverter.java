package io.github.winhour;

import io.github.winhour.misc.ConsoleColors;
import io.github.winhour.model.FPModel;
import io.github.winhour.model.PlaneData;

import java.util.ArrayList;
import java.util.List;

public class OutJSONConverter {

    //Creating JSON output

    public String convert(List<PlaneData> dataList, List <PlaneData> tmpList){

        /* Start of JSON */

        String jsonOut = "{\"src\":1,\"feeds\":[{\"id\":1,\"name\":\"From 3DPS\",\"polarPlot\":false}],\"srcFeed\":1,\"showSil\":true,\"showFlg\":true,\"showPic\":true,\"flgH\":20,\"flgW\":85" +
                ",\n\"acList\":[";

        /* Main JSON Body containing plane data */

        for (PlaneData planeData : dataList) {

            Double velocity = planeData.getVelocity();
            String icao = planeData.getIcao24();
            Double altitude = planeData.getAltitude();
            double latitude = planeData.getLatitude();
            double longitude = planeData.getLongitude();
            int unix_time = planeData.getUnix_time();
            String country = planeData.getCountry();
            String callsign = planeData.getCallsign();
            String arrival = planeData.getArrival();
            String departure = planeData.getDeparture();
            String type = planeData.getType();
            Double track = null;
            String reg = "XXX";
            String model = "XXX";
            String man = "XXX";
            String op = planeData.getCountry();

            boolean isUnknown = true;


            if (tmpList != null) {

                for (PlaneData p2 : tmpList){
                    if (p2.getIcao24().equals(icao)){
                        //System.out.println(latitude + "   " + longitude + "   " + p2.getLatitude() + "   " + p2.getLongitude());
                        isUnknown = false;
                        if (latitude != p2.getLatitude() && longitude != p2.getLongitude()) {
                            track = getAngle(latitude, longitude, p2.getLatitude(), p2.getLongitude()) - 180 ;
                            p2.setLatitude(latitude);
                            p2.setLongitude(longitude);
                            p2.setTrue_track(track);
                        } else {
                            track = p2.getTrue_track();
                        }
                    }
                }

                if (isUnknown){
                    tmpList.add(planeData);
                }

            }



            //System.out.println(track);

            String body = makeJSONBody(velocity, icao, altitude, latitude, longitude, unix_time, track, callsign, arrival, departure, type, country, reg, model, man, op);

            jsonOut += body;


        }

        jsonOut = jsonOut.substring(0, jsonOut.length() - 2);

        /* End of JSON */

        jsonOut += "],\n\"totalAc\":99,\"lastDv\":\"636571117734271581\",\"shtTrlSec\":60,\"stm\":1521574422738}";

        return jsonOut;
    }

    /************************************************************************************************************************************************/

    public String convert(List<PlaneData> dataList){

        /* Start of JSON */

        String jsonOut = "{\"src\":1,\"feeds\":[{\"id\":1,\"name\":\"From 3DPS\",\"polarPlot\":false}],\"srcFeed\":1,\"showSil\":true,\"showFlg\":true,\"showPic\":true,\"flgH\":20,\"flgW\":85" +
                ",\n\"acList\":[";

        /* Main JSON Body containing plane data */

        for (PlaneData planeData : dataList) {

            Double velocity = planeData.getVelocity();
            String icao = planeData.getIcao24();
            Double altitude = planeData.getAltitude();
            double latitude = planeData.getLatitude();
            double longitude = planeData.getLongitude();
            int unix_time = planeData.getUnix_time();
            String country = planeData.getCountry();
            Double track = planeData.getTrue_track();
            String callsign = planeData.getCallsign();
            String arrival = planeData.getArrival();
            String departure = planeData.getDeparture();
            String type = planeData.getType();
            String reg = "XXX";
            String model = "XXX";
            String man = "XXX";
            String op = planeData.getCountry();


            String body = makeJSONBody(velocity, icao, altitude, latitude, longitude, unix_time, track, callsign, arrival, departure, type, country, reg, model, man, op);

            jsonOut += body;


        }

        jsonOut = jsonOut.substring(0, jsonOut.length() - 2);

        /* End of JSON */

        jsonOut += "],\n\"totalAc\":99,\"lastDv\":\"636571117734271581\",\"shtTrlSec\":60,\"stm\":1521574422738}";

        return jsonOut;
    }

    /************************************************************************************************************************************************/

    public String convertFPM(List<FPModel> fpmList, List <FPModel> tmpList){

        /* Start of JSON */

        String jsonOut = "{\"src\":1,\"feeds\":[{\"id\":1,\"name\":\"From 3DPS\",\"polarPlot\":false}],\"srcFeed\":1,\"showSil\":true,\"showFlg\":true,\"showPic\":true,\"flgH\":20,\"flgW\":85" +
                ",\n\"acList\":[";

        /* Main JSON Body containing plane data */

        for (FPModel f : fpmList) {

            Double velocity = f.getSpd();
            Double altitude = (double)f.getAlt();
            String icao = f.getIcao24();
            double latitude = f.getLat_c();
            double longitude = f.getLon_c();
            int unix_time = f.getUnix_time();
            String country = null;
            String callsign = f.getCallsign();
            String departure = f.getDep_apt();
            String arrival = f.getArr_apt();
            Double track = f.getTrue_track();
            String reg = f.getReg();
            String type = f.getType();
            String model = f.getModel();
            String man = f.getMan();
            String op = f.getOp();



            if (tmpList != null) {

                /*
                for (FPModel p1 : fpmList){
                    System.out.println(ConsoleColors.GREEN +p1.toString()+ ConsoleColors.RESET);
                }


                for (FPModel p2 : tmpList){
                    System.out.println(ConsoleColors.YELLOW +p2.toString()+ ConsoleColors.RESET);
                }
                 */

                /*
                for (FPModel p2 : tmpList){
                    if (p2.getIcao24().equals(icao)){
                        //System.out.println("Inside of convertFPM: " + icao +  "   " + latitude + "   " + longitude + "   " + p2.getLat_c() + "   " + p2.getLon_c());
                        if (latitude != p2.getLat_c() && longitude != p2.getLon_c()) {
                            track = getAngle(latitude, longitude, p2.getLat_c(), p2.getLon_c()) - 180 ;
                            //p2.setLat_c(latitude);
                            //p2.setLat_c(longitude);
                            //p2.setTrue_track(track);
                        } else {
                            track = p2.getTrue_track();
                        }
                        //System.out.println(ConsoleColors.RED + "track = " + track + ConsoleColors.RESET);
                    }
                }
                 */
            }


            String body = makeJSONBody(velocity, icao, altitude, latitude, longitude, unix_time, track, callsign, arrival, departure, type, country, reg, model, man, op);

            jsonOut += body;


        }

        jsonOut = jsonOut.substring(0, jsonOut.length() - 2);

        /* End of JSON */

        jsonOut += "],\n\"totalAc\":99,\"lastDv\":\"636571117734271581\",\"shtTrlSec\":60,\"stm\":1521574422738}";

        System.out.println(jsonOut);

        return jsonOut;

    }


    /************************************************************************************************************************************************/

    private String makeJSONBody(Double velocity, String icao, Double altitude, double latitude, double longitude, int unix_time, Double track, String callsign, String arrival, String departure, String type, String country,
                                String reg, String model, String man, String op) {

        return "{\"Id\":666,\"Rcvr\":1,\"HasSig\":false,\"Icao\":\"" + icao + "\",\"Reg\":\"" + reg + "\",\"Alt\":" + altitude + ",\"GAlt\":" + altitude + ",\"AltT\":0,\"Call\":\"" + callsign + "\"," +
                "\"Lat\":" + latitude + ",\"Long\":" + longitude + "" +
                ",\"PosTime\":" + unix_time + ",\"Mlat\":false,\"Tisb\":false,\"Spd\":" + velocity + ",\"Trak\":" + track + ",\"TrkH\":false,\"Type\":\"" + type + "\",\"Mdl\":" +
                "\"" + model + "\",\"Man\":\"" + man + "\",\"From\":\"" + departure + "\",\"To\":\"" + arrival + "\",\"OpIcao\":\"XXX\",\"Op\":\"" + op + "\"},\n";

    }

    /************************************************************************************************************************************************/

    public List<PlaneData> limitRectangle(List<PlaneData> dataList, double latmin, double latmax, double lonmin, double lonmax) {

        List<PlaneData> newList = new ArrayList<>();

        for (PlaneData p : dataList){
            if (p.getLatitude()<latmax && p.getLatitude()>latmin && p.getLongitude()<lonmax && p.getLongitude()>lonmin){
                newList.add(p);
            }
        }


        return newList;
    }

    /************************************************************************************************************************************************/

    protected static double bearing(double lat1, double lon1, double lat2, double lon2){

        /* Probably won't be useful anymore */

        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
        //return Math.atan2(y,x);

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
