/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

/**
 *Used to change the images of the pokemon on the LoginGUI
 * @author craig
 */
public class Timer implements Runnable
{
    private LogInGUI f;
    private long now,prev;
    private boolean run;
    private final static int POKES=257; 
    private int left,right;
    
    /**
     *Constructor for Timer
     * @param f The LogInGUI whose images are to be changed 
    */
    public Timer(LogInGUI f)
    {
        this.f=f;
        prev = System.nanoTime();
        run=true;
    }

    
    @Override
    public void run()
    {
        left=(int)(Math.random()*(POKES))+1;
        right=(int)(Math.random()*(POKES))+1;
        f.updateImage(left, right);
        int delta=0;
        do
        {
            now=System.nanoTime();
            delta = (int)((now-prev)/1000000000);
            if(delta>1)
            {
                f.updateImage(left, right);
                incLeft();
                decRight();
                prev=now;
            }
        }while(run);
        
    }
    
    private void incLeft()
    {
        left++;
        if(left>POKES)
            left=1;
    }
    
    private void decRight()
    {
        right--;
        if(right<1)
            right=POKES;
    }

    /**
     *Sets whether the timer class must run or stop running
     * @param r True= run, False= stop
     */
    public void setRun(boolean r)
    {
        run=r;
    }
}
