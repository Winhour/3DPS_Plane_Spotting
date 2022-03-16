package io.github.winhour.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.winhour.model.PlaneData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class BetaController {

    /* Getting JSON from host */

    public String getFromOS(double lamin, double lamax, double lomin, double lomax) {

        // Host url
        String host = "https://data-live.flightradar24.com/zones/fcgi/feed.js";
        //String charset = "UTF-8";
        // Headers for a request
        String x_host = "data-live.flightradar24.com/zones/fcgi/feed.js";
        // Format query for preventing encoding problems

        String query = "bounds=" + lamax + "," + lamin +","+ lomin +"," + lomax + "&adsb=1&mlat=1&flarm=1&faa=1&air=1&gnd=1&vehicles=1&gliders=1&estimated=1&maxage=14400&stats=1";

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(host + "?" + query)
                    .header("x-host", x_host)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        //assert response != null;

        if (response == null)
            return null;

        System.out.println("Status = " + response.getStatus());
        System.out.println("Content Type = " + response.getHeaders().get("Content-Type"));


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        //System.out.println(prettyJsonString);

        return gson.toJson(je);

    }

    /************************************************************************************************************************************************/

    /* Parsing a list of PlaneData objects from JSON */

    public void createListFromJson(String prettyJsonString, List<PlaneData> dataList) {

        long unixTime = System.currentTimeMillis() / 1000L;
        int uTime = (int) unixTime;


        JSONObject obj = new JSONObject(prettyJsonString);

        Iterator<String> keys= obj.keys();
        while (keys.hasNext())
        {
            String keyValue = (String)keys.next();

            if(!keyValue.equals("full_count") && !keyValue.equals("version") && !keyValue.equals("stats")){

                JSONArray arr = obj.getJSONArray(keyValue);

                PlaneData tmp = new PlaneData();
                String tmpString;

                for (int i = 0; i < arr.length(); i++) {

                    //["406A62",50.256,18.676,288,24525,394,"2246","F-EPML1","B738","G-GDFX",1639422910,"KRK","NCL","LS354",0,2560,"EXS32TE",0,"EXS"]

                    var singleElement = arr.get(i);

                    tmp.setPackId(uTime);

                    if(i == 0){
                        tmp.setIcao24(singleElement.toString());
                    }

                    if (i == 1) {
                        tmpString = singleElement.toString();
                        tmp.setLatitude(Double.parseDouble(tmpString));
                    }

                    if (i == 2) {
                        tmpString = singleElement.toString();
                        tmp.setLongitude(Double.parseDouble(tmpString));
                    }

                    if(i == 4) {
                        tmpString = singleElement.toString();
                        tmp.setAltitude(Double.parseDouble(tmpString));
                    }

                    if(i == 8) {
                        tmp.setModel(singleElement.toString());
                    }

                    if(i == 9) {
                        tmp.setRegistration(singleElement.toString());
                    }

                    if(i == 10){
                        tmpString = singleElement.toString();
                        tmp.setUnix_time(Integer.parseInt(tmpString));
                        tmp.setUnix_last(Integer.parseInt(tmpString));
                    }

                    if(i == 13){
                        tmp.setFlightnumber(singleElement.toString());
                    }

                    if(i == 15) {
                        tmpString = singleElement.toString();
                        tmp.setVelocity(Double.parseDouble(tmpString));
                    }

                    if(i == 16) {
                        tmp.setCallsign(singleElement.toString());
                    }

                    if(i == 18) {
                        tmp.setOpicaocode(singleElement.toString());
                    }

                }

                tmp.setSource("b1");
                dataList.add(tmp);

            }
        }


    }


}
