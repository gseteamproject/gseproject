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

		

		RouteWriter routeWriter = new RouteWriter();
		RouteReader routeReader = new RouteReader();
		sc.RegisterSerializator(RouteContract.class, routeWriter, routeReader);
	}


	private void isObjectSameAfterSerialization(Object expected) {
		String serialized = sc.Serialize(expected);
		assertEquals(sc.Deserialize(expected.getClass(), serialized), expected);
	}

	@Test
	public void robotStateContractSerializationTest() {
		RobotStateContract robotStateContract = new RobotStateContract();
		robotStateContract.isCarryingBlock = true;
		robotStateContract.position = new Position(1, 5);
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
