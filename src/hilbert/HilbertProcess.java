package hilbert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class HilbertProcess {
	
	int threshold = 3;
	int bits = 2;
	int dimensions = 4;
	
	int numOfClusterPoint = 0;
	List<Integer> listOfClusterPoint = new ArrayList<Integer>();
	ArrayList<List<Integer>> cluster = new ArrayList<List<Integer>>();
	ArrayList<List<Long>> medoidPointList = new ArrayList<List<Long>>();
	HilbertCurve hc = HilbertCurve.bits(bits).dimensions(dimensions);
	
	public ArrayList<List<Integer>> clusterAdjacentCell(Integer[] numOfCellPoints) {
		
		// Add one cell with 0 point at the end to generate last cluster, eg. 10,13,14,14,14,14,0 => [13,14,14,14,14]
		Integer[] numOfCellPointsPlusEndCell = new Integer[numOfCellPoints.length+1];
		for(int k=0 ; k < numOfCellPoints.length ; k++)
			numOfCellPointsPlusEndCell[k] = numOfCellPoints[k];
		numOfCellPointsPlusEndCell[numOfCellPoints.length] = 0;
		
		/*         Hilbert index |_0_|_1_|_2_|_3_|_4_|_5_|_6_|_7_|_8_|_9_|_10_|_11_|_12_|_13_|_14_|_15_|
		 * number of cell points | 0 | 4 | 2 | 1 | 0 | 1 | 0 | 0 | 0 | 1 | 1  | 0  | 0  | 1  | 4  | 0  |
		 * if threshold= 3 , cluster=> [[1, 1, 1, 1, 2, 2, 3], [13, 14, 14, 14, 14]]                    */
		for(int i=0 ; i < numOfCellPointsPlusEndCell.length ; i++) {
			if(numOfCellPointsPlusEndCell[i] != 0) {
				numOfClusterPoint += numOfCellPointsPlusEndCell[i];
				for(int j=0 ; j<numOfCellPointsPlusEndCell[i] ; j++)
					listOfClusterPoint.add(i);
			}
			else {
				if(numOfClusterPoint >= threshold) {
					try {
						cluster.add(deepCopy(listOfClusterPoint));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(listOfClusterPoint.size()>0) {
					listOfClusterPoint.clear();					
				}
				numOfClusterPoint = 0;
			}	
		}
		       
        return cluster;
    }
	
	// Avoid List modification affects another list after copy values of a list to a new list
	public List<Integer> deepCopy(List<Integer> src) throws IOException, ClassNotFoundException{
		  ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		  ObjectOutputStream out = new ObjectOutputStream(byteOut);
		  out.writeObject(src);

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
