package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.AnimMapObjectComponent.AnimationState;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.HealthComponent;

public class DamageSystem extends IteratingSystem
{
	public DamageSystem()
	{
		super(Family.all(HealthComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		HealthComponent life = Mappers.lives.get(entity);
		life.currentHealth = Math.max(0, life.currentHealth - life.getIncomingDamage());
		life.resetDamage();
		
		if (Mappers.healthBars.has(entity))
		{
			HealthBarComponent healthBar = Mappers.healthBars.get(entity);
			healthBar.percentLeft = life.currentHealth / life.maxHealth;
		}
		
		if (Mappers.animMapObjects.has(entity))
		{
			AnimMapObjectComponent animation = Mappers.animMapObjects.get(entity);
			if (life.currentHealth <= 0)
				animation.animState = AnimationState.Death;
		}
	}
}
