package Objects;

import java.util.LinkedList;

/**
 * Created by Hano on 5/23/2017.
 */

public class Image {
    private int imageID;
    private int imageNumber;
    private int StatusID;
    private int UserID;
    private String SiteName;
    private String StoreName;
    private int CycleID;
    private int CampaignID;
    private String imageURL;
    private String TimeByDay;
    private String Active;
    private LinkedList<String>Comments=new LinkedList<>();
    public Image(int imageID, int imageNumber, String imageURL, int StatusID, int UserID, String SiteName, String StoreName, int CycleID, int CampaignID, String TimeByDay, String Active){
        this.setImageID(imageID);
        this.setImageNumber(imageNumber);
        this.setStatusID(StatusID);
        this.setUserID(UserID);
        this.setSiteName(SiteName);
        this.setStoreName(StoreName);
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

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
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
    public void addComments(String comment){
        Comments.add(comment);
    }
    public LinkedList<String> getComments(){
        return Comments;
    }
}
