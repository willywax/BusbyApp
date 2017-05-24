package Objects;

import java.util.ArrayList;

/**
 * Created by william on 5/24/17.
 */

public class Location {
    private ArrayList<String> locationArray = new ArrayList<>();
    private String locationName;


    public Location(String a){
        this.locationName = a;
        locationArray.add(locationName);
    }
    public Location(){
        this.locationName = "";
    }

    public String getLocationName() {
        return locationName;
    }

    public void add(String monthName) {
        this.locationName = monthName;
        locationArray.add(monthName);
    }

    public ArrayList<String> getLocationArray() {
        return locationArray;
    }




    @Override
    public String toString() {
        return super.toString();
    }
}

