/**
 * Name:Craig Ransom
 * Student Number:19037237
 */

public class SplayTree<T extends Comparable<T>> {

	protected enum SplayType {
		SPLAY,
		SEMISPLAY,
		NONE
	}	

	protected Node<T> root = null;
	
	/**
	 * Prints out all the elements in the tree
	 * @param verbose
	 *			If false, the method simply prints out the element of each node in the tree
	 *			If true, then the output provides additional detail about each of the nodes.
	 */
	public void printTree(boolean verbose) {
		String result;
		result = preorder(root, verbose);
		System.out.println(result);
	}
	
	protected String preorder(Node<T> node, boolean verbose) {
		if (node != null) {
			String result = "";
			if (verbose) {
				result += node.toString()+"\n";
			} else {
				result += node.elem.toString() + " ";
			}
			result += preorder(node.left, verbose);
			result += preorder(node.right, verbose);
			return result;
		}
		return "";
	}
	
	////// You may not change any code above this line //////

	////// Implement the functions below this line //////
	
	/**
	* Inserts the given element into the tree, but only if it is not already in the tree.
	* @param elem 
	* 		 	The element to be inserted into the tree
	* @return 
	*			Returns true if the element was successfully inserted into the tree. 
	*			Returns false if elem is already in the tree and no insertion took place.
	*
	*/
	public boolean insert(T elem) {
            if(root==null)
            {
                root=new Node(elem);
                return true;
            }
            Node <T> temp=root,prev=temp;
            while(temp!=null)
            {
                prev=temp;
                if(elem.compareTo(temp.elem)==0)
                    return false;
                if(elem.compareTo(temp.elem)<0)
                    temp=temp.left;
                else
                    temp=temp.right;
            }
            if(elem.compareTo(prev.elem)<0)
                prev.left=new Node <T>(elem);
            else
                prev.right=new Node <T>(elem);
                
            
            return true;
	}
	
	/**
	* Checks whether a given element is already in the tree.
	* @param elem 
	* 		 	The element being searched for in the tree
	* @return 
	*			Returns true if the element is already in the tree
	*			Returns false if elem is not in the tree
	*
	*/
	public boolean contains(T elem) {
            Node <T> temp=root;
            while(temp!=null)
            {
                if(elem.equals(temp.elem))
                    return true;
                else if(elem.compareTo(temp.elem)>0)
                    temp=temp.right;
                else
                    temp=temp.left;
            }
            return false;
	}
	
	/**
	 * Accesses the node containing elem. 
	 * If no such node exists, the node should be inserted into the tree.
	 * If the element is already in the tree, the tree should either be semi-splayed so that 
	 * the accessed node moves up and the parent of that node becomes the new root or be splayed 
	 * so that the accessed node becomes the new root.
	 * @param elem
	 *			The element being accessed
	 * @param type
	 *			The adjustment type (splay or semi-splay or none)
	 */
	public void access(T elem, SplayType type) {

		if(contains(elem))
                {
                    switch(type)
                    {
                        case SPLAY:
                            splay(getParent(elem));
                            break;
                        case SEMISPLAY:
                            semisplay(getNode(elem));
                            break;
                        case NONE: 
                            break;
                    }
                }
                else
                    insert(elem);
	}
	
        private Node<T> getParent(T elem)
        {
            Node <T> temp=root,prev=root;
            while(temp!=null)
            {
                prev=temp;
                if(elem.equals(temp.elem))
                    return prev;
                else if(elem.compareTo(temp.elem)>0)
                    temp=temp.right;
                else
                    temp=temp.left;
            }
            return null;
        }
        
        private Node<T> getNode(T elem)
        {
            Node <T> temp=root;
            while(temp!=null)
            {
                if(elem.equals(temp.elem))
                    return temp;
                else if(elem.compareTo(temp.elem)>0)
                    temp=temp.right;
                else
                    temp=temp.left;
            }
            return null;
        }
	/**
	 * Semi-splays the tree using the parent-to-root strategy
	 * @param node
	 *			The node the parent of which will be the new root
	 */
	protected void semisplay(Node<T> node) {
		splay(node);
		
	}

	/**
	 * Splays the tree using the node-to-root strategy
	 * @param node
	 *			The node which will be the new root
	 */
	protected void splay(Node<T> node) {
            if(node==null||node==root)
                return;
            
            Node <T> temp = root;
            while(temp!=null&&(temp.left!=node||temp.right!=node))
            {
                if(temp.elem.compareTo(node.elem)>0)
                    temp=temp.left;
                else
                    temp=temp.right;
            }
            if(temp==null)
                return;
            if(temp.elem.compareTo(node.elem)>0)
                swapRight(temp,node);
            else
                swapLeft(temp,node);
            splay(node);
	}

        private void swapRight(Node<T> parent, Node<T> child)
        {
            parent.left=child.right;
            child.right=parent;
        }
	
        private void swapLeft(Node<T> parent, Node<T> child)
        {
            parent.right=child.left;
            child.left=parent;
        }
	
}