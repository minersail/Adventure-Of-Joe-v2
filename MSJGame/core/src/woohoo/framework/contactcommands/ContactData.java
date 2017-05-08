package woohoo.framework.contactcommands;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.ContactComponent.Faction;

/**
 * ContactData is attached to bodies through getUserData()
 * Any component that owns a body will have a ContactData
 * Entities can have multiple ContactData, one per body
 * 
 * @author jordan
 */
public class ContactData
{
	public ContactType type;
	public Faction faction;
	public Entity owner;
	
	public ContactData(ContactType contactType, Faction fac, Entity owningEntity)
	{
		type = contactType;
		owner = owningEntity;
		faction = fac;
	}
}
