package cyclonefrontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class MMDFile {
    private String documentId;
    private String protocol = "ebXML";
    private String protocolVersion = "2.0";
    private String mdNameFrom;
    private String mdTypeFrom;
    private String mdFromRole;
    private String mdNameTo;
    private String mdTypeTo;
    private String mdToRole;
    private String mdCPAId;
    private String mdConversationId;
    private String mdRefToMessageId;
    private String mdService;
    private String mdServiceType;
    private String mdAction;
    private String payloadId = "ID1";
    private String payloadMimeContentId;
    private String payloadMimeContentType = "application/xml";
    private String payloadLocationType = "filePath";
    private String payloadLocation;
    private String content;
    private String error;
    private String mmdFilePath;
    

    public MMDFile() {
    }

    public MMDFile(String documentId, String cpaId, PartyID fromPartyId,
                   PartyID toPartyId, String fromRole, String toRole,
                   CPAService service, String action, String refToMessageId,
                   String conversationId, String payloadLocation) {
        this.documentId = documentId;
        this.mdCPAId = cpaId;
        this.mdNameFrom = fromPartyId.getId();
        this.mdTypeFrom = fromPartyId.getType();
        this.mdNameTo = toPartyId.getId();
        this.mdTypeTo = toPartyId.getType();
        this.mdFromRole = fromRole;
        this.mdToRole = toRole;
        this.mdService = service.getName();
        this.mdServiceType = service.getType();
        this.mdAction = action;
        this.mdRefToMessageId = refToMessageId;
        this.mdConversationId = conversationId;
        this.payloadLocation = payloadLocation;
        this.payloadMimeContentId = payloadLocation;
    }

    public String getMetaDataElement(String name, String type, String value) {
        String element = "\t<Metadata name=\"" + name + "\"";
        if (!"".equals(type) && type != null) {
            element = element + " type=\"" + type + "\"";
        }
        if (!"".equals(value) && value != null) {
            element = element + ">" + value + "</Metadata>\n";
        } else if (!"".equals(type)&&type!=null) {
            element = element + "/>\n"; //Should not occur: MDE may not be empty
        } else{
            element = "";
        }
        return element;
    }

    public String getMessagePayloadsElement() {
        String element =
            "\t<MessagePayloads>\n" + "\t\t<Payload id=\"" + payloadId +
            "\">\n" + "\t\t\t<MimeContentId>" + payloadMimeContentId +
            "</MimeContentId>\n" + "\t\t\t<MimeContentType>" +
            payloadMimeContentType + "</MimeContentType>\n" +
            "\t\t\t<Location type=\"" + payloadLocationType + "\">" +
            payloadLocation + "</Location>\n" + "\t\t</Payload>\n" +
            "\t</MessagePayloads>\n";
        return element;

    }

    public void generate() {
        content =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<MessageMetadataDocument documentId=\"" +
                documentId + "\" protocol=\"" + protocol +
                "\" protocolVersion=\"" + protocolVersion + "\">\n" +
                getMetaDataElement("From", mdTypeFrom, mdNameFrom) +
                getMetaDataElement("FromRole", "", mdFromRole) +
                getMetaDataElement("To", mdTypeTo, mdNameTo) +
                getMetaDataElement("ToRole", "", mdToRole) +
                getMetaDataElement("CPAId", "", mdCPAId) +
                getMetaDataElement("ConversationId", "", mdConversationId) +
                getMetaDataElement("RefToMessageId", "", mdRefToMessageId) +
                getMetaDataElement("Service", mdServiceType, mdService) +
                getMetaDataElement("Action", "", mdAction) +
                getMessagePayloadsElement() + "</MessageMetadataDocument>";
    }

    public void save() {
      if (mmdFilePath!=null&&content!=null){
          TextFile mmdFile = new TextFile(content, mmdFilePath);
          mmdFile.save();
          error = mmdFile.getError();
      }
    }
    public void save(String filePath) {
      mmdFilePath = filePath;
      save();
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setMdNameFrom(String mdNameFrom) {
        this.mdNameFrom = mdNameFrom;
    }

    public String getMdNameFrom() {
        return mdNameFrom;
    }

    public void setMdTypeFrom(String mdTypeFrom) {
        this.mdTypeFrom = mdTypeFrom;
    }

    public String getMdTypeFrom() {
        return mdTypeFrom;
    }

    public void setMdFromRole(String mdFromRole) {
        this.mdFromRole = mdFromRole;
    }

    public String getMdFromRole() {
        return mdFromRole;
    }

    public void setMdNameTo(String mdNameTo) {
        this.mdNameTo = mdNameTo;
    }

    public String getMdNameTo() {
        return mdNameTo;
    }

    public void setMdTypeTo(String mdTypeTo) {
        this.mdTypeTo = mdTypeTo;
    }

    public String getMdTypeTo() {
        return mdTypeTo;
    }

    public void setMdToRole(String mdToRole) {
        this.mdToRole = mdToRole;
    }

    public String getMdToRole() {
        return mdToRole;
    }

    public void setMdCPAId(String mdCPAId) {
        this.mdCPAId = mdCPAId;
    }

    public String getMdCPAId() {
        return mdCPAId;
    }

    public void setMdConversationId(String mdConversationId) {
        this.mdConversationId = mdConversationId;
    }

    public String getMdConversationId() {
        return mdConversationId;
    }

    public void setMdRefToMessageId(String mdRefToMessageId) {
        this.mdRefToMessageId = mdRefToMessageId;
    }

    public String getMdRefToMessageId() {
        return mdRefToMessageId;
    }

    public void setMdService(String mdService) {
        this.mdService = mdService;
    }

    public String getMdService() {
        return mdService;
    }

    public void setMdServiceType(String mdServiceType) {
        this.mdServiceType = mdServiceType;
    }

    public String getMdServiceType() {
        return mdServiceType;
    }

    public void setMdAction(String mdAction) {
        this.mdAction = mdAction;
    }

    public String getMdAction() {
        return mdAction;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadMimeContentId(String payloadMimeContentId) {
        this.payloadMimeContentId = payloadMimeContentId;
    }

    public String getPayloadMimeContentId() {
        return payloadMimeContentId;
    }

    public void setPayloadMimeContentType(String payloadMimeContentType) {
        this.payloadMimeContentType = payloadMimeContentType;
    }

    public String getPayloadMimeContentType() {
        return payloadMimeContentType;
    }

    public void setPayloadLocationType(String payloadLocationType) {
        this.payloadLocationType = payloadLocationType;
    }

    public String getPayloadLocationType() {
        return payloadLocationType;
    }

    public void setPayloadLocation(String payloadLocation) {
        this.payloadLocation = payloadLocation;
    }

    public String getPayloadLocation() {
        return payloadLocation;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    private void showError(String error) {
        this.error = error;
        System.out.println(this.error);
    }

    public void setMmdFilePath(String mmdFilePath) {
        this.mmdFilePath = mmdFilePath;
    }

    public String getMmdFilePath() {
        return mmdFilePath;
    }
}
