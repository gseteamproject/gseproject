package gseproject.palette;

import java.util.ArrayList;
import java.util.List;

//info: uml https://drive.google.com/open?id=0Bx2FpF7SnEEdcWtYeTlYemVscW8

//todo: this is a dummy coz there is no box till now / 23.10.2016
public interface IBox{
	
}

//todo: a interface for agentcommunication with infos like position, networkadress uid stuff like this? //uml: https://drive.google.com/open?id=0Bx2FpF7SnEEdaVRkSWhzWDlVQ28
//todo: a interface for add and remove would be nice and just parse this class to a communicationlayer as a callback
public class Palette {
	private int _capacity = 500;
	private List<IBox> _box = new ArrayList<IBox>();
	
	public int capacity() {return this._capacity;}
	//public int responseAmountOfBlocks() {return this._capacity;} //info: this is double by the uml
	
	public int currentBlocks(){ return this._box.size(); }
	
	public boolean hasBox(){ return this._box.size() > 0; }
	
	public boolean addBox(IBox box){
		if(this._capacity <=  _box.size())
			return false;
		
		this._box.add(box);
		return true;
	}
	
	public IBox getBox(){
		IBox returnBox = this._box.get(_box.size() -1);
		this._box.remove(returnBox);
		return returnBox;
	}
}
