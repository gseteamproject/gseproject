package gseproject.robot.controller;

import gseproject.core.Direction;
import gseproject.grid.Grid;
import gseproject.grid.Position;
import gseproject.grid.objects.GoalPalette;
import gseproject.grid.objects.SourcePalette;
import gseproject.robot.interaction.VirtualActuator;
import gseproject.robot.skills.SkillsSettings;
import jade.core.AID;

public class DummyController implements IController {

    private Grid _grid;
    private Position _currentPosition;
    private SkillsSettings _skillsSettings;
//    private boolean _hasBlock = false;

    /** Actuators */
//    private RealActuator _realActuator;
    private VirtualActuator _virtActuator;

    /** Sensors */

    /* Default Constructor */
    public DummyController(SkillsSettings skillSettings){
        _grid = new Grid.GridBuilder(20, 11)
                .addGridObject(new AID("sourcePaletteOne", true), new SourcePalette(new Position(0, 0), 2, 3))
                .addGridObject(new AID("sourcePaletteTwo", true), new SourcePalette(new Position(0, 4), 2, 3))
                .addGridObject(new AID("sourcePaletteThree", true), new SourcePalette(new Position(0, 8), 2, 3))
                .addGridObject(new AID("goalPaletteOne", true), new GoalPalette(new Position(18, 0), 2, 3))
                .addGridObject(new AID("goalPaletteTwo", true), new GoalPalette(new Position(18, 4), 2, 3))
                .addGridObject(new AID("goalPaletteThree", true), new GoalPalette(new Position(18, 8), 2, 3))
                .addTrack(new Position(3, 0), Direction.EAST, 13)
                .addTrack(new Position(16, 0), Direction.SOUTH, 11)
                .addTrack(new Position(16, 10), Direction.WEST, 13)
                .addTrack(new Position(3, 10), Direction.NORTH, 11)
                .build();
    	_skillsSettings = skillSettings;
//        _realActuator = new RealActuator();
        _virtActuator = new VirtualActuator();
        _virtActuator.init(_skillsSettings._transport.getDuration()/1);
    }

    /* Move Forward */
    public void move() {
    	System.out.println("Robot called move: " + _skillsSettings._transport.getCost() + "," +  _skillsSettings._transport.getDuration());
        _currentPosition = _grid.GetPosition(_virtActuator.moveForward(), _currentPosition);

        return;
    }

    /* Start Engine */
    public boolean StartEngine() { return _virtActuator.startEngine(); }

    /* Stop Engine */
    public boolean StopEngine() { return _virtActuator.stop(); }

    /*
    public boolean move(color goal) {
    	while(sensor.getColor != goal){
    		if(sensor.getColor == moveForward){
    			actuator.forward();
    			currentPosition.getY()++;
    		} else {
    			motor.right;
    			currentPosition.getX()++;
    		}
    	}
        return true;
    }
    */

    /* Pick Block */
    public boolean pick() {
        if(_virtActuator.pick()){
//            _hasBlock = true;
            return true;
        };
        return false;
    }

    /* Put block on Floor/Pallete */
    public boolean drop() {
        if(_virtActuator.drop()){
//            _hasBlock = false;
            return true;
        };
        return false;
    }

    /* Do Clean or Paint Work */
    public boolean doWork() {
        _virtActuator.doWork();
        return true;
    }

    /* Return Current Position of Robot */
    public Position getCurrentPosition(){
        return _currentPosition;
    }
}
