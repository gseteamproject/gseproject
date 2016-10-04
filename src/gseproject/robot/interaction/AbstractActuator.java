package gseproject.robot.interaction;

import gseproject.infrastructure.ICallback;
import gseproject.interaction.IActuator;

public abstract class Actuator {

    public abstract class AbstractActuator implements IActuator{

        @Override
        public void init(){

        }

        public abstract void someAction(Object param, ICallback callback);
    }

    protected class RealActuator extends AbstractActuator{

        @Override
        public void someAction(Object param, ICallback callback){
            //body
        }

    }
}
