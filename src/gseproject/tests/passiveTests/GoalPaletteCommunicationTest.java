package gseproject.tests.passiveTests;

import gseproject.passive.palette.GoalPaletteBehaviour;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import test.common.Test;
import test.common.TestException;

/**
 * Created by lfp on 15.11.2016.
 */
public class GoalPaletteCommunicationTest extends Test {
    public Behaviour load(Agent a) throws TestException {
        Behaviour b = new GoalPaletteBehaviour(a);
        return b;
    }
}
