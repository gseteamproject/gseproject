package gseproject.tests.unitTests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;

import gseproject.core.grid.Grid;
import gseproject.core.grid.objects.GridObject;
import gseproject.infrastructure.contracts.RobotStateContract;
import jade.core.AID;

public class GridTest {
	
	private Grid g;
	Map<AID, GridObject> test;
	AID id = new AID("testingValue");
	
	@Before
	public void initialize() {
		Map<AID, GridObject> a = new HashMap<>();
		g = new Grid(0, 0, null, a);
	}
	
	@Ignore
	@Test
	public void updateTest() {
		RobotStateContract rsc = new RobotStateContract();
		test = g.getGridObjects();
		System.out.println(g.getGridObjects());
		g.update(id, rsc);
		System.out.println(g.getGridObjects());
	}
	
	
}
