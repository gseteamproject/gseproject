package gui;

import gseproject.palleteRobotCommunication.SimpleRobot;
import task3.Palette;

public interface testinterface {
	
	/**
	 * updates the robot position
	 */
	public void updateRobotPosition(int robotId, int position);
	
	/**
	 * updates the robot work
	 * @param robotId
	 * @param job
	 */
	public void updateRobotJob(int robotId, int job);
	
	/**
	 * adds a new Robot to the gui
	 * @return robotId
	 */
	public int addRobot(SimpleRobot simpleRobot);
	
	/**
	 * updates the palette fill 
	 * @param paletteId
	 * @param fill
	 */
	public void updatePalette(int paletteId, int fill);
	
	/**
	 * adds a palette to the gui
	 * @param palette
	 * @return
	 */
	public int addPalette(Palette palette);
}
