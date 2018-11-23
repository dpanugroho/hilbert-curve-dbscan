package beans;

import java.util.ArrayList;

public class Cluster {
    private ArrayList<Point> members;

    public Cluster() {
        this.members = new ArrayList<>();
    }

    public void add(Point point){
        this.members.add(point);
    }

    public ArrayList<Point> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Point> members) {
        this.members = members;
    }
}
