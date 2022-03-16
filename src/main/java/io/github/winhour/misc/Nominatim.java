package io.github.winhour.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;

public class Nominatim {

    private String jsonString;

    public void initialize(double lat, double lon){

        // Host url
        String host = "https://nominatim.openstreetmap.org/reverse";
        //String charset = "UTF-8";
        // Headers for a request
        String x_host = "https://nominatim.openstreetmap.org/reverse";
        // Format query for preventing encoding problems

        String query = "format=jsonv2&accept-language=en_US&addressdetails=0&lat=" + lat + "&lon=" + lon + "&zoom=10";

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(host + "?" + query)
                    .header("x-host", x_host)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        assert response != null;
        //System.out.println("Status = " + response.getStatus());
        //System.out.println("Content Type = " + response.getHeaders().get("Content-Type"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());

        jsonString = gson.toJson(je);

    }

    /************************************************************************************************************************************************/

    public String getJsonString() {

        return jsonString;

    }

    /************************************************************************************************************************************************/

    public String getDisplayName(){

        String displayName;

        try {

            JSONObject obj = new JSONObject(jsonString);
            displayName = obj.getString("display_name");

        } catch (JSONException jse){

            return "Unknown";

        }

        return displayName;
    }

    /************************************************************************************************************************************************/

    public String getName(){

        String name;

        try {

            JSONObject obj = new JSONObject(jsonString);
            name = obj.getString("name");

        } catch (JSONException jse){

            return "Unknown";

        }

        return name;
    }

    /************************************************************************************************************************************************/

}
