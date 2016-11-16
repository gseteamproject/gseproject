package gseproject.infrastructure.serialization.robot;

import gseproject.core.Direction;
import gseproject.infrastructure.contracts.RouteContract;
import gseproject.infrastructure.serialization.IReader;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

public class RouteReader implements IReader<RouteContract>{

    public RouteContract read(DataInputStream stream) throws IOException {
        int size = stream.readInt();
        Vector<Direction> route = new Vector();
        for (int i = 0; i < size; ++i) {
            route.add(Direction.valueOf(stream.readUTF()));
        }
        RouteContract contract = new RouteContract();
        contract.route = route;
        return contract;
    }
}
