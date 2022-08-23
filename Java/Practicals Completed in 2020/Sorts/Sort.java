// Name: Craig Ransom
// Student number:19037237
import java.util.Arrays;

public class Sort
{

	////// Implement the functions below this line //////	

	/********** HEAP **********/
	public static <T extends Comparable<? super T>> void heapsort(T[] data, boolean debug)
	{
            int length = data.length; 
            for (int i = length / 2 - 1; i >= 0; i--) 
                movedown(data, length, i,debug); 
            
            for (int i=length-1; i>0; i--) 
            { 
                T temp = data[0]; 
                data[0] = data[i]; 
                data[i] = temp; 
                movedown(data, i, 0,debug); 
            }
	}

	private static <T extends Comparable<? super T>> void movedown(T[] data, int first, int last, boolean debug)
	{
            int largest = last;
            int left = 2*last + 1;
            int right = 2*last + 2;

            if (left<first && data[left].compareTo(data[largest])>0) 
                largest = left; 

            if (right<first && data[right].compareTo(data[largest])>0) 
                largest = right; 

            if (last!=largest) 
            { 
                T swap = data[last]; 
                data[last] = data[largest]; 
                data[largest] = swap; 
                movedown(data, first, largest,false);     
            }
            //DO NOT MOVE OR REMOVE!
            if (debug)
                    System.out.println(Arrays.toString(data));
            
	}

	/********** MERGE **********/                                               //int arr[], int l, int m, int r) 
	public static <T extends Comparable<? super T>> void mergesort(T[] data, int first, int last, boolean debug)
	{
            if (first < last) 
            { 
                int mid = (first+last)/2; 
                mergesort(data, first, mid,debug); 
                mergesort(data , mid+1, last,debug); 
                merge(data, first, last,debug); 
            }
	}
     
	private static <T extends Comparable<? super T>> void merge(T[] data, int first, int last, boolean debug)
	{
            int mid = (first+last)/2;
            int leftSize = mid - first + 1; 
            int rightSize = last - mid; 
            Comparable<T> left[] = new Comparable [leftSize]; 
            Comparable<T> right[] = new Comparable [rightSize]; 

            for (int i=0; i<leftSize; ++i) 
                left[i] = (Comparable<T>) data[first + i]; 

            for (int j=0; j<rightSize; ++j) 
                right[j] = (Comparable<T>) data[mid + 1+ j]; 

            int l = 0, r = 0, i = first; 
            while (l < leftSize && r < rightSize) 
            { 
                if (left[l].compareTo((T) right[r])<=0) 
                    data[i] = (T) left[l++]; 
                else
                    data[i] = (T) right[r++]; 
                i++; 
            } 

            while (l < leftSize) 
                data[i++] = (T) left[l++]; 

            while (r < rightSize) 
                data[i++] = (T) right[r++]; 
            
            //DO NOT MOVE OR REMOVE!
            if (debug)
                    System.out.println(Arrays.toString(data));
	}
}