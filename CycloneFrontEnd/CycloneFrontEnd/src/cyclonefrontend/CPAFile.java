package cyclonefrontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.util.Vector;

import oracle.xml.parser.v2.DOMParser;
//import org.apache.xerces.parsers.DOMParser; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;


public class CPAFile {


    private String cpaDocNS = 
        "http://www.oasis-open.org/committees/ebxml-cppa/schema/cpp-cpa-2_0.xsd";
    private String attrCpaId = "tns:cpaid";
    private String tagPartyInfo = "tns:PartyInfo";
    private String attrPartyName = "tns:partyName";
    private String attrPartyType = "tns:type";
    private String tagPartyID = "tns:PartyId";
    private String tagCollabRole = "tns:CollaborationRole";
    private String tagRole = "tns:Role";
    private String attrRoleName = "tns:name";
    private String tagServiceBinding = "tns:ServiceBinding";
    private String tagService = "tns:Service";
    private String attrServiceType = "tns:type";
    private String tagCanSend = "tns:CanSend";
    private String tagActionBinding = "tns:ThisPartyActionBinding";
    private String attrAction = "tns:action";
    private Document cpaDoc;
    private Element cpaRoot;
    private NodeSelected cpaNode;
    private NodeSelection parties;
    private String cpaId;
    private String error;

    private void pl(String text) {
        System.out.println(text);
    }

    public CPAFile(String fileName) {
        try {
            File inputFile = new File(fileName);
            Reader inputReader;

            try {
                inputReader = new FileReader(inputFile);
                DOMParser parser = new DOMParser();
                parser.parse(inputReader);
                cpaDoc = parser.getDocument();

                cpaRoot = cpaDoc.getDocumentElement();
                cpaNode = new NodeSelected((Node)cpaRoot);
                cpaId = cpaNode.getAttrValue(attrCpaId);
                parties = new NodeSelection(cpaNode, tagPartyInfo);


            } catch (FileNotFoundException e) {
                error = "Cpa file not found: " + fileName;
                e.printStackTrace();
            } catch (SAXException e) {
                error = "Error parsing CPA: "+e.toString();
                e.printStackTrace();
            } catch (IOException e) {
                error = "Error reading CPA: "+e.toString();
                e.printStackTrace();
            }
        } catch (Exception e) {
            error = "Cpa file could not be loaded: " + fileName;
            e.printStackTrace();
        }

    }

    public NodeSelection getParties() {
        return parties;
    }

    public Vector getPartyNames() {
        Vector partyNames = new Vector();
        if (parties.getNodesSelected() > 0) {
            String partyName = 
                parties.getFirstNodeSel().getAttrValue(attrPartyName);
            partyNames.addElement(partyName);
            while (parties.hasNextNode()) {
                partyName = 
                        parties.getNextNodeSel().getAttrValue(attrPartyName);
                partyNames.addElement(partyName);
            }

        }
        return partyNames;
    }

    public NodeSelection getPartyIDs(Node party) {
        NodeSelection partyIDs = new NodeSelection(party, tagPartyID);
        return partyIDs;
    }

    public NodeSelection getPartyIDs(int partyIndex) {
        Node party = parties.getNodeByID(partyIndex);
        NodeSelection partyIDs = new NodeSelection(party, tagPartyID);
        return partyIDs;
    }

    private PartyID convertNode2PartyId(NodeSelected partyIdNode) {
        String partyIdType = partyIdNode.getAttrValue(attrPartyType);
        String partyIdValue = partyIdNode.getNodeValue();
        PartyID partyId = new PartyID(partyIdType, partyIdValue);
        return partyId;
    }

    public Vector getPartyIDsVec(int partyIndex) {
        NodeSelection partyIDs = getPartyIDs(partyIndex);
        Vector partyidVec = new Vector();
        if (partyIDs.getNodesSelected() > 0) {
            NodeSelected partyIdNode = partyIDs.getFirstNodeSel();
            PartyID partyId = convertNode2PartyId(partyIdNode);
            partyidVec.addElement(partyId);
            while (partyIDs.hasNextNode()) {
                partyIdNode = partyIDs.getNextNodeSel();
                partyId = convertNode2PartyId(partyIdNode);
                partyidVec.addElement(partyId);
            }
        }
        return partyidVec;
    }

    public NodeSelection getCollabRoles(Node party) {
        NodeSelection collabRoles = new NodeSelection(party, tagCollabRole);
        return collabRoles;
    }

    public NodeSelection getCollabRoles(NodeSelected party) {
        NodeSelection collabRoles = getCollabRoles(party.getNode());
        return collabRoles;
    }

    public NodeSelection getCollabRoles(int partyIndex) {
        Node party = parties.getNodeByID(partyIndex);
        NodeSelection collabRoles = getCollabRoles(party);
        return collabRoles;
    }

    public String getRoleName(Node collabRole) {
        NodeSelection roles = new NodeSelection(collabRole, tagRole);
        NodeSelected role = roles.getFirstNodeSel();
        String roleName = role.getAttrValue(attrRoleName);
        return roleName;
    }

    public String getRoleName(NodeSelected collabRole) {
        String roleName = getRoleName(collabRole.getNode());
        return roleName;
    }

    public String getCpaId() {
        return cpaId;
    }

    public NodeSelection getServiceBindings(Node collabRole) {
        NodeSelection serviceBindings = 
            new NodeSelection(collabRole, tagServiceBinding);
        return serviceBindings;
    }

    public NodeSelection getServiceBindings(NodeSelected collabRole) {
        NodeSelection serviceBindings = 
            getServiceBindings(collabRole.getNode());
        return serviceBindings;
    }

    public CPAService getService(Node serviceBinding) {
        NodeSelection serviceNodes = 
            new NodeSelection(serviceBinding, tagService);
        NodeSelected serviceNode = serviceNodes.getFirstNodeSel();
        String serviceName = serviceNode.getNodeValue();
        String serviceType = serviceNode.getAttrValue(attrServiceType);
        CPAService service = new CPAService(serviceType, serviceName);
        return service;
    }

    public CPAService getService(NodeSelected serviceBinding) {
        CPAService service = getService(serviceBinding.getNode());
        return service;
    }

    public String getActionName(NodeSelected canSend) {
        NodeSelection actionBinding = 
            new NodeSelection(canSend, tagActionBinding);
        String actionName = 
            actionBinding.getFirstNodeSel().getAttrValue(attrAction);
        return actionName;
    }

    public Vector getActionNames(NodeSelected serviceBinding) {
        NodeSelection canSends = new NodeSelection(serviceBinding, tagCanSend);
        Vector actionNames = new Vector();
        if (canSends.getNodesSelected() > 0) {
            String actionName = getActionName(canSends.getFirstNodeSel());
            actionNames.addElement(actionName);
            while (canSends.hasNextNode()) {
                actionName = getActionName(canSends.getNextNodeSel());
                actionNames.addElement(actionName);
            }
        }
        return actionNames;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
