package beans;

public class Point {
    double[] coordinates;
    boolean isCore;
    String label;
    boolean isVisited;

    public Point(double[] coordinates) {
        this.coordinates = coordinates;
        this.isCore = false;
        this.label = ""; // -1: no cluster, 0: not processed yet, other: belong to specified cluster
        this.isVisited = false;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public boolean isBorder() {
        // A border point has fewer than MinPts within Eps, but is in the neighborhood of a core point.
        // That means if it belong to a cluster, but not a core point then it's a border point
        return (!isCore && label.equals(""));
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

    @Override
    public String toString() {
        String coordinate = "";
        for (int i = 0; i < this.coordinates.length; i++) {
            coordinate += this.coordinates[i] + ",";
        }
        return coordinate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
