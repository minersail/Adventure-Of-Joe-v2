package woohoo.msjgame;

import com.badlogic.gdx.Game;
import woohoo.screens.PlayingScreen;

public class MSJGame extends Game
{	
	@Override
	public void create()
	{
		setScreen(new PlayingScreen(this));
	}

	@Override
	public void render()
	{
		super.render();
	}
	
	@Override
	public void dispose ()
	{
		super.dispose();
	}
}
