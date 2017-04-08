package gseproject.robot.interaction;

import java.util.Date;

/**
 * Created by Greg on 08/04/2017.
 */
public class VirtualActuator {

    private double _speed = 0;
    private long _timeStamp = 0;
    private boolean _isMoving = false;

    public void init(int speed) {
        _speed = speed;

        Date date = new Date();
        _timeStamp = date.getTime();
    }

    /* Return TRUE if Ok, and FALSE if fails */
    public double moveForward() {

        if(_isMoving) {
            Date dateNew = new Date();
            Date datePrev = new Date();

            datePrev.setTime(_timeStamp);

            _timeStamp = dateNew.getTime();
            return (dateNew.getTime() - datePrev.getTime()) * _speed;
        }

        return 0;
    }

    public boolean turn() {
        return true;
    }

    public boolean stop() {
        _isMoving = false;
        return true;
    }

    public boolean startEngine() {
        Date date = new Date();
        _timeStamp = date.getTime();
        _isMoving = true;
        return true;
    }

    /* Return BLOCK if Ok, and null if fails */
    public boolean pick() {
        return true;
    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean drop() {
        return true;
    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean doWork() {
        return true;
    }
}
