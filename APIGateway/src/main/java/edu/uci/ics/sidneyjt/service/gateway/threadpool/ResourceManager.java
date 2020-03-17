package edu.uci.ics.sidneyjt.service.gateway.threadpool;

public class ResourceManager
{
    boolean isFree[];
    ResourceManager(){}
    ResourceManager(int numberOfItems)
    {
        isFree = new boolean[numberOfItems];
        for (int i=0; i < isFree.length; ++i)
            isFree[i] = true;
    }
    synchronized int request() throws Exception
    {
        while (true)
        {
            for (int i = 0; i < isFree.length; ++i)
                if ( isFree[i] )
                {
                    isFree[i] = false;
                    return i;
                }
            this.wait(); // block until someone releases Resource
        }
    }
    synchronized void release( int index )
    {
        isFree[index] = true;
        this.notify(); // let a blocked thread run
    }
}
