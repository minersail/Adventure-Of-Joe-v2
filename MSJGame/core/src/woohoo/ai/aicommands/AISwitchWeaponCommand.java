package woohoo.ai.aicommands;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.WeaponComponent;

public class AISwitchWeaponCommand implements AICommand
{
	private int projID;
	
	public AISwitchWeaponCommand(int newProjectileID)
	{
		projID = newProjectileID;
	}
	
	@Override
	public boolean run(Entity entity)
	{
		WeaponComponent oldWeapon = (WeaponComponent)entity.remove(WeaponComponent.class);
		entity.add(new WeaponComponent(projID, oldWeapon.cooldown));
		return true; // Immediately finishes
	}

	@Override
	public void enter(Entity entity)
	{
	}

	@Override
	public void exit(Entity entity)
	{
	}

}
