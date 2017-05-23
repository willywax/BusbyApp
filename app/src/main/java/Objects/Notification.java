package Objects;

/**
 * Created by Hano on 5/23/2017.
 */

public class Notification {
    private int ID;
    private int idUser;
    private int StoreID;
    private String State;
    private String Store;
    private String Site;
    private String time;
    public Notification(int ID, int idUser, int StoreID, String State, String Store,String Site, String time){
        this.ID=ID;
        this.idUser=idUser;
        this.StoreID=StoreID;
        this.setState(State);
        this.Store=Store;
        this.Site=Site;
        this.time=time;
    }

    public int getID() {
        return ID;
    }

    public int getIdUser() {
        return idUser;
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
