package gseproject.robot.controller;

import java.awt.Color;

import gseproject.core.Block;
import gseproject.core.grid.GridSpace;
import gseproject.core.grid.Position;
import gseproject.robot.interaction.RealActuator;
import gseproject.robot.skills.SkillsSettings;

public class DummyController implements IController {

    private Position _currentPosition;
    private SkillsSettings _skillsSettings;

    /** Actuators */
    private RealActuator _realActuator;

    /** Sensors */

    /* Default Constructor */
    public DummyController(SkillsSettings skillSettings){
    	_skillsSettings = skillSettings;
        _realActuator = new RealActuator();
    }
    /* Move Forward */
    public void move(Color color) {
    	if(color.equals(Color.BLACK)){
    		System.out.println("moving to sourcepalette");
    	}
    	System.out.println(_skillsSettings._transport.getCost() + "," +  _skillsSettings._transport.getDuration());
        return;
    }
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
        return true;
    }

    /* Put block on Floor/Pallete */
    public boolean drop() {
        return true;
    }

    /* Do Clean or Paint Work */
    public boolean doWork() {
        return true;
    }

    /* Return Current Position of Robot */
    public Position getCurrentPosition(){
        return _currentPosition;
    }
}
