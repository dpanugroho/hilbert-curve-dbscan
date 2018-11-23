package beans;

public class Point {
    double[] coordinates;
    boolean isBorder;
    boolean isCore;

    public Point(double[] coordinates) {
        this.coordinates = coordinates;
        this.isBorder = false;
        this.isCore = false;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public void setBorder(boolean border) {
        isBorder = border;
    }

    public boolean isCore() {
        return isCore;
    }

    public void setCore(boolean core) {
        isCore = core;
    }
}
