package Objects;

/**
 * Created by Hano on 5/23/2017.
 */

public class Image {
    private int imageID;
    private int imageNumber;
    private int StatusID;
    private int UserID;
    private int SiteID;
    private int StoreID;
    private int CycleID;
    private int CampaignID;
    private String imageURL;
    private String TimeByDay;
    private String Active;
    public Image(int imageID, int imageNumber, String imageURL, int StatusID, int UserID, int SiteID, int StoreID, int CycleID, int CampaignID, String TimeByDay, String Active){
        this.setImageID(imageID);
        this.setImageNumber(imageNumber);
        this.setStatusID(StatusID);
        this.setUserID(UserID);
        this.setSiteID(SiteID);
        this.setStoreID(StoreID);
        this.setCycleID(CycleID);
        this.setCampaignID(CampaignID);
        this.setImageURL(imageURL);
        this.setTimeByDay(TimeByDay);
        this.setActive(Active);
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    public int getStatusID() {
        return StatusID;
    }

    public void setStatusID(int statusID) {
        StatusID = statusID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getSiteID() {
        return SiteID;
    }

    public void setSiteID(int siteID) {
        SiteID = siteID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public int getCycleID() {
        return CycleID;
    }

    public void setCycleID(int cycleID) {
        CycleID = cycleID;
    }

    public int getCampaignID() {
        return CampaignID;
    }

    public void setCampaignID(int campaignID) {
        CampaignID = campaignID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTimeByDay() {
        return TimeByDay;
    }

    public void setTimeByDay(String timeByDay) {
        TimeByDay = timeByDay;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }
}
