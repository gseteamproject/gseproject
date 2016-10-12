package gseproject.experements;

import jade.core.Agent;

public class TestAgent extends Agent{

    protected void setup(){
        addBehaviour(new TestBehaviour(this, 10));
    }

}
