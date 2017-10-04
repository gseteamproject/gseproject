package gseproject.robot.skills;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import gseproject.infrastructure.XMLParser.IXMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

public class SkillsSettings implements IXMLParser {

    public ISkill _paint;
    public ISkill _clean;
    public ISkill _transport;
    public String _robotID;

    /* _mode describes how agents should be started. TODO: Remove hardcode
     * 0 - physical
     * 1 - virtual
     */
    public int _mode = 1;

    public SkillsSettings() {
        _paint = new PaintSkill();
        _clean = new CleanSkill();
        _transport = new TransportSkill();
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
            if (level == 3) {
                if (node.getNodeName().equals("Clean")) {
                    parseClean(childNode);
                    continue;
                }
                if (node.getNodeName().equals("Paint")) {
                    parsePaint(childNode);
                    continue;
                }
                if (node.getNodeName().equals("Transport")) {
                    parseTransport(childNode);
                    continue;
                }
            }
            visit(childNode, level + 1);
        }
    }

    private void parseClean(Node node) {
        if (node.getNodeName().equals("Duration")) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _clean.setDuration(Integer.parseInt(eElement.getTextContent()));
            }
        }
        if (node.getNodeName().equals("Cost")) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _clean.setCost(Integer.parseInt(eElement.getTextContent()));
            }
        }
    }

    private void parsePaint(Node node) {
        if (node.getNodeName().equals("Duration")) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _paint.setDuration(Integer.parseInt(eElement.getTextContent()));
            }
        }
        if (node.getNodeName().equals("Cost")) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _paint.setCost(Integer.parseInt(eElement.getTextContent()));
            }
        }
    }

    private void parseTransport(Node node) {
        if (node.getNodeName().equals("Duration")) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _transport.setDuration(Integer.parseInt(eElement.getTextContent()));
            }
        }
        if (node.getNodeName().equals("Cost")) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
               _transport.setCost(Integer.parseInt(eElement.getTextContent()));
            }
        }
    }

    public double getDPaint(){


        double dPaint = Math.sqrt(((_paint.getCost() * _paint.getCost())
                + (_paint.getDuration() * _paint.getDuration())) / 2);

        return dPaint;
    }

    public double getDClean(){

        double dClean = Math.sqrt(((_clean.getCost() * _clean.getCost())
                + (_clean.getDuration() * _clean.getDuration())) / 2);

        return dClean;
    }

    public double getDTransport(){
        double dTransport = Math.sqrt(((_transport.getCost() * _transport.getCost())
                + (_transport.getDuration() * _transport.getDuration())) / 2);


        return dTransport;
    }

    public String CodeString()
    {
        String str;

        str  = "Transport" + Integer.toString(_transport.getDuration()) + "/" + Integer.toString(_transport.getCost()) + "/";
        str += "Clean" + Integer.toString(_clean.getDuration()) + "/" + Integer.toString(_clean.getCost()) + "/";
        str += "Paint" + Integer.toString(_paint.getDuration()) + "/" + Integer.toString(_paint.getCost()) + "/";

        return str;
    }
    public boolean DecodeString(String str)
    {
        String strBuffer = new String();
        String strNumber = new String();
        int length = str.length();

        char[] chArray = str.toCharArray();

        for(int i = 0; i != length ;++i)
        {
            if(strBuffer.equals("Transport"))
            {
                if(chArray[i] == '/')
                {
                    if(_transport.getDuration() == 0) {
                        _transport.setDuration(Integer.valueOf(strNumber));
                        strNumber = "";
                        continue;
                    }
                    if(_transport.getCost() == 0) {
                        _transport.setCost(Integer.valueOf(strNumber));
                        strNumber = "";
                        strBuffer = "";
                        continue;
                    }
                }
                strNumber += chArray[i];
                continue;
            }

            if(strBuffer.equals("Clean"))
            {
                if(chArray[i] == '/')
                {
                    if(_clean.getDuration() == 0) {
                        _clean.setDuration(Integer.valueOf(strNumber));
                        strNumber = "";
                        continue;
                    }
                    if(_clean.getCost() == 0) {
                        _clean.setCost(Integer.valueOf(strNumber));
                        strNumber = "";
                        strBuffer = "";
                        continue;
                    }
                }
                strNumber += chArray[i];
                continue;
            }

            if(strBuffer.equals("Paint"))
            {
                if(chArray[i] == '/')
                {
                    if(_paint.getDuration() == 0) {
                        _paint.setDuration(Integer.valueOf(strNumber));
                        strNumber = "";
                        continue;
                    }
                    if(_paint.getCost() == 0) {
                        _paint.setCost(Integer.valueOf(strNumber));
                        strNumber = "";
                        strBuffer = "";
                        continue;
                    }
                }
                strNumber += chArray[i];
                continue;
            }

            strBuffer += chArray[i];
        }

        return true;
    }
}
