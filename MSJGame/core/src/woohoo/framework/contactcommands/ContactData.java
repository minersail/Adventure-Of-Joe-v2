package woohoo.framework.contactcommands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import woohoo.gameobjects.components.ContactComponent.ContactType;

/**
 * ContactData is attached to bodies through getUserData()
 * Any component that owns a body will have a ContactData
 * 
 * @author jordan
 */
public class ContactData
{
	public ContactType type;
	public Entity owner;

	// Array of collisions
	public Array<ContactData> collisions;

	public ContactData()
	{
		collisions = null;
	}

	public boolean hasCollisions()
	{
		return collisions.size == 0;
	}
}
