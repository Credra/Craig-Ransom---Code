/*
Name and Surname: Craig Ransom
Student Number: 19037237
*/

public class ThreadedAVLNode<T extends Comparable<? super T>>
{
    /*
    TODO: You must implement a node class which would be appropriate to use with your trees.

    You may add methods and data fields. You may NOT alter the given class name or data fields.
    */

    protected T data;                      // stored data
    protected int balanceFactor;           // balance factor (follow the definition in the textbook and slides exactly)
    protected ThreadedAVLNode<T> left;     // right child
    protected ThreadedAVLNode<T> right;    // left child
    protected boolean hasThread;           // flag indicating whether the right pointer is a thread
    public ThreadedAVLNode()
    {
        data=null;
        balanceFactor=0;
        left=null;
        right=null;
        hasThread=false;
    }
   
    public ThreadedAVLNode(T d)
    {
        data=d;
        balanceFactor=0;
        left=null;
        right=null;
        hasThread=false;
    }
    
    public void balance()
    {
        balanceFactor=childHeight(right)-childHeight(left);
    }
   
    private int childHeight(ThreadedAVLNode<T> temp)
    {
        if(temp==null)
            return 0;
        int left=0,right=0;
        if(temp.left!=null)
            left=childHeight(temp.left);
        if(temp.right!=null)
            right=childHeight(temp.right);
        if(left>right)
            return left+1;
        return right+1;
    }
    
    public ThreadedAVLNode<T> getRight()
    {
        if(hasThread)
            return null;
        return right;
    }
}