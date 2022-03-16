package io.github.winhour.misc;

import java.util.regex.Pattern;

public class RegexComparer {

    public String compare (String insertString){

        System.out.println("REGEX COMPARISON");

        String result = "test failed";

        //AIGAIM_Maximus Air Antonov An-124 (NO MODEL)
        //AIGAIM_RAF-Avia Antonov An-26 - YL-RAD (NO MODEL)

        /*

        if (insertString.matches("(?i)(?=.*Antonov)(?=.*AN(-? ?)124)")) result = "A124";

        if (insertString.matches("(?i)(?=.*Antonov)(?=.*AN(-? ?)140)")) result = "A140";

        if (insertString.matches("(?i)(?=.*Antonov)(?=.*An(-? ?)225)")) result = "A225";

        //if (insertString.matches("(?i)(?=.*Antonov)(?=.*An(-? ?)124)")) result = "lowercase";

        if (Pattern.matches("(?i).*Antonov*", insertString)) {

            System.out.println("ANTONOV");

            if (Pattern.matches("(?i).*AN-124*", insertString)) result = "A124";

            if (Pattern.matches("(?i).*AN-140*", insertString)) result = "A140";

            if (Pattern.matches("(?i).*AN-225*", insertString)) result = "A225";

        }
         */

        if (insertString.toLowerCase().contains("Antonov".toLowerCase())){

            if (insertString.toLowerCase().contains("AN-124".toLowerCase())) result = "A124";

            if (insertString.toLowerCase().contains("AN-140".toLowerCase())) result = "A140";

            if (insertString.toLowerCase().contains("AN-225".toLowerCase())) result = "A225";

            if (insertString.toLowerCase().contains("AN-24".toLowerCase())) result = "AN24";

            if (insertString.toLowerCase().contains("AN-26".toLowerCase())) result = "AN26";

        }

        if (insertString.toLowerCase().contains("Airbus".toLowerCase())){

            if (insertString.toLowerCase().contains("A350".toLowerCase())) result = "A350";

            if (insertString.toLowerCase().contains("A340-500".toLowerCase())) result = "A345";

            if (insertString.toLowerCase().contains("A340-600".toLowerCase())) result = "A346";

        }

        if (insertString.toLowerCase().contains("Aerospatiale/Alenia".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Boeing".toLowerCase())){

            if (insertString.toLowerCase().contains("747-200".toLowerCase())) result = "B742";

            if (insertString.toLowerCase().contains("747-300".toLowerCase())) result = "B743";

            if (insertString.toLowerCase().contains("747-400".toLowerCase())) result = "B744";

        }

        if (insertString.toLowerCase().contains("Cessna".toLowerCase())){
            

        }

        if (insertString.toLowerCase().contains("Eurocopter".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Beechcraft".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Bell".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Bombardier".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Lockheed".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Douglas".toLowerCase())){



            if (insertString.toLowerCase().contains("McDonnell Douglas".toLowerCase())){



            }


        }

        if (insertString.toLowerCase().contains("Embraer".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Fokker".toLowerCase())){



        }

        if (insertString.toLowerCase().contains("Ilyushin".toLowerCase())){



        }


        if (insertString.toLowerCase().contains("Sikorsky".toLowerCase())){



        }


        if (insertString.toLowerCase().contains("Tupolev".toLowerCase())){



        }







        return result;

    }

}
