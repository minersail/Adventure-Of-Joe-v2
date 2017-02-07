package woohoo.gameobjects;

import com.badlogic.ashley.core.Entity;
import java.util.ArrayList;
import java.util.ListIterator;
import woohoo.framework.events.EventListener;

public class BaseEntity extends Entity
{
	protected ArrayList<EventListener> listeners = new ArrayList<>();
	protected float elapsedTime;
	protected String name = "";
	
	public void update(float delta)
	{
		elapsedTime += delta;
		
		for (ListIterator<EventListener> iter = listeners.listIterator(); iter.hasNext();)
		{
			if (iter.next().notify(this))
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
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
}
