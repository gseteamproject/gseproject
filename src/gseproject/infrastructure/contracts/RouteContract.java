package gseproject.infrastructure.contracts;

import gseproject.core.Direction;

import java.util.List;

public class RouteContract {

    @Override
    public String toString() {
	return "RouteContract [route=" + route + "]";
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	RouteContract other = (RouteContract) obj;
	if (route == null) {
	    if (other.route != null)
		return false;
	} else if (!route.equals(other.route))
	    return false;
	return true;
    }

    public List<Direction> route;

}
