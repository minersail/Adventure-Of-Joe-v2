package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.ProjectileComponent;
import woohoo.screens.PlayingScreen;

public class ProjectileSystem extends IteratingSystem
{
	private PlayingScreen screen;
	
	public ProjectileSystem(PlayingScreen scr)
	{
		super(Family.all(ProjectileComponent.class).get());
		
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		ProjectileComponent projectile = Mappers.projectiles.get(entity);
		
		projectile.currentTime += deltaTime;
		
		if (projectile.currentTime > projectile.lifespan)
		{
			//screen.getWorld().destroyBody(Mappers.hitboxes.get(entity).mass);
			//screen.getEngine().removeEntity(entity);
		}
	}
}
