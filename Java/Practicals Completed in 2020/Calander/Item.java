/* You must complete this class such that it can be used as nodes in a 3D sparse table.
Read the comments to determine what each aspect of the class is supposed to do.
You may add any additional features (methods, references) which may aid you in your
task BUT you are not allowed to remove or change the names or properties of any of the features you where given.

Importing Java's built in data structures will result in a mark of 0. */

public class Item
{
    
	public Item back; // The next Item (back) of this to-do list on the same date
	public Item right; // The next Item (right) of this Item on the same day (1st to 31st).
	public Item down; // The next Item (down) of this Item in the same month.
	
	//Item particulars:
	private String description; // A description for this Item.
	private String duration; // The number of minutes/hours that the Item will take.
	private int priority, date,month; // The priority of the Item.
        
	public Item()
	{
            priority=0;
            
		/*You may implement this constructor to suit your needs, or you may add additional constructors. 
		This is the constructor which will be used for marking. 
		Note: priority should be set to zero by default. */ 		
	}	
	
	public void setDescription(String desc)
	{
            description=desc;
	}	
	
	public void setDuration(String dur)
	{
            try
            {
            String temp[]=dur.split(":");
            int hour=Integer.parseInt(temp[0]),minute=Integer.parseInt(temp[1]);
            if(minute>59||minute<0||hour>24||hour<0||(hour==24&&minute!=0))
                duration="00:00";
            else
                duration=dur;
            }
            catch(Exception e)
            {
                duration="00:00";
            }
	}	
	
	public void setPriority(int p)
	{
            if(p<0)
                priority=0;
            else
                priority=p;
		/* Implement this method to set the priority of this Item, 
		where the default priority of zero (0) corresponds to lowest priority.
		The higher the priority, the closer the Item has to be to the 
		head of the list. */
	}
	
	public String getDescription()
	{
		/* This method returns the description of this Item */
		return description;
	}	
	
	public String getDuration()
	{
		/* This method returns the duration of this Item */
		return duration;
	}
	
	public int getPriority()
	{
		/* This method returns the duration of this Item */
		return priority;
	}

    public int getDate()
    {
        return date;
    }

    public int getMonth()
    {
        return month;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }
	 
}
