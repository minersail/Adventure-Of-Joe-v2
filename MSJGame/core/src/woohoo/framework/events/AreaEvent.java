package woohoo.framework.events;

import woohoo.framework.AreaManager;

public class AreaEvent implements Event
{
	private AreaManager manager;
	private int eventArea;
	private int newState;
	
	public AreaEvent(AreaManager am, int area, int state)
	{
		manager = am;
		newState = state;
		eventArea = area;
	}
	
	@Override
	public void activate()
	{
		manager.setAreaState(eventArea, newState);
	}
}
