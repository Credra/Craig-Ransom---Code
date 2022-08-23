@SuppressWarnings("unchecked")
public class BST<T extends Comparable<? super T>> {
    
	protected BSTNode<T> root = null;
	protected static int count = 0;

	public BST() 
	{
    	}
    
	public void clear() 
	{
		root = null;
	}

	public String inorder(BSTNode<T> node) 
	{
		boolean verbose = true;
		if (node != null) {
			String result = "";
			result += inorder(node.left);
			if (verbose) {
				result += node.toString()+"\n";
			} else {
				result += node.element.toString() + " ";
			}
			result += inorder(node.right);
			return result;
		}
		return "";
	}

	////// You may not change any code above this line //////

	////// Implement the functions below this line //////

	public boolean isEmpty() 
	{
		if(root==null)
                    return true;
                return false;
	}

	public BSTNode<T> mirror() 
	{
		BSTNode<T> temp = new BSTNode(root.element, root.right, root.left);
                populateMirror(temp);
                return temp;
	}

        private void populateMirror(BSTNode<T> temp)
        {
            if(temp.left!=null)
            {
                temp.left= new BSTNode(temp.left.element, temp.left.right, temp.left.left);
                populateMirror(temp.left);
            }
            if(temp.right!=null)
            {
                temp.right= new BSTNode(temp.right.element, temp.right.right, temp.right.left);
                populateMirror(temp.right);
            }
        }
        
	public BSTNode<T> clone() 
	{
		BSTNode<T> temp = new BSTNode(root.element, root.left, root.right);
                populateClone(temp);
                return temp;
	}
        
        private void populateClone(BSTNode<T> temp)
        {
            if(temp.left!=null)
            {
                temp.left= new BSTNode(temp.left.element, temp.left.left, temp.left.right);
                populateClone(temp.left);
            }
            if(temp.right!=null)
            {
                temp.right= new BSTNode(temp.right.element, temp.right.left, temp.right.right);
                populateClone(temp.right);
            }
        }

	public void printPreorder() 
	{
		preOrder(root);
                System.out.println();
        }

        private void preOrder(BSTNode<T> temp)
        {
            if(temp!=null)
            {
                System.out.print(temp.element + " ");
                preOrder(temp.left);
                preOrder(temp.right);
            }
        }
        
        public void printPostorder() 
	{
		postOrder(root);
                System.out.println();
	}

        private void postOrder(BSTNode<T> temp)
        {
            if(temp!=null)
            {
                postOrder(temp.left);
                postOrder(temp.right);
                System.out.print(temp.element + " ");
            }
        }
        
	public void insert(T element) 
	{
		if(root==null)
                    root= new BSTNode(element);
                else
                {
                    insertFind(root,element);
                }
	}
        
        private void insertFind(BSTNode<T> temp, T element)
        {
            if(temp.element.compareTo(element)==0)
                        return;
            if(temp.element.compareTo(element)<0)
            {
                if(temp.right==null)
                {
                    temp.right = new BSTNode(element);
                    return;
                }
                insertFind(temp.right, element);
            }
            else
            {
                if(temp.left==null)
                {
                    temp.left= new BSTNode(element);
                    return;
                }
                insertFind(temp.left, element);
            }
        }

	public boolean deleteByMerge(T element) 
	{
            BSTNode<T> temp=root,node,p=root,prev=null;
            while(p!=null&& !p.element.equals(element))
            {
                prev=p;
                if(element.compareTo(p.element)<0)
                    p=p.left;
                else
                    p=p.right;
            }
            node=p;
            if(p!=null && element.equals(p.element))
            {
                if(node.right==null)
                    node=node.left;
                else if(node.left==null)
                    node=node.right;
                else
                {
                    temp = node.left;
                    while(temp.right!=null)
                        temp=temp.right;
                    temp.right=node.right;
                    node=node.left;
                }
                if(p==root)
                    root=node;
                else if(prev.left==p)
                    prev.left=node;
                else
                    prev.right=node;
            }   
            else if(root!=null)
                return false;
            return true;
	}
        
       
        
	public boolean deleteByCopy(T element) 
	{
            BSTNode<T> node, p=root, prev=null;
            while(p!=null && !p.element.equals(element))
            {
                prev=p;
                if(element.compareTo(p.element)<0)
                    p=p.left;
                else
                    p=p.right;
            }
            node=p;
            if(p!=null && p.element.equals(element))
            {
                if(node.right==null)
                    node=node.left;
                else if(node.left==null)
                    node=node.right;
                else
                {
                    BSTNode<T> temp = node.left, previous=node;
                    while(temp.right!=null)
                    {
                        previous = temp;
                        temp=temp.right;
                    }
                    node.element=temp.element;
                    if(previous==node)
                        previous.left=temp.left;
                    else
                        previous.right=temp.left;
                }
                if(p==root)
                    root=node;
                else if(prev.left==p)
                    prev.left=node;
                else
                    prev.right=node;
            }
            else
                return false;
            return true;
	}
        
	public T search(T element) 
	{
		BSTNode<T> temp=root;
                while(temp!=null)
                {
                    if(element.equals(temp.element))
                        return element;
                    if(element.compareTo(temp.element)<0)
                        temp=temp.left;
                    else
                        temp=temp.right;
                }
                return null;
	}

}