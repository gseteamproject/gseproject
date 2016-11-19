package gseproject.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import gseproject.tests.interaction.robot_floor.FloorTest;
import gseproject.tests.unitTests.CleaningFloorTest;
import gseproject.tests.unitTests.GoalpaletteTest;
import gseproject.tests.unitTests.PaintingFloorTest;
import gseproject.tests.unitTests.SourcepaletteTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	CleaningFloorTest.class, PaintingFloorTest.class, SourcepaletteTest.class, GoalpaletteTest.class
 })

public class AllTests {   
} 
