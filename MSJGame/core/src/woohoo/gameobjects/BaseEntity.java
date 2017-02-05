package woohoo.gameobjects;

import com.badlogic.ashley.core.Entity;

public class BaseEntity extends Entity
{
	protected float elapsedTime;
	
	public void update(float delta)
	{
		elapsedTime += delta;
	}
}
