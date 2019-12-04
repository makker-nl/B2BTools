package b2bfrontend;

public class Queue {
    private String name;
    private String owner;
    public Queue(String owner, String name) {
    this.name = name;
    this.owner = owner;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
    public String getDisplayName(){
        return owner+'.'+name;
    }
}
