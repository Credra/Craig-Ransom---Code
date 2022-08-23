/* Complete this class to implement a fully functional sparse table. Read the comments to determine what each aspect of the class is supposed to do.
You must add any additional features (methods, references) which may aid you in your
task BUT you are not allowed to remove or change the names or properties of any of the features you were given.

Note: you are NOT allowed to use any 2D or 3D arrays to simulate the sparse table functionality. Doing so will result in a mark of zero.

Importing Java's built in data structures will result in a mark of zero. Only the use of native 1-dimensional is are allowed. */

public class Calendar
{
    private Item months[], days[];
	public Calendar()
	{
            months= new Item[12];
            days= new Item[31];
		/*You may implement this constructor to suit your needs, or you may add additional constructors.  This is the constructor which will be used for marking*/ 
	}
	
	/*Insertion*/

	public void addItem(int day, String month, String description, String duration, int priority)
	{
            Item item = new Item();
            item.setPriority(priority);
            item.setDuration(duration);
            item.setDescription(description);
            item.setDate(day);
            int m=getMonthValue(month);
            item.setMonth(m);
            
            if(months[m]==null)
            {
                months[m]=item;
            }
            else
            {
                if(months[m].getDate()==(day))
                {
                    //insert by priority
                    Item te =months[m];
                    if(te.getPriority()<priority)
                    {
                        item.back=months[m];
                        months[m]=item;
                    }
                    else
                    {
                        while(te.back!=null)
                        {
                            if(te.getPriority()<priority)
                            {
                                item.back=months[m];
                                months[m]=item;
                                return;
                            }
                            else
                                te=te.back;
                        }
                        te.back=item; 
                    }
                    return;
                }
                else if(months[m].getDate()>day)
                {
                    item.right=months[m];
                    months[m]=item;
                }
                else
                {
                    Item temp =months[m];
                    while(temp.right!=null&&temp.right.getDate()<item.getDate())
                    {
                        if(temp.right.getDate()==(day))
                        {
                            //swap accoring priority
                            Item te =temp.right;
                            if(te.getPriority()<priority)
                            {
                                item.back=temp;
                                months[m]=item;
                            }
                            else
                            {
                                te=te.right;
                                boolean foun=false;
                                while(te.right!=null&&!foun)
                                {
                                    if(te.getPriority()<priority)
                                    {
                                        item.right=months[m];
                                        months[m]=item;
                                        foun=true;
                                    }
                                    else
                                        te=te.right;
                                }
                                if(!foun)
                                {
                                   te.right=item; 
                                } 
                            }
                            //done
                            return;
                        }
                        temp=temp.right;
                    }
                    
                    if(temp.right==null)
                        temp.right=item;
                    else
                    {
                        item.right=temp.right;
                        temp.right=item;
                    }
                }
            }
            //month array done
            
            if(days[day-1]==null)
            {
                days[day-1]=item;
            }
            else
            {
                if(days[day-1].getMonth()>m)
                {
                    item.down=days[day-1];
                    days[day-1]=item;
                    /*swap accoring priority
                    Item te =days[day];
                    if(te.getPriority()<priority)
                    {
                        item.down=days[day];
                        days[day]=item;
                    }
                    else
                    {
                        //te=te.down;
                        boolean foun=false;
                        while(te.down!=null&&!foun)
                        {
                            if(te.getPriority()<priority)
                            {
                                item.down=days[day];
                                days[day]=item;
                                foun=true;
                            }
                            else
                                te=te.down;
                        }
                        if(!foun)
                        {
                           te.down=item; 
                        } 
                    }
                   done*/
                }
                else
                {
                    Item temp=days[day-1];
                    while(temp.down!=null && temp.down.getMonth()<item.getMonth())
                    {
                        temp=temp.down;
                    }
                    if(temp.down==null)
                        temp.down=item;
                    else
                    {
                        item.down=temp.down;
                        temp.down=item;
                    }
                    
                }
            }
		/* Insert an Item at the given day and month combination according to priority. Intialize the Item with the remainder of the parameters	
		Duplicate Items are allowed.*/
	}
	
        private int getMonthValue(String month)
        {
            int i=0;
            month=month.toLowerCase();
            switch(month)
            {
                case "dec":
                    i++;
                case "nov":
                    i++;
                case "oct":
                    i++;
                case "sep":
                    i++;
                case "aug":
                    i++;
                case "jul":
                    i++;
                case "jun":
                    i++;
                case "may":
                    i++;
                case "apr":
                    i++;
                case "mar":
                    i++;
                case "feb":
                    i++;
                case "jan":
                    break;
            }
            return i;
        }
	/*Deletion*/
	
	public Item deleteItem(int day, String month, String description)
	{
            int m=this.getMonthValue(month);
            Item temp=months[m],prev=months[m];
            while(temp!=null&&temp.getDate()!=(day))
            {
                prev=temp;
                temp=temp.right;
            }
            if(temp==null)
                return null;
            if(temp==prev)
            {
                while(temp!=null&&!temp.getDescription().equals(description))
                {
                    prev=temp;
                    temp=temp.back;
                }
                if(temp==null)
                    return null;
                if(temp==prev)
                {
                    months[m]=temp.back;
                }
                else
                {
                    prev.back=temp.back;
                }
            }
            else
            {
                Item pre=temp;
                while(temp!=null&&temp.getDescription().equals(description))
                {
                    pre=temp;
                    temp=temp.back;
                }
                if(temp==null)
                    return null;
                if(temp==pre)
                {
                    prev.right=temp.back;
                    temp.back.right=temp.right;
                }
                else
                {
                    pre.back=temp.back;
                }
            }
            
            if(temp==days[day-1])
            {
                days[day-1]=temp.back;
            }
            return temp;
	}
	
	public void deletePriorityItem(int day, String month, int priority)
	{
            int m=this.getMonthValue(month);
            Item temp=months[m],prev=months[m];
            while(temp!=null&&temp.getDate()!=(day))
            {
                prev=temp;
                temp=temp.right;
            }
            if(temp==null)
                return;
            if(temp==prev)
            {
                while(temp!=null)
                {
                    if(temp.getPriority()==priority)
                    {
                        temp=temp.back;
                        months[m]=temp;
                    }
                    else
                        temp=temp.back;
                }
            }
            else
            {
                while(temp!=null)
                {
                    if(temp.getPriority()==priority)
                    {
                        prev.right=temp.back;
                        temp.back.right=temp.right;
                        temp=temp.back;
                    }
                    else
                        temp=temp.back;
                }
            }
            temp=days[day-1];
            prev=temp;
            while(temp!=null&&temp.getMonth()!=m)
            {
                prev=temp;
                temp=temp.down;
            }
            if(temp==null)
                return;
            if(temp==prev)
            {
                while(temp!=null)
                {
                    if(temp.getPriority()==priority)
                    {
                        temp=temp.back;
                        days[day-1]=temp;
                    }
                    else
                        temp=temp.back;
                }
            }
            else
            {
                while(temp!=null)
                {
                    if(temp.getPriority()==priority)
                    {
                        prev.down=temp.back;
                        temp.back.down=temp.down;
                        temp=temp.back;
                    }
                    else
                        temp=temp.back;
                }
            }
	}
	
	public void deleteItems(int day, String month)
	{
            int m=this.getMonthValue(month);
            Item temp=months[m],prev=months[m];
            while(temp!=null&&temp.getDate()!=(day))
            {
                prev=temp;
                temp=temp.right;
            }
            if(temp==null)
                return;
            if(temp==prev)
            {
                months[m]=temp.right;
            }
            else
            {
                prev.right=temp.right;
            }
            
            temp=days[day-1];
            prev=temp;
            while(temp!=null&&temp.getMonth()!=m)
            {
                prev=temp;
                temp=temp.down;
            }
            if(temp==null)
                return;
            if(temp==prev)
            {
                days[day-1]=temp.down;
            }
            else
            {
                prev.down=temp.down;
            }
	}
	
	/*Clearing Methods*/
	public void clearMonth(String month)
	{
            int m=this.getMonthValue(month);
            months[m]=null;
            for (int i = 0; i < days.length; i++)
            {
                if(days[i]!=null&&days[i].getMonth()==m)
                    days[i]=days[i].down;
            }
	}
	
	public void clearDay(int day)
	{
            days[day-1]=null;
            for (int i = 0; i < months.length; i++)
            {
                if(months[i]!=null&&months[i].getDate()==day)
                    months[i]=months[i].right;
            }
	}
	
	public void clearYear()
	{
            months= new Item[12];
            days= new Item[31];
	}
	
	
	/*Query methods*/
	public Item getItem(int day, String month)
	{
            int m=this.getMonthValue(month);
            Item temp=months[m];
            while(temp!=null&&temp.getDate()!=day)
                temp=temp.right;
            return temp;
	}
	
	public Item getMonthItem(String month)
	{
		return months[this.getMonthValue(month)];
	}
	
	public Item getDayItem(int day)
	{
            return days[day-1];
	}	
}
