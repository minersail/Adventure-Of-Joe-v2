package woohoo.framework.contactcommands;

import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.gameworld.Mappers;

public class EnemyHitContact extends ContactCommand
{
	public EnemyHitContact()
	{
		super(ContactType.Weapon, ContactType.Enemy);
	}

	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		if (contactA.type == ContactType.Enemy) // Switch; Parameters can come in either order but code requires Enemy to be A
		{
			ContactData temp = contactA;
			contactA = contactB;
			contactB = temp;
		}
		
		HitboxComponent hitbox = Mappers.hitboxes.get(contactB.owner);
		WeaponComponent weapon = Mappers.weapons.get(contactA.owner);
		
		switch(Mappers.weapons.get(contactA.owner).weaponDirection)
		{
			case North:			
				hitbox.mass.applyLinearImpulse(new Vector2(0, -weapon.knockback), hitbox.mass.getLocalCenter(), true);
				break;
			case South:			
				hitbox.mass.applyLinearImpulse(new Vector2(0, weapon.knockback), hitbox.mass.getLocalCenter(), true);
				break;
			case West:			
				hitbox.mass.applyLinearImpulse(new Vector2(-weapon.knockback, 0), hitbox.mass.getLocalCenter(), true);
				break;
			case East:			
				hitbox.mass.applyLinearImpulse(new Vector2(weapon.knockback, 0), hitbox.mass.getLocalCenter(), true);
				break;
		}
	}
}
