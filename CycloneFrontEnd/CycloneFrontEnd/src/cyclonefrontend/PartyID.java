package cyclonefrontend;

public class PartyID {
    private String id;
    private String type;
    public PartyID(String type, String id) {
      this.id = id;
      this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public String getDisplay(){
        return type+" - "+id;
    }
}
