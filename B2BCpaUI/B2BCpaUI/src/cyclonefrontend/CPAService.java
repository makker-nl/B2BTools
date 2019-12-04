package cyclonefrontend;

import org.w3c.dom.Node;

public class CPAService {
   private String type;
   private String name;
    public CPAService(String type, String name) {
        this.type = type;
        this.name = name;
    }
    public String getDisplayName(){
        return type + " - " + name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
