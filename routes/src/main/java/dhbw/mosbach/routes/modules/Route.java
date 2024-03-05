package dhbw.mosbach.routes.modules;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Route {
    private final Location start;
    private final Location end;
    private final List<Location> waypoints;

    public Route(Location start, Location end) {
        this.waypoints = new ArrayList<>();
        this.start = start;
        this.end = end;
        waypoints.add(start);
        waypoints.add(end);
    }

    public void addWayPoint(Location point){
        waypoints.remove(waypoints.size()-1);
        waypoints.add(point);
        waypoints.add(end);
    }

    public double[][] getWaypoints() {
        double[][] ret = new double[waypoints.size()][2];
        for (int i = 0; i < ret.length; i++) {
            Location location = waypoints.get(i);
            ret[i][0] = location.getLat();
            ret[i][1] = location.getLng();

        }
        return ret;
    }
}
