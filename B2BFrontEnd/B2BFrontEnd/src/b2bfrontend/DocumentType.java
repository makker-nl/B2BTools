package b2bfrontend;

public class DocumentType {
    private String displayName;
    private String name;
    private String revision;

    public DocumentType(String name, String revision) {
        this.name = name;
        this.revision = revision;
        this.displayName = this.name + " - " + this.revision;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getRevision() {
        return revision;
    }
}
