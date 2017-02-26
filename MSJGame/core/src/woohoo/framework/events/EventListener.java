package woohoo.framework.events;

import java.util.ArrayList;
import java.util.Arrays;

public class EventListener<T>
{
	ArrayList<Event> events;
	EventTrigger trigger;
	
	public EventListener(EventTrigger eT)
	{
		trigger = eT;
		
		events = new ArrayList<>();
	}
	
	public EventListener(EventTrigger eT, Event... e)
	{
		this(eT);
		events.addAll(Arrays.asList(e));
	}
	
	public void addEvent(Event e)
	{
		events.add(e);
	}
	
	public boolean notify(T listenerHolder)
	{
		if (trigger.check(listenerHolder))
		{
			for (Event event : events)
				event.activate();
			
			return true;
		}
		
		return false;
	}
}
