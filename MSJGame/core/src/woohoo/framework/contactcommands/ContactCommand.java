package woohoo.framework.contactcommands;

import woohoo.gameobjects.components.ContactComponent.ContactType;

public abstract class ContactCommand
{
	public ContactType type1;
	public ContactType type2;
	
	protected ContactCommand(ContactType t1, ContactType t2)
	{
		type1 = t1;
		type2 = t2;
	}
	
	public boolean verify(ContactData contactA, ContactData contactB)
	{
		return (contactA.type == type1 && contactB.type == type2) || (contactA.type == type2 && contactB.type == type1);
	}
			
	public abstract void activate(ContactData contactA, ContactData contactB);
}