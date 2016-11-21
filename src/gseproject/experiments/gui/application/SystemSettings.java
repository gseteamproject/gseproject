package gseproject.experiments.gui.application;


import gseproject.infrastructure.XMLParser.IXMLParser;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

public class SystemSettings implements IXMLParser {

    public String[] args;
    public boolean trackManager;
    public String strTitle;
    public Integer minWidth;
    public Integer minHeight;

    public SystemSettings() {
        args = new String[0];
        trackManager = false;
    }

    public void print() {
        System.out.print("Arguments are:\n");
        for(int i = 0; i < args.length; ++i){
            System.out.print(args[i]);
            System.out.print("\n");
        }
        System.out.print("\nTrackManager = ");
        if(trackManager){
            System.out.print(" TRUE \n");
        }
        else{
            System.out.print(" FALSE \n");
        }
        System.out.print("MinWidth = ");
        System.out.print(minWidth);
        System.out.print("\nMinHeight = ");
        System.out.print(minHeight);
        System.out.print("\n");
    }

    public void xmlDocumentDecode(String strPath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(strPath));
        visit(doc, 0);
    }

    private void visit(Node node, int level) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            Node childNode = list.item(i);
            if (level == 2) {
                if (node.getNodeName() == "Arguments") {
                    parseArguments(childNode);
                    continue;
                }
                if (node.getNodeName() == "GUI") {
                    parseGUI(childNode);
                    continue;
                }
            }
            visit(childNode, level + 1);
        }
    }

    private void parseArguments(Node node ) {
        if(node.getNodeName() == "Argument") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) node;
                String [] strTemp = new String[args.length + 1];
                System.arraycopy(args, 0, strTemp, 0, args.length);
                args = strTemp;
                args[args.length - 1] = eElement.getTextContent();
            }
        }
    }

    private void parseGUI(Node node) {
        if (node.getNodeName() == "TrackManager") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                if (eElement.getTextContent() == "TRUE") {
                    trackManager = true;
                }
            }
        }
        if (node.getNodeName() == "Title") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                strTitle = eElement.getTextContent();
            }
        }
        if (node.getNodeName() == "MinWidth") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                minWidth = Integer.parseInt(eElement.getTextContent());
            }
        }
        if (node.getNodeName() == "MinHeight") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                minHeight = Integer.parseInt(eElement.getTextContent());
            }
        }

    }
}
