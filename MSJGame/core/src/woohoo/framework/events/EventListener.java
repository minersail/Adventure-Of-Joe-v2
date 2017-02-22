package woohoo.framework.events;

public class EventListener<T>
{
	Event event;
	EventTrigger trigger;
	
	public EventListener(EventTrigger eT, Event e)
	{
		trigger = eT;
		event = e;
	}
	
	public boolean notify(T listenerHolder)
	{
		if (trigger.check(listenerHolder))
		{
			event.activate();
			return true;
		}
		
		return false;
	}
}
