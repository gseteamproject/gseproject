package gseproject.core.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gseproject.core.Color;
import gseproject.passive.core.Floor;
import gseproject.passive.core.Palette;
import gseproject.robot.domain.RobotState;

public class Track implements Serializable {
	private static final long serialVersionUID = 282577537054366083L;
	private Color[] track;
	private List<RobotState> states = new ArrayList<>();
	private List<Palette> paletteStates = new ArrayList<>();
	private List<Floor> floorStates = new ArrayList<>();

	public Track(List<Color> fields) {
		Color[] track = new Color[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			track[i] = fields.get(i);
		}
		this.track = track;
	}

	public Track(Color[] track) {
		this.track = track;
	}

	public Color[] getTrack() {
		return track;
	}

	public List<RobotState> getStates() {
		return this.states;
	}

	public void addState(RobotState state) {
		if (state == null) {
			throw new IllegalArgumentException("state null");
		}
		if (states.indexOf(state) > -1) {
			throw new IllegalArgumentException("state already registered");
		}
		this.states.add(state);
	}

	public void addFloorState(Floor floor) {
		if (floor == null) {
			throw new IllegalArgumentException("state null");
		}
		if (floorStates.indexOf(floor) > -1) {
			throw new IllegalArgumentException("floor already registered");
		}
		this.floorStates.add(floor);
	}

	public void addPaletteState(Palette palette) {
		if (palette == null) {
			throw new IllegalArgumentException("state null");
		}
		if (paletteStates.indexOf(palette) > -1) {
			throw new IllegalArgumentException("palette already registered");
		}
		this.paletteStates.add(palette);
	}

}
