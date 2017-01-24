package woohoo.framework.contactcommands;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import woohoo.framework.GateManager;
import woohoo.screens.PlayingScreen;

public class GateContact implements ContactCommand
{
	GateManager manager;
	
	public GateContact(GateManager gm)
	{
		manager = gm;
	}
	
	@Override
	public void startContact(Contact contact)
	{
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		// If there is contact between a gate and the player
		if ((fA.getBody().getUserData() == PlayingScreen.WBodyType.Gate && fB.getBody().getUserData() == PlayingScreen.WBodyType.Player))
		{
			// Store the gate's data in the gatemanager so that the gatemanager can process the screen switching during the next game loop
			manager.setNextGate((GateManager.GateData)fA.getUserData());
			manager.getNextGate().setPlayerOffset(fB.getBody().getPosition().x - manager.getNextGate().gatePos().x,
												  fB.getBody().getPosition().y - manager.getNextGate().gatePos().y);
			manager.triggerAreaSwitch();
		} // It can work either way (A is a player and B is a gate, or vice versa)
		else if (fB.getBody().getUserData() == PlayingScreen.WBodyType.Gate && fA.getBody().getUserData() == PlayingScreen.WBodyType.Player)
		{
			manager.setNextGate((GateManager.GateData)fB.getUserData());
			manager.getNextGate().setPlayerOffset(fA.getBody().getPosition().x - manager.getNextGate().gatePos().x,
												  fA.getBody().getPosition().y - manager.getNextGate().gatePos().y);
			manager.triggerAreaSwitch();
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		
	}
}
