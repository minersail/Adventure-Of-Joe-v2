package woohoo.framework.contactcommands;

import woohoo.gameobjects.components.GateComponent;
import woohoo.gameobjects.components.HitboxComponent.ContactType;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameworld.Mappers;

/**
 * Contains all the code for when the player interacts with a gate
 * @author jordan
 */
public class GateContact implements ContactCommand
{	
	public GateContact()
	{
	}
	
	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		// If there is contact between a gate and the player
		if (contactA.type == ContactType.Gate && contactB.type == ContactType.Player)
		{
			GateComponent gate = Mappers.gates.get(contactA.owner);
			PositionComponent playerPos = Mappers.positions.get(contactB.owner);
			
			gate.setPlayerOffset(playerPos.position.cpy().sub(gate.position)); // Distance from center of player to center of gate
			gate.triggered = true;
		} // It can work either way (A is a player and B is a gate, or vice versa)
		else if (contactB.type == ContactType.Gate && contactA.type == ContactType.Player)
		{
			GateComponent gate = Mappers.gates.get(contactB.owner);
			PositionComponent playerPos = Mappers.positions.get(contactA.owner);
			
			gate.setPlayerOffset(playerPos.position.cpy().sub(gate.position)); // Distance from center of player to center of gate
			gate.triggered = true;
		}
	}
}
