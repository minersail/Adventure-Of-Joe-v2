package woohoo.framework.contactcommands;

import woohoo.gameworld.components.ContactComponent.ContactType;
import woohoo.gameworld.components.ContactComponent.Faction;
import woohoo.gameworld.components.HitboxComponent;
import woohoo.gameworld.components.ProjectileComponent;
import woohoo.gameworld.Mappers;

public class ProjectileContact extends ContactCommand
{
	ContactType other; // Should only be ContactType.Player or ContactType.Character
	
	public ProjectileContact(ContactType otherContact)
	{
		super(ContactType.Projectile, otherContact);
		other = otherContact;
	}

	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		if (contactA.type == other) // Switch; Parameters can come in either order but code requires Projectile to be A
		{
			ContactData temp = contactA;
			contactA = contactB;
			contactB = temp;
		}
		
		// No friendly fire
		if (contactA.faction == Faction.Ally && contactB.faction == Faction.Ally ||
			contactA.faction == Faction.Enemy && contactB.faction == Faction.Enemy)
		{
			return;
		}
		
		HitboxComponent hitbox = Mappers.hitboxes.get(contactB.owner);
		ProjectileComponent projectile = Mappers.projectiles.get(contactA.owner);
				
		float forceMult = 5000;
		
		if (hitbox.mass != null)
			hitbox.mass.applyForceToCenter(Mappers.positions.get(contactA.owner).orientation.getVector().scl(projectile.knockback * forceMult), true);
		
		if (Mappers.lives.has(contactB.owner))
			Mappers.lives.get(contactB.owner).damage(projectile.damage);
		
		projectile.currentTime = projectile.lifespan; // destroy projectile
	}
}
