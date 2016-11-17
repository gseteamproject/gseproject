package gseproject.core.grid.communicator;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class GridResponder extends AchieveREResponder {

    private static final long serialVersionUID = 1596416556333141502L;

    public GridResponder(Agent a, MessageTemplate mt, DataStore store) {
	super(a, mt, store);
    }

    public GridResponder(Agent a, MessageTemplate mt) {
	super(a, mt);
    }

}
