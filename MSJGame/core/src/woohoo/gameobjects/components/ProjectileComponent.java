package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;

/**
 * Class containing data for a projectile (the projectile entity, not the spawning entity)
 * @author jordan
 */
public class ProjectileComponent implements Component
{
	public float currentTime;
	public float lifespan;
	public float knockback;
	public float damage;
	
	public ProjectileComponent(float projDamage, float projKnockback, float projLifespan)
	{
		damage = projDamage;
		knockback = projKnockback;
		lifespan = projLifespan;
		currentTime = 0;
	}
}
