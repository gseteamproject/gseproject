package gseproject.passive.palette;

import java.util.ArrayList;
import java.util.List;

import gseproject.Block;
import gseproject.passive.IStationLanguage;

//info: abstract coz this a normal palette, no goal or sourcepalette
public abstract class Palette implements IStationLanguage {
	private List<Block> _blocks = new ArrayList<Block>();
	private Boolean _occupied = false;
	
	@Override
	public boolean hasBlock() {
		//info: palletes get refilled automatically
		if(_blocks.size() == 0){
			_blocks.add(new Block());
		}
		
		return _blocks.size() != 0;
	}

	@Override
	public boolean isOccupied() {
		return _occupied;
	}

	@Override
	public Boolean giveBlock(Block block) {
		_blocks.add(block);
		return true;
	}

	@Override
	/* 
	 *@return return true if the palette could been set to occuppied by this robot, false if a racecondition takes place
	*/
	public Boolean iOccuppy() {
		if(!_occupied){
			_occupied = true;
			return true;
		}
		return false;
	}

	@Override
	public void iLeave() {
		_occupied = false;
	}

	@Override
	/* 
	 *@return return null if the palette is empty
	*/
	public Block takeBlock() {
		if(hasBlock()){
			return _blocks.remove(0);
		}
		return null;
	}

}
