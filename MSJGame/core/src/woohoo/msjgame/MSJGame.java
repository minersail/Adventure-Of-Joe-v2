package woohoo.msjgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import woohoo.screens.PlayingScreen;
import woohoo.screens.SplashScreen;

public class MSJGame extends Game
{	
	PlayingScreen playingScreen;
	SplashScreen splashScreen;
	
	@Override
	public void create()
	{
		resetData();
		playingScreen = new PlayingScreen(this);
		splashScreen = new SplashScreen(this);
		
		setScreen(splashScreen);
		Gdx.input.setInputProcessor(splashScreen);
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
	
	public PlayingScreen getPlayingScreen()
	{
		return playingScreen;
	}
	
	public SplashScreen getSplashScreen()
	{
		return splashScreen;
	}
	
	public void resetData()
	{
		FileHandle raw = Gdx.files.internal("raw/data");
		
		for (FileHandle handle : raw.list())
		{
			handle.copyTo(Gdx.files.local("data/" + handle.name()));
		}
	}
}
