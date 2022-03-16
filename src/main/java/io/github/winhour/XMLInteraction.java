package io.github.winhour;

import io.github.winhour.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XMLInteraction {


    /************************************************************************************************************************************************/

    public void parseXMLStringAirlines(List<FPAirline> airList){


        Document document = null;

        /*
        try {
            document = loadXMLFromString(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

        try {
            document = getDocumentBuilder().parse("FPDatabase.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Element root = document.getDocumentElement();

        NodeList nList = document.getElementsByTagName("Airline");
        System.out.println("============================");

        String file_name;
        String icao;
        String iata;
        String callsign;
        String name;
        String type;
        String season;
        String author;
        String country;
        String logo;
        String sourceurl;


        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;

                file_name = eElement.getAttribute("file");

                icao = eElement.getElementsByTagName("ICAO").item(0).getTextContent();

                iata = eElement.getElementsByTagName("IATA").item(0).getTextContent();

                callsign = eElement.getElementsByTagName("Callsign").item(0).getTextContent();

                name = eElement.getElementsByTagName("Name").item(0).getTextContent();

                type = eElement.getElementsByTagName("Type").item(0).getTextContent();

                season = eElement.getElementsByTagName("Season").item(0).getTextContent();

                author = eElement.getElementsByTagName("Author").item(0).getTextContent();

                country = eElement.getElementsByTagName("Country").item(0).getTextContent();

                logo = eElement.getElementsByTagName("Logo").item(0).getTextContent();

                sourceurl = eElement.getElementsByTagName("SourceURL").item(0).getTextContent();

                //System.out.println(file_name + "  " + icao + "   " + iata);

                FPAirline tmp = new FPAirline();

                tmp.setFile_name(file_name);
                tmp.setIcao(icao);
                tmp.setIata(iata);
                tmp.setCallsign(callsign);
                tmp.setName(name);
                tmp.setType(type);
                tmp.setSeason(season);
                tmp.setAuthor(author);
                tmp.setCountry(country);
                tmp.setLogo(logo);
                tmp.setSourceurl(sourceurl);

                airList.add(tmp);

            }
        }



    }


    /************************************************************************************************************************************************/

    public void parseXMLStringRoutes(String routeString, List<Route> routeList, int i){

        Document document = null;

        routeString = routeString.substring(1); /* Getting rid of some sort of BOM error in xml string */

        //System.out.println(routeString);

        try {
            document = loadXMLFromString(routeString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String routeName;
        int min, max, alt;
        String rule;
        List<Waypoint> waypoints = new ArrayList<>();

        Node nRoute = document.getElementsByTagName("Route").item(0);

        Element rElement = (Element) nRoute;

        routeName = rElement.getAttribute("name");

        min = Integer.parseInt(rElement.getAttribute("min"));

        max = Integer.parseInt(rElement.getAttribute("max"));

        alt = Integer.parseInt(rElement.getAttribute("alt"));

        rule = rElement.getAttribute("rule");

        //System.out.println(routeName + "   " + min + "   " + max + "   " + alt + "   " + rule);


        NodeList nList = document.getElementsByTagName("Waypoint");

        String id;
        Double lat, lon;
        int altW, kts, option;
        int x = 1;


        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) node;
                Waypoint tmpWay = new Waypoint();

                id = eElement.getAttribute("id");
                lat = Double.parseDouble(eElement.getAttribute("lat"));
                lon = Double.parseDouble(eElement.getAttribute("lon"));
                altW = Integer.parseInt(eElement.getAttribute("alt"));
                kts = Integer.parseInt(eElement.getAttribute("kts"));
                option = Integer.parseInt(eElement.getAttribute("option"));

                tmpWay.setId(id);
                tmpWay.setLat(lat);
                tmpWay.setLon(lon);
                tmpWay.setAlt(altW);
                tmpWay.setKts(kts);
                tmpWay.setOption(option);
                tmpWay.setRoute_id(i);
                tmpWay.setWaypoint_id(x);
                x++;

                waypoints.add(tmpWay);

            }
        }

        Route tmpRoute = new Route();

        tmpRoute.setName(routeName);
        tmpRoute.setMax(max);
        tmpRoute.setMin(min);
        tmpRoute.setAlt(alt);
        tmpRoute.setRule(rule);
        tmpRoute.setWaypoints(waypoints);
        tmpRoute.setRoute_id(i);

        routeList.add(tmpRoute);


    }


    /************************************************************************************************************************************************/

    public FlightPlan parseXMLStringFlightPlan(String fplanString, WrapInt fpidcounter, WrapInt airidcounter, WrapInt regidcounter){

        FlightPlan fPlan = new FlightPlan();

        fpidcounter.value++;

        Document document = null;

        fplanString = fplanString.substring(1); /* Getting rid of some sort of BOM error in xml string */

        try {
            document = loadXMLFromString(fplanString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Node nFplan = document.getElementsByTagName("Flightplan").item(0);

        Element fElement = (Element) nFplan;

        String airport_fs = fElement.getAttribute("airport_fs");

        List<String> aigRouteList = new ArrayList<>();
        List<Aircraft> aircraftList = new ArrayList<>();

        NodeList nList = document.getElementsByTagName("AIGRoute");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) node;

                String tmpString = eElement.getAttribute("title");

                aigRouteList.add(tmpString);

            }
        }


        NodeList nList2 = document.getElementsByTagName("Aircraft");

        int id_air;
        String title_air;
        String type_air;

        String id_reg;
        int rotation;

        String aigroute;
        int alt;
        String dep_apt;
        String arr_apt;
        int dep_time;
        int arr_time;
        String callsign;
        int flightnumber;
        String flightrule;
        String tng;


        for (int temp = 0; temp < nList2.getLength(); temp++) {
            Node node = nList2.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Aircraft tmpAir = new Aircraft();

                airidcounter.value++;

                Element eElement = (Element) node;
                id_air = Integer.parseInt(eElement.getAttribute("id"));
                title_air = eElement.getAttribute("title");
                type_air = eElement.getAttribute("type");

                List<Registration> registrations = new ArrayList<>();

                if(eElement.hasChildNodes()) {

                    NodeList regNList = eElement.getElementsByTagName("Registration");

                    for (int temp3 = 0; temp3 < regNList.getLength(); temp3++) {

                        List<Leg> legList = new ArrayList<>();

                        Node rNode = regNList.item(temp3);
                        Element rElement = (Element) rNode;

                        Registration tmpReg = new Registration();

                        id_reg = rElement.getAttribute("id");

                        rotation = Integer.parseInt(rElement.getAttribute("rotation"));

                        regidcounter.value++;

                        NodeList legNList = rElement.getElementsByTagName("Leg");

                        for (int temp2 = 0; temp2 < legNList.getLength(); temp2++) {
                            Node node2 = legNList.item(temp2);
                            if (node2.getNodeType() == Node.ELEMENT_NODE) {

                                Leg tmpLeg = new Leg();

                                Element lElement = (Element) node2;

                                aigroute = lElement.getAttribute("aigroute");
                                alt = Integer.parseInt(lElement.getAttribute("alt"));
                                dep_apt = lElement.getAttribute("dep-apt");
                                arr_apt = lElement.getAttribute("arr-apt");
                                dep_time = Integer.parseInt(lElement.getAttribute("dep-time"));
                                arr_time = Integer.parseInt(lElement.getAttribute("arr-time"));
                                callsign = lElement.getAttribute("callsign");
                                if (lElement.getAttribute("flightnumber").equals("")) {
                                    flightnumber = 0;
                                } else {
                                    flightnumber = Integer.parseInt(lElement.getAttribute("flightnumber"));
                                }
                                flightrule = lElement.getAttribute("flightrule");
                                tng = lElement.getAttribute("tng");

                                tmpLeg.setAigroute(aigroute);
                                tmpLeg.setAlt(alt);
                                tmpLeg.setDep_apt(dep_apt);
                                tmpLeg.setArr_apt(arr_apt);
                                tmpLeg.setDep_time(dep_time);
                                tmpLeg.setArr_time(arr_time);
                                tmpLeg.setCallsign(callsign);
                                tmpLeg.setFlightnumber(flightnumber);
                                tmpLeg.setFlightrule(flightrule);
                                tmpLeg.setTng(tng);
                                tmpLeg.setReg_id(regidcounter.value);

                                legList.add(tmpLeg);
                            }
                        }

                        tmpReg.setReg_id(regidcounter.value);
                        tmpReg.setId(id_reg);
                        tmpReg.setRotation(rotation);

                        tmpReg.setLegList(legList);
                        if (id_reg != null) {
                            tmpReg.setReg_id(regidcounter.value);
                            tmpReg.setAir_id(airidcounter.value);
                        }
                        registrations.add(tmpReg);
                    }

                }

                tmpAir.setId(id_air);
                tmpAir.setTitle(title_air);
                tmpAir.setType(type_air);
                tmpAir.setRegistrations(registrations);
                tmpAir.setAir_id(airidcounter.value);
                tmpAir.setFp_id(fpidcounter.value);

                aircraftList.add(tmpAir);

            }

        }

        fPlan.setAirport_fs(airport_fs);
        fPlan.setAigRouteList(aigRouteList);
        fPlan.setAircraftList(aircraftList);
        fPlan.setFp_id(fpidcounter.value);

        return fPlan;

    }

    /************************************************************************************************************************************************/

    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    /************************************************************************************************************************************************/

    public static DocumentBuilder getDocumentBuilder() throws Exception {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setCoalescing(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setValidating(false);
            return dbf.newDocumentBuilder();
        } catch (Exception exc) {
            throw new Exception(exc.getMessage());
        }
    }

    /************************************************************************************************************************************************/

    public String getStringFromXML(){

        String content = "";

        Path path = FileSystems.getDefault().getPath("FPDatabase.xml");

        try {
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();

        }

        return content;

    }

    /************************************************************************************************************************************************/

}
