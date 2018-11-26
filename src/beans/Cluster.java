package beans;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<Point> members;
    private String label;

    public Cluster() {
        this.members = new ArrayList<>();
    }

    public void add(Point point) {
        this.members.add(point);
    }

    public List<Point> getMembers() {
        return members;
    }

    public void setMembers(List<Point> members) {
        this.members = members;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
