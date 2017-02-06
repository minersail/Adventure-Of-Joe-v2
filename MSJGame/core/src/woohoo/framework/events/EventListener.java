package woohoo.framework.events;

import woohoo.gameobjects.BaseEntity;

public class EventListener
{
	Event event;
	EventTrigger trigger;
	
	public EventListener(EventTrigger eT, Event e)
	{
		trigger = eT;
		event = e;
	}
	
	public boolean notify(BaseEntity entity)
	{
		if (trigger.check(entity))
		{
			event.activate();
			return true;
		}
		
		return false;
	}
}
