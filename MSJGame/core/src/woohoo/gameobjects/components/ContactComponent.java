package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;

/**
 * Not being used at the moment other than for the enum declaration
 * All collision code should instead be in ContactCommands and ContactData, stored in box2D bodies
 * See ContactSystem and EntityLoader
 * @author jordan
 */
public class ContactComponent implements Component
{
	public enum ContactType
	{
		Player("player"),
		Wall("wall"),
		Gate("gate"),
		Item("item"),
		NPC("npc"),
		Projectile("projectile"),
		Enemy("enemy"),
		Chaser("chaser"),
		SightLine("sightline");
		
		private String text;
		
		ContactType(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static ContactType fromString(String str) 
		{
			for (ContactType b : ContactType.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No Orientation with text " + str + " found.");
		}
		
		public int bits()
		{
			return 0x0 << this.ordinal();
		}
		
		public int mask()
		{
			// DO THIS LATER
//			if (this.equals(Player))
//			{
//				return 
//			}
			return 0;
		}
	}
}
