package wim;
import java.util.Arrays;
import java.util.LinkedList;

public class WIM_Craig {

	//function to find the Euclidean Distances between each of the cell phones
    public static double[][] euclideanDistances(double[][] X) {
    	//this array is symmetrical about the diagonal
    	//and so only half is needed to be calculated
        double[][] arr = new double[X.length][X.length];
        double temp;
        for (int i = 0; i < X.length; i++) {
            for (int j = i; j < X.length; j++) {
            	temp=0;
            	//if on the diagonal the distance is 0
            	//as it is the distance to itself
            	if (i != j) {
	            	for (int k = 0; k < 2; k++) {
	            		temp += Math.pow(X[i][k] - X[j][k], 2);
	            	}
	            	temp = Math.sqrt(temp);
	            	//populate the value on either side of the diagonal
	            	// arr[j][i] = arr[i][j] = temp
	            	arr[j][i] = temp;
            	}
                arr[i][j] = temp;
            }
        }
        return arr;
    }
    
    public static void bubbleSort(int [] index, double [] dist, int n)
    {        
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
            	if (dist[index[j]] > dist[index[j + 1]]) {
                    int temp = index[j];
                    index[j] = index[j + 1];
                    index[j + 1] = temp;
                    swapped=true;
                }
            }
            if (swapped == false)
                break;
        }
    }
  
    
    public static void main(String[] args) {
        int [] FREQUENCIES = {110, 111, 112, 113, 114, 115};
        // phone IDs
        char[] cellID = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S'};

        // Easting, Northing, Long, Lat of phones
        // each row corresponds to a column in cellID
        double[][] arr = {
            {536660, 183800, -0.03098, 51.53657},
            {537032, 184006, -0.02554, 51.53833},
            {537109, 183884, -0.02448, 51.53721},
            {537110, 184695, -0.02415, 51.5445},
            {537206, 184685, -0.02277, 51.54439},
            {537248, 185016, -0.02204, 51.54735},
            {537250, 185020, -0.02201, 51.54739},
            {537267, 184783, -0.02185, 51.54525},
            {537269, 183451, -0.02234, 51.53328},
            {537270, 184140, -0.02206, 51.53948},
            {537356, 184927, -0.02052, 51.54653},
            {537380, 184727, -0.02025, 51.54472},
            {537458, 184495, -0.01921, 51.54262},
            {537604, 184134, -0.01725, 51.53934},
            {537720, 184057, -0.01561, 51.53862},
            {537905, 184591, -0.01273, 51.54337},
            {537910, 184441, -0.01272, 51.54202},
            {537953, 184295, -0.01216, 51.5407},
            {538050, 184245, -0.01078, 51.54023}
        };
        int numberOfPhones = cellID.length;
        int numUnassigned = numberOfPhones;
        int numAssigned = 0;
        
        LinkedList<Phone> unassigned = new LinkedList<Phone>();
        LinkedList<Phone> assigned = new LinkedList<Phone>();
        
        for(int i = 0; i<numberOfPhones; i++)
        {
        	unassigned.add(new Phone(cellID[i], arr[i][0], arr[i][1], arr[i][2], arr[i][3],i));
        }
        

        int numFrequencies = FREQUENCIES.length;
        // using easting and northing for positioning
        double[][] X = new double[numberOfPhones][2];

        
        //could have done this without creating X, just pass the length and linkedlist to eucdistance
        // place easting and northing into the array
        for (int i = 0; i < numberOfPhones; i++) {
            X[i][0] = arr[i][0];
            X[i][1] = arr[i][1];
        }

        // euclidean_distances returns a n by n array of the euclidean distance between each point
        // in this case distance is 19 by 19
        double[][] distance = euclideanDistances(X);


        // current lowest euclidean distance for a cluster, the minimum cluster
        double minDist = Double.MAX_VALUE;

        int [] indicesOfCluster = new int[numFrequencies];
        
        
        //find the phone with the smallest total distance of the 5 closest phones 
        int[] index = new int[numberOfPhones];
        
        
        int position = -1;
        for (int i = 0; i < numberOfPhones; i++) {
        	
        	double[] dist = distance[i];
        	for (int j = 0; j < numberOfPhones; j++) {
                index[j] = j;
            }
        	//sort the indices by distance in descending order
        	//Arrays.sort(index, (a, b) -> Double.compare(dist[a], dist[b]));
        	bubbleSort(index, dist, numberOfPhones);
  
        	//find the sum of the 5 closest phones
        	//the loop contains 6 as the distance from the current phone to itself (0) is included
        	
            double tempMinDist = 0;
            for (int k = 0; k < 6; k++) {
                tempMinDist += dist[index[k]];
            }
            
            //if the minimum distance of the current phone is less than the current minimum
            //assign the current phone and its closest neighbours to be the cluster
            if (tempMinDist < minDist) {
                for (int k = 0; k < numFrequencies; k++) {
                	indicesOfCluster[k]=index[k];
                }
                minDist = tempMinDist;
                position = i;
            }
        }
        
	    //assign each of the 6 phones in the cluster a unique frequency
	    for (int j = 0; j < numFrequencies; j++) {
	    	Phone temp = unassigned.get(indicesOfCluster[j]);
	    	temp.frequency=j;
	    	assigned.add(temp);
	    }
	    for (int j = 0; j < numFrequencies; j++) {
	    	unassigned.remove(assigned.get(j));
	    	numUnassigned -= 1;
	        numAssigned += 1;
	    }
	    

	    //loop through all phones
	    while (numUnassigned>0){
	    	//assign minDist to maximum value
	    	//smallest
	        minDist = Double.MAX_VALUE;
	        position = -1;
	        int tempFreq = -1;
	      //loop through all unassigned phones
	        for (int i = 0; i < numUnassigned; i++) {
	        	Phone tempUnassigned = unassigned.get(i);
	        	//find the distance to the closest phone with each of the assigned frequencies
	        	double[] minDistances = new double[numFrequencies];
	        	Arrays.fill(minDistances, Double.MAX_VALUE);
	        	
	            for (int j = 0; j < numAssigned; j++) {
	            	Phone tempAssigned = assigned.get(j);
	            	double dist = distance[tempUnassigned.index][tempAssigned.index];
	            	if (minDistances[tempAssigned.frequency] > dist) {
	            		minDistances[tempAssigned.frequency] = dist;
	                }
	            }
	            //find the frequencies with the maximum distance between the current phone
	            //and the closest phone with that frequency
	            double maxDistance = Double.MIN_VALUE;
	            int maxFreq=-1;
	            
	            for (int j = 0; j < numFrequencies; j++) {
	            	if(minDistances[j]>maxDistance) {
	            		maxDistance = minDistances[j];
	            		maxFreq = j;
	            	}
	            }
	                
                //if the maximum distance is less than previous maximum distance store the maximum distance, frequency and phone.
                if (minDist > maxDistance) {
                    minDist = maxDistance;
                    position = i;
                    tempFreq = maxFreq;
                }
            }
		    
			//assign a frequency to the phone with the smallest distance of the maximum distances
			Phone temp = unassigned.get(position);
			temp.frequency = tempFreq;
			assigned.add(temp);
			unassigned.remove(temp);
			numUnassigned -= 1;
			numAssigned += 1;
	    }
	    
	    //print in order of assignment
	    /*for (int i = 0; i < numAssigned; i++) {
	    	Phone temp = assigned.get(i);
	        System.out.println(temp.ID + ": " + (FREQUENCIES[temp.frequency]));
	    }*/
	    
	    //print in order according to ID
	    int[] freqs = new int[numAssigned];
	    for (int i = 0; i < numAssigned; i++) {
	    	Phone temp = assigned.get(i);
	    	freqs[temp.index]=FREQUENCIES[temp.frequency];
	    }
	    for (int i = 0; i < numAssigned; i++) {
	        System.out.println(cellID[i] + ": " + (freqs[i]));
	    }
	    for (int i = 0; i < numAssigned; i++) {
	        System.out.print((freqs[i])+", ");
	    }
	}
}

