package woohoo.gameworld.gamestates;

import woohoo.gameworld.ContactSystem;
import woohoo.gameworld.GateSystem;
import woohoo.gameworld.SpawnSystem;
import woohoo.screens.PlayingScreen;

public class PlayingState implements GameState
{
	@Override
	public void enter(PlayingScreen screen)
	{
		screen.getEngine().getSystem(SpawnSystem.class).setProcessing(true);
		screen.getEngine().getSystem(ContactSystem.class).setProcessing(true);
		screen.getEngine().getSystem(ContactSystem.class).clearContacts();
	}

	@Override
	public void update(PlayingScreen screen, float delta)
	{
		screen.getEngine().getSystem(GateSystem.class).updateArea(); // Only works outside of the engine update loop
		screen.getWorld().step(delta, 6, 2);
	}

	@Override
	public void exit(PlayingScreen screen)
	{
		screen.getEngine().getSystem(SpawnSystem.class).setProcessing(false);
		screen.getEngine().getSystem(ContactSystem.class).setProcessing(false);
	}
}
