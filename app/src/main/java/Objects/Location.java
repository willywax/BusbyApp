package Objects;

import java.util.ArrayList;

/**
 * Created by william on 5/24/17.
 */

public class Location {
    private ArrayList<String> locationArray = new ArrayList<>();
    private String Store;
    private String Site;
    private String locationName;


    public Location(String Store, String Site){
        this.Store = Store;
        this.Site=Site;
        locationName=Store+"-"+Site;
        locationArray.add(locationName);
    }
    public Location(){
    }

    public String getLocationName() {
        return locationName;
    }

    public void add(String Store, String Site) {
        this.locationName = Store+"-"+Site;
        locationArray.add(locationName);
    }

    public ArrayList<String> getLocationArray() {
        return locationArray;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public String getStore() {
        return Store;
    }

    public String getSite() {
        return Site;
    }
}

