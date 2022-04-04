package io.github.winhour;

import io.github.winhour.model.FPAirline;
import io.github.winhour.model.Waypoint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AIGFPExtractor {

    // Extracting from .aigfp Files in /AIGFP/ folder

    private final static Long MILLS_IN_DAY = 86400000L;

    /************************************************************************************************************************************************/

    //https://zetcode.com/java/zipinputstream/

    public String extract(String file_name, List<String> route_strings){

        //file_name = "Traffic_AIG_9Air_Winter_2018-2019v3.aigfp";


        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read = 0;
        StringBuilder s2 = new StringBuilder();

        try (FileInputStream fis = new FileInputStream("AIGFP/" + file_name);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ZipInputStream zis = new ZipInputStream(bis)) {

            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {

                String tmp = new File(ze.getName()).getName();

                /*System.out.format("File: %s Size: %d Last Modified %s %n",
                        ze.getName(), ze.getSize(),
                        LocalDate.ofEpochDay(ze.getTime() / MILLS_IN_DAY));*/

                if(tmp.equals("Flightplan.aigxml")){

                    while ((read = zis.read(buffer, 0, 1024)) >= 0) {
                        s2.append(new String(buffer, 0, read));
                    }

                } else {

                    while ((read = zis.read(buffer, 0, 1024)) >= 0) {
                        s.append(new String(buffer, 0, read));
                    }
                    route_strings.add(s.toString());
                    s.setLength(0);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        System.out.println(s);

        System.out.println("///////////////////////////////////");

        System.out.println(s2);

        for (String str : route_strings){
            System.out.println(str);
        }
         */

        return s2.toString();


    }

    /************************************************************************************************************************************************/




    /************************************************************************************************************************************************/

}
