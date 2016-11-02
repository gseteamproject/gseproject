package gseproject.experiments.floor;

import gseproject.infrastructure.interaction.IState;

public class FloorState  implements IState {
    private boolean hasRawBlock;
    private boolean isOccupied;
    
    private static final String blockService = "block";
    private static final String workerService = "worker";
    private static final String transporterService = "transporter";
    
    public FloorState(){
	this.hasRawBlock = false;
	this.isOccupied = false;
    }
    
    public String getNextServiceName() throws FloorException {
	if(!hasRawBlock && !isOccupied){
	    return blockService;
	} else if(hasRawBlock && !isOccupied){
	    return workerService;
	} else if(!hasRawBlock && isOccupied){
	    return transporterService;
	} else {
	    throw new FloorException("undefined state");
	}	
    }
    
    public synchronized void updateState(){
	if(!hasRawBlock && !isOccupied){
	    this.hasRawBlock = true;
	    this.isOccupied = false;
	} else if(hasRawBlock && !isOccupied){
	    this.hasRawBlock = false;
	    this.isOccupied = true;
	} else if(!hasRawBlock && isOccupied){
	    this.hasRawBlock = false;
	    this.isOccupied = false;
	}
    }
    
    @Override
    public String toString() {
	return "FloorState [hasBlock=" + hasRawBlock + ", isOccupied=" + isOccupied + "]";
    }
    

    @Override
    public Object Clone() {
	FloorState st = new FloorState();
	st.setHasBlock(this.hasRawBlock);
	st.setOccupied(this.isOccupied);
	return st;
    }

    public boolean hasBlock() {
	return hasRawBlock;
    }

    public void setHasBlock(boolean hasBlock) {
	this.hasRawBlock = hasBlock;
    }

    public boolean isOccupied() {
	return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
	this.isOccupied = isOccupied;
    }

}
