package woohoo.msjgame;

import com.badlogic.gdx.Game;
import woohoo.screens.PlayingScreen;

public class MSJGame extends Game
{	
	/* Dimensions of tiles in-game */
    public static final int G_TILE_WIDTH = 64;
    public static final int G_TILE_HEIGHT = 64;
    
	/* Dimensions of tiles on the spritesheet */
    public static final int T_TILE_WIDTH = 16;
    public static final int T_TILE_HEIGHT = 16;
	
	@Override
	public void create()
	{
        setScreen(new PlayingScreen());
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
