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
    public Integer _robotID;

    /* Temporal attr for parsing and algorithm */
    private Integer _tempRobotID;
    private List<SkillsSettings> _tempSkills;

    public SkillsSettings() {
        _paint = new PaintSkill();
        _clean = new CleanSkill();
        _transport = new TransportSkill();
        _tempSkills = new ArrayList<SkillsSettings>();
    }



    public void xmlDocumentDecode(String strPath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(strPath));
        visit(doc, 0);
        for(int i = 0; i < _tempSkills.size(); ++i)
        {
            if(_tempSkills.get(i) != null)
            {
                if(_tempSkills.get(i)._robotID == _robotID) {
                    _paint = _tempSkills.get(i)._paint;
                    _clean = _tempSkills.get(i)._clean;
                    _transport = _tempSkills.get(i)._transport;
                }
            }
        }
    }

    private void visit(Node node, int level) {
        NodeList list = node.getChildNodes();

        for (int i = 0; i < list.getLength(); ++i) {
            Node childNode = list.item(i);
            if (level == 2) {
                if(node.getNodeName() == "CurrentID")
                {
                    ParseCurrentID(childNode);
                }
                parseID(node);
                if (node.getNodeName() == "Clean") {
                    parseClean(childNode);
                    continue;
                }
                if (node.getNodeName() == "Paint") {
                    parsePaint(childNode);
                    continue;
                }
                if (node.getNodeName() == "Transport") {
                    parseTransport(childNode);
                    continue;
                }
            }
            visit(childNode, level + 1);
        }
    }

    private void parseID(Node node) {
        if (node.getNodeName() == "RobotID") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _tempSkills.add(_tempSkills.size(), new SkillsSettings());
                _tempSkills.get(_tempSkills.size() - 1)._tempRobotID = Integer.parseInt(eElement.getTextContent());
            }
        }
    }

    private void ParseCurrentID(Node node) {
        if (node.getNodeName() == "CurrentRobotID") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _robotID= Integer.parseInt(eElement.getTextContent());
            }
        }
    }

    private void parseClean(Node node) {
        if (node.getNodeName() == "Duration") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _tempSkills.get(_tempSkills.size() - 1)._clean.setDuration(Integer.parseInt(eElement.getTextContent()));
            }
        }
        if (node.getNodeName() == "Cost") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _tempSkills.get(_tempSkills.size() - 1)._clean.setCost(Integer.parseInt(eElement.getTextContent()));
            }
        }
    }

    private void parsePaint(Node node) {
        if (node.getNodeName() == "Duration") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _tempSkills.get(_tempSkills.size() - 1)._paint.setDuration(Integer.parseInt(eElement.getTextContent()));
            }
        }
        if (node.getNodeName() == "Cost") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _tempSkills.get(_tempSkills.size() - 1)._paint.setCost(Integer.parseInt(eElement.getTextContent()));
            }
        }
    }

    private void parseTransport(Node node) {
        if (node.getNodeName() == "Duration") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _tempSkills.get(_tempSkills.size() - 1)._transport.setDuration(Integer.parseInt(eElement.getTextContent()));
            }
        }
        if (node.getNodeName() == "Cost") {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                _tempSkills.get(_tempSkills.size() - 1)._transport.setCost(Integer.parseInt(eElement.getTextContent()));
            }
        }
    }
}
