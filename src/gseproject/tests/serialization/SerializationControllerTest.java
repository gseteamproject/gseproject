package gseproject.tests.serialization;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gseproject.core.Direction;
import gseproject.core.ServiceType;
import gseproject.core.grid.Grid;
import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;
import gseproject.core.grid.Grid.GridBuilder;
import gseproject.infrastructure.contracts.GridContract;
import gseproject.infrastructure.contracts.RobotSkillContract;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.contracts.RouteContract;
import gseproject.infrastructure.contracts.ServiceTypeContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.basic.ServiceTypeReader;
import gseproject.infrastructure.serialization.basic.ServiceTypeWriter;
import gseproject.infrastructure.serialization.grid.GridReader;
import gseproject.infrastructure.serialization.grid.GridWriter;
import gseproject.infrastructure.serialization.robot.RobotSkillReader;
import gseproject.infrastructure.serialization.robot.RobotSkillWriter;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import gseproject.infrastructure.serialization.robot.RouteReader;
import gseproject.infrastructure.serialization.robot.RouteWriter;

public class SerializationControllerTest {

    private static SerializationController sc;

    @BeforeClass
    public static void setUp() {
	sc = SerializationController.Instance;
	RobotStateWriter robotStateWriter = new RobotStateWriter();
	RobotStateReader robotStateReader = new RobotStateReader();
	sc.RegisterSerializator(RobotStateContract.class, robotStateWriter, robotStateReader);

	ServiceTypeWriter serviceTypeWriter = new ServiceTypeWriter();
	ServiceTypeReader serviceTypeReader = new ServiceTypeReader();
	sc.RegisterSerializator(ServiceTypeContract.class, serviceTypeWriter, serviceTypeReader);

	RobotSkillWriter robotSkillWriter = new RobotSkillWriter();
	RobotSkillReader robotSkillReader = new RobotSkillReader();
	sc.RegisterSerializator(RobotSkillContract.class, robotSkillWriter, robotSkillReader);

	GridWriter gridWriter = new GridWriter();
	GridReader gridReader = new GridReader();
	sc.RegisterSerializator(GridContract.class, gridWriter, gridReader);

	RouteWriter routeWriter = new RouteWriter();
	RouteReader routeReader = new RouteReader();
	sc.RegisterSerializator(RouteContract.class, routeWriter, routeReader);
    }

    private Grid initGrid() {
	return new GridBuilder(20, 11)
		// source palettes
		.setSpaceType(0, 0, SpaceType.PALETTE_SOURCE).setSpaceType(1, 0, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 1, SpaceType.PALETTE_SOURCE).setSpaceType(1, 1, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 2, SpaceType.PALETTE_SOURCE).setSpaceType(1, 2, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 4, SpaceType.PALETTE_SOURCE).setSpaceType(1, 4, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 5, SpaceType.PALETTE_SOURCE).setSpaceType(1, 5, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 6, SpaceType.PALETTE_SOURCE).setSpaceType(1, 6, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 8, SpaceType.PALETTE_SOURCE).setSpaceType(1, 8, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 9, SpaceType.PALETTE_SOURCE).setSpaceType(1, 9, SpaceType.PALETTE_SOURCE)
		.setSpaceType(0, 10, SpaceType.PALETTE_SOURCE).setSpaceType(1, 10, SpaceType.PALETTE_SOURCE)
		// goal palettes
		.setSpaceType(18, 0, SpaceType.PALETTE_GOAL).setSpaceType(19, 0, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 1, SpaceType.PALETTE_GOAL).setSpaceType(19, 1, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 2, SpaceType.PALETTE_GOAL).setSpaceType(19, 2, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 4, SpaceType.PALETTE_GOAL).setSpaceType(19, 4, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 5, SpaceType.PALETTE_GOAL).setSpaceType(19, 5, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 6, SpaceType.PALETTE_GOAL).setSpaceType(19, 6, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 8, SpaceType.PALETTE_GOAL).setSpaceType(19, 8, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 9, SpaceType.PALETTE_GOAL).setSpaceType(19, 9, SpaceType.PALETTE_GOAL)
		.setSpaceType(18, 10, SpaceType.PALETTE_GOAL).setSpaceType(19, 10, SpaceType.PALETTE_GOAL)
		// main track
		.setSpaceType(3, 0, SpaceType.TRACK).setSpaceType(4, 0, SpaceType.TRACK)
		.setSpaceType(5, 0, SpaceType.TRACK).setSpaceType(6, 0, SpaceType.TRACK)
		.setSpaceType(7, 0, SpaceType.TRACK).setSpaceType(8, 0, SpaceType.TRACK)
		.setSpaceType(9, 0, SpaceType.TRACK).setSpaceType(10, 0, SpaceType.TRACK)
		.setSpaceType(11, 0, SpaceType.TRACK).setSpaceType(12, 0, SpaceType.TRACK)
		.setSpaceType(13, 0, SpaceType.TRACK).setSpaceType(14, 0, SpaceType.TRACK)
		.setSpaceType(15, 0, SpaceType.TRACK).setSpaceType(3, 10, SpaceType.TRACK)
		.setSpaceType(4, 10, SpaceType.TRACK).setSpaceType(5, 10, SpaceType.TRACK)
		.setSpaceType(6, 10, SpaceType.TRACK).setSpaceType(7, 10, SpaceType.TRACK)
		.setSpaceType(8, 10, SpaceType.TRACK).setSpaceType(9, 10, SpaceType.TRACK)
		.setSpaceType(10, 10, SpaceType.TRACK).setSpaceType(11, 10, SpaceType.TRACK)
		.setSpaceType(12, 10, SpaceType.TRACK).setSpaceType(13, 10, SpaceType.TRACK)
		.setSpaceType(14, 10, SpaceType.TRACK).setSpaceType(15, 10, SpaceType.TRACK)
		.setSpaceType(3, 1, SpaceType.TRACK).setSpaceType(3, 2, SpaceType.TRACK)
		.setSpaceType(3, 3, SpaceType.TRACK).setSpaceType(3, 4, SpaceType.TRACK)
		.setSpaceType(3, 5, SpaceType.TRACK).setSpaceType(3, 6, SpaceType.TRACK)
		.setSpaceType(3, 7, SpaceType.TRACK).setSpaceType(3, 8, SpaceType.TRACK)
		.setSpaceType(3, 9, SpaceType.TRACK).setSpaceType(3, 10, SpaceType.TRACK)
		.setSpaceType(16, 0, SpaceType.TRACK).setSpaceType(16, 1, SpaceType.TRACK)
		.setSpaceType(16, 2, SpaceType.TRACK).setSpaceType(16, 3, SpaceType.TRACK)
		.setSpaceType(16, 4, SpaceType.TRACK).setSpaceType(16, 5, SpaceType.TRACK)
		.setSpaceType(16, 6, SpaceType.TRACK).setSpaceType(16, 7, SpaceType.TRACK)
		.setSpaceType(16, 8, SpaceType.TRACK).setSpaceType(16, 9, SpaceType.TRACK)
		.setSpaceType(16, 10, SpaceType.TRACK)
		// cleaning station
		.setSpaceType(7, 0, SpaceType.CLEANING_STATION_TRANSPORT).setSpaceType(7, 1, SpaceType.CLEANING_STATION)
		.setSpaceType(7, 2, SpaceType.CLEANING_STATION_WORKSPACE).setSpaceType(6, 1, SpaceType.TRACK)
		.setSpaceType(6, 2, SpaceType.TRACK).setSpaceType(8, 1, SpaceType.TRACK)
		.setSpaceType(8, 2, SpaceType.TRACK)
		// painting station
		.setSpaceType(12, 0, SpaceType.PAINTING_STATION_TRANSPORT)
		.setSpaceType(12, 1, SpaceType.PAINTING_STATION)
		.setSpaceType(12, 2, SpaceType.PAINTING_STATION_WORKSPACE).setSpaceType(11, 1, SpaceType.TRACK)
		.setSpaceType(11, 2, SpaceType.TRACK).setSpaceType(13, 1, SpaceType.TRACK)
		.setSpaceType(13, 2, SpaceType.TRACK)
		// finally...
		.build();

    }

    private void isObjectSameAfterSerialization(Object expected) {
	String serialized = sc.Serialize(expected);
	assertEquals(sc.Deserialize(expected.getClass(), serialized), expected);
    }

    @Test
    public void robotStateContractSerializationTest() {
	RobotStateContract robotStateContract = new RobotStateContract();
	robotStateContract.isCarryingBlock = true;
	robotStateContract.direction = Direction.EAST;
	robotStateContract.position = new Position(1, 5);
	robotStateContract.goal = new Position(5, 5);
	isObjectSameAfterSerialization(robotStateContract);
    }

    @Test
    public void serviceTypeSerializationTest() {
	ServiceTypeContract serviceTypeContract = new ServiceTypeContract();
	serviceTypeContract.serviceType = ServiceType.GIVE_BLOCK;
	isObjectSameAfterSerialization(serviceTypeContract);
    }

    @Test
    public void robotSkillContractTest() {
	RobotSkillContract robotSkillContract = new RobotSkillContract();
	robotSkillContract.cost = 5;
	robotSkillContract.duration = 7;
	robotSkillContract.id = new UUID(0, 0);
	isObjectSameAfterSerialization(robotSkillContract);
    }

    @Test
    public void gridContractTest() {
	Grid grid = initGrid();
	GridContract gridContract = new GridContract(grid);
	assertEquals(grid, gridContract.getGrid());
	isObjectSameAfterSerialization(gridContract);
    }

    @Test
    public void routeContractTest() {
	RouteContract routeContract = new RouteContract();
	List<Direction> route = new ArrayList<Direction>();
	route.add(Direction.EAST);
	route.add(Direction.NORTH);
	route.add(Direction.SOUTH);
	route.add(Direction.NORTH);
	routeContract.route = route;
	isObjectSameAfterSerialization(routeContract);
    }
}
