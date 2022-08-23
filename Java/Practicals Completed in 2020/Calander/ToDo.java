public class ToDo
{
	/*Use this class to test your implementation.  This file will be overwritten for marking purposes.*/
	
	public static void main(String[] args)
	{
		Calendar c = new Calendar();
                c.addItem(1, "Feb", "My B-day", "24:00", 10);
                c.addItem(1, "FeB", "Lunch w/ fam", "01:00", 5);
                c.addItem(1, "FEb", "XXXX", "24:10", 0);
                c.addItem(6, "Dec", "James B-day", "24:00", 2);
                c.addItem(31, "Aug", "Lunch w/ mom", "01:30", 5);
                c.addItem(2, "JAn", "XXXX", "24:10", 5);
                c.addItem(4, "Feb", "B-day", "24:00", 10);
                c.addItem(5, "FeB", "Lunch w/ fam", "01:00", 5);
                c.addItem(16, "FEb", "Poppie B-day", "24:10", 6);
                c.addItem(2, "Jan", "B-day", "24:00", 10);
                c.addItem(5, "ApR", "Lunch w/ fam", "01:00", 5);
                c.addItem(4, "Mar", "XXXX", "24:10", 5);
                
                Item t=c.getMonthItem("Feb");
                while(t!=null)
                {
                    System.out.println(t.getPriority()+": "+t.getDescription()+" on the "+t.getDate()+" 1=="+t.getMonth());
                    Item tb=t.back;
                    while(tb!=null)
                    {
                        System.out.println(tb.getPriority()+": "+tb.getDescription()+" on the "+tb.getDate()+" 1=="+tb.getMonth());
                        tb=tb.back;
                    }
                    t=t.right;
                }
	}
	
}
