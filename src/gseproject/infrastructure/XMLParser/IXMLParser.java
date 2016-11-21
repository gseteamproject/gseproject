package gseproject.infrastructure.XMLParser;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public interface IXMLParser {
    void xmlDocumentDecode(String strPath) throws ParserConfigurationException, IOException, SAXException;
}
