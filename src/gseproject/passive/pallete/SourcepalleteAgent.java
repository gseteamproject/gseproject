package gseproject.passive.pallete;

import gseproject.passive.communicator.PalleteCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.pallete.core.Goalpalette;
import gseproject.passive.pallete.core.Sourcepalette;

public class SourcepalleteAgent extends PalleteAgent {
    private static final long serialVersionUID = -7283341637605998716L;

    @Override
    protected void setup() {
	this.pallete = new Sourcepalette();
	this.stationCommunicator = new PalleteCommunicator(this.pallete);
	this.addBehaviour(new ServiceTypeResponder(this, this.robotServiceRequestTemplate, stationCommunicator));
    }

    @Override
    protected void takeDown() {

    }

}
