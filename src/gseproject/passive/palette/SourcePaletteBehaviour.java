package gseproject.passive.palette;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

/**
 * Created by lfp on 15.11.2016.
 */
public class SourcePaletteBehaviour  extends Behaviour {

    /**
     *
     */
    private static final long serialVersionUID = 4061251203992216021L;
    private SourcePaletteAgent paletteAgent;

    public SourcePaletteBehaviour(Agent agent) {
        super.setAgent(agent);
    }

    @Override
    public void action() {
        paletteAgent = (SourcePaletteAgent) getAgent();
        ACLMessage msg = myAgent.receive();
        if(msg!=null) {
            String title = msg.getContent();
            ACLMessage reply = msg.createReply();
            if(title.equals(ServiceType.I_OCCUPY)) {
                if(paletteAgent.getIfOccupied()) {
                    try {
                        reply.setContentObject(ServiceType.IS_OCCUPIED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    paletteAgent.iOccupy();
                }
            } else if (title.equals(ServiceType.I_LEAVE)) {
                paletteAgent.iLeave();
            }  else if (title.equals(ServiceType.GIVE_BLOCK_DIRTY)) {
                Block b = paletteAgent.getNewBlock();
                try {
                    reply.setContentObject(ServiceType.GIVE_BLOCK_DIRTY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                paletteAgent.send(reply);
            }
        }
    }

    /**
     *
     * @param block
     * @return the status (0: dirty, 1: clean 2: painted
     * @throws BlockNullException
     */
    private byte checkIfBlockFinished(Block block) throws BlockNullException {
        if (block == null) {
            BlockNullException e = new BlockNullException();
            throw e;
        }
        if (block.Status == Block.possibleBlockStatus.PAINTED) {
            return 2;
        } else if (block.Status == Block.possibleBlockStatus.CLEANED) {
            return 1;
        } else {
            return 0;
        }
    }

    public class BlockNullException extends Exception {
        /**
         * This Exception is thrown when a Block is Null
         */
        private static final String message = "The Block value is null";
        private static final long serialVersionUID = 1L;

        // public BlockNullException() { super(); }
        public BlockNullException() {
            super(message);
        }

        public BlockNullException(Throwable cause) {
            super(message, cause);
        }
        // public BlockNullException(Throwable cause) { super(cause); }
    }

    @Override
    public boolean done() {
        // TODO Auto-generated method stub
        return false;
    }

}