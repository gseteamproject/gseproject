package gseproject.passive.communicator;

import gseproject.passive.pallete.core.GoalPalette;
import gseproject.passive.pallete.core.Pallete;
import jade.lang.acl.ACLMessage;

public class GoalPalleteCommunicator implements IStationCommunicator{
    private GoalPalette pallete;
    public GoalPalleteCommunicator(Pallete pallete){
	this.pallete = (GoalPalette) pallete;
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
