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

import java.util.List;

public class AlphaController {

    /* Getting JSON from host */

    public String getFromOS(double lamin, double lamax, double lomin, double lomax) {

        // Host url
        String host = "https://opensky-network.org/api/states/all";
        //String charset = "UTF-8";
        // Headers for a request
        String x_host = "opensky-network.org/api/states/all";
        // Format query for preventing encoding problems

        String query = "lamin=" + lamin + "&lamax=" + lamax + "&lomin=" + lomin + "&lomax=" + lomax;

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(host + "?" + query)
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

    /* Parsing a list of PlaneData objects from JSON */

    public void createListFromJson(String prettyJsonString, List<PlaneData> dataList) {

        JSONObject obj = new JSONObject(prettyJsonString);

        int packId = obj.getInt("time");
        //System.out.println(packId);

        JSONArray arr = obj.getJSONArray("states");

        for (int i = 0; i < arr.length(); i++) {

            JSONArray element_arr = arr.getJSONArray(i);
            PlaneData tmp = new PlaneData();
            String tmpString;

            for (int j = 0; j < element_arr.length(); j++) {

                var singleElement = element_arr.get(j);
                //System.out.println(singleElement);

                if (j == 0) {
                    tmp.setIcao24(singleElement.toString());
                }

                if (j == 1) {
                    tmp.setIcao(singleElement.toString());
                }

                if (j == 2) {
                    tmp.setCountry(singleElement.toString());
                }

                if (j == 3) {
                    tmpString = singleElement.toString();
                    tmp.setUnix_time(Integer.parseInt(tmpString));
                }

                if (j == 4) {
                    tmpString = singleElement.toString();
                    tmp.setUnix_last(Integer.parseInt(tmpString));
                }

                if (j == 5) {
                    tmpString = singleElement.toString();
                    tmp.setLongitude(Double.parseDouble(tmpString));
                }

                if (j == 6) {
                    tmpString = singleElement.toString();
                    tmp.setLatitude(Double.parseDouble(tmpString));
                }

                if (j == 7) {
                    tmpString = singleElement.toString();
                    if (tmpString == null || tmpString.equals("null")) {
                        tmp.setBaro_altitude(null);
                    } else {
                        tmp.setBaro_altitude(Double.valueOf(tmpString));
                    }
                }

                if (j == 8) {
                    tmpString = singleElement.toString();
                    tmp.setOn_ground(Boolean.parseBoolean(tmpString));
                }

                if (j == 9) {
                    tmpString = singleElement.toString();
                    if (tmpString == null || tmpString.equals("null")) {
                        tmp.setVelocity(null);
                    } else {
                        tmp.setVelocity(Double.valueOf(tmpString));
                    }
                }

                if (j == 10) {
                    tmpString = singleElement.toString();
                    if (tmpString == null || tmpString.equals("null")) {
                        tmp.setTrue_track(null);
                    } else {
                        tmp.setTrue_track(Double.valueOf(tmpString));
                    }
                }

                if (j == 11) {
                    tmpString = singleElement.toString();
                    if (tmpString == null || tmpString.equals("null")) {
                        tmp.setVertical_rate(null);
                    } else {
                        tmp.setVertical_rate(Double.valueOf(tmpString));
                    }
                }

                if (j == 13) {
                    tmpString = singleElement.toString();
                    if (tmpString == null || tmpString.equals("null")) {
                        tmp.setAltitude(null);
                    } else {
                        tmp.setAltitude(Double.valueOf(tmpString));
                    }
                }

                if (j == 14) {
                    tmp.setSquawk(singleElement.toString());
                }

                if (j == 15) {
                    tmpString = singleElement.toString();
                    tmp.setSpi(Boolean.parseBoolean(tmpString));
                }

                if (j == 16) {
                    tmpString = singleElement.toString();
                    tmp.setPosition_source(Integer.parseInt(tmpString));
                }

            }

            tmp.setSource("a1");
            tmp.setPackId(packId);
            dataList.add(tmp);

        }

    }


}
