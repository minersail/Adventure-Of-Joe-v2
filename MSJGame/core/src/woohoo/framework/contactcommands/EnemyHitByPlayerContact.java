package woohoo.framework.contactcommands;

import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.ProjectileComponent;
import woohoo.gameworld.Mappers;

public class EnemyHitByPlayerContact extends ContactCommand
{
	public EnemyHitByPlayerContact()
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
		ProjectileComponent projectile = Mappers.projectiles.get(contactA.owner);
				
		float forceMult = 5000;
		
		hitbox.mass.applyForceToCenter(Mappers.positions.get(contactA.owner).orientation.getVector().scl(projectile.knockback * forceMult), true);
		
		if (Mappers.lives.has(contactB.owner))
			Mappers.lives.get(contactB.owner).damage(projectile.damage);		
	}
}
