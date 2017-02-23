package woohoo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import woohoo.msjgame.MSJGame;

public class SplashScreen implements Screen, InputProcessor
{
	private MSJGame game;
	
	private SpriteBatch batcher;
	private Texture background;
	
	public SplashScreen(MSJGame g)
	{
		game = g;
		
		batcher = new SpriteBatch();
		background = new Texture("images/splash/title.png");
	}
	
	@Override
	public void render(float delta)
	{
		batcher.begin();
		batcher.draw(background, 0, 0);
		batcher.end();
	}
	
	@Override
    public void resize(int width, int height)
    {
        System.out.println("MenuScreen - resize called");
    }

    @Override
    public void show()
    {
        System.out.println("MenuScreen - show called");
    }

    @Override
    public void hide()
    {
        System.out.println("MenuScreen - hide called");
    }

    @Override
    public void pause()
    {
        System.out.println("MenuScreen - pause called");
    }

    @Override
    public void resume()
    {
        System.out.println("MenuScreen - resume called");
    }

    @Override
    public void dispose()
    {

    }

	@Override
	public boolean keyDown(int keycode)
	{
		game.setScreen(game.getPlayingScreen());
        Gdx.input.setInputProcessor(new InputMultiplexer(game.getPlayingScreen().getUI(), game.getPlayingScreen().getInput()));
		game.getPlayingScreen().getRenderer().getBatch().enableBlending();
		game.getPlayingScreen().getRenderer().startFade(Color.BLACK);
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
