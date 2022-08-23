/**
 * Name:Craig Ransom
 * Student Number:19037237
 */

public class TransposeOrganisingList extends OrganisingList {

    ////// Implement the function below this line //////

    /**
     * Retrieve the node with the specified key and swap the
     * accessed node with its predecessor, then recalculate all data fields.
     * @return The node with its data value before it has been moved,
     * otherwise 'null' if the key does not exist.
     */
    @Override
    public ListNode searchNode(Integer key) {
        if(root==null)
            return null;
        if(root.key==key)
            return root;
        
        ListNode temp= root,prev=root;
        while(temp.next!=null)
        {
            if(temp.next.key==key)
            {
                ListNode val = new ListNode(temp.next.key, temp.next.data);
                val.next=temp.next.next;
                prev.next=temp.next;
                prev.next.next=temp; 
                prev.next.next.next=val.next;
                calculateData();
                return val;
            }
            prev=temp;
            temp=temp.next;
        }
        return null;
    }
}