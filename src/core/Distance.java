package core;

public class Distance {
    // TODO: Write docs
    public static double getL2Distance(double[] source, double[] target){
        // TODO: Check to make sure source and target has the same dimension

        // Calculate the distance
        float tmp = 0;
        for (int i=0; i<source.length; i++){
            tmp+= Math.pow((source[i] - target[i]),2);
        }
        return Math.sqrt(tmp);
    }
}
