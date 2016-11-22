package gseproject.tests.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import gseproject.core.Direction;
import gseproject.core.grid.Grid;
import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;
import gseproject.core.grid.Grid.GridBuilder;
import gseproject.infrastructure.contracts.FloorContract;
import gseproject.infrastructure.contracts.PaletteContract;
import gseproject.passive.core.CleaningFloor;
import gseproject.passive.core.Floor;
import gseproject.passive.core.GoalPalette;
import gseproject.passive.core.PaintingFloor;
import gseproject.passive.core.Palette;
import gseproject.passive.core.SourcePalette;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class SerializableTest {

	private static ACLMessage getDummyMessage() {
		return new ACLMessage(ACLMessage.INFORM);
	}

	@Test
	public void CleaningFloorContractTest() {
		Floor f = new CleaningFloor();
		FloorContract expected = new FloorContract(new Position(1, 2), 2, 4, SpaceType.CLEANING_STATION, f);
		ACLMessage message = getDummyMessage();
		try {
			message.setContentObject(expected);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FloorContract actual = null;
		try {
			actual = (FloorContract) message.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}

	@Test
	public void PaintingFloorContractTest() {
		Floor f = new PaintingFloor();
		FloorContract expected = new FloorContract(new Position(1, 2), 2, 4, SpaceType.PAINTING_STATION, f);
		ACLMessage message = getDummyMessage();
		try {
			message.setContentObject(expected);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FloorContract actual = null;
		try {
			actual = (FloorContract) message.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}

	@Test
	public void SourcePalleteContractTest() {
		Palette palette = new SourcePalette(0, 0);
		PaletteContract expected = new PaletteContract(new Position(1, 2), 2, 4, SpaceType.SOURCE_PALETTE, palette);
		ACLMessage message = getDummyMessage();
		try {
			message.setContentObject(expected);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PaletteContract actual = null;
		try {
			actual = (PaletteContract) message.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}

	@Test
	public void GoalPalleteContractTest() {
		Palette palette = new GoalPalette(0);
		PaletteContract expected = new PaletteContract(new Position(1, 2), 2, 4, SpaceType.GOAL_PALETTE, palette);
		ACLMessage message = getDummyMessage();
		try {
			message.setContentObject(expected);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PaletteContract actual = null;
		try {
			actual = (PaletteContract) message.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		assertEquals(expected, actual);
	}

	@Test
	public void GridContractTest() {
	}
}
