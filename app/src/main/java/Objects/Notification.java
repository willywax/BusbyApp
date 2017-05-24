package Objects;

/**
 * Created by Hano on 5/23/2017.
 */

public class Notification {
    private int ID;
    private int UserID;
    private int StoreID;
    private String State;
    private String Store;
    private String Site;
    private String time;
    public Notification(int ID, int UserID, int StoreID, String State, String Store,String Site, String time){
        this.ID=ID;
        this.UserID=UserID;
        this.StoreID=StoreID;
        this.setState(State);
        this.Store=Store;
        this.Site=Site;
        this.time=time;
    }

    public int getID() {
        return ID;
    }

    public int getUserID() {
        return UserID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public String getState() {
        return State;
    }

    public String getStore() {
        return Store;
    }
    public String getSite() {
        return Site;
    }

    public String getTime() {
        return time;
    }

    public void setState(String state) {
        State = state;
    }
}
