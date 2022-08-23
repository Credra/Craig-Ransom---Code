/**
 * Name:Craig Ransom
 * Student Number:19037237
 */

@SuppressWarnings("unchecked")
public class MinHeap<T extends Comparable<T>> extends Heap<T> {

    public MinHeap(int capacity) {
	super(capacity);
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line //////

    @Override
    public void insert(T elem) {
        mH[++currentSize]=elem;
        orderUp();
    }

    public T removeMin() {

        if(this.isEmpty())
           return null;
       T temp = (T) mH[1];
       if(currentSize!=1)
       {
            mH[1]=mH[currentSize];
            currentSize--;
            orderDown(1);
        }
       return temp;
    }

    public void delete(T elem)
    {
        int pos=find(elem,1);
        if(pos==-1)
            return;
        mH[pos]=mH[currentSize];
        mH[currentSize]=null;
        currentSize--;
        orderDown(pos);
    }

    protected void orderUp()
    {
        int parent = currentSize/2,position=currentSize;
        while(position>1&&mH[parent].compareTo((T) mH[position])>0)
        {
            T temp = (T)mH[parent];
            mH[parent]=mH[position];
            mH[position]=temp;
            position=parent;
            parent = position/2;
        }
    }

    protected void orderDown(int pos)
    {
        int left=pos*2,right=pos*2+1;                          
        try
        {
            while((mH[left]!=null&&mH[left].compareTo((T)mH[pos])<0)||(mH[right]!=null&&mH[right].compareTo((T)mH[pos])<0))
            {
                if(mH[right]!=null)
                {                                   
                    if(mH[left].compareTo((T)mH[right])<0)
                    {
                       T temp = (T)mH[left];
                       mH[left]=mH[pos];
                       mH[pos]=temp;
                       pos=left;
                       left=pos*2;
                       right=pos*2+1;
                    }
                    else
                    {
                        T temp = (T)mH[right];
                        mH[right]=mH[pos];
                        mH[pos]=temp;
                        pos=right;
                        left=pos*2;
                        right=pos*2+1;
                    }
                }
                else
                {
                    T temp = (T)mH[left];
                    mH[left]=mH[pos];
                    mH[pos]=temp;
                    pos=left;
                    left=pos*2;
                    right=pos*2+1;
                }
            }
        }catch(Exception ex)
        {
            if(left<this.capacity)
            {
                if(mH[left].compareTo((T)mH[pos])<0)
                {
                   T temp = (T)mH[left];
                   mH[left]=mH[pos];
                   mH[pos]=temp;
                }
            }
        }
    }

}