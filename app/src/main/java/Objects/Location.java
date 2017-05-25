package Objects;

/**
 * Created by william on 5/24/17.
 */

public class Location {
    private String Store;
    private String Site;
    private int SiteID;
    private String locationName;


    public Location(String Store, String Site, int SiteID){
        this.Store=Store;
        this.Site=Site;
        this.SiteID=SiteID;
        setLocationName(Store+"-"+Site);
    }
    public Location(){
    }

    public String getLocationName() {
        return locationName;
    }

    public String getStore() {
        return Store;
    }

    public String getSite() {
        return Site;
    }

    public int getSiteID() {
        return SiteID;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}

