public class Trie {	
	protected char[] letters;
	protected Node root = null;
	private int numPtrs;

	public Trie(char[] letters) {
		this.letters = letters;
		this.numPtrs = letters.length + 1;
	}


	//Provided Helper functions
	
	private int index(char c) {
		for (int k = 0; k < letters.length; k++) {
			if (letters[k] == (c)) {
				return k+1;
			}
		}
		return -1;
	}
	
	private char character(int i) {
		if (i == 0) {
			return '#';
		} else {
			return letters[i-1];
		}
	}
	
	private String nodeToString(Node node, boolean debug) {
		if (node.isLeaf) {
			return node.key;
		}
		else {
			String res = "";
			for (int k = 0; k < node.ptrs.length; k++) {
				if (node.ptrs[k] != null) {
					res += " (" + character(k) + ",1) ";
				} else if (debug) {
					res += " (" + character(k) + ",0) ";
				}
			}
			return res;
		}
	}

	public void print(boolean debug) {
		Queue queue = new Queue();
		Node n = root;
		if (n != null) {
			n.level = 1;
			queue.enq(n);
			while (!queue.isEmpty()){
				n = queue.deq();
				System.out.print("Level " + n.level + " [");
				System.out.print(nodeToString(n, debug));
				System.out.println("]");
				for (int k = 0; k < n.ptrs.length; k++) {
					if (n.ptrs[k] != null) {
						n.ptrs[k].level = n.level+1;
						queue.enq(n.ptrs[k]);
					}
				}
			}
		}
	}


	////// You may not change any code above this line //////

	////// Implement the functions below this line //////

	// Function to insert the given key into the trie at the correct position.
	public void insert(String key)
        {
            if(root==null)
                root=new Node(letters.length+1);
            Node node=root;
            int i=0;
            int index=0;
            while(i<key.length())
            {
                index=index(key.charAt(i));
                if(node.ptrs[index]==null)
                {
                    node.ptrs[index]=new Node(key,letters.length+1);
                    node.ptrs[index].level=node.level+1;
                    if(key.length()==i+1)
                        node.ptrs[index].endOfWord=true;
                    return;
                }
                node=node.ptrs[index];
                i++;
                if(!node.endOfWord&&node.key!=null)
                {
                    String temp=node.key; 
                    if(temp==key)
                        return;
                    node.key=null;
                    int l;
                    if(temp.length()<key.length())
                        l=temp.length();
                    else
                        l=key.length();
                    while(i<l&&temp.charAt(i)==key.charAt(i))
                    {
                        index=index(key.charAt(i));
                        node.isLeaf=false;
                        node.ptrs[index]=new Node(letters.length+1);
                        node.ptrs[index].isLeaf=true;
                        node=node.ptrs[index];
                        i++;
                    }
                    node.isLeaf=false;
                    if(i<l)
                    {
                        index=index(key.charAt(i));
                        node.ptrs[index]=new Node(key,letters.length+1);
                        node.ptrs[index].level=node.level+1;
                        
                        index=index(temp.charAt(i));
                        node.ptrs[index]=new Node(temp,letters.length+1);
                        node.ptrs[index].level=node.level+1;
                        return;
                    }
                    else
                    {
                        if(temp.length()>key.length())
                        {
                            i--;
                            if(temp.charAt(i)==key.charAt(i))
                            {
                                /*index=index(key.charAt(i));
                                node=node.ptrs[index];*/
                                node.ptrs[0]=new Node(key,letters.length+1);
                                node.endOfWord=true;
                                node.isLeaf=false;
                                i++;
                                index=index(temp.charAt(i));
                                node.ptrs[index]=new Node(temp,letters.length+1);
                                if(temp.length()==i+1)
                                    node.ptrs[index].endOfWord=true;
                                return;
                            }
                            
                            /*index=index(key.charAt(i));
                            node=node.ptrs[index];*/
                            node.ptrs[0]=new Node(key,letters.length+1);
                            node.ptrs[0].level=node.level+1;
                            node.ptrs[0].endOfWord=true;
                            
                            index=index(temp.charAt(i));
                            node.ptrs[index]=new Node(temp,letters.length+1);
                            node.ptrs[index].level=node.level+1;
                            return;
                        }
                        else
                        {
                            i--;
                            if(temp.charAt(i)==key.charAt(i))
                            {
                                index=0;//index(temp.charAt(i));
                                node.ptrs[index]=new Node(temp,letters.length+1);
                                //node=node.ptrs[index];
                                node.endOfWord=true;
                                node.isLeaf=false;
                                i++;
                                index=index(key.charAt(i));
                                node.ptrs[index]=new Node(key,letters.length+1);
                                if(key.length()==i+1)
                                    node.ptrs[index].endOfWord=true;
                                return;
                            }
                            index=index(key.charAt(i));
                            node.ptrs[index]=new Node(key,letters.length+1);
                            node.ptrs[index].level=node.level+1;
                            
                            index=0;//index(temp.charAt(i));
                            node.ptrs[index]=new Node(temp,letters.length+1);
                            node.ptrs[index].level=node.level+1;
                            node.ptrs[index].endOfWord=true;
                            return;
                        }
                    }
                }
                
            }
        }


	// Function to determine if a node with the given key exists.
	public boolean contains(String key) 
        {
            String temp=" ";
            for (int i = 0; i < letters.length+1; i++)
                temp+=print(root.ptrs[i]);
            return temp.indexOf(" "+key+" ")!=-1;
	}

	
	// Function to print all the keys in the trie in alphabetical order.
	public void printKeyList() 
        {
            String temp="";
            for (int i = 0; i < letters.length+1; i++)
                temp+=print(root.ptrs[i]);
            System.out.println(temp);
	}
        
        private String print(Node node)
        {
            if(node==null)
                return"";
            String temp="";
            if(node.key!=null)
            {
                temp+=node.key+" ";
            }
            for (int i = 0; i < node.ptrs.length; i++)
            {
                temp+=print(node.ptrs[i]);
            }
            return temp;
        }
    
}