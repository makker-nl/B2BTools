package cyclonefrontend;

import org.w3c.dom.Node;

public class NodeSelected {
    Node node;

    public NodeSelected(Node node) {
        this.node = node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public String getAttrValue(String AttributeName) {
        Node attribute = node.getAttributes().getNamedItem(AttributeName);
        String attributeValue;
        if (attribute!=null){
            attributeValue = attribute.getNodeValue();
        }else{
            attributeValue=null;
        }
        return attributeValue;
    }

    public String getNodeValue() {
        String value = node.getFirstChild().getNodeValue();
        return value;
    }
}
