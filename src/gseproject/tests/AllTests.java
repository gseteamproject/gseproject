package gseproject.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import gseproject.tests.unitTests.GoalpaletteTest;
import gseproject.tests.unitTests.SourcepaletteTest;
import gseproject.tests.unitTests.StationCommunicatorSourcepaletteTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	StationCommunicatorSourcepaletteTest.class, GoalpaletteTest.class, SourcepaletteTest.class
 })

public class AllTests {   
} 
