package io.github.winhour.controller;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.winhour.model.PlaneData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class DeltaController {

    /* Getting JSON from host */

    public String getFromOS() {

        // Host url
        String host = "https://data.vatsim.net/v3/";
        //String charset = "UTF-8";
        // Headers for a request
        String x_host = "data.vatsim.net/v3/";
        // Format query for preventing encoding problems

        String query = "vatsim-data.json";

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(host + "" + query)
                    .header("x-host", x_host)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        assert response != null;
        System.out.println("Status = " + response.getStatus());
        System.out.println("Content Type = " + response.getHeaders().get("Content-Type"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        //System.out.println(prettyJsonString);

        return gson.toJson(je);

    }

    /************************************************************************************************************************************************/


    public void createListFromJson(String prettyJsonString, List<PlaneData> dataList) {

        JSONObject obj = new JSONObject(prettyJsonString);

        JSONArray arr = obj.getJSONArray("pilots");

        long unixTime = System.currentTimeMillis();

        for (int i = 0; i < arr.length(); i++) {

            JSONObject pilotObj = arr.getJSONObject(i);

            PlaneData tmp = new PlaneData();
            String tmpString;

            double latitude;
            double longitude;
            int altitude;
            String icao, callsign;
            String arrival, departure;
            int speed;
            String type;

            icao = String.valueOf(pilotObj.getInt("cid"));
            latitude = pilotObj.getDouble("latitude");
            longitude = pilotObj.getDouble("longitude");
            altitude = pilotObj.getInt("altitude");
            callsign = pilotObj.getString("callsign");
            speed = pilotObj.getInt("groundspeed");

            if(pilotObj.has("flight_plan")) {

                JSONObject flight_plan = pilotObj.getJSONObject("flight_plan");

                arrival = flight_plan.getString("arrival");
                departure = flight_plan.getString("departure");
                type = flight_plan.getString("aircraft_short");

            } else {

                arrival = "";
                departure = "";
                type = "";

            }

            //System.out.println(i + ": icao:" + icao + "  latitude:" + latitude + "   longitude:" + longitude + "   altitude:" + altitude);

            tmp.setPackId((int)unixTime);
            tmp.setUnix_time((int)unixTime);
            tmp.setUnix_last((int)unixTime);
            tmp.setIcao(icao);
            tmp.setIcao24(icao);
            tmp.setCountry(pilotObj.getString("name")+"-"+pilotObj.getString("server"));
            tmp.setLatitude(latitude);
            tmp.setLongitude(longitude);
            tmp.setAltitude((double)altitude);
            tmp.setVelocity((double)speed);
            tmp.setSource("d1");
            if (altitude == 0){
                tmp.setOn_ground(true);
            } else {
                tmp.setOn_ground(false);
            }
            tmp.setCallsign(callsign);
            tmp.setArrival(arrival);
            tmp.setDeparture(departure);
            tmp.setType(type);

            dataList.add(tmp);


        }

    }

    /************************************************************************************************************************************************/


}
