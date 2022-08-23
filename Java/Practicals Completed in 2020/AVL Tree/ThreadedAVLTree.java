/*
Name and Surname: Craig Ransom
Student Number: 19037237
*/

public class ThreadedAVLTree<T extends Comparable<? super T>>
{
    /*
    TODO: You must complete each of the methods in this class to create your own threaded AVL tree.
    Note that the AVL tree is single-threaded, as described in the textbook and slides.

    You may add any additional methods or data fields that you might need to accomplish your task.
    You may NOT alter the given class name, data fields, or method signatures.
    */

    private ThreadedAVLNode<T> root;       // the root node of the tree
    private boolean added;
    public ThreadedAVLTree()
    {
       root=null;
       added=false;
    }
   
    public ThreadedAVLTree(ThreadedAVLTree<T> other)
    {
        root=null;
        added=false;
        if(other!=null&&other.root!=null)
        {   
            ThreadedAVLNode node = other.root;
            root=new ThreadedAVLNode(node.data);
            addChildren(node,root,false);
        }
    }
    
    private void addChildren(ThreadedAVLNode<T> node, ThreadedAVLNode<T> temp, boolean thread)
    {
        if(!thread)
        {
            try
            {
                temp.left=new ThreadedAVLNode<T>(node.left.data);
                addChildren(node.left,temp.left,false);
            }catch(Exception e){
            }
        }
        try
        {
            if(node.hasThread)
            {
                temp.hasThread=true;
                temp.right=find(root,node.right.right.data);
            }
            else
                temp.right=new ThreadedAVLNode<T>(node.right.data);
            addChildren(node.right,temp.right,node.hasThread);
        }catch(Exception e){
        }
    }
    
   public ThreadedAVLTree<T> clone()
   {
       if(root==null)
           return new ThreadedAVLTree();
      return new ThreadedAVLTree(this);
   }
   
   public ThreadedAVLNode<T> getRoot()
   {
      return root;
   }
   
   public int getHeight()
    {
        if(root==null)
            return 0;
        return childHeight(root);
    }
   
    private int childHeight(ThreadedAVLNode<T> temp)
    {
        if(temp==null)
            return 0;
        int left=0,right=0;
        if(temp.left!=null)
            left=childHeight(temp.left);
        if(temp.right!=null&&!temp.hasThread)
            right=childHeight(temp.right);
        if(left>right)
            return left+1;
        return right+1;
    }
    
   public int getNumberOfNodes()
   {
        int num=0;
        ThreadedAVLNode<T> temp=root;
        boolean thread=false;
        while(temp!=null)
        {
            if(thread)
            {
                thread=temp.hasThread;
                temp=temp.right;
            }
            else
            {
                num++;
                if(temp.left!=null)
                    temp=temp.left;
                else
                {
                    thread=temp.hasThread;
                    temp=temp.right;
                }
            }
            
        }
        return num;
    }

    private void removeThread(ThreadedAVLNode<T> node)
    {
        if(node==null)
            return;
        if(node.hasThread)
        {
            node.hasThread=false;
            node.right=null;
        }
        removeThread(node.right);
        removeThread(node.left);

    }

     private void thread(ThreadedAVLNode<T> node)
     {
        if(node==null)
            return;
        thread(node.left);
        thread(node.getRight());
        ThreadedAVLNode<T> temp=node;
        if(temp.left!=null)
        {
            temp=temp.left;
            while(temp.getRight()!=null)
                temp=temp.right;
            temp.hasThread=true;
            temp.right=node;
        }
     }

    
    public boolean insert(T element)
    {
        removeThread(root);
        added=true;
        root = insert(root, element);
        thread(root);
        return added;
    }
    
    private ThreadedAVLNode<T> insert(ThreadedAVLNode<T> node, T el)
    {
        if (node == null)
            node = new ThreadedAVLNode<T>(el);
        else
        {
            if (el.compareTo(node.data) < 0)//left
            {
                node.left = insert(node.left, el);
                node.balance();
                if(node.balanceFactor>1||node.balanceFactor<-1)
                {
                    if(el.compareTo(node.left.data) < 0)
                        node=rotateRight(node);
                    else if(el.compareTo(node.left.data) > 0)
                    {
                        node.left=rotateLeft(node.left);
                        node=rotateRight(node);
                    }
                }
                node.balance();
            }
            else if(el.compareTo(node.data) > 0)//right
            {
                node.right = insert(node.right, el);
                node.balance();
                if(node.balanceFactor>1||node.balanceFactor<-1)
                {
                    if(el.compareTo(node.right.data) > 0)
                            node=rotateLeft(node);
                    else if(el.compareTo(node.right.data) < 0)
                    {
                        node.right=rotateRight(node.right);
                        node=rotateLeft(node);
                    }
                }
                node.balance();
            }
            else
            {
                added=false;
                return node;
            }
        }
        node.balance();
        return node;
    }
    
    ThreadedAVLNode<T> rotateRight(ThreadedAVLNode<T> b) 
    {
        ThreadedAVLNode<T> a=b.left;
        if(a==null)
            return b;
        b.left=a.right;
        a.right=b;
        return a;
    } 
    
    ThreadedAVLNode<T> rotateLeft(ThreadedAVLNode<T> b) 
    { 
        ThreadedAVLNode<T> c=b.right;
        if(c==null)
            return b;
        b.right=c.left;
        c.left=b;
        return c;
    } 

    public boolean delete(T element)
    {
        added=true;
        removeThread(root);
        root=deleteNode(root,element);
        thread(root);
        return added;
    }
    
    
    ThreadedAVLNode<T> deleteNode(ThreadedAVLNode<T> node, T el)  
    {  
        if (node == null)//node not in tree  
        {
            added=false;
            return node;
        }  
        //find node
        if (el.compareTo(node.data) < 0)  
            node.left = deleteNode(node.left, el);  
        else if (el.compareTo(node.data) > 0)  
            node.right = deleteNode(node.right, el);  
        else
        {
            //delete by copy
            ThreadedAVLNode<T> temp=node.left, prev=node;
            if(temp==null)
                return null;
            while(temp.right!=null)
            {
                prev=temp;
                temp=temp.right;
            }
            if(prev==node)
            {
                temp.right=node.right;
                node=temp;
            }
            else
            {
                prev.right=temp.left;
                temp.right=node.right;
                temp.left=node.left;
                node=temp;     
            }
        } 
        node.balance();
        if(added)
        {
            if(node.balanceFactor>1)
            {
                if(node.right.balanceFactor==0||node.right.balanceFactor==1)
                    node=rotateLeft(node);
                else if(node.right.balanceFactor==-1&&node.right.left!=null&&(node.right.left.balanceFactor==-1||node.right.left.balanceFactor==1))
                {
                    node.right=rotateRight(node.right);
                    node=rotateLeft(node);
                }
            }
            else if(node.balanceFactor<-1)
            {
                if(node.left.balanceFactor==0||node.left.balanceFactor==1)
                    node=rotateRight(node);
                else if(node.left.balanceFactor==-1&&node.left.right!=null&&(node.left.right.balanceFactor==-1||node.left.right.balanceFactor==1))
                {
                    node.left=rotateLeft(node.left);
                    node=rotateRight(node);
                }
            }
        }
        return node;
    }

    public boolean contains(T element)
   {
        if(find(root,element)==null)
            return false;
        return true;
   }
   
   private ThreadedAVLNode find(ThreadedAVLNode<T> node, T ele)
   {
       if(node==null)
           return null;
       if(node.data==ele)
           return node;
       if(node.data.compareTo(ele)<0)
           return find(node.getRight(),ele);
       return find(node.left,ele);
   }
   
   public String inorder()
   {
        ThreadedAVLNode<T> node=root;
        String temp="";
        boolean thread=false;
        while(node!=null)
        {
        if(thread)
        {
            temp+=node.data;
            if(node.right!=null)
                temp+=",";
            thread=node.hasThread;
            node=node.right;

        }
        else
        {
            while(node.left!=null)
            node=node.left;
            temp+=node.data;
            if(node.right!=null)
                temp+=",";
            thread=node.hasThread;
            node=node.right;
        }

        }
         return temp;
      /*
      This method must return a string representation of the elements in the tree visited during an
      inorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first inorder traversal.
      
      Note that there are no spaces in the string, and the elements are comma-separated. Note that
      no comma appears at the end of the string.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      The following string must be returned:
      
      A,B,C,D,E,F
      */
   }
   public String recInorder(ThreadedAVLNode<T> node)
   {
       if(node==null)
           return"";
       String temp=recInorder(node.left);
       temp+=node.data+",";
       temp+=recInorder(node.getRight());
       return temp;
   }
   public String inorderDetailed()
   {
        ThreadedAVLNode<T> node=root;
        String temp="";
        boolean thread=false;
        while(node!=null)
        {
        if(thread)
        {
            temp+=node.data;
            if(node.right!=null)
            {
                if(node.hasThread)
                    temp+="|";
                else
                    temp+=",";
            }
            thread=node.hasThread;
            node=node.right;

        }
        else
        {
            while(node.left!=null)
            node=node.left;
            temp+=node.data;
            if(node.right!=null)
            {
                if(node.hasThread)
                    temp+="|";
                else
                    temp+=",";
            }
            thread=node.hasThread;
            node=node.right;
        }

        }
         return temp;
         
      /*
      This method must return a string representation of the elements in the tree visited during an
      inorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first inorder traversal.
      
      Note that there are no spaces in the string, and the elements are comma-separated.
      Additionally, whenever a thread is followed during the traversal, a pipe symbol should be
      printed instead of a comma. Note that no comma or pipe symbol appears at the end of the
      string. Also note that if multiple threads are followed directly after one another, multiple
      pipe symbols will be printed next to each other.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      In this tree, there is a thread linking the right branch of node A to node B, a thread
      linking the right branch of node B to node C, and a thread linking the right branch of node D
      to node E. The following string must therefore be returned:
      
      A|B|C,D|E,F
      */
   }
   
   public String preorder()
   {
        ThreadedAVLNode<T> node=root;
        String temp="";
        boolean thread=false;
        while(node!=null)
        {
            if(!thread)
            {
                temp+=node.data;
                if(node.left!=null)
                {
                    temp+=",";
                    node=node.left;
                }
                else
                {
                    thread=node.hasThread;
                    node=node.right;
                    if(!thread&&node!=null)
                        temp+=",";
                }     
            }
            else
            {
                thread=node.hasThread;
                if(!thread)
                    temp+=",";
                node=node.right;
            }
        }
        while(temp.length()>1&&temp.charAt(temp.length()-1)==',')
            temp=temp.substring(0, temp.length()-1);
        return temp;
      /*
      This method must return a string representation of the elements in the tree visited during a
      preorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first preorder traversal. See the last paragraph on page 240 of the
      textbook for more detail on this procedure.
      
      Note that there are no spaces in the string, and the elements are comma-separated. Note that
      no comma appears at the end of the string.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      The following string must be returned:
      
      C,B,A,E,D,F
      */
   }
   
   public String preorderDetailed()
   {
        ThreadedAVLNode<T> node=root;
        String temp="";
        boolean thread=false;
        while(node!=null)
        {
            if(!thread)
            {
                temp+=node.data;
                if(node.left!=null)
                {
                    temp+=",";
                    node=node.left;
                }
                else
                {
                    thread=node.hasThread;
                    node=node.right;
                    if(!thread&&node!=null)
                        temp+=",";
                }     
            }
            else
            {
                temp+="|";
                thread=node.hasThread;
                node=node.right;
            }
        }
        while(temp.length()>1&&(temp.charAt(temp.length()-1)==','||temp.charAt(temp.length()-1)=='|'))
            temp=temp.substring(0, temp.length()-1);
        return temp;
      /*
      This method must return a string representation of the elements in the tree visited during a
      preorder traversal. This method must not be  recursive. Instead, the threads must be utilised
      to perform a depth-first preorder traversal. See the last paragraph on page 240 of the
      textbook for more detail on this procedure.
      
      Note that there are no spaces in the string, and the elements are comma-separated.
      Additionally, whenever a thread is followed during the traversal, a pipe symbol should be
      printed instead of a comma. Note that no comma or pipe symbol appears at the end of the
      string. Also note that if multiple threads are followed directly after one another, multiple
      pipe symbols will be printed next to each other.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      In this tree, there is a thread linking the right branch of node A to node B, a thread
      linking the right branch of node B to node C, and a thread linking the right branch of node D
      to node E. The following string must therefore be returned:
      
      C,B,A||E,D|F
      
      Note that two pipe symbols are printed between A and E, because the thread linking the right
      child of node A to node B is followed, B is not printed because it has already been visited,
      and the thread linking the right child of node B to node C is followed.
      */
   }
}
