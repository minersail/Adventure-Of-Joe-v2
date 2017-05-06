package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.screens.PlayingScreen;

public class WeaponSystem extends IteratingSystem
{
	private PlayingScreen screen;
	
	public WeaponSystem(PlayingScreen scr)
	{
		super(Family.all(WeaponComponent.class, PositionComponent.class).get());
		
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		WeaponComponent weapon = Mappers.weapons.get(entity);
		PositionComponent position = Mappers.positions.get(entity);
		
		weapon.cooldownTimer = Math.max(weapon.cooldownTimer - deltaTime, 0);
		
		if (weapon.projectileSpawned)
		{
			Entity projectile = screen.getEntityLoader().loadEntity(weapon.getProjectile());
			Mappers.movements.get(projectile).direction = Direction.fromString(position.orientation.text());
			Mappers.positions.get(projectile).position.set(position.position.cpy().add(position.orientation.getVector()));
			
			weapon.cooldownTimer = weapon.cooldown;
			weapon.projectileSpawned = false;
		}
	}
	
	public void equip(Entity equipped, WeaponComponent weapon)
    {
    }
	
	public void unequip(Entity equipped)
	{
	}
}