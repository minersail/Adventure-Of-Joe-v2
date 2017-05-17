package woohoo.gameworld.components;

import com.badlogic.ashley.core.Component;
import woohoo.framework.loading.EntityMold;

public class SpawnComponent implements Component
{
	private EntityMold mold; // Mold of the entity that the spawner will spawn
							 // If string constructor is used will be initialized by SpawnSystem.initialize();
	
	public String moldName; // Name of the entity to mold (so that the spawnSystem can find the entity) 
	public float interval; // Every <interval> seconds the SpawnComponent
	public float timer; // keeps track of time elapsed
	
	public SpawnComponent(String name)
	{
		this(name, 1.0f);
	}
	
	public SpawnComponent(String name, float spawnTime)
	{
		moldName = name;
		interval = spawnTime;
		timer = 0;
	}
	
	/**
	 * @return null if the component has not yet been initialized
	 */
	public EntityMold getMold()
	{		
		return mold;
	}
	
	public void setMold(EntityMold em)
	{
		mold = em;
	}
}
