package io.github.winhour;

import io.github.winhour.model.PlaneData;

import java.util.ArrayList;
import java.util.List;

public class PlaneDataCreator {

    private int howmany = 3;

    public List<PlaneData> createIntermediaryPlaneData(int iteration, List<PlaneData> dataList1, List<PlaneData> dataList2){

        List<PlaneData> interList = new ArrayList<>();
        PlaneData tmpPlane;
        double tmpdouble;

        for(PlaneData p1 :dataList1){
            tmpPlane = new PlaneData();
            for (PlaneData p2 :dataList2){
                if(p1.getIcao24().equals(p2.getIcao24())){

                    tmpPlane.setIcao24(p1.getIcao24());

                    tmpdouble = p1.getPackId()+(calculateStep(p1.getPackId(), p2.getPackId(), howmany)*iteration);
                    tmpPlane.setPackId((int) (tmpdouble));

                    tmpPlane.setIcao(p1.getIcao());
                    tmpPlane.setCountry(p1.getCountry());

                    tmpdouble = p1.getUnix_time()+(calculateStep(p1.getUnix_time(), p2.getUnix_time(), howmany)*iteration);
                    tmpPlane.setUnix_time((int) (tmpdouble));

                    tmpdouble = p1.getUnix_last()+(calculateStep(p1.getUnix_last(), p2.getUnix_last(), howmany)*iteration);
                    tmpPlane.setUnix_last((int) (tmpdouble));

                    tmpdouble = p1.getLatitude()+(calculateStep(p1.getLatitude(), p2.getLatitude(), howmany)*iteration);
                    tmpPlane.setLatitude(tmpdouble);

                    tmpdouble = p1.getLongitude()+(calculateStep(p1.getLongitude(), p2.getLongitude(), howmany)*iteration);
                    tmpPlane.setLongitude(tmpdouble);

                    if(p1.getBaro_altitude() == null || p2.getBaro_altitude() == null) {
                        tmpPlane.setBaro_altitude(null);
                    } else {
                        tmpdouble = p1.getBaro_altitude() + (calculateStep(p1.getBaro_altitude(), p2.getBaro_altitude(), howmany) * iteration);
                        tmpPlane.setBaro_altitude(tmpdouble);
                    }

                    if(p1.getAltitude() == null || p2.getAltitude() == null) {
                        tmpPlane.setAltitude(null);
                    } else {
                        tmpdouble = p1.getAltitude() + (calculateStep(p1.getAltitude(), p2.getAltitude(), howmany) * iteration);
                        tmpPlane.setAltitude(tmpdouble);
                    }

                    tmpPlane.setOn_ground(p1.isOn_ground());

                    if(p1.getVelocity() != null && p2.getVelocity() != null) {
                        tmpdouble = p1.getVelocity() + (calculateStep(p1.getVelocity(), p2.getVelocity(), howmany) * iteration);
                        tmpPlane.setVelocity(tmpdouble);
                    } else {
                        tmpPlane.setVelocity(null);
                    }

                    if(p1.getTrue_track() != null && p2.getTrue_track() != null) {
                        tmpdouble = p1.getTrue_track() + (calculateStep(p1.getTrue_track(), p2.getTrue_track(), howmany) * iteration);
                        tmpPlane.setTrue_track(tmpdouble);
                    } else {
                        tmpPlane.setTrue_track(null);
                    }

                    if(p1.getVertical_rate() == null || p2.getVertical_rate() == null) {
                        tmpPlane.setVertical_rate(null);
                    } else {
                        tmpdouble = p1.getVertical_rate() + (calculateStep(p1.getVertical_rate(), p2.getVertical_rate(), howmany) * iteration);
                        tmpPlane.setVertical_rate(tmpdouble);
                    }

                    tmpPlane.setSquawk(p1.getSquawk());

                    tmpPlane.setSpi(p1.isSpi());

                    tmpPlane.setPosition_source(p1.getPosition_source());

                    String newString = p1.getSource().substring(0,1)+'0';

                    tmpPlane.setSource(newString);

                    break;

                }
            }
            if(tmpPlane.getPackId() != 0) {
                interList.add(tmpPlane);
            }
        }

        return interList;
    }


    public double calculateStep(double a, double b, int howmany){

        double result = b-a;

        return result/howmany;

    }

    public int getHowmany() {
        return howmany;
    }

    public void setHowmany(int howmany) {
        this.howmany = howmany;
    }
}
