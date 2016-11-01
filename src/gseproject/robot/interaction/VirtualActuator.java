package gseproject.robot.interaction;

import gseproject.IGridSpace;
import gseproject.infrastructure.ICallback;
import gseproject.infrastructure.ICallbackArgumented;

public class VirtualActuator extends AbstractActuator {

    /**
    @Override
    public void someAction(Object param, ICallback callback){
        //WORK
        callback.Callback();
    }

    public void trySomeAction(Object param, ICallbackArgumented<Boolean> callback){

        callback.Callback(true);

    }*/
    @Override
    public void move (IGridSpace movePos) {

        return;
    }
    @Override
    public void doWork (IGridSpace workPos) {

        return;
    }
    @Override
    public void drop (IGridSpace dropPos) {

        return;
    }
    @Override
    public void pick (IGridSpace dropPos) {

        return;
    }
}