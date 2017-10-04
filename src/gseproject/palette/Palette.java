package gseproject.palette;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//info: uml https://drive.google.com/open?id=0Bx2FpF7SnEEdcWtYeTlYemVscW8



// todo: a interface for agentcommunication with infos like position,
// networkadress uid stuff like this? //uml:
// https://drive.google.com/open?id=0Bx2FpF7SnEEdaVRkSWhzWDlVQ28
// todo: a interface for add and remove would be nice and just parse this class
// to a communicationlayer as a callback
public class Palette {
	private final Lock lock = new ReentrantLock();

	private int _capacity = 500;
	private List<IBox> _box = new ArrayList<IBox>();

	public int capacity() {
		return this._capacity;
	}
	// public int responseAmountOfBlocks() {return this._capacity;} //info: this
	// is double by the uml

	public int currentBlocks() {
		return this._box.size();
	}

	public boolean hasBox() {
		return this._box.size() > 0;
	}

	/*
	 * @return boolean return false if capacity is reached
	 */
	public boolean addBox(IBox box) {
		if (this._capacity <= _box.size())
			return false;

		// check for a racecondition
		while (!this.lock.tryLock()) {
			try {
				//maybe someone add a box befor the lock could locked ...
				if (this._capacity <= _box.size())
					return false;
				
				this._box.add(box);
			} finally {
				this.lock.unlock();
			}
		}
		return true;
	}

	/*
	 * @return IBox return a box or null on an error like no box left
	 */
	public IBox getBox() {
		if(!this.hasBox())
			return null;
		
		while (!this.lock.tryLock()) {
			try {
				if(!this.hasBox())
					return null;
				
				IBox returnBox = this._box.get(_box.size() - 1);
				this._box.remove(returnBox);
				return returnBox;
			} finally {
				this.lock.unlock();
			}
		}
		return null;
	}
}
