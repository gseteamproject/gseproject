package gseproject.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import gseproject.tests.unitTests.CleaningFloorTest;
import gseproject.tests.unitTests.FloorCommunicatorTest;
import gseproject.tests.unitTests.GoalPaletteCommunicatorTest;
import gseproject.tests.unitTests.GoalpaletteTest;
import gseproject.tests.unitTests.PaintingFloorTest;
import gseproject.tests.unitTests.SerializableTest;
import gseproject.tests.unitTests.SourcePaletteCommunicatorTest;
import gseproject.tests.unitTests.SourcepaletteTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	CleaningFloorTest.class, PaintingFloorTest.class, SourcepaletteTest.class, GoalpaletteTest.class, SerializableTest.class, GoalPaletteCommunicatorTest.class, 
	SourcePaletteCommunicatorTest.class, FloorCommunicatorTest.class
 })

public class AllTests {   
} 
