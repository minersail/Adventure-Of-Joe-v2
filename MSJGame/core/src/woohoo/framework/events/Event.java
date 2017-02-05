package woohoo.framework.events;

import woohoo.gameobjects.BaseEntity;

public interface Event
{	
	public boolean check(BaseEntity entity);
	public void activate();
}
