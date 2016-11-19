package gseproject.passive.communicator;

import gseproject.passive.pallete.core.SourcePalette;
import jade.lang.acl.ACLMessage;

public class SourcePalleteCommunicator implements IStationCommunicator{
    private SourcePalette sourcePallete;
    public SourcePalleteCommunicator(SourcePalette sourcePallete){
	this.sourcePallete = sourcePallete;
    }
    @Override
    public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ACLMessage handleAction(ACLMessage actionDone) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ACLMessage notifyGrid() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ACLMessage notifyRobot() {
	// TODO Auto-generated method stub
	return null;
    }

}
