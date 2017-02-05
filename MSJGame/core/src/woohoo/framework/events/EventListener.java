package woohoo.framework.events;

import woohoo.gameobjects.BaseEntity;

public class EventListener
{
	Event event;
	
	public EventListener(Event e)
	{
		event = e;
	}
	
	public boolean notify(BaseEntity entity)
	{
		if (event.check(entity))
		{
			event.activate();
			return true;
		}
		
		return false;
	}
}
