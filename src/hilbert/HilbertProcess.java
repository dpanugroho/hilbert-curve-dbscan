package hilbert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static util.ListUtil.deepCopy;

public class HilbertProcess {

    private int threshold;
    private int bits;
    private int dimensions;
    private HilbertCurve hc;

    public HilbertProcess(int bits, int dimensions, int threshold) {
        this.bits = bits;
        this.dimensions = dimensions;
        this.threshold = threshold;
        hc = HilbertCurve.bits(bits).dimensions(dimensions);
    }

		  ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		  ObjectInputStream in =new ObjectInputStream(byteIn);
		  @SuppressWarnings("unchecked")
		  List<Integer> dest = (List<Integer>)in.readObject();
		  
		  return dest;
	}
	
	public ArrayList<List<Long>> getMedoidPointList(List<List<Integer>> cluster){		
		for (int i=0 ; i < cluster.size() ; i++) {
			//num of points in the cluster is odd
			if(cluster.get(i).size() % 2 != 0) {
				//System.out.println((long)cluster.get(i).get(cluster.get(i).size()/2));
				List<Long> point = new ArrayList<Long>();
				for(int j=0; j<dimensions; j++){
					point.add(hc.point((long)cluster.get(i).get(cluster.get(i).size()/2))[j]);
				}
				medoidPointList.add(point);
			}
			//num of points in the cluster is even
			else {
				//System.out.println((long)cluster.get(i).get(cluster.get(i).size()/2-1));
				List<Long> point = new ArrayList<Long>();
				for(int j=0; j<dimensions; j++){
					point.add(hc.point((long)cluster.get(i).get(cluster.get(i).size()/2-1))[j]);
				}
				medoidPointList.add(point);
			}
		}
		
		return medoidPointList;
	}
	public static BigInteger mapCoordinatesToIndex(long[] coordinates, int bits, int dimensions) {
		HilbertCurve c = HilbertCurve.bits(bits).dimensions(dimensions);
		BigInteger index = c.index(coordinates);
		return index;
	}
	public static int[] createHilbertDistanceList(BigInteger index, int[] hilbertDistance) {
		//System.out.println(index.intValue());
		//System.out.println(hilbertDistance.length);
		hilbertDistance[index.intValue()] +=1;
		return hilbertDistance;
	}


}
