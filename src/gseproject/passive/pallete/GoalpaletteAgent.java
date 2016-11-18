package gseproject.passive.pallete;

import gseproject.passive.communicator.PalleteCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.pallete.core.Goalpalette;
import gseproject.passive.pallete.core.Sourcepalette;

public class GoalpaletteAgent extends PalleteAgent {
    private static final long serialVersionUID = -1104113813018831544L;

    @Override
    protected void setup() {
	this.pallete = new Goalpalette();
	this.stationCommunicator = new PalleteCommunicator(this.pallete);
	this.addBehaviour(new ServiceTypeResponder(this, this.robotServiceRequestTemplate, this.stationCommunicator));
    }

    @Override
    protected void takeDown() {

    }

}
