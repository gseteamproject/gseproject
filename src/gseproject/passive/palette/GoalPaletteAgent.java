package gseproject.passive.palette;

import jade.core.AID;
import jade.core.Agent;

/**
 * Created by lfp on 15.11.2016.
 */
public class GoalPaletteAgent extends Agent {

    /**
     * This is the agent to communicate with the goalpalette
     */
    private static final long serialVersionUID = -2585006384176589513L;
    private Goalpalette palette;
    private AID id = new AID(GoalPaletteAgent.class.getSimpleName(),AID.ISLOCALNAME);


    public GoalPaletteAgent(Goalpalette palette) {
        this.palette = palette;
    }

    protected void setup() {
        System.out.println("Palette-agent "+id+" registered.");
        Object[] args = getArguments();
        if(args!= null & args.length>0) {
            //do something with arguments
        }
        addBehaviour(new GoalPaletteBehaviour(this));
    }


    protected void takeDown() {
        System.out.println("Palette Agent "+id+" terminating.");
    }

    protected boolean getIfOccupied() {
        return palette.isOccupied();
    }
    protected void iOccupy() {
        palette.iOccuppy();
    }
    protected void iLeave() {
        palette.iLeave();
    }

}
