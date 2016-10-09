package gseproject.robot.interaction;

import gseproject.infrastructure.ICallback;
import gseproject.infrastructure.ICallbackArgumented;

public class VirtualActuator extends AbstractActuator {

    @Override
    public void someAction(Object param, ICallback callback){
        //WORK
        callback.Callback();
    }

    public void trySomeAction(Object param, ICallbackArgumented<Boolean> callback){

        callback.Callback(true);

    }

}