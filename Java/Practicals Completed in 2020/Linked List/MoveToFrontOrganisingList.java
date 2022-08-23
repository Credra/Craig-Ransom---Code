/**
 * Name:Craig Ransom
 * Student Number:19037237
 */

public class MoveToFrontOrganisingList extends OrganisingList {

    ////// Implement the function below this line //////

    /**
     * Retrieve the node with the specified key and move the accessed node
     * to the front, then recalculate all data fields.
     * @return The node with its data value before it has been moved to the front,
     * otherwise 'null' if the key does not exist.
     */
    @Override
    public ListNode searchNode(Integer key) {
        if(root==null)
            return null;
        if(root.key==key)
            return root;
        
        ListNode temp= root;
        temp=root;
        while(temp.next!=null)
        {
            if(temp.next.key==key)
            {
                ListNode val = new ListNode(temp.next.key, temp.next.data);
                val.next=root;
                root=temp.next;
                temp.next=temp.next.next;
                root.next=val.next;
                calculateData();
                return val;
            }
            temp=temp.next;
        }
        return null;
    }
}