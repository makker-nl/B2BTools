package cyclonefrontend;

import java.util.Vector;

import oracle.xml.parser.v2.XMLDOMImplementation;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeSelection {

  private Vector nodesSelected = new Vector();
  private NodeSelected currentNode;
  private int currentIndex;
  
    private void selectNodesByName(NodeList nodes, String nodeName){
        Node node = nodes.item(0);
        while(node!=null){
            if (node.getNodeName().equals(nodeName)){
                nodesSelected.addElement(node);
            }
            node = node.getNextSibling();
        }
        // Initialize currentNode
        if (getNodesSelected()>0){
            currentNode = new NodeSelected((Node)nodesSelected.firstElement());
            currentIndex=0;
        }
   }  
    public NodeSelection(NodeList nodes, String nodeName) {
        selectNodesByName( nodes, nodeName);
    }
    public NodeSelection(NodeSelected node, String nodeName) {
        NodeList nodes = node.getNode().getChildNodes();
        selectNodesByName( nodes, nodeName);
    }
    public NodeSelection(Node node, String nodeName) {
        NodeList nodes = node.getChildNodes();
        selectNodesByName( nodes, nodeName);
    }
    public NodeSelected getFirstNodeSel(){
        currentNode.setNode((Node)nodesSelected.firstElement());
        currentIndex = nodesSelected.indexOf(nodesSelected.firstElement());
        return currentNode;
    }
    public Node getFirstNode(){
        currentNode.setNode((Node)nodesSelected.firstElement());
        currentIndex = nodesSelected.indexOf(nodesSelected.firstElement());
        return currentNode.getNode();
    }
    public NodeSelected getNextNodeSel(){
        currentIndex++;
        currentNode.setNode((Node)nodesSelected.elementAt(currentIndex));
        return currentNode;
    }
    public Node getNextNode(){
        currentIndex++;
        currentNode.setNode((Node)nodesSelected.elementAt(currentIndex));
        return currentNode.getNode();
    }
    public NodeSelected getNodeByIDSel(int index){
        currentIndex=index;
        currentNode.setNode((Node)nodesSelected.elementAt(currentIndex));
        return currentNode;
    }
    public Node getNodeByID(int index){
        currentIndex=index;
        currentNode.setNode((Node)nodesSelected.elementAt(currentIndex));
        return currentNode.getNode();
    }
    public boolean hasNextNode(){
        int lastIndex = nodesSelected.indexOf(nodesSelected.lastElement());
        return (currentIndex<lastIndex);
    }
    public NodeSelected getCurrentNode(){
        return currentNode;
    }
    
    public NodeSelected getPreviousNodeSel(){
        currentIndex--;
        currentNode.setNode((Node)nodesSelected.elementAt(currentIndex));
        return currentNode;
    }
    public Node getPreviousNode(){
        currentIndex--;
        currentNode.setNode((Node)nodesSelected.elementAt(currentIndex));
        return currentNode.getNode();
    }
    public boolean hasPreviousNode(){
        int firstIndex = nodesSelected.indexOf(nodesSelected.firstElement());
        return (currentIndex>firstIndex);
    }
    public int getNodesSelected(){
        return nodesSelected.size();
    }
    public NodeSelected getLastNodeSel(){
        currentNode.setNode((Node)nodesSelected.lastElement());
        currentIndex = nodesSelected.indexOf(nodesSelected.lastElement());
        return currentNode;
    }
    public Node getLastNode(){
        currentNode.setNode((Node)nodesSelected.lastElement());
        currentIndex = nodesSelected.indexOf(nodesSelected.lastElement());
        return currentNode.getNode();
    }
    public String getCurNodeAttrValue(String AttributeName){
            Node attribute = currentNode.getNode().getAttributes().getNamedItem(AttributeName);
            return attribute.getNodeValue();
    }
}
