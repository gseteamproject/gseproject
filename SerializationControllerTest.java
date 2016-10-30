package gseproject.serialization.robot.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gseproject.infrastructure.serialization.SerializationController;

public class SerializationControllerTest {

	SerializationController sc;
	RobotStateWriter writer = new RobotStateWriter();
	RobotStateReader reader = new RobotStateReader();
	

	@Before
	public void setUp() {
		sc = SerializationController.Instance;
		sc.RegisterSerializator(RobotStateContract.class, writer, reader);
	}

	@Test
	public void serializeDeserialize() {
		RobotStateContract rtcA = new RobotStateContract();
		rtcA.isCarryingBlock = true;
		rtcA.position = 65f;
		rtcA.by = 127;
		rtcA.sh = 1000;
		rtcA.intt = 13999;
		rtcA.lng = 1;
		rtcA.dbl = 10.001;
		rtcA.ch = 'G';
		rtcA.str1 = "Hello";
		String str = sc.Serialize(rtcA);			

		RobotStateContract rtcB = sc.Deserialize(RobotStateContract.class, str);

		Assert.assertEquals(rtcA.isCarryingBlock, rtcB.isCarryingBlock);
		Assert.assertEquals(rtcA.position, rtcB.position, 0.05);
		Assert.assertEquals(rtcA.by, rtcB.by);
		Assert.assertEquals(rtcA.sh, rtcB.sh);
		Assert.assertEquals(rtcA.intt, rtcB.intt);
		Assert.assertEquals(rtcA.lng, rtcB.lng);
		Assert.assertEquals(rtcA.dbl, rtcB.dbl, 0.05);
		Assert.assertEquals(rtcA.ch, rtcB.ch);
		Assert.assertEquals(rtcA.str1, rtcB.str1);
	}
}
