package beans;

import java.util.ArrayList;

public class Cluster implements Comparable {
    private ArrayList<Point> members;
    private int label;

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

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return label == cluster.label;
    }

    // TODO: Make sure that none of them have no label
    @Override
    public int compareTo(Object cluster) {
        return this.label - ((Cluster) cluster).getLabel();
    }
}
