package woohoo.framework.events;

import woohoo.gameobjects.BaseEntity;

public interface EventTrigger
{
	public boolean check(BaseEntity entity);
}
