package gseproject.robot.domain;

import gseproject.core.Block;
import gseproject.core.interaction.IState;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class RobotState implements IState {

	public boolean isCarryingBlock;
	public int _position;
	public Block block;

	public RobotState(){
		isCarryingBlock = false;
		_position = 0;
		block = new Block();
	}

	@Override
	public IState Clone() {

		RobotState state = new RobotState();

		state.isCarryingBlock = isCarryingBlock;
		state._position = _position;
		state.block = block;

		return state;
	}

	@Override
	public String toString() {
		return "RobotState [isCarryingBlock=" + isCarryingBlock + ", position=" + _position + ", block=" + block + "]";
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
				if (node.getNodeName() == "Position") {
					parsePosition(childNode);
					continue;
				}
				if(node.getNodeName() == "Block"){
					parseBlockState(childNode);
					continue;
				}
			}
			visit(childNode, level + 1);
		}
	}

	private void parseBlockState(Node node) {
		if (node.getNodeName() == "Status") {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				String strContent = eElement.getTextContent();
				if(strContent.equals("FALSE")){
					block = null;
				}
				if(strContent.equals("CLEANED")){
					block = new Block();
					block.Status = Block.possibleBlockStatus.CLEANED;
				}
				if(strContent.equals("DIRTY")){
					block = new Block();
					block.Status = Block.possibleBlockStatus.DIRTY;
				}
				if(strContent.equals("PAINTED")){
					block = new Block();
					block.Status = Block.possibleBlockStatus.PAINTED;
				}
			}
		}
	}

	public void incrementPosition() {
		/* TODO: REMOVE HARDCODE */
		if(_position == 15)
		{
			_position = 0;
			return;
		}
		++_position;
	}

	private void parsePosition(Node node) {
		if (node.getNodeName() == "CoordX") {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				//_position.setX(Integer.parseInt(eElement.getTextContent()));
			}
		}
		if (node.getNodeName() == "CoordY") {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				//_position.setY(Integer.parseInt(eElement.getTextContent()));
			}
		}
	}

}
