package cyclonefrontend;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;

import java.nio.file.Files;

import java.util.Map;

import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class SoapRequest {
    private static final String ACTION="Aap";
    private static final String NAMESPACE="http://aap.noot.mies";
    private static String urlWS ="localhost:8089";

    public SoapRequest() {
        super();
    }
    // Create SOAP Message
    private static SOAPMessage createSOAPRequest(File uploadFile, Map<String, String> values, String fileName,
                                                 String[] attributes) throws Exception {

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Header
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", ACTION);

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("sch", NAMESPACE);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement createItemRequest = soapBody.addChildElement("CreateItemRequest", "sch");
        SOAPElement authenticationData = createItemRequest.addChildElement("AuthenticationData", "sch");
        SOAPElement serverDef = authenticationData.addChildElement("ServerDef", "sch");
        SOAPElement serverType = serverDef.addChildElement("ServerType", "sch");
        serverType.addTextNode("ICM");
        SOAPElement serverName = serverDef.addChildElement("ServerName", "sch");
        serverName.addTextNode("icmnlsdb");
        SOAPElement loginData = authenticationData.addChildElement("LoginData", "sch");
        SOAPElement userID = loginData.addChildElement("UserID", "sch");
        userID.addTextNode("*******");
        SOAPElement password = loginData.addChildElement("Password", "sch");
        password.addTextNode("*******");
        SOAPElement item = createItemRequest.addChildElement("Item", "sch");
        SOAPElement itemXML = item.addChildElement("ItemXML", "sch");
        SOAPElement type = itemXML.addChildElement(values.get("Type"), "sch");

        for (int i = 0; i < attributes.length; i++)
            type.setAttribute(attributes[i], values.get(attributes[i]));

        SOAPElement icmBASE = type.addChildElement("ICMBASE", "sch");
        SOAPElement resourceObject = icmBASE.addChildElement("resourceObject", "sch");


        resourceObject.setAttribute("MIMEType", Files.probeContentType(uploadFile.toPath()));

        resourceObject.setAttribute("xmlns", "http://www.ibm.com/xmlns/db2/cm/api/1.0/schema");
        SOAPElement label = resourceObject.addChildElement("label", "sch");
        label.setAttribute("name", fileName);

        AttachmentPart attachment = soapMessage.createAttachmentPart();

        InputStream targetStream = new FileInputStream(uploadFile);

        attachment.setRawContent(targetStream, Files.probeContentType(uploadFile.toPath()));

        attachment.setContentId(fileName);

        soapMessage.addAttachmentPart(attachment);

        soapMessage.saveChanges();

        return soapMessage;
    }
    // HTTP POST Upload
    public void doUpload(File uploadFile, Map<String, String> values, String nombreArchivo,
                         String[] nombreAtributos) throws Exception {

        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        SOAPMessage soapResponse =
            soapConnection.call(createSOAPRequest(uploadFile, values, nombreArchivo, nombreAtributos), urlWS);

        soapConnection.close();
    }
}
