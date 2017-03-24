package woohoo.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import woohoo.msjgame.MSJGame;

public class GameOverScreen implements Screen
{
	private final MSJGame game;
	
	private final SpriteBatch batcher;
	private final Texture background;
	
	private final Color batchColor;
	private final float fadeSpeed = 0.1f;//0.01f;
	
	public GameOverScreen(MSJGame g)
	{
		game = g;
		
		batcher = new SpriteBatch();
		batcher.enableBlending();
		background = new Texture("images/screens/gameover.png");
		batchColor = new Color(Color.BLACK);
	}
	
	@Override
	public void render(float delta)
	{
		batchColor.add(fadeSpeed, fadeSpeed, fadeSpeed, 0);
		batcher.setColor(batchColor);
		
		batcher.begin();
		batcher.draw(background, 0, 0);
		batcher.end();
	}
	
	@Override
	public void show() 
	{	
	}

	@Override
	public void resize(int width, int height) 
	{		
	}

	@Override
	public void pause() 
	{	
	}

	@Override
	public void resume() 
	{		
	}

	@Override
	public void hide() 
	{		
	}

	@Override
	public void dispose() 
	{		
	}
}
