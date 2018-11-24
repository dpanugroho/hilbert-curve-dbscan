package beans;

public class Point {
    double[] coordinates;
    boolean isCore;
    int label;
    boolean isVisited;

    public Point(double[] coordinates) {
        this.coordinates = coordinates;
        this.isCore = false;
        this.label = 0; // -1: no cluster, 0: not processed yet, other: belong to specified cluster
        this.isVisited = false;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public boolean isBorder() {
        // A border point has fewer than MinPts within Eps, but is in the neighborhood of a core point.
        // That means if it belong to a cluster, but not a core point then it's a border point
        return (!isCore && label > 0);
    }

    public boolean isCore() {
        return isCore;
    }

    public void setCore(boolean core) {
        isCore = core;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
