@SuppressWarnings("unchecked")
class BTreeNode<T extends Comparable<T>> {
	boolean leaf;
	int keyTally;
	Comparable<T> keys[];
	BTreeNode<T> references[];
	int m;
	static int level = 0;

	// Constructor for BTreeNode class
	public BTreeNode(int order, boolean leaf1)
	{
    		// Copy the given order and leaf property
		m = order;
    		leaf = leaf1;
 
    		// Allocate memory for maximum number of possible keys
    		// and child pointers
    		keys =  new Comparable[2*m-1];
    		references = new BTreeNode[2*m];
 
    		// Initialize the number of keys as 0
    		keyTally = 0;
	}

	// Function to print all nodes in a subtree rooted with this node
	public void print()
	{
		level++;
		if (this != null) {
			System.out.print("Level " + level + " ");
			this.printKeys();
			System.out.println();

			// If this node is not a leaf, then 
        		// print all the subtrees rooted with this node.
        		if (!this.leaf)
			{	
				for (int j = 0; j < 2*m; j++)
    				{
        				if (this.references[j] != null)
						this.references[j].print();
    				}
			}
		}
		level--;
	}

	// A utility function to print all the keys in this node
	private void printKeys()
	{
		System.out.print("[");
    		for (int i = 0; i < this.keyTally; i++)
    		{
        		System.out.print(" " + this.keys[i]);
    		}
 		System.out.print("]");
	}

	// A utility function to give a string representation of this node
	public String toString() {
		String out = "[";
		for (int i = 1; i <= (this.keyTally-1); i++)
			out += keys[i-1] + ",";
		out += keys[keyTally-1] + "] ";
		return out;
	}

	////// You may not change any code above this line //////
	////// Implement the functions below this line //////

        public BTreeNode<T> insert(T key)
	{
            if(keyTally==2*m-1)
            {
                BTreeNode <T> node = new BTreeNode(m, false); 
                node.references[0]=this;
                node.spilt(this,0);
                
                int i=0;
                if(node.keys[0].compareTo(key)<0)
                    i++;
                node.references[i].add(key);
                return node;
            }
            add(key);
            return this;
        }
        
        private void add(T key)
        {
            int i=keyTally-1;
            if(leaf)
            {
                while(i>=0 && keys[i].compareTo(key)>0)
                {
                    keys[i+1]=keys[i];
                    i--;
                }
                keys[i+1]=key;
                keyTally++;
                return;
            }
            while(i>=0 &&keys[i].compareTo(key)>0)
                i--;
            if(references[i+1].keyTally==2*m-1)
            {
                spilt(references[i+1], i+1);
                if(keys[i+1].compareTo(key)<0)
                    i++;
            }
            references[i+1].add(key);
        }
        
        private void spilt(BTreeNode<T> node1, int i)
        {
            BTreeNode<T> node2 = new BTreeNode<T>(m,node1.leaf);
            node2.keyTally=m-1;
            
            for (int j = 0; j < m-1; j++)
                node2.keys[j] = node1.keys[j+m];
            
            if(!node1.leaf)
                for (int j = 0; j < m; j++)
                    node2.references[j]=node1.references[j+m];
            
            node1.keyTally=m-1;
            for (int j = keyTally; j >=i+1; j--)
                references[j+1]=references[j];
            references[i+1]=node2;
            
            for (int j = keyTally-1; j >= i; j--)
                keys[j+1] = keys[j];
            keys[i] = node1.keys[m-1];
            keyTally++;
            for (int j = node1.keyTally+1; j < node1.references.length; j++)
                node1.references[j]=null;
        }

	// Function to search a key in a subtree rooted with this node
        public BTreeNode<T> search(T key)
        {  
            int i = 0; 
            while (i < keyTally && keys[i].compareTo(key)<0) 
                i++; 
            if (i<keyTally&&keys[i].equals(key)) 
                return this; 
            if (leaf == true) 
                return null; 
            return references[i].search(key);
        }
        
	// Function to traverse all nodes in a subtree rooted with this node
        public void traverse()
	{
            int i; 
            for (i = 0; i < keyTally; i++) 
            { 
                if (leaf == false) 
                    references[i].traverse(); 
                System.out.print(" "+keys[i]); 
            }
            if (leaf == false) 
                references[i].traverse(); 
        }
}