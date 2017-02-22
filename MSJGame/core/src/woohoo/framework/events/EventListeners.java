package woohoo.framework.events;

import java.util.ArrayList;
import java.util.ListIterator;

public class EventListeners<T>
{
	private ArrayList<EventListener> listeners = new ArrayList<>();
	
	public void notifyAll(T listenerHolder)
	{		
		for (ListIterator<EventListener> iter = listeners.listIterator(); iter.hasNext();)
		{
			if (iter.next().notify(listenerHolder))
				iter.remove();
		}
	}
	
	public void addListener(EventListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeListener(EventListener listener)
	{
		listeners.remove(listener);
	}
	
	public void clearListeners()
	{
		listeners.clear();
	}
	
	public int getCount()
	{
		return listeners.size();
	}
}
