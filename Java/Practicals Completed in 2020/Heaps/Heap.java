/**
 * Name:Craig Ransom
 * Student Number:19037237
 */

@SuppressWarnings("unchecked")
public abstract class Heap<T extends Comparable<T>> {

    int capacity;
    Comparable<T> mH[];
    int currentSize;

    public Heap(int capacity) {
        this.capacity = capacity;
        mH = new Comparable[capacity+1]; //Use index positions 1 to capacity 
        currentSize = 0;
    }

    protected int getCapacity(){
        return capacity;
    }

    protected int getCurrentSize(){
        return currentSize;
    }

    public void display() {
        for(int i = 1; i <= currentSize; i++) {
            System.out.print(mH[i] + " ");
        }
        System.out.println("");
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line //////

    protected boolean isEmpty() {
        if(currentSize<1)
            return true;
        return false;
        /* 
        if(mH[0]==null)
            return true;
        return false;
        */
    }

    protected int find(T elem, int pos)
    {
        try
        {
            if(mH[pos*2].compareTo(elem)==0)
                return pos*2;
            if(mH[pos*2+1].compareTo(elem)==0)
                return pos*2+1;
            int i=find(elem,2*pos);
            if(i!=-1)
                return i;
            i=find(elem,2*pos+1);
            return i;
        }catch(Exception ex){return-1;}
    }
    
    public abstract void insert(T elem);
    protected abstract void orderUp();    
    protected abstract void orderDown(int pos);
    

}