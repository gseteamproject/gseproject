package gseproject.passive.palette;

import gseproject.core.Block;
import jade.core.AID;
import jade.core.Agent;

/**
 * Created by lfp on 15.11.2016.
 */
public class SourcePaletteAgent extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = -8730311711198273242L;
    private Sourcepalette palette;
    private AID id = new AID(SourcePaletteAgent.class.getSimpleName(),AID.ISLOCALNAME);


    public SourcePaletteAgent(Sourcepalette palette) {
        this.palette = palette;
    }

    protected void setup() {
        System.out.println("Palette-agent "+id+" registered.");
        Object[] args = getArguments();
        if(args!= null & args.length>0) {
            //do something with arguments
        }
        addBehaviour(new SourcePaletteBehaviour(this));
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
    protected Block getNewBlock() {
        //TODO: make Block to Object
        return new Block();
    }

}
